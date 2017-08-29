package me.star2478.jstorm.service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.MidiDevice.Info;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import clojure.lang.Obj;
import me.star2478.jstorm.common.StrategyUtil;
import me.star2478.jstorm.redis.KnowledgeRedis;
import redis.clients.jedis.exceptions.JedisDataException;

/**
 * 知识策略
 * @author heliuxing
 *
 */
@Service
public class StrategyServiceFloat extends StrategyService {

	private static Logger logger = Logger.getLogger(StrategyServiceFloat.class);
 
//	public KnowledgeStrategyServiceFloat() {
//		
//	}
//	public KnowledgeStrategyServiceFloat(KnowledgeRedis knowledgeRedis) {
//		super(knowledgeRedis);
//	}

	@Override
	public Object calValue2Persistent(Object curVal, Object persistentedVal){
		if(curVal == null || persistentedVal == null || !StrategyUtil.isNumber(curVal.toString()) || !StrategyUtil.isNumber(persistentedVal.toString())) {
			return Float.valueOf(0);
		}
		Float a = (Float)curVal;
		Float b = (Float)persistentedVal;
		return a - b;
	}
	
	@Override
	public Object incMethodHandler(String key, String field, Object value) {
		try {
			return knowledgeRedis.hincrByFloat(key, field, Float.valueOf(value.toString())).floatValue();
		} catch (JedisDataException | InvalidDataAccessApiUsageException e) {	// 如果redis现在数据不是数字，就会捕获异常，改用set方式
			logger.fatal("inc redis fail, because new value="+value+" is numeric type but old value of db is non-numeric type");
			knowledgeRedis.hset(key, field, value);
			return value;
		}
	}
	
//	private Object incMethodHandlerExceptionHandler(String key, String field, Object value) {
//		logger.fatal("inc redis fail, because new value="+value+" is numeric type but old value of db is non-numeric type");
//		knowledgeRedis.hset(key, field, value);
//		return value;
//	}
	
	@Override
	public KnowledgeRedis getKnowledgeRedis() {
		return knowledgeRedis;
	}
	
	@Override
	public Object getByteMapValueByKey(Map<String, Object> map, String key){
		Object perValueObj = map.get(key);
		if(perValueObj == null) {
			return Float.valueOf(0);
		}
		String perValue = perValueObj.toString();
		if(!StrategyUtil.isNumber(perValue)) {
			return Float.valueOf(0);
		}
		return Float.valueOf(perValue);
	}
	
	@Override
	public void updRedisWithoutKnowledge(String key, Map<String, Object> map) {
		knowledgeRedis.hmset(key, map);
	}
	
	@Override
	public void updRedisWhenExpireTimeout(String key, Map<String, Object> map, String... delFields) {
//		System.out.println("=========:" + key + "," + map.toString());
		knowledgeRedis.hdel(key, delFields);
		knowledgeRedis.hmset(key, map);
	}
	
	@Override
	public Object updRedisUnderNormal(String key, String field, Object value) {
		return incMethodHandler(key, field, value);
	}
	
	@Override
	public void hdelKnowledgeInRedis(String key, String... delFields) {
		knowledgeRedis.hdel(key, delFields);
	}

	@Override
	public boolean checkOpenAppStrategy(Object knowledgeVal, String op4AppStrategy, Object value4AppStrategy) {
		float value1 = Float.valueOf(knowledgeVal.toString());
		float value2 = Float.valueOf(value4AppStrategy.toString());
		switch (op4AppStrategy) {
		case ">":
			if (value1 > value2) {
				return true;
			}
			break;

		case "<":
			if (value1 < value2) {
				return true;
			}
			break;

		case "=":
			if (value1 == value2) {
				return true;
			}
			break;

		case "!=":
			if (value1 != value2) {
				return true;
			}
			break;

		case ">=":
			if (value1 >= value2) {
				return true;
			}
			break;

		case "<=":
			if (value1 <= value2) {
				return true;
			}
			break;

		default:
			logger.error("op[" + op4AppStrategy + "] invalid");
			return false;
		}
		return false;
	}
	
	@Override
	public boolean checkOpenAppStrategy(String field, Object curVal, String key4AppStrategy, String op4AppStrategy, Object value4AppStrategy) {
		// 如果传过来的field和配置的key不相等，则无需继续判断是否满足条件
		if (!key4AppStrategy.equals(ANY_KEY) && !key4AppStrategy.equals(field)) {
			return false;
		}
		float value1 = Float.valueOf(curVal.toString());
		float value2 = Float.valueOf(value4AppStrategy.toString());
		switch (op4AppStrategy) {
		case ">":
			if (value1 > value2) {
				return true;
			}
			break;

		case "<":
			if (value1 < value2) {
				return true;
			}
			break;

		case "=":
			if (value1 == value2) {
				return true;
			}
			break;

		case ">=":
			if (value1 >= value2) {
				return true;
			}
			break;

		case "<=":
			if (value1 <= value2) {
				return true;
			}
			break;

		default:
			logger.error("op[" + op4AppStrategy + "] invalid");
			return false;
		}
		return false;
	}
	
//	@Override
//	public void saveData2MogonDB(String key, Map<String, Object> persistentMap) {
//		
//	}
}
