package com.gov.wiki.organization.service;

import com.gov.wiki.common.entity.system.OrgCustomer;
import com.gov.wiki.common.service.IBaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

public interface ICustomerService extends IBaseService<OrgCustomer, String> {
    Page<OrgCustomer> findAll(Specification specification, PageRequest pageRequest);
}
