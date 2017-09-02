package me.star2478.jstorm.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.discovery.converters.Auto;

import me.star2478.jstorm.common.StrategyUtil;
import me.star2478.jstorm.dao.AppStrategyConfigDAO;
import me.star2478.jstorm.dao.KnowledgeStrategyBaseInfoDAO;
import me.star2478.jstorm.dao.KnowledgeStrategyConfigDAO;
import me.star2478.jstorm.dao.KnowledgeStrategyStatDAO;
import me.star2478.jstorm.dao.StrategyAnalysisSummaryDAO;
import me.star2478.jstorm.dto.AppStrategyConfigDTO;
import me.star2478.jstorm.dto.KnowledgeStrategyConfigDTO;
import me.star2478.jstorm.dto.KnowledgeStrategyStatDTO;
import me.star2478.jstorm.dto.StrategyAnalysisSummaryDTO;
import me.star2478.jstorm.dto.KnowledgeStrategyConfigDTO.AppStrategyTrigger;
import me.star2478.jstorm.redis.KnowledgeRedis;
import me.star2478.jstorm.sqlengine.SQLEngine;


/**
 * 
 * @author heliuxing
 *
 */
@Service
public abstract class StrategyService {
	
	private static Logger logger = Logger.getLogger(StrategyService.class);
	
	public final static String ANY_KEY = "*"; // 代表任意key
	
	public final static String REDIS_BASE_INFO_SEPARATOR = ","; // redis中基本信息数据，value值间的分隔符
	public enum BaseInfoEnum {
        APPID, OS, APPVERSION;
    }
	
	// 定位状态：0-定位未触发，1-定位中，2-定位成功，3-定位失败
	public enum DataAnalysisStatusEnum {
        NOGOV, DOING, SUCCESS, FAIL;
    }
	
	// 治理状态：0-治理未触发，1-治理中，2-治理成功，3-治理失败
	public enum GovStatusEnum {
        NOGOV, DOING, SUCCESS, FAIL;
    }
	
	public final static String REDIS_BASE_INFO_SUFFIX = "-BaseInfo"; // redis中，基本信息（appId、os、appVersion等）会存入具有此后缀的key中

	public final static String EXPIRETIME_KEYNAME = "expireTime"; // 过期时间在redis中的key名
	public final static String PERSISTENTED_SUFFIX = "-Persistented"; // 已持久化的数值会存入具有此后缀的key中
	
	public final static int INDEX_CURRENT_VALUE = 0; // 当前值所在下标
	public final static int INDEX_PERSISTENTED_VALUE = 1; // 已持久化值所在下标
	
	public abstract Object calValue2Persistent(Object curVal, Object persistentedVal);
	
	public abstract Object incMethodHandler(String key, String field, Object value);
	
	public abstract KnowledgeRedis getKnowledgeRedis();
	
	public abstract Object getByteMapValueByKey(Map<String, Object> map, String key);
	
	public abstract boolean checkOpenAppStrategy(String field, Object curVal, String key4AppStrategy, String op4AppStrategy, Object value4AppStrategy);
	
	public abstract boolean checkOpenAppStrategy(Object knowledgeVal, String op4AppStrategy, Object value4AppStrategy);
	
	// 当redis中没有相应知识数据时，redis知识数据的更新策略
	public abstract void updRedisWithoutKnowledge(String key, Map<String, Object> map);
	
	// 当redis中知识数据超时时，redis知识数据的更新策略
	public abstract void updRedisWhenExpireTimeout(String key, Map<String, Object> map, String... delFields);
	
	// 正常情况下的，redis知识数据的更新策略
	public abstract Object updRedisUnderNormal(String key, String field, Object value);
	
	public abstract void hdelKnowledgeInRedis(String key, String... delFields);

	@Autowired
	private KnowledgeStrategyConfigDAO knowledgeStrategyConfigDAO;

	@Autowired
	private KnowledgeStrategyBaseInfoDAO knowledgeStrategyBaseInfoDAO;

	@Autowired
	private KnowledgeStrategyStatDAO knowledgeStrategyStatDAO;
	
	@Autowired
	private AppStrategyConfigDAO appStrategyConfigDAO;
	
	@Autowired
	private StrategyAnalysisSummaryDAO strategyAnalysisSummaryDAO;
	
	@Autowired
	protected KnowledgeRedis knowledgeRedis;
	
	/**
	 * 将知识数据存入redis，然后返回当前数据值
	 * @param key
	 * @param appId
	 * @param os
	 * @param appVersion
	 * @param triggerName
	 * @param value
	 * @param knowledgeInRedis：redis中的老知识数据
	 * @param expire
	 * @return 一个具有2个元素的Object数组：[0]存放redis中当前数值，[1]存放已持久化的数值
	 */
	public Object[] getCurrentValue(String key, String appId, String os, String appVersion, String triggerName, 
			Object value, Map<String, Object> knowledgeInRedis, int expire) {
		Object newVal[] = new Object[2];
		newVal[INDEX_CURRENT_VALUE] = value;

		// 知识数据不需要存入redis，直接返回当前值
		if (expire <= 0) {
			return newVal;
		}
		String fieldForRedis = StrategyUtil.strcatKnowledgeRedisField(appId, os, appVersion, triggerName);
//		byte expireTimeKeyByte[] = EXPIRETIME_KEYNAME.getBytes();
//		byte valueByte[] = String.valueOf(value).getBytes();
//		byte expireTimeByte[] = expireTime.getBytes();
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 从redis获取对应的知识数据
		String expireTime = StrategyUtil.calExpireTime(expire);
		map.put(fieldForRedis, value.toString());
		map.put(EXPIRETIME_KEYNAME, expireTime);
		
		// 如果redis中该策略没有任何知识数据，把新的知识策略写入redis
		if (knowledgeInRedis == null || knowledgeInRedis.keySet().size() <= 0) {
			updRedisWithoutKnowledge(key, map);
			return newVal;
		}
		
		// 如果知识数据已过期，把新的知识策略写入redis
		if (StrategyUtil.checkTimeOut((String)knowledgeInRedis.get(EXPIRETIME_KEYNAME))) {
			updRedisWhenExpireTimeout(key, map, fieldForRedis, EXPIRETIME_KEYNAME);
			return newVal;
		}

		newVal[INDEX_PERSISTENTED_VALUE] = getByteMapValueByKey(knowledgeInRedis,
				fieldForRedis + PERSISTENTED_SUFFIX);
		// 更新redis
		newVal[INDEX_CURRENT_VALUE] = updRedisUnderNormal(key, fieldForRedis, value);
		return newVal;
	}
	
	/**
	 * 取出指定field的value，计算并返回要持久化的数据
	 * @param knowledgeInRedis
	 * @param field
	 * @return
	 */
	public Map<String, Object> getPersistentKnowledgeByField(Map<String, Object> knowledgeInRedis, String field) {
		Map<String, Object> persistentMap = new HashMap<String, Object>();
		
		// 扫描knowledgeInRedis中的所有field和value
		for (String strField : knowledgeInRedis.keySet()) {
			// 只对指定field的value进行持久化
			if (strField.equals(field)) {
				Object curVal = getByteMapValueByKey(knowledgeInRedis, strField);
				Object persistentedVal = getByteMapValueByKey(knowledgeInRedis, strField + PERSISTENTED_SUFFIX);
				Object val2Persistent = calValue2Persistent(curVal, persistentedVal);
				persistentMap.put(strField, val2Persistent);
				break;
			}
		}
		return persistentMap;
	}
	
	public void saveData2MogonDB(String key, Map<String, Object> persistentMap, String command) {
		// 获取当前时间的半点或者整点
		String nowTime = StrategyUtil.sdf.format(new Date());
		String adjustNowtime = StrategyUtil.getAdjustTime(nowTime);

		for (String field : persistentMap.keySet()) {
			Object value = persistentMap.get(field);
			Map<String, Object> fieldData = StrategyUtil.splitKnowledgeRedisField(field);
			if(fieldData == null) {
				logger.fatal("split knowledge redis fail! field=" + field);
				continue;
			}
			KnowledgeStrategyStatDTO newKnowledgeStrategyStat = new KnowledgeStrategyStatDTO();
			newKnowledgeStrategyStat.setKey(key);
			newKnowledgeStrategyStat.setAppId(fieldData.get("appId").toString());
			newKnowledgeStrategyStat.setOs(fieldData.get("os").toString());
			newKnowledgeStrategyStat.setAppVersion(fieldData.get("appVersion").toString());
			newKnowledgeStrategyStat.setTriggerName(fieldData.get("triggerName").toString());
			newKnowledgeStrategyStat.setTime(adjustNowtime);
			
			switch (command) {
			case SQLEngine.INC:
				knowledgeStrategyStatDAO.upsertIncKnowledgeStrategyStat(newKnowledgeStrategyStat, value);
				break;
	
			case SQLEngine.SET:
				knowledgeStrategyStatDAO.upsertSetKnowledgeStrategyStat(newKnowledgeStrategyStat, value);
				break;
			}
		}
	}
	

	/**
	 * 将app基本信息进行同步：如果在redis中key没有对应的appId、os、appVersion，则将这些app基本信息存入redis和mongodb
	 * @param key
	 * @param appId
	 * @param os
	 * @param appVersion
	 */
	public void synBaseInfo2Mongodb(String key, String appId, String os, String appVersion) {
		String keyForRedis = key + StrategyService.REDIS_BASE_INFO_SUFFIX;

		List<Object> baseInfoAllItems = new ArrayList<Object>();
		for (StrategyService.BaseInfoEnum baseInfoItem : StrategyService.BaseInfoEnum.values()) {
			baseInfoAllItems.add(baseInfoItem.name());
		}
		Map<Object, Object> redisData = knowledgeRedis.hgetAllByFields(keyForRedis, baseInfoAllItems);
		Map<String, Object> map4Redis = new HashMap<String, Object>();
		
		// 解析基本信息，将要存入redis和mongodb的信息项存入mapByte
		for (Object baseInfoAllItem : baseInfoAllItems) {
			String baseInfoKey = baseInfoAllItem.toString();
			String baseInfoValue = (String) redisData.get(baseInfoKey);
			// 如果redis里不存在某项基本信息field
			if (baseInfoValue == null) {
				if (baseInfoKey.equals(StrategyService.BaseInfoEnum.APPID.toString())) {
					map4Redis.put(baseInfoKey, appId);
				} else if (baseInfoKey.equals(StrategyService.BaseInfoEnum.OS.toString())) {
					map4Redis.put(baseInfoKey, os);
				} else if (baseInfoKey.equals(StrategyService.BaseInfoEnum.APPVERSION.toString())) {
					map4Redis.put(baseInfoKey, appVersion);
				}
			} else {	// 如果redis已存在某项基本信息field，就要判断是否包含相应的value
				List<String> baseInfoList = Arrays.asList(baseInfoValue.split(StrategyService.REDIS_BASE_INFO_SEPARATOR));
				if (baseInfoKey.equals(StrategyService.BaseInfoEnum.APPID.toString())) {
					if (!baseInfoList.contains(appId)) {
						map4Redis.put(baseInfoKey, (baseInfoValue + StrategyService.REDIS_BASE_INFO_SEPARATOR + appId));
					}
				} else if (baseInfoKey.equals(StrategyService.BaseInfoEnum.OS.toString())) {
					if (!baseInfoList.contains(os)) {
						map4Redis.put(baseInfoKey, (baseInfoValue + StrategyService.REDIS_BASE_INFO_SEPARATOR + os));
					}
				} else if (baseInfoKey.equals(StrategyService.BaseInfoEnum.APPVERSION.toString())) {
					if (!baseInfoList.contains(appVersion)) {
						map4Redis.put(baseInfoKey, (baseInfoValue + StrategyService.REDIS_BASE_INFO_SEPARATOR + appVersion));
					}
				}
			}
		}
		
		// 需要将app基本信息存入redis和mongodb
		if(map4Redis.size() > 0) {
			// 存mongodb
			knowledgeStrategyBaseInfoDAO.upsertBaseInfo(key, appId, os, appVersion);
			
			// 存redis
			knowledgeRedis.hmset(keyForRedis, map4Redis);
		}
	}

	/**
	 * 获取知识策略配置
	 * 
	 * @param name
	 * @return
	 */
	public KnowledgeStrategyConfigDTO getKnowledgeStrategyConfig(String key) {
		KnowledgeStrategyConfigDTO knowledgeStrategyConfig = null;
		// 先从local内存取///////////////

		// local内存没有，则从mongodb取
		knowledgeStrategyConfig = knowledgeStrategyConfigDAO.getConfigByKey(key);

		// 写进local内存///////////////
		return knowledgeStrategyConfig;
	}
	
	/**
	 * 获取应用策略配置
	 * @param name
	 * @return
	 */
	public AppStrategyConfigDTO getAppStrategyConfig(String key) {

		AppStrategyConfigDTO appStrategyConfig = null;
		// 先从local内存取///////////////
		
		// local内存没有，则从mongodb取
		appStrategyConfig = appStrategyConfigDAO.getConfigByKey(key);
		
		// 写进local内存///////////////
		return appStrategyConfig;
	}

	public KnowledgeStrategyConfigDAO getKnowledgeStrategyConfigDAO() {
		return knowledgeStrategyConfigDAO;
	}

	public void setKnowledgeStrategyConfigDAO(KnowledgeStrategyConfigDAO knowledgeStrategyConfigDAO) {
		this.knowledgeStrategyConfigDAO = knowledgeStrategyConfigDAO;
	}

	public KnowledgeStrategyBaseInfoDAO getKnowledgeStrategyBaseInfoDAO() {
		return knowledgeStrategyBaseInfoDAO;
	}

	public void setKnowledgeStrategyBaseInfoDAO(KnowledgeStrategyBaseInfoDAO knowledgeStrategyBaseInfoDAO) {
		this.knowledgeStrategyBaseInfoDAO = knowledgeStrategyBaseInfoDAO;
	}

	public KnowledgeStrategyStatDAO getKnowledgeStrategyStatDAO() {
		return knowledgeStrategyStatDAO;
	}

	public void setKnowledgeStrategyStatDAO(KnowledgeStrategyStatDAO knowledgeStrategyStatDAO) {
		this.knowledgeStrategyStatDAO = knowledgeStrategyStatDAO;
	}

	public void setKnowledgeRedis(KnowledgeRedis knowledgeRedis) {
		this.knowledgeRedis = knowledgeRedis;
	}
	
	public void incNewStrategySummary(StrategyAnalysisSummaryDTO strategyAnalysisSummaryDTO) {
		strategyAnalysisSummaryDAO.incStrategySummary(strategyAnalysisSummaryDTO);
	}
	
	public void updStrategySummary(StrategyAnalysisSummaryDTO strategyAnalysisSummaryDTO) {
		strategyAnalysisSummaryDAO.updStrategySummary(strategyAnalysisSummaryDTO);
	}
	
	public Map<String, AppStrategyConfigDTO> getAllSolutionsByStrategies(List<AppStrategyTrigger> strategies) {
		List<String> strategyNameList = new ArrayList<String>();
		for (AppStrategyTrigger strategy : strategies) {
			strategyNameList.add(strategy.getDataStrategyName());	// 获取数据分析方案
			strategyNameList.add(strategy.getAppStrategyName());	// 获取治理方案
		}
		List<AppStrategyConfigDTO> appStrategyConfigDTOs = appStrategyConfigDAO.getAllSolutionsByStrategies(strategyNameList);
		Map<String, AppStrategyConfigDTO> result = new HashMap<String, AppStrategyConfigDTO>();
		for (AppStrategyConfigDTO appStrategyConfigDTO : appStrategyConfigDTOs) {
			String key = appStrategyConfigDTO.getKey();
			result.put(key, appStrategyConfigDTO);
		}
		return result;
	}
	
}
