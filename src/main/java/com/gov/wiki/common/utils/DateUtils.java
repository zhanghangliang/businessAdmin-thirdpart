package com.gov.wiki.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.gov.wiki.common.beans.ResultCode;

public class DateUtils {

	/**
	 * 获取当前时间，默认格式：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getDateFormat(){
		String farmat = "yyyy-MM-dd HH:mm:ss";
		return getDateFormat(farmat);
	}
	
	/**
	 * 获取某一天的时间范围：从00：00：00-23：59：59
	 * 
	 * @param date
	 * @return
	 */
	public static Date[] getRangeByDay(Date date) {
		// 根据日期获取当天的时间范围
		int amountMonth = 1;
		Date[] dates = getDay(date, amountMonth);
		return new Date[] { dates[1], dates[0] };
	}
	
	private static Date[] getDay(Date date, int amountDay) {
		Date lastDay, day;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		day = cal.getTime();
		cal.add(Calendar.DATE, amountDay);
		lastDay = new Date(cal.getTimeInMillis() - 1);
		return new Date[] { lastDay, day };
	}
	
	/**
	 * 获取某一个月份当前时间的范围
	 * @param date
	 * @return
	 */
	public static Date[] getRangeByMongth(Date date) {
		// 根据月份获取当月的时间范围
		int amountMonth = 1;
		Date[] dates = getMonth(date, amountMonth);
		// 获取当月第一天
		return new Date[] { dates[1], dates[0] };
	}
	
	/**
	 * 计算两个时间之间的月份差
	 * 
	 * @param startDate
	 * @param dueTime
	 * @return
	 */
	public static int monthByRanges(Date startDate, Date dueTime) {
		CheckUtil.check(startDate.before(dueTime), ResultCode.COMMON_ERROR, "开始时间不能大于结束时间");
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		Calendar end = Calendar.getInstance();
		end.setTime(dueTime);
		int year = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
		int month = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
		int day = end.get(Calendar.DAY_OF_MONTH) - start.get(Calendar.DAY_OF_MONTH);
		int totalMonth = 0;
		totalMonth += year * 12;
		totalMonth += month;
		if (day < -1)
			totalMonth--;
		return totalMonth;
	}
	
	/**
	 * 获取当前时间 格式化
	 * @param format
	 * 			时间格式
	 * @return
	 */
	public static String getDateFormat(String format){
		return getDateFormat(format, new Date());
	}
	
	/**
	 * 获取任意时间格式化
	 * @param format
	 * 			时间格式
	 * @param date
	 * 			时间日期
	 * @return
	 */
	public static String getDateFormat(String format, Date date){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	/**
	 * 时间戳转换成日期
	 * @param format 时间格式
	 * @param date 时间日期
	 * @return
	 */
	public static String getDateFormat(String format, long date){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(date));
	}
	/**
	 * 根据时间戳获取时间
	 * @param date
	 * @return
	 */
	public static String getDateFormat(long date){
		String format = "yyyy-MM-dd HH:mm:ss";
		return getDateFormat(format,date);
	}
	   
    /**时间转换成时间戳
     * @param dateStr
     * @return
     */
    public static Long dateToStamp(String dateStr){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
		try {
			date = simpleDateFormat.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return date.getTime();
    }
	
    /**
	 * @Title: stringToDate 
	 * @Description: 字符串转时间
	 * @param 设定文件 
	 * @return Date    返回类型 
	 * @throws
	 */
	public static Date stringToDate(String dateStr, String formatStr) throws ParseException {
		if(StringUtils.isBlank(dateStr)) {
			return null;
		}
		formatStr = StringUtils.isBlank(formatStr)?"yyyy-MM-dd":formatStr;
		SimpleDateFormat df = new SimpleDateFormat(formatStr);
		return df.parse(dateStr);
	}

	/**
	 * 	获取两个时间的天数
	 * @param startTime
	 * @param date
	 * @return
	 */
	public static Integer getDays(Date startDate, Date endDate) {
		long startTime = startDate.getTime();
		long endTime = endDate.getTime();
		return (int) ((endTime - startTime)/3600*1000*24);
	}
	
	/**
	 * 获取月份的第一天
	 */
	public static String getMonthOfFirstDay(String formatStr, int mon) {
		formatStr = StringUtils.isBlank(formatStr)?"yyyy-MM-dd":formatStr;
		SimpleDateFormat df = new SimpleDateFormat(formatStr);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, mon);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return df.format(calendar.getTime());
	}
	
	/**
	 * 获取月份的最后一天
	 */
	public static String getMonthOfLastDay(String formatStr, int mon) {
		formatStr = StringUtils.isBlank(formatStr)?"yyyy-MM-dd":formatStr;
		SimpleDateFormat df = new SimpleDateFormat(formatStr);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, mon);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return df.format(calendar.getTime());
	}

	/**
	 * 获取上个月的时间区间：eg:2019-11-01 00:00:00 - 2019-11-30 23:59:59;
	 * 	date[0] 上个月开始时间
	 * 	date[1] 上个月结束时间
	 * @return
	 */
	public static Date[] getLastMonth() {
		// TODO Auto-generated method stub
		Date date = new Date();
		int amountMonth = -1;
		//获取当月第一天
		return getMonth(date,amountMonth);
	}
	/**
	 * 获取本月时间范围
	 * @return
	 */
	public static Date[] getMonth() {
		// TODO Auto-generated method stub
		Date date = new Date();
		int amountMonth = 1;
		Date[] dates = getMonth(date, amountMonth);
		//获取当月第一天
		return new Date[] {dates[1],dates[0]};
	}

	private static Date[] getMonth(Date date,int amount) {
		Date lastMonth,month;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		month = cal.getTime();
		cal.add(Calendar.MONTH, amount);
		lastMonth = cal.getTime();
		return new Date[]{lastMonth,month};
	}
	
	public static void main(String[] args) {
		Date[] month = getMonth();
		SimpleDateFormat sdf  =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Date date : month) {
			System.out.println(sdf.format(date));
		}
	}
}