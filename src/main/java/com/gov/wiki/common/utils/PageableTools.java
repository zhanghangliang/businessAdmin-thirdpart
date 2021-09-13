package com.gov.wiki.common.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @ClassName: PageableTools 
 * @Description: 分页工具
 * @author cys 
 * @date 2017年4月20日 下午4:30:09
 */
public class PageableTools {
	/**
	 * 初始页
	 */
	public static final Integer initPage = 1;
	
	/**
	 * 每页大小
	 */
	public static final Integer pageSize = 10;
	
	 /**
     * 获取基础分页对象
     * @param page 获取第几页
     * @param size 每页条数
     * @param dtos 排序对象数组
     * @return
     */
    public static Pageable basicPage(Integer page, Integer size, SortDto... dtos) {
        Sort sort = SortTools.basicSort(dtos);
        page = (page==null || page<=0)? initPage-1 : page-1;
        size = (size==null || size<=0)? pageSize : size;
        Pageable pageable = new PageRequest(page, size, sort);
        return pageable;
    }

    /**
     * 获取基础分页对象，每页条数默认15条
     *  - 默认以id降序排序
     * @param page 获取第几页
     * @return
     */
    public static Pageable basicPage(Integer page) {
        return basicPage(page, pageSize, new SortDto("desc", "id"));
    }

    /**
     * 获取基础分页对象，每页条数默认15条
     * @param page 获取第几页
     * @param dtos 排序对象数组
     * @return
     */
    public static Pageable basicPage(Integer page, SortDto... dtos) {
        return basicPage(page, pageSize, dtos);
    }

    /**
     * 获取基础分页对象，排序方式默认降序
     * @param page 获取第几页
     * @param size 每页条数
     * @param orderField 排序字段
     * @return
     */
    public static Pageable basicPage(Integer page, Integer size, String orderField) {
        return basicPage(page, size, new SortDto("desc", orderField));
    }

    /**
     * 获取基础分页对象
     *  - 每页条数默认15条
     *  - 排序方式默认降序
     * @param page 获取第几页
     * @param orderField 排序字段
     * @return
     */
    public static Pageable basicPage(Integer page, String orderField) {
        return basicPage(page, pageSize, new SortDto("desc", orderField));
    }
}