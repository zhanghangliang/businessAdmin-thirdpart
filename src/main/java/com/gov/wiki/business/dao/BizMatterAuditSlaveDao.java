package com.gov.wiki.business.dao;

import com.gov.wiki.business.res.SlavesRes;
import com.gov.wiki.common.entity.buss.BizMatterAuditSlave;
import com.gov.wiki.common.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface BizMatterAuditSlaveDao extends BaseRepository<BizMatterAuditSlave,String> {
    List<BizMatterAuditSlave> findByMatterAuditId(String mainid);

    @Query("select new com.gov.wiki.business.res.SlavesRes(l.id,r.materialId,r.materialType,r.front,r.materialName,l.number,l.inspect,l.collect,l.necessity,l.materialDepositoryId) from BizMatterAuditSlave l left JOIN BizMaterialDepository r on l.materialDepositoryId=r.id where l.matterAuditId =?1")
    List<SlavesRes> findAllSlave(String matterAuditId);

    @Modifying
    @Transactional
    @Query("delete from BizMatterAuditSlave t where t.materialDepositoryId=?1")
    void deleteByMaterialDepositoryId(String materialId);
}
