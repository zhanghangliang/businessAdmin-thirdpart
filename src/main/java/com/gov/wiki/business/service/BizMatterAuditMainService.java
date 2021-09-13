package com.gov.wiki.business.service;

import com.gov.wiki.business.req.MatterQuery;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.entity.buss.BizMatterAuditMain;
import com.gov.wiki.common.service.IBaseService;
import com.gov.wiki.common.utils.PageInfo;
import com.gov.wiki.common.utils.ReqBean;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;

public interface BizMatterAuditMainService extends IBaseService<BizMatterAuditMain, String> {
    List<BizMatterAuditMain> findByUpmatterId(String upmatterid);
    Page<BizMatterAuditMain> findAll(Specification specification,PageRequest pageRequest);
    
    ResultBean<PageInfo> pageAuditMetter(ReqBean<MatterQuery> bean);
}
