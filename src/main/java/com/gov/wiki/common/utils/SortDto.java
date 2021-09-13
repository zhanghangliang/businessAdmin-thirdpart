/**
 * @Title: SortDto.java 
 * @Package com.edurm.web.util 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author cys  
 * @date 2017年4月20日 下午4:36:51 
 * @version V1.0 
 */
package com.gov.wiki.common.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @ClassName: SortDto
 * @Description: 排序DTO对象
 * @author cys
 * @date 2017年4月20日 下午4:36:51
 */
@ApiModel(value = "SortDto", description = "排序DTO对象")
public class SortDto {
	// 排序方式
	@ApiModelProperty(name = "orderType", value = "排序方式")
	private String orderType;

	// 排序字段
	@ApiModelProperty(name = "orderField", value = "排序字段")
	private String orderField;

	public String getOrderField() {
		return orderField;
	}

	public void setOrderField(String orderField) {
		this.orderField = orderField;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public SortDto(String orderType, String orderField) {
		this.orderType = orderType;
		this.orderField = orderField;
	}

	// 默认为DESC排序
	public SortDto(String orderField) {
		this.orderField = orderField;
		this.orderType = "desc";
	}
	
	public SortDto() {
		
	}
}