package com.gov.wiki.business.dao;

import com.gov.wiki.common.entity.buss.BizMatterAuditMain;
import com.gov.wiki.common.repository.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BizMatterAuditMainDao extends BaseRepository<BizMatterAuditMain,String> {
    List<BizMatterAuditMain> findByUpmatterId(String upmatterid);
}
