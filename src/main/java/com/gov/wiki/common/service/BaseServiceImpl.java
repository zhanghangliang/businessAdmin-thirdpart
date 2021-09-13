/**
 * @Title: BaseServiceImpl.java
 * @Package com.jade.filesystem.service.impl
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2019年7月25日
 * @version V1.0
 */
package com.gov.wiki.common.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.check.CheckClass;
import com.gov.wiki.common.entity.IdEntity;
import com.gov.wiki.common.repository.BaseRepository;
import com.gov.wiki.common.utils.PageInfo;
import com.gov.wiki.common.utils.PageableTools;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.SortTools;
import org.springframework.data.jpa.domain.Specification;

/**
 * @ClassName: BaseServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author cys
 * @date 2019年7月25日
 */
@Transactional
public abstract class BaseServiceImpl<T extends IdEntity, ID extends Serializable, R extends BaseRepository<T, ID>> implements IBaseService<T, ID> {

	@Autowired
	public R baseRepository;
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	@Override
	public T saveOrUpdate(T t) {
		return baseRepository.save(t);
	}

	@Override
	public ResultBean<T> saveOrUpdateEntity(T t) {
		T entity = saveOrUpdate(t);
		return new ResultBean(entity);
	}

	@Override
	public void delete(T t) {
		baseRepository.delete(t);
	}

	@Override
	public List<T> findAll() {
		return baseRepository.findAll();
	}

	@Override
	public void deleteById(ID id) {
		Optional<T> op = baseRepository.findById(id);
		if(!op.isPresent()) {
			return;
		}
		baseRepository.deleteById(id);
	}

	@Override
	public T findById(ID id) {
		T t = null;
		Optional<T> op = baseRepository.findById(id);
		if(op.isPresent()) {
			t = op.get();
		}
		return t;
	}

	@Override
	public void deleteAll(Iterable<? extends T> entities) {
		baseRepository.deleteAll(entities);
	}

	@Override
	public List<T> findAll(Example<T> example) {
		return baseRepository.findAll(example);
	}

	@Override
	public Page<T> findAll(Pageable pageable) {
		return baseRepository.findAll(pageable);
	}

	@Override
	public Page<T> findAll(Example<T> example, Pageable pageable) {
		return baseRepository.findAll(example, pageable);
	}

	@Override
	public List<T> saveAll(List<T> entities) {
		return baseRepository.saveAll(entities);
	}

	@Override
	public void batchDelete(List<ID> ids) {
		List<T> entities = baseRepository.findAllById(ids);
		baseRepository.deleteAll(entities);
	}

	@Override
	public ResultBean<PageInfo> pageList(ReqBean<T> bean, Class<T> cls) throws InstantiationException, IllegalAccessException {
		T param = bean.getBody();
		param = param == null?cls.newInstance():param;
		PageInfo pageInfo = bean.getPage();
		Pageable pageable = PageableTools.basicPage(pageInfo.getCurrentPage(), pageInfo.getPageSize(), pageInfo.getSortList());
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withStringMatcher(StringMatcher.CONTAINING)
				.withIgnoreNullValues();
		Example<T> example = Example.of(param, matcher);
		Page<T> page = this.findAll(example, pageable);
		pageInfo.setDataList(page.getContent());
		pageInfo.setTotalPages(page.getTotalPages());
		pageInfo.setTotal(page.getTotalElements());
		return new ResultBean(pageInfo);
	}
	
	@Override
	public PageInfo page(ReqBean<T> bean, Class<T> cls) {
		try {
			return pageList(bean, cls).getData();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ResultBean<PageInfo> pageList(ReqBean<T> bean, Class<T> cls, ExampleMatcher matcher) throws InstantiationException, IllegalAccessException {
		T param = bean.getBody();
		param = param == null?cls.newInstance():param;
		PageInfo pageInfo = bean.getPage();
		Pageable pageable = PageableTools.basicPage(pageInfo.getCurrentPage(), pageInfo.getPageSize(), pageInfo.getSortList());
		if(matcher == null) {
			matcher = ExampleMatcher.matching()
					.withStringMatcher(StringMatcher.CONTAINING)
					.withIgnoreNullValues();
		}
		Example<T> example = Example.of(param, matcher);
		Page<T> page = this.findAll(example, pageable);
		pageInfo.setDataList(page.getContent());
		pageInfo.setTotalPages(page.getTotalPages());
		pageInfo.setTotal(page.getTotalElements());
		return new ResultBean(pageInfo);
	}

	@Override
	public List<T> findByIds(List<ID> ids) {
		if(ids == null || ids.isEmpty()) {
			return null;
		}
		return baseRepository.findAllById(ids);
	}

	@Override
	public ResultBean<List<T>> findList(ReqBean<T> bean, Class<T> cls) throws InstantiationException, IllegalAccessException {
		T param = bean.getBody();
		param = param == null?cls.newInstance():param;
		PageInfo pageInfo = bean.getPage();
		Sort sort = SortTools.basicSort(pageInfo.getSortList());
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withStringMatcher(StringMatcher.CONTAINING)
				.withIgnoreNullValues();
		Example<T> example = Example.of(param, matcher);
		List<T> list = baseRepository.findAll(example, sort);
		return new ResultBean(list);
	}

	@Override
	public ResultBean<List<T>> findList(ReqBean<T> bean, Class<T> cls, ExampleMatcher matcher)
			throws InstantiationException, IllegalAccessException {
		T param = bean.getBody();
		param = param == null?cls.newInstance():param;
		PageInfo pageInfo = bean.getPage();
		Sort sort = SortTools.basicSort(pageInfo.getSortList());
		if(matcher == null) {
			matcher = ExampleMatcher.matching()
					.withStringMatcher(StringMatcher.CONTAINING)
					.withIgnoreNullValues();
		}
		Example<T> example = Example.of(param, matcher);
		List<T> list = baseRepository.findAll(example, sort);
		return new ResultBean(list);
	}
	
	protected PageInfo createPageInfo(Page page) {
		PageInfo pageInfo = new PageInfo();
		return setPageData(page, pageInfo);
	}

	protected List<Object[]> listSelect(Specification<T> spec, Class<T> clazz) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
		Root<T> root = query.from(clazz);
		Predicate predicate = spec.toPredicate(root, query, builder);
		if (predicate != null) {
			query.where(predicate);
		}
		return entityManager.createQuery(query).getResultList();
	}

	protected PageInfo setPageData(Page page, PageInfo pageInfo) {
		pageInfo.setDataList(page.getContent());
		pageInfo.setTotalPages(page.getTotalPages());
		pageInfo.setTotal(page.getTotalElements());
		return pageInfo;
	}
}