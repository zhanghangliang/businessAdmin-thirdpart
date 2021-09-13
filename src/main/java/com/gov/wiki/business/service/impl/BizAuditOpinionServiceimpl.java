package com.gov.wiki.business.service.impl;


import com.gov.wiki.business.dao.BizAuditDao;
import com.gov.wiki.business.dao.BizAuditOpinionDao;
import com.gov.wiki.business.service.BizAuditOpinionService;
import com.gov.wiki.common.entity.buss.BizAuditOpinion;
import com.gov.wiki.common.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BizAuditOpinionServiceimpl extends BaseServiceImpl<BizAuditOpinion,String, BizAuditOpinionDao> implements BizAuditOpinionService {
    @Override
    public BizAuditOpinion findByAuditId(String AuditId) {
        return this.baseRepository.findByAuditId(AuditId);
    }
}
