package com.gov.wiki.wechat.service.Impl;

import com.gov.wiki.common.entity.wechat.WxSearchRecord;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.wechat.dao.WxSearchRecordDao;
import com.gov.wiki.wechat.service.WxSearchRecordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WxSearchRecordServiceImpl extends BaseServiceImpl<WxSearchRecord, String, WxSearchRecordDao> implements WxSearchRecordService {
    @Override
    public void deleteByMemberId(String memberId) {
        this.baseRepository.deleteByMemberIdAndSearchType(memberId,0);
    }

    @Override
    public Page<WxSearchRecord> findAll(Specification specification, Pageable pageable) {
        return this.baseRepository.findAll(specification,pageable);
    }

    @Override
    public List<WxSearchRecord> findByMemberId(String memberId) {
        return this.baseRepository.findByMemberIdAndSearchType(memberId,0);
    }

    @Override
    public WxSearchRecord findBySearchContent(String searchContent) {
        return this.baseRepository.findBySearchContentAndSearchType(searchContent,1);
    }
}
