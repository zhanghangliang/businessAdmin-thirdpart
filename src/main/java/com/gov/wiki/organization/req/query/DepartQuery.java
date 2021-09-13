/**
 * @Title: DepartQuery.java 
 * @Package com.insolu.spm.organization.req 
 * @Description: 部门查询请求参数
 * @author cys 
 * @date 2019年11月5日 下午10:32:01 
 * @version V1.0 
 */
package com.gov.wiki.organization.req.query;

import com.gov.wiki.organization.req.BaseQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "DepartQuery", description = "部门查询参数对象")
public class DepartQuery extends BaseQuery{

	/**
	 * 父级ID
	 */
	@ApiModelProperty(value = "父级ID")
	private String parentId;
}