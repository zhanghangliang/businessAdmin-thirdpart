package com.gov.wiki.common.entity.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;

import com.gov.wiki.common.check.Check;
import com.gov.wiki.common.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: OrgRole
 * @Description: 角色实体
 * @author cys
 * @date 2019年11月2日
 */
@Data
@Entity
@Table(name = "org_role")
@Where(clause = "del_flag != 1")
@ApiModel(value = "OrgRole", description = "角色对象")
@DynamicInsert
public class OrgRole extends BaseEntity{
	
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;

	/**
	*是否默认角色 0-否 1-是
	*/
	@ApiModelProperty(name = "default_val", value = "是否默认角色 0-否 1-是")
	@Column(name = "default_val", nullable = true)
	private Integer defaultVal;
	
	/**
	*描述
	*/
	@ApiModelProperty(name = "description", value = "描述")
	@Column(name = "description", nullable = true)
	private String description;
	
	/**
	*角色名称
	*/
	@ApiModelProperty(name = "name", value = "角色名称")
	@Check(nullable = false, title = "角色名称")
	@Column(name = "name", nullable = true)
	private String name;
	
	/**
	*状态 0-停用 1-启用
	*/
	@ApiModelProperty(name = "status", value = "状态 0-停用 1-启用")
	@Column(name = "status", nullable = true)
	private Integer status;
	
	/**
	*角色编码
	*/
	@ApiModelProperty(name = "code", value = "角色编码")
	@Column(name = "code", nullable = true)
	private String code;
	
	/**
	*排序号
	*/
	@ApiModelProperty(name = "sortNo", value = "排序号")
	@Column(name = "sort_no", nullable = true)
	private Integer sortNo;
} 