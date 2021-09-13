package com.gov.wiki.organization.service;

import com.gov.wiki.common.entity.system.OrgCompany;
import com.gov.wiki.common.service.IBaseService;

import java.util.List;

public interface ICompanyService extends IBaseService<OrgCompany, String> {
    List<Object[]> findAllcompany(List<String> creater);
}
