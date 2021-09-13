package com.gov.wiki.wechat.service.Impl;

import com.github.wenhao.jpa.PredicateBuilder;
import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.entity.wechat.WxMember;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.PasswordUtil;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.organization.req.LoginReq;
import com.gov.wiki.wechat.dao.WxMemberDao;
import com.gov.wiki.wechat.service.WxMemberService;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static com.gov.wiki.common.utils.CheckUtil.check;

@Service
public class WxMemberServiceImpl extends BaseServiceImpl<WxMember, String, WxMemberDao> implements WxMemberService {
    @Override
    public WxMember externalLogin(LoginReq bean) {
        WxMember wxMember = this.baseRepository.findByMobile(bean.getUsername());
        CheckUtil.notNull(wxMember, ResultCode.COMMON_ERROR, "用户不存在");
        check(PasswordUtil.checkEncryption(bean.getPassword(), wxMember.getPassword())
                , ResultCode.COMMON_ERROR, "密码错误");
        return wxMember;
    }

    @Override
    public WxMember findByMobile(String mobile) {
        return this.baseRepository.findByMobile(mobile);
    }

    @Override
    public WxMember findByOpenId(String openid) {
        return this.baseRepository.findByOpenid(openid);
    }

	@Override
	public Page<WxMember> pageByName(ReqBean<String> bean) {
		PredicateBuilder<WxMember> builder = Specifications.and();
        if(StringUtils.isNotBlank(bean.getBody())) {
        	builder.like("realName", "%"+bean.getBody()+"%");
        }
        return this.baseRepository.findAll(builder.build(), bean.getHeader().getPageable());
	}
}
