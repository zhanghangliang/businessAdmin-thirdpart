package com.gov.wiki.common.entity.system;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.gov.wiki.common.entity.MemberInfoReq;
import javafx.beans.binding.ObjectExpression;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;

import com.gov.wiki.common.check.Check;
import com.gov.wiki.common.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName: OrgDepart
 * @Description: 部门实体
 * @author cys
 * @date 2019年11月1日
 */
@Data
@Entity
@Table(name = "org_depart")
@Where(clause = "del_flag != 1")
@ApiModel(value = "OrgDepart", description = "部门对象")
@DynamicInsert
@Accessors(chain = true)
public class OrgDepart extends BaseEntity{
	
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 类型：单位
	 */
	public static final int TYPE_ACCOUNT = 1;
	
	/**
	 * 类型：公司
	 */
	public static final int TYPE_COMPANY = 2;
	
	/**
	 * 类型：部门
	 */
	public static final int TYPE_DEPART = 3;
	
	public static final String ACCOUNT_PATH = "0000";
	
	/**
	*部门分管领导
	*/
	@ApiModelProperty(name = "inChargeLeader", value = "部门分管领导")
	@Column(name = "in_charge_leader", nullable = true)
	private String inChargeLeader;
	
	/**
	 * 部门分管领导列表
	 */
	@ApiModelProperty(name = "inChargeLeaderList", value = "部门分管领导列表")
	@Transient
	private List<MemberInfoReq> inChargeLeaderList;
	
	/**
	*部门主管
	*/
	@ApiModelProperty(name = "director", value = "部门主管")
	@Column(name = "director", nullable = true)
	private String director;
	
	@ApiModelProperty(name = "directorName", value = "部门主管名称")
	@Formula("(select m.real_name from org_member m where m.id=director)")
	private String directorName;

	/**
	*部门名称
	*/
	@ApiModelProperty(name = "name", value = "部门名称")
	@Check(nullable = false, title = "部门名称")
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
	@ApiModelProperty(name = "status", value = "状态 0-停用 1-启用，默认启用")
	@Column(name = "status", nullable = true)
	private Integer status;
	
	/**
	*上级部门
	*/
	@ApiModelProperty(name = "parentId", value = "上级部门ID")
	@Column(name = "parent_id", nullable = true)
	private String parentId;
	
	/**
	 * 父级名称
	 */
	@ApiModelProperty(name = "parentName", value = "父级名称")
	@Formula("(select o.name from org_depart o where o.id=parent_id)")
	private String parentName;
	
	/**
	*部门管理员
	*/
	@ApiModelProperty(name = "administrator", value = "部门管理员")
	@Column(name = "administrator", nullable = true)
	private String administrator;
	
	@ApiModelProperty(name = "administratorName", value = "部门管理员名称")
	@Formula("(select m.real_name from org_member m where m.id=administrator)")
	private String administratorName;
	
	/**
	*部门编码
	*/
	@ApiModelProperty(name = "code", value = "部门编码")
	@Column(name = "code", nullable = true)
	private String code;
	
	/**
	*排序号
	*/
	@ApiModelProperty(name = "sortNo", value = "排序号")
	@Column(name = "sort_no", nullable = true)
	private Integer sortNo;
	
	/**
	*简称
	*/
	@ApiModelProperty(name = "shortName", value = "简称")
	@Column(name = "short_name", nullable = true)
	private String shortName;
	
	/**
	*类型 1-公司 2-部门,默认部门
	*/
	@ApiModelProperty(name = "type", value = "类型 1-单位 2-公司，3-部门，默认部门")
	@Column(name = "type", nullable = true)
	private Integer type = 3;
	
	@ApiModelProperty(name = "leaf", value = "叶子数")
	@Formula("(select count(*) from org_depart o where o.parent_id = id)")
	private int leaf;
	
	@ApiModelProperty(name = "path", value = "长编码")
	@Column(name = "path", nullable = true)
	private String path;
	
	@ApiModelProperty(name = "seq", value = "序号")
	@Column(name = "seq", nullable = true, updatable = false)
	private Integer seq;

	@ApiModelProperty(name = "companyId", value = "公司编号")
	@Column(name = "companyId", nullable = true, updatable = false)
	private String companyId;
	
	/**
	 * 子部门
	 */
	@ApiModelProperty(name = "departList", value = "子部门")
	@Transient
	private List<OrgDepart> departList = new ArrayList<OrgDepart>();
	
	public static final int STATUS_ENABLE = 1;
	public static final int STATUS_UNABLE = 0;
}