package com.gov.wiki.common.entity.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.gov.wiki.common.entity.IdEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: PrivRoleResource
 * @Description: 角色资源关系实体
 * @author cys
 * @date 2019年11月2日
 */
@Data
@Entity
@Table(name = "priv_role_resource")
@ApiModel(value = "PrivRoleResource", description = "角色资源关系对象")
public class PrivRoleResource extends IdEntity{
	
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	*角色ID
	*/
	@ApiModelProperty(name = "roleId", value = "角色ID")
	@Column(name = "role_id", nullable = true)
	private String roleId;
	
	/**
	*资源ID
	*/
	@ApiModelProperty(name = "resourceId", value = "资源ID")
	@Column(name = "resource_id", nullable = true)
	private String resourceId;
}