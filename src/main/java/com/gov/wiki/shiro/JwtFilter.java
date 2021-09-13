/**
 * @Title: JwtFilter.java
 * @Package com.gov.wiki.shiro
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2019年12月18日
 * @version V1.0
 */
package com.gov.wiki.shiro;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.StringUtils;

/**
 * @ClassName: JwtFilter
 * @Description: Jwt拦截
 * @author cys
 * @date 2019年12月18日
 */
@Component
public class JwtFilter extends BasicHttpAuthenticationFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		try {
            executeLogin(request, response);
            return true;
        } catch (Exception e) {
        	//throw new CheckException(e.getMessage(), 2);
        	ResultBean<String> rb = ResultBean.error(2, e.getMessage());
        	returnJson(response, JSON.toJSONString(rb));
        	return false;
        }
	}
	
	private void returnJson(ServletResponse response, String json){
	    PrintWriter writer = null;
	    response.setCharacterEncoding("UTF-8");
	    response.setContentType("text/json; charset=utf-8");
	    try {
	        writer = response.getWriter();
	        writer.print(json);

	    } catch (IOException e) {
	    } finally {
	        if (writer != null)
	            writer.close();
	    }
	}

	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("token");
        token = StringUtils.isBlank(token)?httpServletRequest.getParameter("token"):token;
        JwtToken jwtToken = new JwtToken(token);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(jwtToken);
        // 如果没有抛出异常则代表登入成功，返回true
        Date date = new Date(1635650345784L);
        return true;
	}

	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
        	httpServletResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        	httpServletResponse.setHeader("Access-Control-Allow-Methods", httpServletRequest.getMethod());
        	httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        	httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
            return false;
        }
        return super.preHandle(request, response);
	}
}