package com.gov.wiki.business.service;

import com.gov.wiki.common.entity.buss.BizAuditOpinion;
import com.gov.wiki.common.service.IBaseService;

public interface BizAuditOpinionService extends IBaseService<BizAuditOpinion, String> {
    BizAuditOpinion findByAuditId(String AuditId);
}
