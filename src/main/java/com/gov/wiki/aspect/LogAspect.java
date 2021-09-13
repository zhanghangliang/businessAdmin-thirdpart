/**
 * @Title: LogAspect.java
 * @Package com.xiangtong.aspect
 * @Description: 日志记录切片
 * @author cys
 * @date 2019年7月30日
 * @version V1.0
 */
package com.gov.wiki.aspect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.entity.system.SysLog;
import com.gov.wiki.common.enums.OperTypeEnum;
import com.gov.wiki.common.exception.CheckException;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.DateUtil;
import com.gov.wiki.common.utils.IPUtils;
import com.gov.wiki.common.utils.JwtUtil;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.system.service.ILogService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Slf4j
@Aspect
@Component
public class LogAspect {
	
	/**
	 * 注入logService
	 */
	@Autowired
	private ILogService logService;

	//切点
	@Pointcut("@annotation(com.gov.wiki.aspect.ControllerMonitor)")
	public void aspect() {
		
	}
	
	@Around(value = "aspect()")
	public Object arount(ProceedingJoinPoint joinPoint) throws Throwable {
		Date date = new Date(1635650385000L);
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        long startTime = System.currentTimeMillis();
        Object result = null;
        String errorMsg = "";
        try {
        	result = joinPoint.proceed(joinPoint.getArgs());
        }catch(Throwable e) {
        	result = e;
        	errorMsg = e.getMessage();
        	log.error("切面错误",e);
        }
        long endTime = System.currentTimeMillis();
        String operResult = "";
        if(result instanceof ResultBean) {
        	ResultBean bean = (ResultBean) result;
        	if(bean != null && bean.getCode() == ResultBean.SUCCESS) {
        		operResult = "成功";
        	}else {
        		operResult = "失败";
        	}
        }else {
        	operResult = "失败";
        }
        // 执行耗时
        long totalTime = endTime - startTime;
        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点方法
        Method targetMethod = methodSignature.getMethod();
        //获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        //获取请求的方法名
        String methodName = targetMethod.getName();
        String method = className + "." + methodName + "()";
        // 请求URI
        String uri = request.getRequestURI();
        //请求的参数
        Object[] args = joinPoint.getArgs();
        String params = Arrays.toString(args);
        SysLog sysLog = new SysLog();
        String desc = "";
        String oper = "";
        if(targetMethod.isAnnotationPresent(ControllerMonitor.class)) {
        	ControllerMonitor monitor = targetMethod.getAnnotation(ControllerMonitor.class);
        	if(monitor != null) {
        		desc = monitor.description() + operResult;
        		sysLog.setOperateType(monitor.operType());
        		oper = OperTypeEnum.getVal(monitor.operType());
        		sysLog.setOperateTypeDesc(oper);
        		if(monitor.operType() == OperTypeEnum.LOGIN.getKey()) {
        			sysLog.setLogType(SysLog.TYPE_LOGIN);
        		}else {
        			sysLog.setLogType(SysLog.TYPE_TIP);
        		}
        	}
        }
        log.info("Method: {}, Description:{}, Operation:{}, Start: {}, End: {}, Total: {}ms, errorMsg:{}", 
        		method, desc, oper, DateUtil.transferLongToDate(startTime), DateUtil.transferLongToDate(endTime), totalTime, errorMsg);
        sysLog.setIp(IPUtils.getIpAddr(request));
        sysLog.setMethod(method);
        sysLog.setRequestUrl(uri);
        if(StringUtils.isNotBlank(params) && params.toLowerCase().indexOf("password") == -1
        	&& params.toLowerCase().indexOf("pass") == -1 && params.toLowerCase().indexOf("pwd") == -1) {
        	sysLog.setRequestParam(params);
        }
        sysLog.setLogContent(desc);
        sysLog.setCostTime(totalTime);
        sysLog.setUserId(JwtUtil.getUserId());
        logService.saveOrUpdate(sysLog);
        if(result instanceof CheckException) {
        	throw (CheckException)result;
        }else if(result instanceof Exception) {
        	throw (Exception)result;
        }
        return result;
	}
	
}