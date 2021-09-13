package com.gov.wiki.business.dao;

import com.gov.wiki.common.entity.buss.BizStudyRecord;
import com.gov.wiki.common.repository.BaseRepository;

public interface BizStudyRecordDao extends BaseRepository<BizStudyRecord,String> {
    BizStudyRecord findByMemberIdAndStudyId(String memberId,String StudyId);
}
