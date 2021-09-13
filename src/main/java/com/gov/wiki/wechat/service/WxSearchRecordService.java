package com.gov.wiki.wechat.service;

import com.gov.wiki.common.entity.wechat.WxSearchRecord;
import com.gov.wiki.common.service.IBaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;


public interface WxSearchRecordService extends IBaseService<WxSearchRecord, String> {
    void deleteByMemberId(String memberId);
    Page<WxSearchRecord> findAll(Specification specification, Pageable pageable);
    List<WxSearchRecord> findByMemberId(String memberId);
    WxSearchRecord findBySearchContent(String searchContent);
}
