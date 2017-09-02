package io.hyperion.managerPlatform.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.hyperion.managerPlatform.dao.KnowledgeStrategyBaseInfoDAO;
import io.hyperion.managerPlatform.dao.KnowledgeStrategyConfigDAO;
import io.hyperion.managerPlatform.dao.KnowledgeStrategyStatDAO;
import io.hyperion.managerPlatform.dao.StrategyAnalysisSummaryDAO;
import io.hyperion.managerPlatform.dto.KnowledgeStrategyBaseInfoDTO;
import io.hyperion.managerPlatform.dto.KnowledgeStrategyConfigDTO;
import io.hyperion.managerPlatform.dto.KnowledgeStrategyConfigDTO.AppStrategyTrigger;
import io.hyperion.managerPlatform.dto.KnowledgeStrategyStatDTO;
import io.hyperion.managerPlatform.dto.StrategyAnalysisSummaryDTO;
import io.hyperion.managerPlatform.utils.CommonUtil;
import io.hyperion.managerPlatform.utils.Const;
import io.hyperion.managerPlatform.utils.ResponseUtil;
import io.hyperion.managerPlatform.utils.ResultInfo;
import io.hyperion.managerPlatform.utils.StrategyUtil;
import io.hyperion.managerPlatform.vo.GetAllTriggersByKeyVo;
import io.hyperion.managerPlatform.vo.StrategyAnalysisSummaryVo;
import io.hyperion.managerPlatform.vo.StrategyAnalysisSummaryVo.Field4SASummaryVo;
import io.hyperion.managerPlatform.vo.StrategyStatVo;
import io.hyperion.managerPlatform.vo.StrategyStatVo.CountVo;
import io.hyperion.managerPlatform.vo.StrategyStatVo.FieldVo;

@RequestMapping("/strategy")
@Controller
public class StrategyStatController {
	
	private final Logger logger = Logger.getLogger(getClass());
	
	private final static String ALL_STRATEGY_ITEM_NAME = "所有策略";
	
	@Autowired
	private KnowledgeStrategyStatDAO knowledgeStrategyStatDAO;
	@Autowired
	private KnowledgeStrategyConfigDAO knowledgeStrategyConfigDAO;
	@Autowired
	private KnowledgeStrategyBaseInfoDAO knowledgeStrategyBaseInfoDAO;
	
	/**
	 * 根据策略key、appID、os、appVersion、triggerName获取统计数据
	 * @param key
	 * @param appID
	 * @param os
	 * @param appVersion
	 * @param triggerName
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping("/getStatByKeyAndTriggerAndBaseInfo")
	@ResponseBody
	public ResultInfo getStatByKeyAndTriggerAndBaseInfo(String key, String appID, String os, String appVersion, String triggerName, String beginTime, String endTime) {
		
		try {
			
			if(StringUtils.isBlank(key) || StringUtils.isBlank(beginTime) || StringUtils.isBlank(endTime)) {
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			
			String adjustBeginTime = CommonUtil.getAdjustTime(beginTime);
			String adjustEndTime = CommonUtil.getAdjustTime(endTime);

			StrategyStatVo strategyStatVo = new StrategyStatVo();
			List<FieldVo> fieldList = new ArrayList<FieldVo>();
			strategyStatVo.setKey(key);
			
			List<KnowledgeStrategyStatDTO> knowledgeStrategyStats = knowledgeStrategyStatDAO.getStatByKeyAndTriggerAndBaseInfo(key, appID, os, appVersion, triggerName, adjustBeginTime, adjustEndTime);
			//用一个map来保存统计信息，其中key为时间,value为统计数据
			 Map<String, Object> knowledgeStrategyStatMap = new HashMap<String, Object>();
			//如果knowledgeStrategyStats为空，则没有数据，所有返回的时间点的数据都为0,不需要往knowledgeStrategyStatMap里面添加数据
	        for (KnowledgeStrategyStatDTO knowledgeStrategyStat : knowledgeStrategyStats) {
	        	String time = knowledgeStrategyStat.getTime();
	        	String newValue = knowledgeStrategyStat.getValue().toString();
	        	// 如果newValue是非数字，则置为0
	        	if(!StrategyUtil.isNumber(newValue)) {
	        		newValue = "0";
	        	}
	        	Object oldValue = knowledgeStrategyStatMap.get(time);
	        	if(oldValue != null) {
	        		knowledgeStrategyStatMap.put(time, Float.valueOf(newValue) + Float.valueOf(oldValue.toString()));
	        	} else {
	        		knowledgeStrategyStatMap.put(time, Float.valueOf(newValue));
				}
			}
            
            List<CountVo> countList = new LinkedList<CountVo>();
            
            while(CommonUtil.getTimeInterVal(adjustEndTime, adjustBeginTime) >= 0) {
            	CountVo countVo = new CountVo();
            	countVo.setTime(adjustEndTime);
            	
            	//如果map中存在该时间点，值获取其值，如果没有改时间点，则其值为0
            	if(knowledgeStrategyStatMap.containsKey(adjustEndTime) && knowledgeStrategyStatMap.get(adjustEndTime) != null){
            		countVo.setValue(knowledgeStrategyStatMap.get(adjustEndTime));
            	}else {
            		countVo.setValue((float) 0);
            	}
            	countList.add(countVo);
            	adjustEndTime = CommonUtil.get30MinuteAgo(adjustEndTime);
            }
            
            FieldVo fieldVo = new FieldVo();
            String chartFieldName = (triggerName != null)?triggerName:ALL_STRATEGY_ITEM_NAME; 
            fieldVo.setField(chartFieldName); 
            fieldVo.setCountList(countList);
            
            fieldList.add(fieldVo);
			
            strategyStatVo.setFieldList(fieldList);

			return new ResultInfo(ResponseUtil.success_code, strategyStatVo);
		} catch (Exception e) {
			// TODO: handle exception
			logger.info(e);
			e.printStackTrace();
			return new ResultInfo(ResponseUtil.faile_code);
		}
	}
	
    /**
     * 根据策略key和field获取统计数据
     * @author CHENWEIXIN769
     * @param key
     * @param field
     * @param beginTime
     * @param endTime
     * @return
     */
	@RequestMapping("/getStatByKeyAndField")
	@ResponseBody
	public ResultInfo getStatByKeyAndField(String key, String field, String beginTime, String endTime) {
		
		try {
			
			if(StringUtils.isBlank(key) || StringUtils.isBlank(field) || StringUtils.isBlank(beginTime) || StringUtils.isBlank(endTime)) {
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			
			String adjustBeginTime = CommonUtil.getAdjustTime(beginTime);
			String adjustEndTime = CommonUtil.getAdjustTime(endTime);

			StrategyStatVo strategyStatVo = new StrategyStatVo();
			List<FieldVo> fieldList = new ArrayList<FieldVo>();
			strategyStatVo.setKey(key);
			
			List<KnowledgeStrategyStatDTO> knowledgeStrategyStats = knowledgeStrategyStatDAO.getStatByKeyAndField(key, field, adjustBeginTime, adjustEndTime);
			//用一个map来保存统计信息，其中key为时间,value为统计数据
			 Map<String, Float> knowledgeStrategyStatMap = new HashMap<String, Float>();
			 //如果knowledgeStrategyStats为空，则没有数据，所有返回的时间点的数据都为0,不需要往knowledgeStrategyStatMap里面添加数据
			if(knowledgeStrategyStats != null && knowledgeStrategyStats.size() > 0){
            for (KnowledgeStrategyStatDTO knowledgeStrategyStat : knowledgeStrategyStats) {
				knowledgeStrategyStatMap.put(knowledgeStrategyStat.getTime(), Float.valueOf(knowledgeStrategyStat.getValue().toString()));
			}
			}
            
            List<CountVo> countList = new LinkedList<CountVo>();
            
            while(CommonUtil.getTimeInterVal(adjustEndTime, adjustBeginTime) >= 0) {
            	CountVo countVo = new CountVo();
            	countVo.setTime(adjustEndTime);
            	
            	//如果map中存在该时间点，值获取其值，如果没有改时间点，则其值为0
            	if(knowledgeStrategyStatMap.containsKey(adjustEndTime) && knowledgeStrategyStatMap.get(adjustEndTime) != null){
            		countVo.setValue(knowledgeStrategyStatMap.get(adjustEndTime));
            	}else {
            		countVo.setValue((float) 0);
            	}
            	countList.add(countVo);
            	adjustEndTime = CommonUtil.get30MinuteAgo(adjustEndTime);
            }
            
            FieldVo fieldVo = new FieldVo();
            fieldVo.setField(field); 
            fieldVo.setCountList(countList);
            
            fieldList.add(fieldVo);
			
            strategyStatVo.setFieldList(fieldList);

			return new ResultInfo(ResponseUtil.success_code, strategyStatVo);
		} catch (Exception e) {
			// TODO: handle exception
			logger.info(e);
			e.printStackTrace();
			return new ResultInfo(ResponseUtil.faile_code);
		}
	}
	
	
	/**
	 * 获取某一 key下面所有field的统计
	 * @param key
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping("/getStatByKey")
	@ResponseBody
	public ResultInfo getStatByKey(String key, String beginTime, String endTime) {
		
		try {
			if (StringUtils.isBlank(key) || StringUtils.isBlank(beginTime)
					|| StringUtils.isBlank(endTime)) {
				return new ResultInfo(ResponseUtil.param_error_code);
			}

			String adjustBeginTime = CommonUtil.getAdjustTime(beginTime);
			String adjustEndTime = CommonUtil.getAdjustTime(endTime);

			StrategyStatVo strategyStatVo = new StrategyStatVo();
			List<KnowledgeStrategyStatDTO> knowledgeStrategyStats = knowledgeStrategyStatDAO
					.getStatByKey(key, adjustBeginTime, adjustEndTime);

			List<FieldVo> fieldList = new ArrayList<FieldVo>();
			strategyStatVo.setKey(key);

			// 用一个map来保存统计信息，其中key为"field+time",value为统计数据
			Map<String, Float> knowledgeStrategyStatMap = new HashMap<String, Float>();
			// 用一个fieldSet来保存所有该策略的field
			HashSet<String> fieldSet = new HashSet<String>();
			if (knowledgeStrategyStats != null
					&& knowledgeStrategyStats.size() > 0) {
				for (KnowledgeStrategyStatDTO knowledgeStrategyStat : knowledgeStrategyStats) {
					String fie = knowledgeStrategyStat.getTriggerName();
					String time = knowledgeStrategyStat.getTime();
					knowledgeStrategyStatMap.put(fie + time,
							Float.valueOf(knowledgeStrategyStat.getValue().toString()));
					fieldSet.add(fie);
				}
			}else{
				fieldSet.add("*");
			}

				for (String field : fieldSet) {
					List<CountVo> countList = new LinkedList<CountVo>();

					// 定义一个游动的时间,初始值为adjustEndTime,递减的时间间隔为30分钟
					String cursorTime = adjustEndTime;
					while (CommonUtil.getTimeInterVal(cursorTime,
							adjustBeginTime) >= 0) {
						String mapKey = field + cursorTime;
						CountVo countVo = new CountVo();
						countVo.setTime(cursorTime);

						// 如果map中存在该时间点，值获取其值，如果没有改时间点，则其值为0
						if (knowledgeStrategyStatMap.containsKey(mapKey)
								&& knowledgeStrategyStatMap.get(mapKey) != null) {
							countVo.setValue(knowledgeStrategyStatMap
									.get(mapKey));
						} else {
							countVo.setValue((float) 0);
						}
						countList.add(countVo);
						cursorTime = CommonUtil.get30MinuteAgo(cursorTime);
					}

					FieldVo fieldVo = new FieldVo();
					fieldVo.setField(field);
					fieldVo.setCountList(countList);

					fieldList.add(fieldVo);
				}

			strategyStatVo.setFieldList(fieldList);

			return new ResultInfo(ResponseUtil.success_code, strategyStatVo);

		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e);
			return new ResultInfo(ResponseUtil.faile_code);
		}
	}
	
	@RequestMapping("/getAllFieldByKey")
	@ResponseBody
	public ResultInfo getAllFieldByKey(String key) {
		
		try {
			List<KnowledgeStrategyStatDTO> knowledgeStrategyStats = knowledgeStrategyStatDAO
					.getAllFieldByKey(key);
			Set<String> allFields = new HashSet<String>(
					knowledgeStrategyStats.size());
			if (knowledgeStrategyStats != null
					|| knowledgeStrategyStats.size() > 0) {
				for (KnowledgeStrategyStatDTO knowledgeStrategyStatDTO : knowledgeStrategyStats) {
					allFields.add(knowledgeStrategyStatDTO.getTriggerName());
				}
			}

			return new ResultInfo(ResponseUtil.success_code, allFields);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResultInfo(ResponseUtil.faile_code);
		}
	}
	
	@RequestMapping("/getAllTriggersByKey")
	@ResponseBody
	public ResultInfo getAllTriggersByKey(String key) {
		
		try {
			KnowledgeStrategyConfigDTO knowledgeStrategyConfigDTO = knowledgeStrategyConfigDAO.getConfigByKey(key);
			List<GetAllTriggersByKeyVo> allTriggers = new ArrayList<GetAllTriggersByKeyVo>();
			if (knowledgeStrategyConfigDTO.getAppStrategyTriggers() != null) {
				for (AppStrategyTrigger appStrategyTrigger : knowledgeStrategyConfigDTO.getAppStrategyTriggers()) {
					GetAllTriggersByKeyVo triggerInfo = new GetAllTriggersByKeyVo();
					triggerInfo.setName(appStrategyTrigger.getName());
					int chart = 0;
					// 如果数据分析方案需要展现的是图表
					if(Arrays.asList(Const.needShowChartStrategyList).contains(appStrategyTrigger.getDataStrategyName())) {
						chart = 1;
					}
					triggerInfo.setChart(chart);
					allTriggers.add(triggerInfo);
				}
			}

			return new ResultInfo(ResponseUtil.success_code, allTriggers);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResultInfo(ResponseUtil.faile_code);
		}
	}
	

	@RequestMapping("/getAllBaseInfoByKey")
	@ResponseBody
	public ResultInfo getAllBaseInfoByKey(String key) {
		
		try {
			KnowledgeStrategyBaseInfoDTO knowledgeStrategyBaseInfoDTO = knowledgeStrategyBaseInfoDAO.getBaseInfo(key);
			Map<String, Object> data = new HashMap<String, Object>();
			Map<String, Object> allBaseInfo = new HashMap<String, Object>();
			if(knowledgeStrategyBaseInfoDTO != null) {
				List<String> appIDList = (knowledgeStrategyBaseInfoDTO.getAPPID()!=null)?Arrays.asList(knowledgeStrategyBaseInfoDTO.getAPPID()):new ArrayList<String>();
				List<String> osList = (knowledgeStrategyBaseInfoDTO.getOS()!=null)?Arrays.asList(knowledgeStrategyBaseInfoDTO.getOS()):new ArrayList<String>();
				List<String> appVersionList = (knowledgeStrategyBaseInfoDTO.getAPPVERSION()!=null)?Arrays.asList(knowledgeStrategyBaseInfoDTO.getAPPVERSION()):new ArrayList<String>();
				
				allBaseInfo.put("appID", appIDList);
				allBaseInfo.put("os", osList);
				allBaseInfo.put("appVersion", appVersionList);
			}
			data.put("baseInfo", allBaseInfo);
			return new ResultInfo(ResponseUtil.success_code, data);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResultInfo(ResponseUtil.faile_code);
		}
	}
	
}
