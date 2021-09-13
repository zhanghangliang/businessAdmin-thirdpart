/**
 * @Title: PostServiceImpl.java
 * @Package com.gov.wiki.organization.service.impl
 * @Description: 岗位管理处理接口实现
 * @author cys
 * @date 2019年11月2日
 * @version V1.0
 */
package com.gov.wiki.organization.service.impl;

import java.util.List;
import javax.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.gov.wiki.common.entity.system.OrgPost;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.PageInfo;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.SortTools;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.organization.dao.OrgPostDao;
import com.gov.wiki.organization.req.BaseQuery;
import com.gov.wiki.organization.service.IPostService;

@Service("postService")
public class PostServiceImpl extends BaseServiceImpl<OrgPost, String, OrgPostDao> implements IPostService {

	@Override
	public int countPost() {
		return this.baseRepository.countPost();
	}

	@Override
	public List<OrgPost> queryPostList(ReqBean<BaseQuery> bean) {
		BaseQuery bq = bean.getBody();
		PageInfo pageInfo = bean.getPage();
		Sort sort = SortTools.basicSort(pageInfo.getSortList());
		final BaseQuery param = bq == null?new BaseQuery():bq;
		Specification<OrgPost> spec = (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.conjunction();
			if(StringUtils.isNotBlank(param.getKeywords())) {
				predicate.getExpressions().add(criteriaBuilder.or(
					criteriaBuilder.like(root.get("code").as(String.class), "%" + param.getKeywords() + "%"),
					criteriaBuilder.like(root.get("name").as(String.class), "%" + param.getKeywords() + "%")
				));
			}
			return predicate;
		};
		List<OrgPost> postList = this.baseRepository.findAll(spec, sort);
		return postList;
	}
}