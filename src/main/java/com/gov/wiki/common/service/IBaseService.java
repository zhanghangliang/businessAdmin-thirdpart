/**
 * @Title: IBaseService.java
 * @Package com.jade.filesystem.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2019年7月25日
 * @version V1.0
 */
package com.gov.wiki.common.service;

import java.util.List;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.utils.PageInfo;
import com.gov.wiki.common.utils.ReqBean;

/**
 * @ClassName: IBaseService
 * @Description: 基础业务处理类
 * @author cys
 * @date 2019年7月25日
 */
public interface IBaseService<T, ID> {
	/**
	 * @Title: saveOrUpdate
	 * @Description: 新增或者修改
	 * @param t
	 * @return T 返回类型
	 * @throws
	 */
	T saveOrUpdate(T t);
	
	/**
	 * @Title: saveOrUpdateEntity 
	 * @Description: 新增或者修改实体
	 * @param 设定文件 
	 * @return ResultBean<T>    返回类型 
	 * @throws
	 */
	ResultBean<T> saveOrUpdateEntity(T t);
	
	/**
	 * @Title: saveAll
	 * @Description: 批量保存实体
	 * @param entities
	 * @return List<T> 返回类型
	 * @throws
	 */
	List<T> saveAll(List<T> entities);
	
	/**
	 * @Title: delete
	 * @Description: 删除
	 * @param t
	 * @return void 返回类型
	 * @throws
	 */
	void delete(T t);
	
	/**
	 * @Title: deleteById
	 * @Description: 根据ID删除对象
	 * @param id
	 * @return void 返回类型
	 * @throws
	 */
	void deleteById(ID id);
	
	/**
	 * @Title: deleteAll
	 * @Description: 批量删除实体信息
	 * @param entities
	 * @return void 返回类型
	 * @throws
	 */
	void deleteAll(Iterable<? extends T> entities);
	
	/**
	 * @Title: batchDelete
	 * @Description: 批量删除
	 * @param ids
	 * @return void 返回类型
	 * @throws
	 */
	void batchDelete(List<ID> ids);
	
	/**
	 * @Title: findAll
	 * @Description: 查询所有结果
	 * @return List<T> 返回类型
	 * @throws
	 */
	List<T> findAll();
	
	/**
	 * @Title: findAll 
	 * @Description: 根据参数查询列表
	 * @param 设定文件 
	 * @return List<T>    返回类型 
	 * @throws
	 */
	ResultBean<List<T>> findList(ReqBean<T> bean, Class<T> cls) throws InstantiationException, IllegalAccessException;
	
	/**
	 * @Title: findList 
	 * @Description: 根据参数查询列表
	 * @param 设定文件 
	 * @return ResultBean<List<T>>    返回类型 
	 * @throws
	 */
	ResultBean<List<T>> findList(ReqBean<T> bean, Class<T> cls, ExampleMatcher matcher) throws InstantiationException, IllegalAccessException;
	
	/**
	 * @Title: findById
	 * @Description: 根据ID查询对象
	 * @param id
	 * @return T 返回类型
	 * @throws
	 */
	T findById(ID id);
	
	/**
	 * @Title: findByIds
	 * @Description: 根据ID查询实体信息
	 * @param ids
	 * @return List<T> 返回类型
	 * @throws
	 */
	List<T> findByIds(List<ID> ids);
	
	/**
	 * @Title: findAll
	 * @Description: 根据参数条件查询
	 * @param example
	 * @return List<T> 返回类型
	 * @throws
	 */
	List<T> findAll(Example<T> example);
	
	/**
	 * @Title: findAll
	 * @Description: 分页查询
	 * @param pageable
	 * @return Page<T> 返回类型
	 * @throws
	 */
    Page<T> findAll(Pageable pageable);
    
    /**
     * @Title: findAll
     * @Description: 分页查询
     * @param example
     * @param pageable
     * @return Page<T> 返回类型
     * @throws
     */
    Page<T> findAll(Example<T> example, Pageable pageable);
    
    /**
     * @Title: pageList
     * @Description: 分页查询数据
     * @param bean
     * @return ResultBean<PageInfo> 返回类型
     * @throws
     */
    ResultBean<PageInfo> pageList(ReqBean<T> bean, Class<T> cls) throws InstantiationException, IllegalAccessException;
    
    /**
     * @Title: pageList 
     * @Description: 分页查询数据
     * @param 设定文件 
     * @return ResultBean<PageInfo>    返回类型 
     * @throws
     */
    ResultBean<PageInfo> pageList(ReqBean<T> bean, Class<T> cls, ExampleMatcher matcher) throws InstantiationException, IllegalAccessException;

	PageInfo page(ReqBean<T> bean, Class<T> cls);
}