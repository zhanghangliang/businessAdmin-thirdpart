package com.gov.wiki.wechat.dao;

import com.gov.wiki.common.entity.wechat.WxOperationRecordCount;
import com.gov.wiki.common.repository.BaseRepository;

public interface WxOperationRecordCountDao extends BaseRepository<WxOperationRecordCount, String> {
    WxOperationRecordCount findBySubjectId(String subjectid);
}
