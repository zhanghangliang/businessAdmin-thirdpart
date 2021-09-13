package com.gov.wiki.config;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.exception.CheckException;

/**
 * @ClassName: CustomControllerAdvice 
 * @Description: 统一异常处理
 * @author cys 
 * @date 2018年3月15日 下午4:08:00
 */
@RestControllerAdvice
public class CommonExceptionAdvice {
	
    @ExceptionHandler(value = Exception.class)
    public ResultBean<String> errorHandler(Exception ex) {
    	ResultBean<String> rb = ResultBean.error(ex.getMessage());
    	if(ex instanceof CheckException) {
    		CheckException ce = (CheckException) ex;
    		rb = ResultBean.error(ce.getCode(), ce.getMessage());
    		rb.setMsg(ce.getMessage1());
    	}
        return rb;
    }
}