package com.gov.wiki.common.utils;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 常量定义
 * @author csxx_wmw
 *
 */
public abstract class Constants {

	public static final String TOKEN = "token";
	public static final String USERNO = "userNo";
	//生产上需要置位false,不然生产上获取不到创建人和更新人
	public static final boolean SKIP_LOGIN = false;
	
	public static final long USER_EXPIRATION_DATE = 72*60*60*1000L;
	
	/**
	 * 操作日志
	 */
	public static final int LOG_TYPE_2 = 2;
	/**
	 * 登录人日志
	 */
	public static final int LOG_TYPE_1 = 1;
	/**
	 * 定时任务
	 */
	public static final int LOG_TYPE_3 = 3;
	
	public static Queue<String> loanQueue = new LinkedBlockingQueue<String>();
	public static Map<String, Object> cacheLoanMap = new ConcurrentHashMap<>();
}
