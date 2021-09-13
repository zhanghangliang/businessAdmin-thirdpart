/**   
 * @Copyright:  成都北诺星科技有限公司  All rights reserved.Notice 官方网站：http://www.beinuoxing.com
 */
package com.github.wenhao.jpa.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class GroupBySpecification<T> extends AbstractSpecification<T> {

	private final String property;
	
	
	public GroupBySpecification(String property) {
		super();
		this.property = property;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1747715163250704798L;

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		From from = getRoot(property, root);
        String field = getProperty(property);
		query.groupBy(from.get(field));
		return cb.conjunction();
	}

}
