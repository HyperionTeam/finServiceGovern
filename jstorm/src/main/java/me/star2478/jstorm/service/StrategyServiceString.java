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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import clojure.lang.Obj;
import me.star2478.jstorm.redis.KnowledgeRedis;

/**
 * 知识策略
 * 
 * @author heliuxing
 *
 */
@Service
public class StrategyServiceString extends StrategyService {

	private static Logger logger = Logger.getLogger(StrategyServiceString.class);

//	public KnowledgeStrategyServiceString(){
//		
//	}
//	
//	public KnowledgeStrategyServiceString(KnowledgeRedis knowledgeRedis) {
//		super(knowledgeRedis);
//	}

	@Override
	public Object calValue2Persistent(Object curVal, Object persistentedVal) {
		return curVal;
	}

	@Override
	public Object incMethodHandler(String key, String field, Object value) {
		knowledgeRedis.hset(key, field, value);
		return value;
	}

	@Override
	public KnowledgeRedis getKnowledgeRedis() {
		return knowledgeRedis;
	}
	
	@Override
	public Object getByteMapValueByKey(Map<String, Object> map, String key){
		Object perValueObj = map.get(key);
		return (perValueObj != null) ? perValueObj.toString() : null;
	}
	
	@Override
	public void updRedisWithoutKnowledge(String key, Map<String, Object> map) {
		knowledgeRedis.hmset(key, map);
	}
	
	@Override
	public void updRedisWhenExpireTimeout(String key, Map<String, Object> map, String... delFields) {
		knowledgeRedis.hdel(key, delFields);
		knowledgeRedis.hmset(key, map);
	}
	
	@Override
	public Object updRedisUnderNormal(String key, String field, Object value) {
		knowledgeRedis.hset(key, field, value);
		return value;
	}
	
	@Override
	public void hdelKnowledgeInRedis(String key, String... delFields) {
		knowledgeRedis.hdel(key, delFields);
	}

	@Override
	public boolean checkOpenAppStrategy(Object knowledgeVal, String op4AppStrategy, Object value4AppStrategy) {
		String value1 = knowledgeVal.toString();
		String value2 = value4AppStrategy.toString();
		int compare = value1.compareTo(value2);
		// 字符串比较中，如果不是=或!=的话，一概返回false
		switch (op4AppStrategy) {
		case ">":
			return false;
//			if (compare > 0) {
//				return true;
//			}
//			break;

		case "<":
			return false;
//			if (compare < 0) {
//				return true;
//			}
//			break;

		case "=":
			if (compare == 0) {
				return true;
			}
			break;

		case "!=":
			if (compare != 0) {
				return true;
			}
			break;

		case ">=":
			return false;
//			if (compare >= 0) {
//				return true;
//			}
//			break;

		case "<=":
			return false;
//			if (compare <= 0) {
//				return true;
//			}
//			break;

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
		String value1 = curVal.toString();
		String value2 = value4AppStrategy.toString();
		int compare = value1.compareTo(value2);
		switch (op4AppStrategy) {
		case ">":
			if (compare == 1) {
				return true;
			}
			break;

		case "<":
			if (compare == -1) {
				return true;
			}
			break;

		case "=":
			if (compare == 0) {
				return true;
			}
			break;

		case ">=":
			if (compare == 1 || compare == 0) {
				return true;
			}
			break;

		case "<=":
			if (compare == -1 || compare == 0) {
				return true;
			}
			break;

		default:
			logger.error("op[" + op4AppStrategy + "] invalid");
			return false;
		}
		return false;
	}
}
