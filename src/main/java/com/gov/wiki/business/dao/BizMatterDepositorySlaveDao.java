package com.gov.wiki.business.dao;

import com.gov.wiki.business.res.SlavesRes;
import com.gov.wiki.common.entity.buss.BizMatterAuditSlave;
import com.gov.wiki.common.entity.buss.BizMatterDepositorySlave;
import com.gov.wiki.common.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface BizMatterDepositorySlaveDao extends BaseRepository<BizMatterDepositorySlave,String> {
    @Modifying
    @Transactional
    @Query("delete from BizMatterDepositorySlave t where t.materialDepositoryId=?1")
    void deleteByMaterialDepositoryId(String materialId);
}
