package com.gov.wiki.common.entity.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Where;

import com.gov.wiki.common.check.Check;
import com.gov.wiki.common.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName: OrgJob
 * @Description: 职务实体
 * @author cys
 * @date 2019年11月1日
 */
@Data
@Entity
@Table(name = "org_job")
@Where(clause = "del_flag != 1")
@ApiModel(value = "OrgJob", description = "职务对象")
@DynamicInsert
@Accessors(chain = true)
public class OrgJob extends BaseEntity{
	
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	*排序号
	*/
	@ApiModelProperty(name = "sortNo", value = "排序号")
	@Column(name = "sort_no", nullable = true)
	private Integer sortNo;
	
	/**
	*职务级别编码
	*/
	@ApiModelProperty(name = "code", value = "职务级别编码")
	@Column(name = "code", nullable = true)
	private String code;
	
	/**
	*职务级别名称
	*/
	@ApiModelProperty(name = "name", value = "职务级别名称")
	@Check(nullable = false, title = "职务级别名称")
	@Column(name = "name", nullable = true)
	private String name;
	
	/**
	*描述
	*/
	@ApiModelProperty(name = "description", value = "描述")
	@Column(name = "description", nullable = true)
	private String description;
	
	/**
	*状态 0-停用 1-启用
	*/
	@ApiModelProperty(name = "status", value = "状态 0-停用 1-启用")
	@Column(name = "status", nullable = true)
	private Integer status;
}