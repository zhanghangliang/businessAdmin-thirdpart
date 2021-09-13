/**
 * @Title: WorkTaskServiceImpl.java
 * @Package com.gov.wiki.business.service.impl
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2020年11月25日
 * @version V1.0
 */
package com.gov.wiki.business.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.github.wenhao.jpa.PredicateBuilder;
import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.business.dao.BizWorkTaskDao;
import com.gov.wiki.business.service.IWorkTaskService;
import com.gov.wiki.common.entity.buss.BizWorkTask;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.PageInfo;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.organization.req.BaseQuery;

/**
 * @ClassName: WorkTaskServiceImpl
 * @Description: 工作任务管理业务处理接口实现
 * @author cys
 * @date 2020年11月25日
 */
@Service("workTaskService")
public class WorkTaskServiceImpl extends BaseServiceImpl<BizWorkTask, String, BizWorkTaskDao> implements IWorkTaskService {

	@Override
	public PageInfo pageList(ReqBean<BaseQuery> bean) {
		PageInfo pageInfo = bean.getPage();
		PredicateBuilder<BizWorkTask> builder = Specifications.and();
		BaseQuery query = bean.getBody();
		if(query == null) {
			query = new BaseQuery();
		}
		if(StringUtils.isNotBlank(query.getKeywords())) {
			builder.like("name", "%" + query.getKeywords() + "%");
		}
		Page<BizWorkTask> page = this.baseRepository.findAll(builder.build(), bean.getHeader().getPageable());
		pageInfo.setDataList(page.getContent());
		pageInfo.setTotalPages(page.getTotalPages());
		pageInfo.setTotal(page.getTotalElements());
		return pageInfo;
	}
}
