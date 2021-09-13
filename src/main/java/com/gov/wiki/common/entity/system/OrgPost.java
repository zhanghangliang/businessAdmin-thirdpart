package com.gov.wiki.common.entity.system;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.gov.wiki.common.check.Check;
import com.gov.wiki.common.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName: OrgPost
 * @Description: 岗位实体
 * @author cys
 * @date 2019年11月2日
 */
@Data
@Entity
@Table(name = "org_post")
@Where(clause = "del_flag != 1")
@ApiModel(value = "OrgPost", description = "岗位对象")
@DynamicInsert
@Accessors(chain = true)
public class OrgPost extends BaseEntity{
	
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;

	/**
	*岗位类别
	*/
	@NotFound(action = NotFoundAction.IGNORE)
	@ApiModelProperty(name = "category", value = "岗位类别")
	@ManyToOne(cascade = CascadeType.REFRESH, optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "category")
	private SysDictItem category;
	
	/**
	*描述
	*/
	@ApiModelProperty(name = "description", value = "描述")
	@Column(name = "description", nullable = true)
	private String description;
	
	/**
	*岗位名称
	*/
	@ApiModelProperty(name = "name", value = "岗位名称")
	@Check(nullable = false, title = "岗位名称")
	@Column(name = "name", nullable = true)
	private String name;
	
	/**
	*状态 0-停用 1-启用
	*/
	@ApiModelProperty(name = "status", value = "状态 0-停用 1-启用")
	@Column(name = "status", nullable = true)
	private Integer status;
	
	/**
	*岗位编码
	*/
	@ApiModelProperty(name = "code", value = "岗位编码")
	@Column(name = "code", nullable = true)
	private String code;
	
	/**
	*排序号
	*/
	@ApiModelProperty(name = "sortNo", value = "排序号")
	@Column(name = "sort_no", nullable = true)
	private Integer sortNo;
}