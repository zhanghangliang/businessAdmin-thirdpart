package com.gov.wiki.organization.service.impl;

import com.gov.wiki.common.entity.system.OrgCustomer;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.organization.dao.OrgCustomerDao;
import com.gov.wiki.organization.service.ICustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl extends BaseServiceImpl<OrgCustomer, String, OrgCustomerDao> implements ICustomerService {
    @Override
    public Page<OrgCustomer> findAll(Specification specification, PageRequest pageRequest) {
        return this.baseRepository.findAll(specification,pageRequest);
    }
}
