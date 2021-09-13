package com.gov.wiki;

import javax.servlet.http.HttpServletRequest;

import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.exception.CheckException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice("com.gov.wiki")
@Slf4j
public class GlobalExceptionHandler {

	private int code = 0;

	@ExceptionHandler(value = CheckException.class)
	public ResultBean<String> checkException(HttpServletRequest req, CheckException e) {
		log.info("请求异常：{}",e.getMessage());
		return ResultBean.error(e.getCode(), e.getMessage1());
	}
	
	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	public ResultBean<String> httpMessageNotReadableException(HttpServletRequest req, HttpMessageNotReadableException e) {
		log.info("请求异常：{}",e.getMessage());
		return ResultBean.error("请求参数格式存在错误，请检查后在提交");
	}
	
	@ExceptionHandler(value = Exception.class)
	public ResultBean<String> exception(HttpServletRequest req, Exception e){
		ResultBean<String> bean = null;
		++code;
		log.error("请求错误code:" + code, e);
		bean = ResultBean.error("请求错误：code:" + code);
		if (code >= 10000) {
			code = 0;
		}
		return bean;
	}
}
