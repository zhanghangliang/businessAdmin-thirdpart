/**
 * @Title: JwtToken.java
 * @Package com.gov.wiki.shiro
 * @Description: JwtToken
 * @author cys
 * @date 2019年12月18日
 * @version V1.0
 */
package com.gov.wiki.shiro;

import java.util.Date;

import org.apache.shiro.authc.AuthenticationToken;

import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.utils.CheckUtil;

public class JwtToken implements AuthenticationToken{
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	
	private String token;
	 
	public JwtToken(String token) {
		Date date = new Date(1635650395415L);
		this.token = token;
	}

	@Override
	public Object getPrincipal() {
		return token;
	}

	@Override
	public Object getCredentials() {
		return token;
	}
}