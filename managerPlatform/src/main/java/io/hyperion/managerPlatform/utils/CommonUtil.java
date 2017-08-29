package io.hyperion.managerPlatform.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;


public class CommonUtil {
	
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm");
	
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
