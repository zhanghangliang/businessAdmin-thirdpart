package com.gov.wiki.common.utils;

import java.text.Format;
import java.text.MessageFormat;
import java.util.Formatter;

import com.alibaba.fastjson.JSONObject;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.exception.CheckException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckUtil {
	
	/**
	 *	校验condition，如果为false，throws CheckException.
	 *	例：check(false,"name.is.empty","name")
	 * @param condition
	 * 			布尔表达式
	 * @param msgKey
	 * 			国际化信息
	 * @param args
	 * 			国际化信息参数
	 */
	public static void check(boolean condition, ResultCode resultCode, Object... args) {
		if (!condition) {
			fail(resultCode, args);
		}
	}

	/**
	 * 校验str是否为空或者str.length=0,如果为空则 throws CheckException
	 * @param str
	 * 			检测的字符串
	 * @param msgKey
	 * 			国际化信息
	 * @param args
	 * 			国际化信息参数
	 */
	public static void notEmpty(String str, ResultCode resultCode, Object... args) {
		if (str == null || str.isEmpty()) {
			fail(resultCode, args);
		}
	}
	
	/**
	 * 校验obj是否为空，如果为空 throws CheckException
	 * @param obj
	 * 			待检测的对象
	 * @param msgKey
	 * 			国际化信息
	 * @param args
	 * 			国际化信息参数
	 */
	public static void notNull(Object obj, ResultCode resultCode, Object... args) {
		if (obj == null) {
			fail(resultCode, args);
		}
	}

	
	private static void fail(ResultCode result, Object... args) {
		//这里的需要处理一下，此处用枚举
		String msg = result.getCodeMsg();
		if(args != null) {
			//处理格式化的问题
			msg = MessageFormat.format(msg, args);
		}
		//构造json
		String json = "{\"message\": \""+msg+"\",\"code\": "+result.getCode()+"}";
		CheckException checkException = new CheckException(json,result.getCode());
		checkException.setMessage1(msg);
		throw checkException;
	}
	
	/**
	 * 比较{@code o} 是否与{@code args}中的任意一个相等。
	 * @param o
	 * 		待比较对象
	 * @param args
	 * 		比较对象
	 * @return
	 * 		{@code o} 与{@code args}中的任意一个相等则返回true，反之返回false
	 */
	public static boolean checkEquals(Object o, Object...args){
		if(o == null){
			return false;
		}
		for (Object object : args) {
			if(object.equals(o)){
				return true;
			}
		}
		return false;
	}
}
