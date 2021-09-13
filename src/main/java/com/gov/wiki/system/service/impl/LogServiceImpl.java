/**
 * @Title: LogServiceImpl.java 
 * @Package com.gov.wiki.system.service.impl
 * @Description: 日志管理Service接口实现
 * @author cys 
 * @date 2019年11月5日 下午9:24:33 
 * @version V1.0 
 */
package com.gov.wiki.system.service.impl;

import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.entity.system.SysLog;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.JwtUtil;
import com.gov.wiki.common.utils.PageInfo;
import com.gov.wiki.common.utils.PageableTools;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.system.dao.SysLogDao;
import com.gov.wiki.system.req.query.LogQuery;
import com.gov.wiki.system.service.ILogService;

@Service("logService")
public class LogServiceImpl extends BaseServiceImpl<SysLog, String, SysLogDao> implements ILogService{

	@Override
	public ResultBean<PageInfo> pageList(ReqBean<LogQuery> bean) {
		PageInfo pageInfo = bean.getPage();
		Pageable pageable = PageableTools.basicPage(pageInfo.getCurrentPage(), pageInfo.getPageSize(), pageInfo.getSortList());
		final LogQuery param = bean.getBody() == null?new LogQuery():bean.getBody();
		Specification<SysLog> spec = (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.conjunction();
			if(param.getLogType() != null) {
				predicate.getExpressions().add(criteriaBuilder.equal(root.get("logType").as(String.class), param.getLogType()));
			}
			if(StringUtils.isNotBlank(param.getKeywords())) {
				predicate.getExpressions().add(
					criteriaBuilder.or(
						criteriaBuilder.like(root.get("logContent").as(String.class), "%" + param.getKeywords() + "%"),
						criteriaBuilder.like(root.get("ip").as(String.class), "%" + param.getKeywords() + "%")
					));
			}
			return predicate;
		};
		Page<SysLog> page = this.baseRepository.findAll(spec, pageable);
		pageInfo.setDataList(page.getContent());
		pageInfo.setTotalPages(page.getTotalPages());
		pageInfo.setTotal(page.getTotalElements());
		return new ResultBean(pageInfo);
	}

	@Override
	public SysLog queryLastLoginLog() {
		SysLog log = null;
		String userId = JwtUtil.getUserId();
		if(StringUtils.isBlank(userId)) {
			return log;
		}
		Pageable pageable = PageableTools.basicPage(1, 2, "updateTime");
		Specification<SysLog> spec = (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.conjunction();
			predicate.getExpressions().add(criteriaBuilder.equal(root.get("logType").as(String.class), SysLog.TYPE_LOGIN));
			predicate.getExpressions().add(criteriaBuilder.equal(root.get("userId").as(String.class), userId));
			return predicate;
		};
		Page<SysLog> page = this.baseRepository.findAll(spec, pageable);
		List<SysLog> list = page.getContent();
		log = (list != null && !list.isEmpty() && list.size() == 2)?list.get(1):null;
		return log;
	}

	@Override
	public Page<SysLog> findAll(Specification specification, PageRequest pageRequest) {
		return this.baseRepository.findAll(specification,pageRequest);
	}
}
