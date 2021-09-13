package com.gov.wiki.common.utils;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.wenhao.jpa.Sorts;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: ReqHeader 
 * @Description: 请求头
 * @author cys 
 * @date 2019年2月28日 下午4:37:45
 */
@ApiModel(value = "ReqHeader", description = "请求头")
@Data
public class ReqHeader {

	/**
	 * 分页页码
	 */
	@ApiModelProperty(name = "pageNumber", value = "分页页码")
	private Integer pageNumber;
	
	/**
	 * 每页条数
	 */
	@ApiModelProperty(name = "pageSize", value = "每页条数")
	private Integer pageSize;
	
	/**
	 * 排序参数
	 */
	@ApiModelProperty(name = "sortList", value = "排序参数")
	private SortDto[] sortList;

	@JsonIgnore
	public Pageable getPageable() {
		Pageable page = PageableTools.basicPage(pageNumber, pageSize, sortList);
		return page;
	}

	@JsonIgnore
	public Sort getSort(){
		Sort sort;
		if("desc".equals(sortList[0].getOrderType())){
			sort=Sorts.builder().desc(sortList[0].getOrderField()).build();

		}
		else{
			sort=Sorts.builder().asc(sortList[0].getOrderField()).build();
		}
		return sort;
	}
}