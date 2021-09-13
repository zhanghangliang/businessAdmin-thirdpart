package com.gov.wiki.wechat.dao;

import com.gov.wiki.common.entity.wechat.WxMember;
import com.gov.wiki.common.repository.BaseRepository;

public interface WxMemberDao extends BaseRepository<WxMember, String> {
    WxMember findByMobile(String mobile);
    WxMember findByOpenid(String openid);
}
