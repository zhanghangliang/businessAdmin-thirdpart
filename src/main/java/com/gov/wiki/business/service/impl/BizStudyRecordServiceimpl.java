package com.gov.wiki.business.service.impl;

import com.gov.wiki.business.dao.BizStudyRecordDao;
import com.gov.wiki.business.service.BizStudyRecordService;
import com.gov.wiki.common.entity.buss.BizStudyRecord;
import com.gov.wiki.common.service.BaseServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class BizStudyRecordServiceimpl extends BaseServiceImpl<BizStudyRecord,String, BizStudyRecordDao> implements BizStudyRecordService {
    @Override
    public Page<BizStudyRecord> findAll(Specification specification, Pageable pageable) {
        return this.baseRepository.findAll(specification,pageable);
    }

    @Override
    public BizStudyRecord findByMemberIdAndStudyId(String memberId,String studyId){
        return this.baseRepository.findByMemberIdAndStudyId(memberId,studyId);
    }
}
