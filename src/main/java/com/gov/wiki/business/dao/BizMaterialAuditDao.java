package com.gov.wiki.business.dao;

import com.gov.wiki.common.entity.buss.BizMaterialAudit;
import com.gov.wiki.common.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BizMaterialAuditDao extends BaseRepository<BizMaterialAudit,String> {
    @Query("select distinct t.materialType from BizMaterialAudit t where t.createBy in(?1)")
    List<String> findAllAuditMaterialType(List<String> creater);
}
