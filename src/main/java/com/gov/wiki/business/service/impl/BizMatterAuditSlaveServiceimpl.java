package com.gov.wiki.business.service.impl;

import com.gov.wiki.business.dao.BizMatterAuditSlaveDao;
import com.gov.wiki.business.res.SlavesRes;
import com.gov.wiki.business.service.BizMatterAuditSlaveService;
import com.gov.wiki.common.entity.buss.BizMatterAuditSlave;
import com.gov.wiki.common.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BizMatterAuditSlaveServiceimpl extends BaseServiceImpl<BizMatterAuditSlave,String, BizMatterAuditSlaveDao> implements BizMatterAuditSlaveService {
    @Override
    public List<BizMatterAuditSlave> findByMatterAuditId(String mainid) {
        return this.baseRepository.findByMatterAuditId(mainid);
    }

    @Override
    public void deleteByMaterialDepositoryId(String materialId) {
        this.baseRepository.deleteByMaterialDepositoryId(materialId);
    }
}
