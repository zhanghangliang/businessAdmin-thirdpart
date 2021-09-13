/**   
 * @Copyright:  成都北诺星科技有限公司  All rights reserved.Notice 官方网站：http://www.beinuoxing.com
 */
package com.github.wenhao.jpa.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class NotNullOrNullSpecification<T> extends AbstractSpecification<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7630276209101917431L;
	private final String property;
	private final boolean isNull;
	
	
	public NotNullOrNullSpecification(String property, boolean isNull) {
		super();
		this.property = property;
		this.isNull = isNull;
	}


	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		From from = getRoot(property, root);
        String field = getProperty(property);
        if (isNull) {
            return cb.isNull(from.get(field));
        } 
        return cb.isNotNull(from.get(field));
	}

}
