package me.star2478.jstorm.bolt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import clojure.lang.Obj;
import me.star2478.jstorm.JstormMain;
import me.star2478.jstorm.common.PropertiesUtil;
import me.star2478.jstorm.common.StrategyUtil;
import me.star2478.jstorm.dao.KnowledgeStrategyBaseInfoDAO;
import me.star2478.jstorm.dao.KnowledgeStrategyBaseInfoDAOImpl;
import me.star2478.jstorm.dao.KnowledgeStrategyConfigDAO;
import me.star2478.jstorm.dao.KnowledgeStrategyConfigDAOImpl;
import me.star2478.jstorm.dao.KnowledgeStrategyStatDAO;
import me.star2478.jstorm.dao.KnowledgeStrategyStatDAOImpl;
import me.star2478.jstorm.dto.AppStrategyConfigDTO;
import me.star2478.jstorm.dto.KnowledgeStrategyConfigDTO;
import me.star2478.jstorm.dto.KnowledgeStrategyStatDTO;
import me.star2478.jstorm.dto.StrategyAnalysisSummaryDTO;
import me.star2478.jstorm.dto.KnowledgeStrategyConfigDTO.AppStrategyTrigger;
import me.star2478.jstorm.redis.KnowledgeRedis;
import me.star2478.jstorm.service.StrategyService;
import me.star2478.jstorm.service.StrategyServiceFactory;
import me.star2478.jstorm.service.StrategyService.GovStatusEnum;
import me.star2478.jstorm.sqlengine.SQLEngine;
import me.star2478.jstorm.utils.Const;

@Controller
public class KnowledgeAnalysisBolt extends BaseRichBolt {
	
	private static final long serialVersionUID = 1L;
	
	private static final String DEFAULT_VALUE = "default";

	private OutputCollector collector;

	private String redis_strategy_key = PropertiesUtil.getProperty("redis_strategy_key");
	
	private static Logger logger = Logger.getLogger(KnowledgeAnalysisBolt.class);
	
	private StrategyServiceFactory factory;
	
	private StrategyService strategyServiceCommon;

	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		factory = (StrategyServiceFactory) JstormMain.ctx.getBean("StrategyServiceFactory");
	}

	/**
	 * 接收数据流
	 * 取出策略配置，解析sql知识条件
	 * 如果数据流满足sql知识条件，则产生知识数据，过期时间>0则将知识存入redis，=0则不存入redis
	 * 如果知识数据满足应用条件，启动对应的应用策略或持久化
	 * 
	 * @param key 知识策略名
	 * @param appId app唯一标示
	 * @param os 操作系统
	 * @param appVersion app版本号
	 * @param data 知识数据field和value
	 * @return
	 */
	public void execute(Tuple input) {
		
		try {

			// get source data from message-queue, such as kafka and redis.
			String strategyJson = input.getStringByField(redis_strategy_key);
			JSONObject map = JSON.parseObject(strategyJson);
			String key = (String) map.get("key");
			String data = (String) map.get("data");
			String appId = (String)map.get("appId");
			String os = (String) map.get("os");
			String appVersion = (String) map.get("appVersion");
			String sourceData = (String) map.get("sourceData");

			if (StringUtils.isBlank(key) || StringUtils.isBlank(data)) {
				logger.error("params fail! key=" + key);
				return;
			}
			
			appId = (StringUtils.isBlank(appId)) ? DEFAULT_VALUE : appId;
			os = (StringUtils.isBlank(os)) ? DEFAULT_VALUE : os;
			appVersion = (StringUtils.isBlank(appVersion)) ? DEFAULT_VALUE : appVersion;

			Map<String, Object> dataMap = StrategyUtil.json2Map(data);
			// 将dataMap和map部分字段合并到dataMap，如果dataMap中包含了和map一样的key，以map中的value为准
			StrategyUtil.buildDataMap(dataMap, map);

			strategyServiceCommon = factory.getKnowledgeStrategyBeanByCommand(null);
			
			// 获取知识策略的配置
			KnowledgeStrategyConfigDTO knowledgeStrategyConfig = strategyServiceCommon.getKnowledgeStrategyConfig(key);
			if (StrategyUtil.checkStrategyConfig(knowledgeStrategyConfig) == false) {
				return;
			}
			
			// 将基本信息同步到mongodb
			strategyServiceCommon.synBaseInfo2Mongodb(key, appId, os, appVersion);
			
			int expire = knowledgeStrategyConfig.getExpire();
			List<AppStrategyTrigger> strategies = knowledgeStrategyConfig.getAppStrategyTriggers();
			
			// 获取所有策略的方案信息
			Map<String, AppStrategyConfigDTO> solutions = strategyServiceCommon.getAllSolutionsByStrategies(strategies);
			
			// 扫描对应key下的所有策略
			for (AppStrategyTrigger strategy : strategies) {
				String dataSolutionName = strategy.getDataStrategyName();	// 获取数据分析方案
				String govSolutionName = strategy.getAppStrategyName();	// 获取治理方案
				String dataAnalysisResult = null;
				boolean needGov = false;
				
				// 将本次请求保存下来
				StrategyAnalysisSummaryDTO strategyAnalysisSummaryDTO = initStrategyAnalysisSummaryDTO(key, strategy.getName(), sourceData);
				
				if (Arrays.asList(Const.needCheckSQLStrategyList).contains(dataSolutionName)) {
					Map<String, Object> result4SQLAnalyser = SQLAnalyserHandler(key, appId, os, appVersion, dataMap, strategy, expire);
					if (result4SQLAnalyser == null) {
						continue;
					}
					needGov = Boolean.valueOf(result4SQLAnalyser.get("needGov").toString());
					dataAnalysisResult = result4SQLAnalyser.get("dataResult").toString();
				} else {
					// 将数据传给数据分析方案进行分析
					AppStrategyConfigDTO dataSolution = solutions.get(dataSolutionName);
					dataAnalysisResult = DataAnalysisHandler(dataSolution.getCommand(), dataMap);
					needGov = true;
				}
				if(dataAnalysisResult == null) {
					continue;
				}
				// 将分析结果保存下来？？？可以把处理中的状态去掉，节省一次写mongodb
				storeDataAnalysisResult(strategyAnalysisSummaryDTO, dataAnalysisResult);
				// 将分析结果传给治理方案进行治理
				if (needGov == true) {
					AppStrategyConfigDTO govSolution = solutions.get(govSolutionName);
					dataMap.put("dataAnalysisResult", dataAnalysisResult);
					String govResult = govHandler(govSolution.getCommand(), dataMap);
					if(govResult != null) {	// 将治理成功结果保存下来
						storeGovSucResult(strategyAnalysisSummaryDTO, govResult);
					} else {	// 将治理失败结果保存下来
						storeGovFailResult(strategyAnalysisSummaryDTO, govResult);
					}
				}
			}


//			Map<String, Object> knowledgeInRedis11 = knowledgeRedisBean.hgetAll(key);
//			System.out.println("aaa---------------------:" + knowledgeInRedis11.toString());
            logger.info("knowledge strategy bolt success!");
		} catch (Exception e) {
			logger.error("knowledge strategy bolt Exception:" + e);
			e.printStackTrace();
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("data", "appStrategyTrigger", "command", "arrValue"));
	}
	
	private StrategyAnalysisSummaryDTO initStrategyAnalysisSummaryDTO(String key, String strategyName, String sourceData) {
		StrategyAnalysisSummaryDTO strategyAnalysisSummaryDTO = new StrategyAnalysisSummaryDTO();
		String curTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		strategyAnalysisSummaryDTO.setTime(curTime);
		strategyAnalysisSummaryDTO.setKey(key);
		strategyAnalysisSummaryDTO.setTriggerName(strategyName);
		strategyAnalysisSummaryDTO.setSourceData(sourceData);
		strategyAnalysisSummaryDTO.setGovernStatus(GovStatusEnum.NOGOV.ordinal());
		strategyServiceCommon.incNewStrategySummary(strategyAnalysisSummaryDTO);
		return strategyAnalysisSummaryDTO;
	}
	
	private void storeDataAnalysisResult(StrategyAnalysisSummaryDTO strategyAnalysisSummaryDTO, String dataResult) {
		strategyAnalysisSummaryDTO.setDataResult(dataResult);
		strategyAnalysisSummaryDTO.setGovernStatus(GovStatusEnum.DOING.ordinal());
		strategyServiceCommon.updStrategySummary(strategyAnalysisSummaryDTO);
//		return strategyAnalysisSummaryDTO;
	}
	
	private void storeGovSucResult(StrategyAnalysisSummaryDTO strategyAnalysisSummaryDTO, String govResult) {
		strategyAnalysisSummaryDTO.setGovernResult(govResult);
		strategyAnalysisSummaryDTO.setGovernStatus(GovStatusEnum.SUCCESS.ordinal());
		strategyServiceCommon.updStrategySummary(strategyAnalysisSummaryDTO);
	}
	
	private void storeGovFailResult(StrategyAnalysisSummaryDTO strategyAnalysisSummaryDTO, String govResult) {
		strategyAnalysisSummaryDTO.setGovernResult(govResult);
		strategyAnalysisSummaryDTO.setGovernStatus(GovStatusEnum.FAIL.ordinal());
		strategyServiceCommon.updStrategySummary(strategyAnalysisSummaryDTO);
	}
	
	
	/**
	 * 处理sql文件分析器策略
	 * @param key
	 * @param appId
	 * @param os
	 * @param appVersion
	 * @param dataMap
	 * @param strategyServiceCommon
	 * @param knowledgeStrategyConfig
	 */
	private Map<String, Object> SQLAnalyserHandler(String key, String appId, String os, String appVersion, 
			Map<String, Object> dataMap,
			AppStrategyTrigger appStrategyTrigger,
			int expire) {
	
		Map<String, Object> result = new HashMap<String, Object>();
		
		/////////////需要优化，不能每次循环都取redis
		// 读出redis中的指定key的所有field和value
		KnowledgeRedis knowledgeRedisBean = strategyServiceCommon.getKnowledgeRedis();
		Map<String, Object> knowledgeInRedis = knowledgeRedisBean.hgetAll(key);
		

//		for (AppStrategyTrigger appStrategyTrigger : appStrategyTriggers) {
			appStrategyTrigger.getName();
			int persistent = appStrategyTrigger.getPersistent();
			String triggerName = appStrategyTrigger.getName();
			String sql = appStrategyTrigger.getSql();

			// 分析数据流，检查是否符合知识条件，如果符合则产生知识数据
			Map<String, Object> newKnowledgeMap = SQLEngine.createKnowledge(sql, dataMap);
			// 如果数据流不满足知识条件，则检查下一个知识条件
			if(newKnowledgeMap == null) {
				return null;
			}
			String command = (String) newKnowledgeMap.get(SQLEngine.COMMAND);
			String keyOfKnowledge = (String) newKnowledgeMap.get(SQLEngine.KEYOFKNOWLEDGE);
			Object knowledgeVal = newKnowledgeMap.get(SQLEngine.VALUE);
			if(knowledgeVal == null) {
				return null;
			}
			StrategyService knowledgeStrategyService = factory.getKnowledgeStrategyBeanByCommand(command);
			
			// 如果redis中当前知识条件对应的知识数据已过期且要求持久化，则把知识数据持久化到mongodb
			String expireTime = (String) knowledgeInRedis.get(StrategyService.EXPIRETIME_KEYNAME);
			String fieldForRedis = StrategyUtil.strcatKnowledgeRedisField(appId, os, appVersion, triggerName);
			if (persistent == 1 && expireTime != null && StrategyUtil.checkTimeOut(new String(expireTime))) {
				logger.info("key=" + key + " expireTimeout, persistent triggerName=" + triggerName + " into mongodb");
				Map<String, Object> persistentMap = knowledgeStrategyService.getPersistentKnowledgeByField(knowledgeInRedis, fieldForRedis);
				knowledgeStrategyService.saveData2MogonDB(key, persistentMap, command);
			}
			
			// 将知识数据inc或set进redis，得到新知识数据
			Object[] arrValue = knowledgeStrategyService.getCurrentValue(key, appId, os, appVersion, triggerName, knowledgeVal, knowledgeInRedis, expire);
			
			// 如果符合应用条件，则触发下一个bolt（应用策略bolt）
			if (knowledgeStrategyService.checkOpenAppStrategy(
					arrValue[StrategyService.INDEX_CURRENT_VALUE], 
					appStrategyTrigger.getOp(), 
					appStrategyTrigger.getValue())) {
//				logger.info("knowledge_strategy[" + key + "] hit app_strategy[" + appStrategyTrigger.getAppStrategyName() + "]");
//				collector.emit(new Values(dataMap, appStrategyTrigger, command, arrValue));
				result.put("needGov", true);
			} else {
				result.put("needGov", false);
			}

			result.put("dataResult", arrValue[StrategyService.INDEX_CURRENT_VALUE]);
			return result;
//		}
	}
	
	private String DataAnalysisHandler(String command, Map<String, Object> dataMap) {
		return CommonHandler(command, dataMap, "dataAnalysis");
	}
	
	private String govHandler(String command, Map<String, Object> dataMap) {
		return CommonHandler(command, dataMap, "serviceGovermance");
	}
	
	
	/**
	 * 数据分析方案处理
	 * @param key
	 * @param appId
	 * @param os
	 * @param appVersion
	 * @return
	 */
	private String CommonHandler(String command, Map<String, Object> dataMap, String handlerName) {

		HttpURLConnection httpUrlsConnection = null;
		try {
			// 替换命令中的变量
			for (String field : dataMap.keySet()) {
				Object value = dataMap.get(field);
				command = command.replace("{"+field+"}", URLEncoder.encode(String.valueOf(value), "utf-8"));
			} 
			URL urlObj = new URL(command);
			URLConnection connection = urlObj.openConnection();
			httpUrlsConnection  =  (HttpURLConnection) connection;
			httpUrlsConnection.connect();
			String code = new Integer(httpUrlsConnection.getResponseCode()).toString();
			// 判断http请求返回码是否以2开头，比如200
			if(!code.startsWith("2")){
				logger.error(handlerName + " command open fail: command=" + command + ", ResponseCode is not begin with 2, code="+code);
				return null;
			}
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpUrlsConnection.getInputStream(),"utf-8"));	//设置编码,否则中文乱码
	        String lines;
	        JSONObject result = null;
	        while ((lines = reader.readLine()) != null){
	            result = (JSONObject) JSONObject.parse(lines);
	            break;
	        }
	        reader.close();
	        
	        // 返回结果必须带code且等于0
	        if(result == null || result.get("code") == null || !result.get("code").equals("0")) {
	        	logger.error(handlerName + " command fail, return code must be 0! result=" + result);
	        	return null;
	        } 
            logger.info(handlerName + " command success!");
            return (result.get("data")!=null)?result.get("data").toString():null;
		} catch (Exception e) {
			logger.error(handlerName + " command Exception: {}", e);
		} finally {
			if (httpUrlsConnection != null) {
				httpUrlsConnection.disconnect();
			}
		}
		return null;
	}

}
