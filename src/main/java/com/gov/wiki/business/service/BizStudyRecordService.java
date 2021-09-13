package com.gov.wiki.business.service;

import com.gov.wiki.common.entity.buss.BizStudyRecord;
import com.gov.wiki.common.service.IBaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface BizStudyRecordService extends IBaseService<BizStudyRecord, String> {
    Page<BizStudyRecord> findAll(Specification specification, Pageable pageable);
    BizStudyRecord findByMemberIdAndStudyId(String memberId,String studyId);
}
