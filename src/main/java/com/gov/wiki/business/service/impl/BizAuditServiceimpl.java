package com.gov.wiki.business.service.impl;

import com.gov.wiki.business.dao.BizAuditDao;
import com.gov.wiki.business.service.BizAuditService;
import com.gov.wiki.common.entity.buss.BizAudit;
import com.gov.wiki.common.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BizAuditServiceimpl extends BaseServiceImpl<BizAudit,String, BizAuditDao> implements BizAuditService {
    @Override
    public BizAudit findByAuditedId(String AuditedId) {
        return this.baseRepository.findByAuditedId(AuditedId);
    }
}
