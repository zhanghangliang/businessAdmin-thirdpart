package com.gov.wiki.business.service;

import com.gov.wiki.common.entity.buss.BizAudit;
import com.gov.wiki.common.service.IBaseService;

public interface BizAuditService extends IBaseService<BizAudit, String> {
    BizAudit findByAuditedId(String AuditedId);
}
