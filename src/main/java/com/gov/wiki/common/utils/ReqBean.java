package com.gov.wiki.common.utils;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: ReqBean 
 * @Description: 请求Bean
 * @author cys 
 * @date 2018年12月12日 上午11:21:36
 * @param <T>
 */
@ApiModel(value = "ReqBean", description = "请求Bean")
@Data
public class ReqBean<T> implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(name = "body", value = "body参数")
	private T body;
	
	@ApiModelProperty(name = "header", value = "header参数")
	private ReqHeader header;
	
	/**
	 * @Title: getPage 
	 * @Description: 获取排序参数
	 * @param 设定文件 
	 * @return Page    返回类型 
	 * @throws
	 */
	@JsonIgnore
	public PageInfo getPage() {
		PageInfo page = new PageInfo();
		if(this.header != null) {
			if(this.header.getPageNumber() != null && this.header.getPageNumber().intValue() > 0) {
				page.setCurrentPage(this.header.getPageNumber());
			}
			if(this.header.getPageSize() != null && this.header.getPageSize().intValue() > 0) {
				page.setPageSize(this.header.getPageSize());
			}
			page.setSortList(this.header.getSortList());
		}
		return page;
	}
	
	
}
