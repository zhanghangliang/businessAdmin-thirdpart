package com.gov.wiki.organization.service.impl;


import com.gov.wiki.common.entity.system.OrgCompany;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.organization.dao.OrgCompanyDao;
import com.gov.wiki.organization.service.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl extends BaseServiceImpl<OrgCompany, String, OrgCompanyDao> implements ICompanyService {
    @Autowired
    private OrgCompanyDao orgCompanyDao;
    @Override
    public List<Object[]> findAllcompany(List<String> creater) {
        return orgCompanyDao.findAllcompany(creater);
    }
}
