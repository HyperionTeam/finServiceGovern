package io.hyperion.managerPlatform.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import io.hyperion.managerPlatform.dao.AppStrategyConfigDAO;
import io.hyperion.managerPlatform.dao.KnowledgeStrategyConfigDAO;
import io.hyperion.managerPlatform.dto.AppStrategyConfigDTO;
import io.hyperion.managerPlatform.dto.KnowledgeStrategyConfigDTO;
import io.hyperion.managerPlatform.dto.KnowledgeStrategyConfigDTO.AppStrategyTrigger;
import io.hyperion.managerPlatform.sqlengine.SQLEngine;
import io.hyperion.managerPlatform.utils.Const;
import io.hyperion.managerPlatform.utils.ResponseUtil;
import io.hyperion.managerPlatform.utils.ResultInfo;
import io.hyperion.managerPlatform.vo.AllAppStrategyVo;
import io.hyperion.managerPlatform.vo.AllAppStrategyVo.AllAppStrategy;
import io.hyperion.managerPlatform.vo.AllKnowledgeStrategyVo;
import io.hyperion.managerPlatform.vo.AllKnowledgeStrategyVo.AllKnowledgeStrategy;
import io.hyperion.managerPlatform.vo.OneAppStrategyVo;
import io.hyperion.managerPlatform.vo.OneKnowledgeStrategyVo;


import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;

/**
 * 
 * @author heliuxing
 *
 */
@RequestMapping("/strategy")
@Controller
public class StrategyConfigController {
	
	private final Logger logger = Logger.getLogger(getClass());

	private final static int TYPE_AI = 1; // 机器学习分析策略
	private final static int TYPE_TA = 2; // 简单文本分析策略
	
	private final static String REGX_NAME = "^[A-Za-z0-9\\-_]+$"; //正则表达式，匹配字母、数字、-和_
	private final static String REGX_STRATEGY_KEY = "^[A-Za-z]+[A-Za-z0-9]*$"; //正则表达式，匹配字母、数字、-和_
	

	@Autowired
	private KnowledgeStrategyConfigDAO knowledgeStrategyConfigDAO;
	
	@Autowired
	private AppStrategyConfigDAO appStrategyConfigDAO;


	/*
	 * 分页获取知识策略
	 * @param request
	 * @return
	 */
	
	@RequestMapping(value = "/getKnowledgeStrategyByPage", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo getKnowledgeStrategyByPage(HttpServletRequest request) {
		try {
			String offset = request.getParameter("offset");
			String limit = request.getParameter("limit");
			if(StringUtils.isBlank(offset) || StringUtils.isBlank(limit) || Integer.valueOf(offset) < 1 ||Integer.valueOf(limit) < 1){
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			
			Integer offsetInt = Integer.valueOf(offset);
			Integer limitInt = Integer.valueOf(limit);
			long strategyNumbers = knowledgeStrategyConfigDAO.getKnowledgeStrategyNumber();
			Long totalPageNumer = (strategyNumbers % limitInt) == 0 ? strategyNumbers/limitInt :(strategyNumbers/limitInt) + 1;
			//如果请求的页码大于总页数，则返回错误
			if(totalPageNumer > 0 && offsetInt > totalPageNumer) {
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			
			Integer skip = (offsetInt-1) * limitInt;
			List<KnowledgeStrategyConfigDTO> knowledgeStrategyConfigs = knowledgeStrategyConfigDAO.getKnowledgeStrategyByPage(skip, limitInt);
			AllKnowledgeStrategyVo allKnowledgeStrategyVo = new AllKnowledgeStrategyVo();
			List<AllKnowledgeStrategy> knowledgeStrategies = new ArrayList<AllKnowledgeStrategy>();
			if (strategyNumbers > 0 && knowledgeStrategyConfigs.size() > 0) {
				for (KnowledgeStrategyConfigDTO knowledgeStrategyConfig : knowledgeStrategyConfigs) {
					 AllKnowledgeStrategy allKnowledgeStrategy = new AllKnowledgeStrategy();
					 allKnowledgeStrategy.setKey(knowledgeStrategyConfig.getKey());
					 allKnowledgeStrategy.setStatus(knowledgeStrategyConfig.getStatus());
					 allKnowledgeStrategy.setOpTime(knowledgeStrategyConfig.getOpTime());
	                 knowledgeStrategies.add(allKnowledgeStrategy);
				}
			}
			allKnowledgeStrategyVo.setStrategy(knowledgeStrategies);
			allKnowledgeStrategyVo.setTotalPageNumber(totalPageNumer);
			allKnowledgeStrategyVo.setCurrentPageNumber(offsetInt);
			return new ResultInfo(ResponseUtil.success_code, allKnowledgeStrategyVo);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResultInfo(ResponseUtil.faile_code);
		}
	}


	/*
	 * 添加知识策略
	 * @param request
	 * @return
	 */
	
	@RequestMapping(value = "/addKnowledgeStrategy", method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo addKnowledgeStrategy(HttpServletRequest request) {
		
		try {
//			String jsonStr = CommonUtils
//					.Stream2String(request.getInputStream());
			String jsonStr = request.getParameter("subJson");
			if(StringUtils.isBlank(jsonStr)) {
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			
			KnowledgeStrategyConfigDTO knowledgeStrategyConfig = JSON
					.parseObject(jsonStr, KnowledgeStrategyConfigDTO.class);
			if (knowledgeStrategyConfig == null
					|| "".equals(knowledgeStrategyConfig)) {
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			
			String key = knowledgeStrategyConfig.getKey();
			if(!key.matches(REGX_STRATEGY_KEY)){
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			
			String description = knowledgeStrategyConfig.getDescription();
			Integer status = knowledgeStrategyConfig.getStatus();
			Integer expire = knowledgeStrategyConfig.getExpire();
			List<AppStrategyTrigger> appStrategyTriggers = knowledgeStrategyConfig
					.getAppStrategyTriggers();
			if (StringUtils.isBlank(key) || StringUtils.isBlank(description)
					|| appStrategyTriggers.size() < 0 || expire < 0
					|| (status != Const.DISABLE && status != Const.ENABLE)) {
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			
			if(!validateAppStrategyTrigger(knowledgeStrategyConfig)){
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String opTime = sdf.format(new Date());
			knowledgeStrategyConfig.setOpTime(opTime);
			knowledgeStrategyConfigDAO
					.insertKnowledgeStrategy(knowledgeStrategyConfig);
			return new ResultInfo(ResponseUtil.success_code);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("======:" + e.getMessage());
			return new ResultInfo(ResponseUtil.faile_code, e.getMessage(), null);
		}
	}
	

	/*
	 * 获取单条知识策略
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getKnowledgeStrategyByName" , method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo getKnowledgeStrategyByName(HttpServletRequest request){
		
		try {
			OneKnowledgeStrategyVo oneKnowledgeStrategyVo = new OneKnowledgeStrategyVo();
			String name = request.getParameter("name");
			if(StringUtils.isBlank(name)){
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			KnowledgeStrategyConfigDTO knowledgeStrategyConfigDTO = knowledgeStrategyConfigDAO.getConfigByKey(name);
			if(knowledgeStrategyConfigDTO == null){
				return new ResultInfo(ResponseUtil.faile_code);
			}
			oneKnowledgeStrategyVo.setStrategy(knowledgeStrategyConfigDTO);

			return new ResultInfo(ResponseUtil.success_code, oneKnowledgeStrategyVo);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResultInfo(ResponseUtil.faile_code);
		}
	}
	

	/*
	 * 修改单条知识策略
	 * @param request
	 * @return
	 */
	
	@RequestMapping(value = "/modifyKnowledgeStrategyByName" , method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo modifyKnowledgeStrategyByName (HttpServletRequest request) {
		
		try {
//			String jsonStr = CommonUtils.Stream2String(request.getInputStream());
			String jsonStr = request.getParameter("subJson");
			if(StringUtils.isBlank(jsonStr)) {
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			
			KnowledgeStrategyConfigDTO knowledgeStrategyConfig = JSON.parseObject(jsonStr, KnowledgeStrategyConfigDTO.class);
			if (knowledgeStrategyConfig == null
					|| "".equals(knowledgeStrategyConfig)) {
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			
			String key = knowledgeStrategyConfig.getKey();
			if(!key.matches(REGX_STRATEGY_KEY)){
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			
			String description = knowledgeStrategyConfig.getDescription();
			Integer status = knowledgeStrategyConfig.getStatus();
			Integer expire = knowledgeStrategyConfig.getExpire();
			List<AppStrategyTrigger> appStrategyTriggers = knowledgeStrategyConfig
					.getAppStrategyTriggers();
			if (StringUtils.isBlank(key) || StringUtils.isBlank(description)
					|| appStrategyTriggers.size() < 0 || expire < 0
					|| (status != Const.DISABLE && status != Const.ENABLE)) {
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			
			if(!validateAppStrategyTrigger(knowledgeStrategyConfig)){
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String opTime = sdf.format(new Date());
			knowledgeStrategyConfig.setOpTime(opTime);
			boolean updateSuccess = knowledgeStrategyConfigDAO.modifyKnowledgeStrategyByName(knowledgeStrategyConfig);
			if(updateSuccess) {
				return new ResultInfo(ResponseUtil.success_code);
			}else{
				return new ResultInfo(ResponseUtil.faile_code);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResultInfo(ResponseUtil.faile_code);
		}
	}
	

	/*
	 * 删除单条知识策略
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deleteKnowledgeStrategyByName" , method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo deleteKnowledgeStrategyByName(HttpServletRequest request) {
		
		try {
			String name = request.getParameter("name");
			if(StringUtils.isBlank(name)){
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			boolean deleteSuccess = knowledgeStrategyConfigDAO.deleteKnowledgeStrategyByName(name);
			if(deleteSuccess){
				return new ResultInfo(ResponseUtil.success_code);
			}else{
				return new ResultInfo(ResponseUtil.faile_code);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResultInfo(ResponseUtil.faile_code);
		}
	}
	
	/*
	 * 分页获取应用策略
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getAppStrategyByPage" , method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo getAppStrategyByPage(HttpServletRequest request) {
		
		try {
			String offset = request.getParameter("offset");
			String limit = request.getParameter("limit");
			if(StringUtils.isBlank(offset) || StringUtils.isBlank(limit) || Integer.valueOf(offset) < 1 ||Integer.valueOf(limit) < 1){
				return new ResultInfo(ResponseUtil.param_error_code);
			}

			Integer offsetInt = Integer.valueOf(offset);
			Integer limitInt = Integer.valueOf(limit);
			long strategyNumbers = appStrategyConfigDAO.getAppStrategyNumber();
			Long totalPageNumer = (strategyNumbers % limitInt) == 0 ? strategyNumbers/limitInt :(strategyNumbers/limitInt) + 1;
			//如果请求的页码大于总页数，则返回错误
			if(totalPageNumer > 0 && offsetInt > totalPageNumer) {
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			
			Integer skip = (offsetInt-1) * limitInt;
			List<AppStrategyConfigDTO> appStrategyConfigs = appStrategyConfigDAO.getAppStrategyByPage(skip, limitInt);
			AllAppStrategyVo allAppStrategyVo = new AllAppStrategyVo();
			List<AllAppStrategy> appStrategies = new ArrayList<AllAppStrategy>();
			if(strategyNumbers > 0 && appStrategyConfigs.size() > 0) {
				for (AppStrategyConfigDTO appStrategyConfig : appStrategyConfigs) {
					AllAppStrategy allAppStrategy = new AllAppStrategy();
					allAppStrategy.setKey(appStrategyConfig.getKey());
					allAppStrategy.setOpTime(appStrategyConfig.getOpTime());
					appStrategies.add(allAppStrategy);
				}
			}
			allAppStrategyVo.setStrategy(appStrategies);
			allAppStrategyVo.setTotalPageNumber(totalPageNumer);
			allAppStrategyVo.setCurrentPageNumber(offsetInt);
			return new ResultInfo(ResponseUtil.success_code, allAppStrategyVo);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResultInfo(ResponseUtil.faile_code);
		}
	}
	
	
	/*
	 * 添加应用策略
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/addAppStrategy" , method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo addAppStrategy (HttpServletRequest request) {
		
		try {
//			String jsonStr = CommonUtils
//					.Stream2String(request.getInputStream());
			String jsonStr = request.getParameter("subJson");
			if(StringUtils.isBlank(jsonStr)) {
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			AppStrategyConfigDTO appStrategyConfig = JSON.parseObject(jsonStr, AppStrategyConfigDTO.class);
			if (appStrategyConfig == null
					|| "".equals(appStrategyConfig)) {
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			
			String key = appStrategyConfig.getKey();
			if(!key.matches(REGX_NAME)){
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			
			String description = appStrategyConfig.getDescription();
			String command = appStrategyConfig.getCommand();
			if(StringUtils.isBlank(key) || StringUtils.isBlank(description) || StringUtils.isBlank(command)) {
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String opTime = sdf.format(new Date());
			appStrategyConfig.setOpTime(opTime);
			
			appStrategyConfigDAO.insertAppStrategy(appStrategyConfig);
			return new ResultInfo(ResponseUtil.success_code);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResultInfo(ResponseUtil.faile_code);
		}
	}
	
	/*
	 * 修改应用策略
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyAppStategyByName" , method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo modifyAppStategyByName(HttpServletRequest request){
		
		try {
//			String jsonStr = CommonUtils
//					.Stream2String(request.getInputStream());
			String jsonStr = request.getParameter("subJson");
			if(StringUtils.isBlank(jsonStr)) {
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			
			AppStrategyConfigDTO appStrategyConfig = JSON.parseObject(jsonStr, AppStrategyConfigDTO.class);
			if (appStrategyConfig == null
					|| "".equals(appStrategyConfig)) {
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			
			String key = appStrategyConfig.getKey();
			if(!key.matches(REGX_NAME)){
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			
			String description = appStrategyConfig.getDescription();
			String command = appStrategyConfig.getCommand();
			if(StringUtils.isBlank(key) || StringUtils.isBlank(description) || StringUtils.isBlank(command)) {
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			
			AppStrategyConfigDTO appStrategyConfigDTO = appStrategyConfigDAO.getConfigByKey(key);
			if(appStrategyConfigDTO == null) {
				return new ResultInfo(ResponseUtil.faile_code);
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String opTime = sdf.format(new Date());
			appStrategyConfig.setOpTime(opTime);
			boolean updateSuccess = appStrategyConfigDAO.modifyAppStrategy(appStrategyConfig);
			if (updateSuccess) {
				return new ResultInfo(ResponseUtil.success_code);
			}else{
				return new ResultInfo(ResponseUtil.faile_code);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResultInfo(ResponseUtil.faile_code);
		}
	}
	
	/*
	 * 获取单条应用策略
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getAppStrategyByName" , method = RequestMethod.POST)
	@ResponseBody
	public ResultInfo getAppStrategyByName (HttpServletRequest request){
		
		try {
			String name = request.getParameter("name");
			if(StringUtils.isBlank(name)){
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			AppStrategyConfigDTO appStrategyConfigDTO = appStrategyConfigDAO.getConfigByKey(name);
			if(appStrategyConfigDTO == null) {
				return new ResultInfo(ResponseUtil.param_error_code);
			}
			OneAppStrategyVo oneAppStrategyVo = new OneAppStrategyVo();
			oneAppStrategyVo.setStrategy(appStrategyConfigDTO);
			return new ResultInfo(ResponseUtil.success_code, oneAppStrategyVo);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResultInfo(ResponseUtil.faile_code);
		}
	}
	
	/*
	 * 获取所有的应用策略
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getAllAppStrategy" , method = RequestMethod.GET)
	@ResponseBody
	public ResultInfo getAllAppStrategy (){
		
		try {
			List<AppStrategyConfigDTO> appStrategyConfigs = appStrategyConfigDAO.getAllAppStrategy();
			AllAppStrategyVo allAppStrategyVo = new AllAppStrategyVo();
			List<AllAppStrategy> appStrategies = new ArrayList<AllAppStrategy>();
			if(appStrategyConfigs.size() > 0) {
				for (AppStrategyConfigDTO appStrategyConfig : appStrategyConfigs) {
					AllAppStrategy allAppStrategy = new AllAppStrategy();
					allAppStrategy.setKey(appStrategyConfig.getKey());
					allAppStrategy.setOpTime(appStrategyConfig.getOpTime());
					appStrategies.add(allAppStrategy);
				}
			}
			allAppStrategyVo.setStrategy(appStrategies);
			return new ResultInfo(ResponseUtil.success_code, allAppStrategyVo);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResultInfo(ResponseUtil.faile_code);
		}
	}
	
	
	/**
	 * 获取所有已持久化的策略
	 * @author CHENWEIXIN769
	 * @return
	 */
	@RequestMapping("/getAllPersistentedStrategy")
	@ResponseBody
	public ResultInfo getAllKey() {
		
		try {
//			List<KnowledgeStrategyConfigDTO> knowledgeStrategys = knowledgeStrategyConfigDAO.getAllPersistentedStrategy(Const.ENABLE, Const.ENABLE, Const.PERSISTENCE_EXPIRETIME);
			int skip = 0;
			int limit = 100000;
			List<KnowledgeStrategyConfigDTO> knowledgeStrategys = knowledgeStrategyConfigDAO.getKnowledgeStrategyByPage(skip, limit);
			List<String> persistentedKnowledgeStrategys = new ArrayList<String>(knowledgeStrategys.size());
			for(KnowledgeStrategyConfigDTO knowledgeStrategyConfigDTO : knowledgeStrategys) {
			    persistentedKnowledgeStrategys.add(knowledgeStrategyConfigDTO.getKey());	
			}

			return new ResultInfo(ResponseUtil.success_code, persistentedKnowledgeStrategys);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResultInfo(ResponseUtil.faile_code);
		}
	}
	
	/**
	 * 修改或增加知识策略时进行的检查策略
	 * @param appStrategyTriggers
	 * @return
	 * @throws Exception
	 */
	private boolean validateAppStrategyTrigger(KnowledgeStrategyConfigDTO knowledgeStrategyConfig) throws Exception {
		List<AppStrategyTrigger> appStrategyTriggers = knowledgeStrategyConfig.getAppStrategyTriggers();
		int expire = knowledgeStrategyConfig.getExpire();
		String knowledgeStrategyName = knowledgeStrategyConfig.getKey();
		List<String> nameList = new ArrayList<String>();
		for (AppStrategyTrigger appStrategyTrigger : appStrategyTriggers) {
			String name = appStrategyTrigger.getName();
			String sql = appStrategyTrigger.getSql();
			String op = appStrategyTrigger.getOp();
			Object value = appStrategyTrigger.getValue();
			String appStrategyName = appStrategyTrigger.getAppStrategyName();
			Integer persistent = appStrategyTrigger.getPersistent();
			int type = appStrategyTrigger.getType();
			if(StringUtils.isBlank(name) || StringUtils.isBlank(sql) || StringUtils.isBlank(op) || StringUtils.isBlank(value.toString()) ||
			   StringUtils.isBlank(appStrategyName) || (persistent != Const.DISABLE && persistent != Const.ENABLE)){
				logger.error("app strategy config fail");
				return false;
			}
			// 如果过期时间未到最小阀值，则不允许持久化
			if(persistent == Const.ENABLE && expire < Const.PERSISTENCE_EXPIRETIME) {
				throw new Exception("数据过期时间过小，不允许持久化知识数据");
			}
			// trigger名重复
			if(nameList.contains(name)) {
				throw new Exception("name="+name+" duplicate");
			}
			nameList.add(name);
			
			if (type == TYPE_TA) {
				// 检查sql
				Select select = SQLEngine.parse(sql);
				if(select == null) {
					throw new Exception("sql非法");
				}
				// 获取表名
				TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
				List<String> tableList = tablesNamesFinder.getTableList(select);
				String tableName = tableList.get(0);
				if(!tableName.equals(knowledgeStrategyName)) {
					throw new Exception("sql中的表名必须为知识策略名");
				}
			}
		}
		return true;
	}
	
}
