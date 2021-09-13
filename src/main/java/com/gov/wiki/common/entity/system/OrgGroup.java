package com.gov.wiki.common.entity.system;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import com.gov.wiki.common.check.Check;
import com.gov.wiki.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: OrgGroup
 * @Description: 组实体
 * @author cys
 * @date 2019年11月1日
 */
@Data
@Entity
@Table(name = "org_group")
@Where(clause = "del_flag != 1")
@ApiModel(value = "OrgGroup", description = "组对象")
@DynamicInsert
public class OrgGroup extends BaseEntity{
	
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;

	/**
	*组成员(成员ID，英文逗号隔开)
	*/
	@ApiModelProperty(name = "member", value = "组成员(成员ID，英文逗号隔开)")
	@Column(name = "member", nullable = true)
	private String member;
	
	/**
	*组领导
	*/
	@ApiModelProperty(name = "leader", value = "组领导")
	@Column(name = "leader", nullable = true)
	private String leader;
	
	@ApiModelProperty(name = "leaderName", value = "组领导名称")
	@Formula("(select m.real_name from org_member m where m.id=leader)")
	private String leaderName;
	
	/**
	*组名
	*/
	@ApiModelProperty(name = "name", value = "组名")
	@Check(nullable = false, title = "组名称")
	@Column(name = "name", nullable = true)
	private String name;
	
	/**
	*权限属性 1-公开组 2-私有组
	*/
	@ApiModelProperty(name = "authority", value = "权限属性 1-公开组 2-私有组")
	@Column(name = "authority", nullable = true)
	private Integer authority;
	
	/**
	*描述
	*/
	@ApiModelProperty(name = "description", value = "描述")
	@Column(name = "description", nullable = true)
	private String description;
	
	/**
	*公开范围
	*/
	@ApiModelProperty(name = "publicRange", value = "公开范围")
	@Column(name = "public_range", nullable = true)
	private String publicRange;
	
	/**
	 * 公开组织列表
	 */
	@ApiModelProperty(name = "publicRangeList", value = "公开组织列表")
	@Transient
	private List<OrgDepart> publicRangeList;
	
	/**
	*组主管
	*/
	@ApiModelProperty(name = "director", value = "组主管")
	@Column(name = "director", nullable = true)
	private String director;
	
	@ApiModelProperty(name = "directorName", value = "组主管名称")
	@Formula("(select m.real_name from org_member m where m.id=director)")
	private String directorName;
	
	/**
	*状态 0-停用 1-启用
	*/
	@ApiModelProperty(name = "status", value = "状态 0-停用 1-启用")
	@Column(name = "status", nullable = true)
	private Integer status;
	
	/**
	*排序号
	*/
	@ApiModelProperty(name = "sortNo", value = "排序号")
	@Column(name = "sort_no", nullable = true)
	private Integer sortNo;
	
	/**
	*组类型
	*/
	@NotFound(action = NotFoundAction.IGNORE)
	@ApiModelProperty(name = "groupType", value = "组类型")
	@Check(nullable = false, title = "组类型")
	@OneToOne(cascade = CascadeType.REFRESH, optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "group_type", insertable = true, updatable = true)
	private SysDictItem groupType;
	
	/**
	*所属范围
	*/
	@NotFound(action = NotFoundAction.IGNORE)
	@ApiModelProperty(name = "belong", value = "所属范围")
	@ManyToOne(cascade = CascadeType.REFRESH, optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "belong", insertable = true, updatable = true)
	private OrgDepart belong;
	
	/**
	 * 组成员
	 */
	@ApiModelProperty(name = "memberList", value = " 组成员")
	@Transient
	private List<OrgMember> memberList;
}