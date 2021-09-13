package com.gov.wiki.business.service;

import com.gov.wiki.common.entity.buss.BizMatterDepositorySlave;
import com.gov.wiki.common.service.IBaseService;


public interface BizMatterDepositorySlaveService extends IBaseService<BizMatterDepositorySlave, String> {
    void deleteByMaterialDepositoryId(String materialId);
}
