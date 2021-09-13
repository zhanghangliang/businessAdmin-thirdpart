package com.gov.wiki.business.service.impl;

import com.gov.wiki.business.dao.BizMatterDepositoryMainDao;
import com.gov.wiki.business.req.MatterQuery;
import com.gov.wiki.business.service.BizMatterDepositoryMainService;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.entity.buss.BizMatterAuditMain;
import com.gov.wiki.common.entity.buss.BizMatterDepositoryMain;
import com.gov.wiki.common.entity.system.OrgDepart;
import com.gov.wiki.common.entity.system.OrgJob;
import com.gov.wiki.common.entity.system.OrgMember;
import com.gov.wiki.common.entity.system.OrgPost;
import com.gov.wiki.common.entity.system.SysDictItem;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.PageInfo;
import com.gov.wiki.common.utils.PageableTools;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.organization.req.query.MemberQuery;
import com.gov.wiki.system.service.IFileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

@Service
public class BizMatterDepositoryMainServiceimpl extends BaseServiceImpl<BizMatterDepositoryMain, String, BizMatterDepositoryMainDao>
		implements BizMatterDepositoryMainService {
	
	@Autowired
	private IFileService fileService;
	
	@Override
	public List<BizMatterDepositoryMain> findByUpmatterId(String upmatterid) {
		return this.baseRepository.findByUpmatterIdAndOnline(upmatterid, true);
	}

	@Override
	public Page<BizMatterDepositoryMain> findAll(Specification specification, PageRequest pageRequest) {
		return this.baseRepository.findAll(specification, pageRequest);
	}

	@Override
	public List<Object[]> groupUpMatterId(List<String> creater) {
		return this.baseRepository.groupUpMatterId(creater);
	}

	@Override
	public List<Object[]> findIdByUpmatterId(String upmatterid, List<String> creater) {
		return this.baseRepository.findIdByUpmatterId(upmatterid, creater);
	}

	@Override
	public List<BizMatterDepositoryMain> findsonbyids(List<String> ids) {
		return this.baseRepository.findsonbyids(ids);
	}

	@Override
	public Page<BizMatterDepositoryMain> findAll(Specification specification, Pageable pageable) {
		return this.baseRepository.findAll(specification, pageable);
	}

	@Override
	public List<String> findOnline() {
		return this.baseRepository.findOnline();
	}

	@Override
	public List<BizMatterDepositoryMain> findAll(Specification specification) {
		return this.baseRepository.findAll(specification);
	}

	@Override
	public ResultBean<PageInfo> pageDepositoryMetter(ReqBean<MatterQuery> bean) {
		PageInfo pageInfo = bean.getPage();
		Pageable pageable = PageableTools.basicPage(pageInfo.getCurrentPage(), pageInfo.getPageSize(), pageInfo.getSortList());
		final MatterQuery param = bean.getBody() == null?new MatterQuery():bean.getBody();
		Specification<BizMatterDepositoryMain> spec = (root, query, criteriaBuilder) -> {
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
 
		Page<BizMatterDepositoryMain> page = this.baseRepository.findAll(spec, pageable);
		List<BizMatterDepositoryMain> list = page.getContent();
		if(list != null && !list.isEmpty()) {
			for(BizMatterDepositoryMain mdm:list) {
				if(StringUtils.isNotBlank(mdm.getProcessFlow())) {
					mdm.setProcessFlowFiles(fileService.findByReferenceId(mdm.getProcessFlow()));
				}
			}
		}
		pageInfo.setDataList(list);
		pageInfo.setDataList(page.getContent());
		pageInfo.setTotalPages(page.getTotalPages());
		pageInfo.setTotal(page.getTotalElements());
		return new ResultBean(pageInfo);
	}

	@Override
	public List<BizMatterDepositoryMain> queryChildsByParent(String parentId) {
		parentId = StringUtils.isBlank(parentId)?"-1":parentId;
		List<BizMatterDepositoryMain> list = this.baseRepository.findByUpmatterId(parentId);
		if(list != null && !list.isEmpty()) {
			for(BizMatterDepositoryMain main:list) {
				if(main != null && StringUtils.isNotBlank(main.getProcessFlow())) {
		        	main.setProcessFlowFiles(fileService.findByReferenceId(main.getProcessFlow()));
		        }
			}
		}
		return list;
	}
}