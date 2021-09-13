/**
 * @Title: RoleQuery.java 
 * @Package com.insolu.spm.organization.req.query 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author cys 
 * @date 2019年11月11日 下午9:22:49 
 * @version V1.0 
 */
package com.gov.wiki.organization.req.query;

import com.gov.wiki.organization.req.BaseQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: RoleQuery 
 * @Description: 角色查询参数
 * @author cys
 * @date 2019年11月11日 下午9:22:49
 */
@Data
@ApiModel(value = "RoleQuery", description = "角色查询参数")
public class RoleQuery extends BaseQuery{
	
	@ApiModelProperty(name = "defaultVal", value = "默认角色")
	private Integer defaultVal;
}
