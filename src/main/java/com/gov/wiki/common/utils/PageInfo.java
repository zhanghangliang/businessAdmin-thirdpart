package com.gov.wiki.common.utils;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @ClassName: PageInfo 
 * @Description: 分页信息
 * @author cys 
 * @date 2018年4月13日 下午4:02:36
 */
@ApiModel(value = "PageInfo", description = "分页对象")
public class PageInfo {
	/**
	 * 当前页
	 */
	@ApiModelProperty(name = "currentPage", value = "当前页")
	private Integer currentPage;
	
	/**
	 * 每页显示条数
	 */
	@ApiModelProperty(name = "pageSize", value = "每页显示条数")
	private Integer pageSize;
	
	/**
	 * 总页数
	 */
	@ApiModelProperty(name = "totalPages", value = "总页数")
	private Integer totalPages;
	
	/**
	 * 总数
	 */
	@ApiModelProperty(name = "total", value = "总条数")
	private long total;
	
	/**
	 * 结果集
	 */
	@ApiModelProperty(name = "dataList", value = "结果集")
	private List<?> dataList;
	
	/**
	 * 排序字段对象
	 */
	@ApiModelProperty(name = "sortList", value = "排序字段对象")
	private SortDto[] sortList;
	
	public PageInfo(){
		
	}

	public Integer getCurrentPage() {
		if(currentPage == null){
			currentPage = PageableTools.initPage;
		}
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		if(pageSize == null){
			pageSize = PageableTools.pageSize;
		}
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public List<?> getDataList() {
		return dataList;
	}

	public void setDataList(List<?> dataList) {
		this.dataList = dataList;
	}

	public SortDto[] getSortList() {
		return sortList;
	}

	public void setSortList(SortDto[] sortList) {
		this.sortList = sortList;
	}

	/**
	 * @return the total
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(long total) {
		this.total = total;
	}
}