package me.star2478.jstorm.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import me.star2478.jstorm.dto.KnowledgeStrategyConfigDTO;
import me.star2478.jstorm.common.StrategyUtil;

public class StrategyUtil {

	public final static int STATUS_DISABLE = 0; // 策略禁用
	public final static int STATUS_ENABLE = 1; // 策略启用
	
	public final static int TIME_EQUAL = 0; // 时间相等
	public final static int TIME_GREATER = 1; // 第一个时间比第二个时间大
	public final static int TIME_LESS = 2; // 第一个时间比第二个时间小
	
	public final static String INTERVAL_KNOWLEDGE_REDIS_FIELD = ":"; // 知识数据在redis field的间隔符
	
	private final static Logger logger = Logger.getLogger(StrategyUtil.class);

	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm");
	
	/**
	 * 计算过期时间
	 * 
	 * @return
	 */
	public static String calExpireTime(int expire) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, expire);
		return sdf.format(cal.getTime());
	}

	/**
	 * 对比时间
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static int compareTime(String time1, String time2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date1 = sdf.parse(time1);
			Date date2 = sdf.parse(time2);
			int c = (int) (date1.getTime() - date2.getTime());
			if (c > 0) {
				return TIME_GREATER;
			} else if (c < 0) {
				return TIME_LESS;
			} else {
				return TIME_EQUAL;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return TIME_EQUAL;
		}
	}
	
	/**
	 * 判断time是否已过期，time早于当前时间则过期
	 * @return true：过期，false：未过期
	 */
	public static boolean checkTimeOut(String time) {
		// 如果time为null，则表示已过期
		if(time == null) {
			return true;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String curTime = sdf.format(new Date());
		 // 已过期
		if (StrategyUtil.compareTime(curTime, time) == TIME_GREATER) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 检查策略配置是否合法
	 * @param knowledgeStrategyConfig
	 * @return
	 */
	public static boolean checkStrategyConfig(KnowledgeStrategyConfigDTO knowledgeStrategyConfig) {
		if (knowledgeStrategyConfig == null) {
			logger.error("config not exists");
			return false;
		}
		// 未配置任何知识条件
		if(knowledgeStrategyConfig.getAppStrategyTriggers() == null || knowledgeStrategyConfig.getAppStrategyTriggers().size() <= 0) {
			logger.warn("has no any appStrategyTrigger");
			return false;
		}
		// 检查策略状态
		if (knowledgeStrategyConfig.getStatus() == STATUS_DISABLE) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 拼接
	 * @param appId
	 * @param os
	 * @param appVersion
	 * @param triggerName
	 * @return
	 */
	public static String strcatKnowledgeRedisField(String appId, String os, String appVersion, String triggerName) {
		return String.format("%s"+ INTERVAL_KNOWLEDGE_REDIS_FIELD +"%s"+ INTERVAL_KNOWLEDGE_REDIS_FIELD +"%s"+ INTERVAL_KNOWLEDGE_REDIS_FIELD +"%s", 
				appId, os, appVersion, triggerName);
	}
	
	/**
	 * 解析知识数据在redis中的field
	 * @param strField
	 * @return
	 */
	public static Map<String, Object> splitKnowledgeRedisField(String strField) {
		Map<String, Object> fieldData = new HashMap<String, Object>();
		String[] arrField = strField.split(INTERVAL_KNOWLEDGE_REDIS_FIELD);
		if(arrField.length < 4) {
			return null;
		}
		fieldData.put("appId", arrField[0]);
		fieldData.put("os", arrField[1]);
		fieldData.put("appVersion", arrField[2]);
		fieldData.put("triggerName", arrField[3]);
		return fieldData;
	}
	
	/**
	 * 判断是否是个数字
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str){
		if(str == null) {
			return false;
		}
		String reg = "^[0-9]+(\\.[0-9]+)?$";
		return str.matches(reg);
	}
	
	/**
	 * json串转为map
	 * 
	 * @param jsonString
	 * @return
	 */
	public static HashMap<String, Object> json2Map(String jsonString) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		// 将json字符串转换成jsonObject
		JSONObject jsonObject = JSONObject.parseObject(jsonString);
		// 遍历jsonObject数据，添加到Map对象
		for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
	}

	
	/**
	 * json串转为array
	 * 
	 * @param jsonString
	 * @return
	 */
	public static List<HashMap<String, String>> json2Array(String jsonString) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		// 将json数组字符串转换成jsonArray
		JSONArray jsonArray	= JSONArray.parseArray(jsonString);
		
		// 遍历jsonArray
		for (Object object : jsonArray) {
			JSONObject jsonObject = (JSONObject) object;
			HashMap<String, String> map = new HashMap<String, String>();
			for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
				String value = (entry.getValue() != null)?entry.getValue().toString():null;
				map.put(entry.getKey(), value);
			}
			list.add(map);
		}
		return list;
	}

	public static void buildDataMap(Map<String, Object> dataMap, Map<String, Object> map) {

		Map<String, Object> mergeMap = new HashMap<String, Object>();

		mergeMap.put("key", (String) map.get("key"));
		mergeMap.put("appId", (String) map.get("appId"));
		mergeMap.put("os", (String) map.get("os"));
		mergeMap.put("appVersion", (String) map.get("appVersion"));
		mergeMap.put("osVersion", (String) map.get("osVersion"));
		mergeMap.put("deviceId", (String) map.get("deviceId"));
		mergeMap.put("deviceName", (String) map.get("deviceName"));
		mergeMap.put("channel", (String) map.get("channel"));
		mergeMap.put("location", (String) map.get("location"));
		mergeMap.put("ip", (String) map.get("ip"));
		mergeMap.put("mobilePhone", (String) map.get("mobilePhone"));
		mergeMap.put("networkEnv", (String) map.get("networkEnv"));
		mergeMap.put("telecomOperator", (String) map.get("telecomOperator"));
		mergeMap.put("time", (String) map.get("time"));
		mergeMap.put("userAgent", (String) map.get("userAgent"));
		mergeMap.put("screenSize", (String) map.get("screenSize"));
		
		mergeData(dataMap, mergeMap);
	}
	
	/**
	 * 将dataMap和mergeMap合并到dataMap，如果dataMap中包含了和mergeMap一样的key，
	 * 以mergeMap中的value为准
	 * 
	 * @param dataMap
	 * @param mergeMap
	 */
	public static void mergeData(Map<String, Object> dataMap, Map<String, Object> mergeMap) {
		for (Map.Entry<String, Object> entry : mergeMap.entrySet()) {
			if(entry.getValue() != null) {
				dataMap.put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	/**
	 * 获取给定时间的整点或者半点，小于30分钟的区当前小时的半点，大于30分钟的取下一小时的整点，例如5:10则取5:30,5:40则取6:00
	 * 
	 * @author chenweixin769
	 * @param originTime
	 * @return
	 */
	public static String getAdjustTime(String originTime){
		String adjustTime = "";
		try{
		Date originTimeDate = sdf.parse(originTime);
		adjustTime = originTime;
		
		//给定时间的整点
		Calendar hourCal = Calendar.getInstance();
		hourCal.setTime(originTimeDate);
		hourCal.set(Calendar.MINUTE, 0);
		Date hourDate = hourCal.getTime();
//		String hourTime = sdf.format(hourDate);
		
		//给定时间的半点
		Calendar halfHourCal = Calendar.getInstance();
		halfHourCal.setTime(originTimeDate);
		halfHourCal.set(Calendar.MINUTE, 30);
		Date halfHourDate = halfHourCal.getTime();
		String halfHourTime = sdf.format(halfHourDate);
		
		
		if(originTimeDate.after(hourDate) && originTimeDate.before(halfHourDate)) {
			   adjustTime = halfHourTime;
		}else if(originTimeDate.after(halfHourDate)) {
			Calendar nextHourCal = Calendar.getInstance();
			nextHourCal.setTime(originTimeDate);
			nextHourCal.set(Calendar.MINUTE, 0);
			nextHourCal.add(Calendar.HOUR_OF_DAY, 1);
			adjustTime = sdf.format(nextHourCal.getTime());
		}
		}catch (ParseException e) {
			e.printStackTrace();
		}
		
		return adjustTime;
	}

	
	
	/**
	 * 获取给定时间的前30分钟
	 * @param time 时间，格式为 yyy-MM-dd HH:mm
	 */
	public static String get30MinuteAgo(String time) {
		
		String after30Minute = "";
		try {
			Date timeDate = sdf.parse(time);
			Calendar timeCal = Calendar.getInstance();
			timeCal.setTime(timeDate);
			
			timeCal.add(Calendar.MINUTE, -30);
			
			Date after30MinuteDate = timeCal.getTime();
			after30Minute = sdf.format(after30MinuteDate);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return after30Minute;
	}
	
	/**
	 * 获取两个时间的间隔，单位为秒
	 * @author CHENWEIXIN769
	 * @param time1 较大的时间
	 * @param time2  较小的时间
	 */
	public static Long getTimeInterVal(String time1,String time2) {
	    Long timeIntervalSecond = (long) 0;
		try {
			Date date1 = sdf.parse(time1);
			Date date2 = sdf.parse(time2);
			timeIntervalSecond = (date1.getTime() - date2.getTime())/(1000);  //获取时间间隔,单位为秒
			return timeIntervalSecond;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
}
