/**
 * @Title: ExceptionController.java
 * @Package com.gov.wiki.exceptions
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2019年8月1日
 * @version V1.0
 */
package com.gov.wiki.exceptions;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import com.gov.wiki.common.beans.ResultBean;

import springfox.documentation.annotations.ApiIgnore;

/**
 * @ClassName: ExceptionController
 * @Description: 异常处理
 * @author cys
 * @date 2019年8月1日
 */
@ApiIgnore
@RestController
@EnableConfigurationProperties({ServerProperties.class})
public class ExceptionController implements ErrorController {
	
	private ErrorAttributes errorAttributes;
	 
    @Autowired
    private ServerProperties serverProperties;
 
    /**
     * @Description:创建一个新的实例 ExceptionController
     * @param errorAttributes
     */
    @Autowired
    public ExceptionController(ErrorAttributes errorAttributes) {
        Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
        this.errorAttributes = errorAttributes;
    }
 
    /**
     * @Title: error
     * @Description: 处理错误异常
     * @param request
     * @return ResultBean 返回类型
     * @throws
     */
    @RequestMapping(value = "/error") 
    @ResponseBody
    public ResultBean error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
        HttpStatus status = getStatus(request);
        return ResultBean.error(status.value(), body.get("message").toString());
    }
 
    /**
     * @Title: isIncludeStackTrace
     * @Description: Determine if the stacktrace attribute should be included.
     * @param request
     * @param produces
     * @return boolean 返回类型
     * @throws
     */
    protected boolean isIncludeStackTrace(HttpServletRequest request, MediaType produces) {
        ErrorProperties.IncludeStacktrace include = this.serverProperties.getError().getIncludeStacktrace();
        if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
            return true;
        }
        if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
            return getTraceParameter(request);
        }
        return false;
    }
 
 
    /**
     * @Title: getErrorAttributes
     * @Description: 获取错误的信息
     * @param request
     * @param includeStackTrace
     * @return Map<String,Object> 返回类型
     * @throws
     */
    private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
    	ServletWebRequest requestAttributes =  new ServletWebRequest(request);
        return this.errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }
 
    /**
     * @Title: getTraceParameter
     * @Description: 是否包含trace
     * @param request
     * @return boolean 返回类型
     * @throws
     */
    private boolean getTraceParameter(HttpServletRequest request) {
        String parameter = request.getParameter("trace");
        if (parameter == null) {
            return false;
        }
        return !"false".equals(parameter.toLowerCase());
    }
 
    /**
     * @Title: getStatus
     * @Description: 获取错误编码
     * @param request
     * @return HttpStatus 返回类型
     * @throws
     */
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
 
    /**
     * @Description: 实现错误路径,暂时无用
     */
    @Override
    public String getErrorPath() {
        return "";
    }
}