package me.star2478.jstorm.bolt;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.mongodb.DB;

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
import me.star2478.jstorm.dao.KnowledgeStrategyStatDAO;
import me.star2478.jstorm.dto.KnowledgeStrategyStatDTO;
import me.star2478.jstorm.dto.AppStrategyConfigDTO;
import me.star2478.jstorm.dto.KnowledgeStrategyConfigDTO.AppStrategyTrigger;
import me.star2478.jstorm.service.StrategyService;
import me.star2478.jstorm.service.StrategyServiceFactory;
import me.star2478.jstorm.sqlengine.SQLEngine;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import storm.trident.testing.IFeeder;

public class ApplicationAnalysisBolt extends BaseRichBolt {

	private OutputCollector collector;
	
	private static Logger logger = Logger.getLogger(ApplicationAnalysisBolt.class);

	private StrategyServiceFactory factory;
	
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		factory = (StrategyServiceFactory) JstormMain.ctx.getBean("StrategyServiceFactory");
	}

	public void execute(Tuple input) {

		HttpURLConnection httpUrlsConnection = null;
		try {
			Map<String, Object> dataMap = (Map<String, Object>) input.getValueByField("data");
			AppStrategyTrigger appStrategyTrigger = (AppStrategyTrigger) input.getValueByField("appStrategyTrigger");
			String command = (String) input.getValueByField("command");
			Object[] arrValue = (Object[]) input.getValueByField("arrValue");
			
			String key = dataMap.get("key").toString();
			int persistent = appStrategyTrigger.getPersistent();
			String appStrategyName = appStrategyTrigger.getAppStrategyName();
			String fieldForRedis = StrategyUtil.strcatKnowledgeRedisField(
					dataMap.get("appId").toString(), 
					dataMap.get("os").toString(), 
					dataMap.get("appVersion").toString(),
					appStrategyTrigger.getName());
			Object curKnowledge = arrValue[StrategyService.INDEX_CURRENT_VALUE];
			Object persistentedKnowledge = arrValue[StrategyService.INDEX_PERSISTENTED_VALUE];
			StrategyService strategyService = factory.getKnowledgeStrategyBeanByCommand(command);
			dataMap.put("curKnowledge", curKnowledge);
			dataMap.put("info", JSON.toJSONString(dataMap));
			
			// 执行应用策略
			AppStrategyConfigDTO appStrategyConfigDTO = strategyService.getAppStrategyConfig(appStrategyName);
			String appCommand = appStrategyConfigDTO.getCommand();
			
			// 替换命令中的变量
			for (String field : dataMap.keySet()) {
				Object value = dataMap.get(field);
				appCommand = appCommand.replace("{"+field+"}", URLEncoder.encode(String.valueOf(value), "utf-8"));
			}
			
			// 发起http请求
//			logger.error("========" + appCommand);
//			logger.error("========" + URLEncoder.encode(appCommand, "utf-8"));
//			appCommand = "http://119.29.91.153:5001/govMailNotify?info=abc";
			URL urlObj = new URL(appCommand);
			URLConnection connection = urlObj.openConnection();
			httpUrlsConnection  =  (HttpURLConnection) connection;
			httpUrlsConnection.connect();
			    
			String code = new Integer(httpUrlsConnection.getResponseCode()).toString();

			//String message = httpUrlConnection.getResponseMessage();
			// 判断http请求返回码是否以2开头，比如200
			if(!code.startsWith("2")){
				logger.error("app_strategy[" + appStrategyName + "] open fail: appCommand["
						+ appCommand + "]: ResponseCode is not begin with 2, code="+code);
				return ;
			}
			
			// 将知识数据持久化到mongodb
			if (persistent == 1) {
				Object val2Persistent = strategyService.calValue2Persistent(curKnowledge, persistentedKnowledge);
				Map<String, Object> persistentMap = new HashMap<String, Object>();
				persistentMap.put(fieldForRedis, val2Persistent);
				strategyService.saveData2MogonDB(key, persistentMap, command);
			}
			// 删除redis对应的知识数据，从零开始统计知识数据
			strategyService.hdelKnowledgeInRedis(key, fieldForRedis, (fieldForRedis + StrategyService.PERSISTENTED_SUFFIX));

            logger.info("app strategy bolt success!");
		} catch (Exception e) {
			logger.error("app strategy bolt Exception:" + e);
			e.printStackTrace();
		} finally {
			httpUrlsConnection.disconnect();
		}

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
	}
}
