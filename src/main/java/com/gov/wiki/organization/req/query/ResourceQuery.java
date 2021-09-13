/**
 * @Title: ResourceQuery.java 
 * @Package com.gov.wiki.organization.req.query
 * @Description: 资源查询请求参数
 * @author cys 
 * @date 2019年11月5日 下午10:32:01 
 * @version V1.0 
 */
package com.gov.wiki.organization.req.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ResourceQuery", description = "资源参数对象")
public class ResourceQuery {

	/**
	 * 关键字
	 */
	@ApiModelProperty(value = "关键字")
	private String keywords;
	
	/**
	 * 父级ID
	 */
	@ApiModelProperty(value = "父级ID")
	private String parentId;
}