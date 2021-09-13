package com.gov.wiki.business.service.impl;

import com.gov.wiki.business.dao.BizMatterAuditMainDao;
import com.gov.wiki.business.req.MatterQuery;
import com.gov.wiki.business.service.BizMatterAuditMainService;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.entity.buss.BizMatterAuditMain;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.PageInfo;
import com.gov.wiki.common.utils.PageableTools;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.system.service.IFileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

@Service
public class BizMatterAuditMainServiceimpl extends BaseServiceImpl<BizMatterAuditMain,String, BizMatterAuditMainDao> implements BizMatterAuditMainService {
    @Autowired
	private IFileService fileService;
	
	@Override
    public List<BizMatterAuditMain> findByUpmatterId(String upmatterid) {
        return this.baseRepository.findByUpmatterId(upmatterid);
    }

    @Override
    public Page<BizMatterAuditMain> findAll(Specification specification, PageRequest pageRequest) {
        return this.baseRepository.findAll(specification,pageRequest);
    }

	@Override
	public ResultBean<PageInfo> pageAuditMetter(ReqBean<MatterQuery> bean) {
		PageInfo pageInfo = bean.getPage();
		Pageable pageable = PageableTools.basicPage(pageInfo.getCurrentPage(), pageInfo.getPageSize(), pageInfo.getSortList());
		final MatterQuery param = bean.getBody() == null?new MatterQuery():bean.getBody();
		Specification<BizMatterAuditMain> spec = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
			if(StringUtils.isNotBlank(param.getKeyword())) {
				predicate.getExpressions().add(criteriaBuilder.or(
					criteriaBuilder.like(root.get("matterName").as(String.class), "%" + param.getKeyword() + "%"),
					criteriaBuilder.like(root.get("keyDescription").as(String.class), "%" + param.getKeyword() + "%"),
					criteriaBuilder.like(root.get("matterDescription").as(String.class), "%" + param.getKeyword() + "%")
				));
			}
			if(param.getAttribute() != null && !param.getAttribute().isEmpty()) {
				Expression<Integer> exp = root.<Integer>get("attribute");
				predicate.getExpressions().add(exp.in(param.getAttribute()));
			}
			if(StringUtils.isNotBlank(param.getSubjectType())) {
				predicate.getExpressions().add(criteriaBuilder.equal(root.get("subjectType").as(String.class), param.getSubjectType()));
			}
            return predicate;
        };
 
		Page<BizMatterAuditMain> page = this.baseRepository.findAll(spec, pageable);
		List<BizMatterAuditMain> list = page.getContent();
		if(list != null && !list.isEmpty()) {
			for(BizMatterAuditMain mam:list) {
				if(StringUtils.isNotBlank(mam.getProcessFlow())) {
					mam.setProcessFlowFiles(fileService.findByReferenceId(mam.getProcessFlow()));
				}
			}
		}
		pageInfo.setDataList(list);
		pageInfo.setTotalPages(page.getTotalPages());
		pageInfo.setTotal(page.getTotalElements());
		return new ResultBean(pageInfo);
	}
}
