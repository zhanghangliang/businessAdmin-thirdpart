package com.gov.wiki.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.StringUtils;

/**
 * @ClassName: DateUtil 
 * @Description: 通用日期处理类
 * @author cys 
 * @date 2018年4月22日 下午10:06:45
 */
public class DateUtil {

	/**
	 * @Title: dateToString 
	 * @Description: 日期转字符串
	 * @param 设定文件 
	 * @return String    返回类型 
	 * @throws
	 */
	public static String dateToString(Date date, String formatStr){
		if(date == null){
			return null;
		}
		formatStr = StringUtils.isEmpty(formatStr)?"yyyy-MM-dd":formatStr;
		SimpleDateFormat df = new SimpleDateFormat(formatStr);
        return df.format(date);
	}
	
	/**
	 * @Title: transferLongToDate
	 * @Description: 时间戳转日期字符串
	 * @param dateFormat
	 * @param millSec
	 * @return String 返回类型
	 * @throws
	 */
	public static String transferLongToDate(Long millSec, String... dateFormat) {
		String formatStr = dateFormat == null || dateFormat.length <= 0?"":dateFormat[0];
		formatStr = StringUtils.isEmpty(formatStr)?"yyyy-MM-dd HH:mm:ss":formatStr;
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		Date date = new Date(millSec);
		return sdf.format(date);
	}
	
	/**
	 * @Title: getCurrentDateStr 
	 * @Description: 获取当前日期字符串
	 * @param 设定文件 
	 * @return String    返回类型 
	 * @throws
	 */
	public static String getCurrentDateStr(String formatStr){
		return dateToString(new Date(), formatStr);
	}
	
	/**
	 * @Title: stringToDate 
	 * @Description: 字符串转时间
	 * @param 设定文件 
	 * @return Date    返回类型 
	 * @throws
	 */
	public static Date stringToDate(String dateStr, String formatStr) throws ParseException {
		if(StringUtils.isEmpty(dateStr)) {
			return null;
		}
		formatStr = StringUtils.isEmpty(formatStr)?"yyyy-MM-dd":formatStr;
		SimpleDateFormat df = new SimpleDateFormat(formatStr);
		return df.parse(dateStr);
	}
}
