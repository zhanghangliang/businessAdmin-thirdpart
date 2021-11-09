package com.gov.wiki.wechat.service;

import org.springframework.data.domain.Page;

import com.gov.wiki.common.entity.wechat.WxMember;
import com.gov.wiki.common.service.IBaseService;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.organization.req.LoginReq;

public interface WxMemberService extends IBaseService<WxMember, String> {
    WxMember externalLogin(LoginReq bean);
    WxMember findByMobile(String mobile);
    WxMember findByOpenId(String openid);
	Page<WxMember> pageByName(ReqBean<String> bean);

    void deleteByMobile(String mobile);

    void deleteByOpenId(String openId);
}
