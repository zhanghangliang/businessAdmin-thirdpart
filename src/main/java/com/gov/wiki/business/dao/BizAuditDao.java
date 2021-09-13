package com.gov.wiki.business.dao;

import com.gov.wiki.common.entity.buss.BizAudit;
import com.gov.wiki.common.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

public interface BizAuditDao extends BaseRepository<BizAudit,String> {
    BizAudit findByAuditedId(String Audited_id);
}
