package com.gov.wiki.business.service;

import com.gov.wiki.common.entity.buss.BizMatterAuditSlave;
import com.gov.wiki.common.service.IBaseService;

import java.util.List;

public interface BizMatterAuditSlaveService extends IBaseService<BizMatterAuditSlave, String> {
    List<BizMatterAuditSlave> findByMatterAuditId(String mainid);
    void deleteByMaterialDepositoryId(String materialId);
}
