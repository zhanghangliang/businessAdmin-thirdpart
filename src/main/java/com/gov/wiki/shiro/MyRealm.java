/**
 * @Title: MyRealm.java
 * @Package com.gov.wiki.shiro
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2019年12月18日
 * @version V1.0
 */
package com.gov.wiki.shiro;

import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.gov.wiki.common.entity.wechat.WxMember;
import com.gov.wiki.wechat.service.WxMemberService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.gov.wiki.common.entity.system.OrgMember;
import com.gov.wiki.common.enums.StatusEnum;
import com.gov.wiki.common.redis.RedisManager;
import com.gov.wiki.common.utils.JwtUtil;
import com.gov.wiki.common.utils.SessionUser;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.organization.service.IMemberService;
@Slf4j
@Component
public class MyRealm extends AuthorizingRealm {
	
	/**
	 * 注入memberService
	 */
	@Autowired
	private IMemberService memberService;
	/**
	 * 注入redisManager
	 */
	@Autowired
	private RedisManager redisManager;

	@Autowired
	private WxMemberService wxmemberService;

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof JwtToken;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        return simpleAuthorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
		String token = (String) auth.getCredentials();
		if(StringUtils.isBlank(token)) {
			throw new AuthenticationException("TOKEN无效");
		}
        String username = JwtUtil.getUsername(token);
        String openId = JwtUtil.getOpenId(token);
        if(StringUtils.isBlank(username) && StringUtils.isBlank(openId)) {
        	 throw new AuthenticationException("TOKEN认证失败,用户名或OpenId不存在！");
        }
        if (!JwtUtil.verify(token, username, openId)) {
            throw new AuthenticationException("TOKEN认证失败,Jwt校验未通过！");
        }
        
        Date expiresDate = JwtUtil.getExpiresAt();
        Date currentDate = new Date();
        if(expiresDate == null || expiresDate.getTime() < currentDate.getTime()) {
        	throw new AuthenticationException("TOKEN已过期！");
        }
        SessionUser sessionUser = (SessionUser) redisManager.getSessionUser(token);
        OrgMember member = sessionUser != null?sessionUser.getMember():null;
        if (member == null) {
            throw new AuthenticationException("用户未登录，TOKEN无效！");
        }
        OrgMember sysMember = null;
        if(StringUtils.isNotBlank(username)) {
        	sysMember = memberService.getByUsername(username);
        	if(sysMember==null){
				WxMember byMobile = wxmemberService.findByOpenId(member.getOpenId());
				if(byMobile!=null){
					OrgMember orgMember = new OrgMember();
					orgMember.setPassword(byMobile.getPassword());
					orgMember.setRealName(byMobile.getName());
					orgMember.setSex(byMobile.getSex());
					orgMember.setMobile(byMobile.getMobile());
					orgMember.setStatus(StatusEnum.ENABLE.getValue());
					orgMember.setOpenId(byMobile.getOpenid());
					sysMember=orgMember;
				}
			}
        }
        if(sysMember == null) {
        	throw new AuthenticationException("用户不存在，TOKEN无效！");
        }
        if(sysMember.getStatus() != StatusEnum.ENABLE.getValue()) {
        	throw new AuthenticationException("用户已禁用，TOKEN无效！");
        }
        return new SimpleAuthenticationInfo(token, token, "my_realm");
	}
}