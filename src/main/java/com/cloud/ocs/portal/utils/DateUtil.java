package com.cloud.ocs.portal.utils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * 用于处理Date的工具类
 * 
 * @author Wang Chao
 *
 * @date 2015-1-21 下午7:29:04
 *
 */
public class DateUtil {

	/**
	 * 将CloudStack API返回的字符串日期格式装换为Timestamp
	 * @param date 类似于2014-12-28T15:43:58+0800格式
	 * @return
	 */
	public static Timestamp convertCsDateToTimestamp(String date) {
		if (date == null) {
			return null;
		}
		
		String str = date.replace('T', ' ');
		
		return Timestamp.valueOf(str.substring(0, str.indexOf('+')));
	}
	
	/**
	 * 将传入的date在second上进行增或减
	 * @param date
	 * @param amount 增减的数量
	 * @return
	 */
	public static Timestamp transferDateInSecondField(Date date, int amount) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, amount);
		Date resultInDate = calendar.getTime();
		Timestamp result = new Timestamp(resultInDate.getTime());
		
		return result;
	}
	
	/**
	 * 将传入的date在millisecond上进行增或减
	 * @param date
	 * @param amount 增减的数量
	 * @return
	 */
	public static Timestamp transferDateInMilliSecondField(Date date, int amount) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(date);
		calendar.add(Calendar.MILLISECOND, amount);
		Date resultInDate = calendar.getTime();
		Timestamp result = new Timestamp(resultInDate.getTime());
		
		return result;
	}
	
	public static Date transferDateInMonthField(Date date, int amount) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, amount);
		
		return calendar.getTime();
	}
}
