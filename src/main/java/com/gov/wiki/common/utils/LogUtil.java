package com.gov.wiki.common.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.gov.wiki.common.entity.system.SysLog;

import lombok.extern.slf4j.Slf4j;

/**
 * 	日志相关操作
 * @author sdfasfgh
 *
 */
@Slf4j
public abstract class LogUtil {

	public static SysLog addLog(long time, Integer logType, String content, String userId) {
		SysLog sysLog = new SysLog();	
		sysLog.setLogContent(content);
		sysLog.setLogType(logType);
		sysLog.setUserId(userId);
		addLog(time, sysLog, null);
		return sysLog;
	}
	
	public static SysLog addLoginLog(long time, String userId, String content) {
		return addLog(time, SysLog.TYPE_LOGIN, content,userId);
	}
	
	public static void addLog(long time, SysLog sysLog, String methodName) {
		sysLog.setMethod(methodName);
		//获取request
		ServletRequestAttributes sra = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = sra.getRequest();
		//设置IP地址
		sysLog.setIp(IPUtils.getIpAddr(request));
		sysLog.setUserId(JwtUtil.getUserId());
		//耗时
		sysLog.setCostTime(time);
	}
}
