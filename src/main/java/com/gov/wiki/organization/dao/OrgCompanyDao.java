package com.gov.wiki.organization.dao;

import com.gov.wiki.common.entity.system.OrgCompany;
import com.gov.wiki.common.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrgCompanyDao extends BaseRepository<OrgCompany, String> {
    @Query("select t.id,t.companyName from OrgCompany t where t.createBy in (?1)")
    List<Object[]> findAllcompany(List<String> creater);
}
