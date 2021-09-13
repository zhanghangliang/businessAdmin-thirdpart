package com.gov.wiki.business.service.impl;

import com.gov.wiki.business.dao.BizMatterDepositorySlaveDao;
import com.gov.wiki.business.res.SlavesRes;
import com.gov.wiki.business.service.BizMatterDepositorySlaveService;
import com.gov.wiki.common.entity.buss.BizMatterAuditSlave;
import com.gov.wiki.common.entity.buss.BizMatterDepositorySlave;
import com.gov.wiki.common.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BizMatterDepositorySlaveServiceimpl extends BaseServiceImpl<BizMatterDepositorySlave,String, BizMatterDepositorySlaveDao> implements BizMatterDepositorySlaveService {
    @Override
    public void deleteByMaterialDepositoryId(String materialId) {
        this.baseRepository.deleteByMaterialDepositoryId(materialId);
    }
}
