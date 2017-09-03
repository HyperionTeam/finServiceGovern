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
public class StrategyAnalysisSummaryController {
	
	private final Logger logger = Logger.getLogger(getClass());
	
	private final static String ALL_STRATEGY_ITEM_NAME = "所有策略";
	
	@Autowired
	private StrategyAnalysisSummaryDAO strategyAnalysisSummaryDAO;
	
	
	/**
	 * 根据策略key、appID、os、appVersion、triggerName获取统计数据
	 * @param key 数据源
	 * @param appID
	 * @param os
	 * @param appVersion
	 * @param triggerName
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping("/getDataListByKeyAndTriggerAndBaseInfo")
	@ResponseBody
	public ResultInfo getDataListByKeyAndTriggerAndBaseInfo(String key, String appID, String os, String appVersion, String triggerName, String beginTime, String endTime) {
		
		try {
			
			if(StringUtils.isBlank(key) || StringUtils.isBlank(beginTime) || StringUtils.isBlank(endTime)) {
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			
			String adjustBeginTime = CommonUtil.getAdjustTime(beginTime);
			String adjustEndTime = CommonUtil.getAdjustTime(endTime);

			StrategyAnalysisSummaryVo strategyVo = new StrategyAnalysisSummaryVo();
			List<Field4SASummaryVo> fieldList = new ArrayList<Field4SASummaryVo>();
			strategyVo.setKey(key);
			
			List<StrategyAnalysisSummaryDTO> strategyAnalysisSummaries = strategyAnalysisSummaryDAO.getStatByKeyAndTriggerAndBaseInfo(key, appID, os, appVersion, triggerName, adjustBeginTime, adjustEndTime);
                
            Field4SASummaryVo fieldVo = new Field4SASummaryVo();
            String chartFieldName = (triggerName != null)?triggerName:ALL_STRATEGY_ITEM_NAME; 
            fieldVo.setField(chartFieldName); 
            fieldVo.setCountList(strategyAnalysisSummaries);
            
            fieldList.add(fieldVo);
			
            strategyVo.setFieldList(fieldList);

			return new ResultInfo(ResponseUtil.success_code, strategyVo);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e);
			e.printStackTrace();
			return new ResultInfo(ResponseUtil.faile_code);
		}
	}
	
	/**
	 * 获取最近一个summary
	 * @param key
	 * @param triggerName
	 * @return
	 */
	@RequestMapping("/getLastestSummary")
	@ResponseBody
	public ResultInfo getTheLastestSummary(String key, String triggerName) {
		
		try {
			
			if(StringUtils.isBlank(key) || StringUtils.isBlank(triggerName)) {
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			
			StrategyAnalysisSummaryDTO strategyAnalysisSummary = strategyAnalysisSummaryDAO.getTheLastestSummary(key, triggerName);
			strategyAnalysisSummary.setEventList(strategyAnalysisSummary.getEvenList());
			
			return new ResultInfo(ResponseUtil.success_code, strategyAnalysisSummary);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e);
			e.printStackTrace();
			return new ResultInfo(ResponseUtil.faile_code);
		}
	}
	
}
