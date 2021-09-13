package com.gov.wiki.business.dao;

import com.gov.wiki.common.entity.buss.BizAuditOpinion;
import com.gov.wiki.common.repository.BaseRepository;

public interface BizAuditOpinionDao extends BaseRepository<BizAuditOpinion,String> {
    BizAuditOpinion findByAuditId(String Audit_id);
}
