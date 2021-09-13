package com.gov.wiki.common.entity.system;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Where;

import com.gov.wiki.common.check.Check;
import com.gov.wiki.common.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: PrivResource
 * @Description: 资源实体
 * @author cys
 * @date 2019年11月2日
 */
@Data
@Entity
@Table(name = "priv_resource")
@Where(clause = "del_flag != 1")
@ApiModel(value = "PrivResource", description = "资源对象")
@DynamicInsert
public class PrivResource extends BaseEntity{
	
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;

	/**
	*菜单类型(1:菜单:2:按钮权限)
	*/
	@ApiModelProperty(name = "menuType", value = "菜单类型(1:菜单:2:按钮权限)")
	@Check(nullable = false, title = "菜单类型")
	@Column(name = "menu_type", nullable = true)
	private Integer menuType;
	
//	/**
//	*所属模块ID
//	*/
//	@ApiModelProperty(name = "moduleId", value = "所属模块ID")
//	@Column(name = "module_id", nullable = true)
//	private String moduleId;
	
	/**
	*是否隐藏路由: 0否,1是
	*/
	@ApiModelProperty(name = "hidden", value = "是否隐藏路由: 0否,1是，默认否")
	@Column(name = "hidden", nullable = true)
	private Integer hidden = 0;
	
	/**
	*父id
	*/
	@ApiModelProperty(name = "parentId", value = "父id")
	@Column(name = "parent_id", nullable = true)
	private String parentId;

	@ApiModelProperty(name = "menuId", value = "菜单id")
	@Column(name = "menu_id", nullable = true)
	private String menuId;
	

	
	/**
	*状态 0-停用 1-启用
	*/
	@ApiModelProperty(name = "status", value = "状态 0-停用 1-启用")
	@Column(name = "status", nullable = true)
	private Integer status;
	
	/**
	*菜单权限编码
	*/
	@ApiModelProperty(name = "perms", value = "菜单权限编码")
	@Column(name = "perms", nullable = true)
	private String perms;
	
	/**
	*描述
	*/
	@ApiModelProperty(name = "description", value = "描述")
	@Column(name = "description", nullable = true)
	private String description;
	
	/**
	*排序号
	*/
	@ApiModelProperty(name = "sortNo", value = "排序号")
	@Column(name = "sort_no", nullable = true)
	private Integer sortNo;
	
	/**
	*是否路由菜单: 0:不是  1:是（默认值1）
	*/
	@ApiModelProperty(name = "isRoute", value = "是否路由菜单: 0:不是  1:是（默认值1）")
	@Column(name = "is_route", nullable = true)
	private Integer isRoute = 1;
	
	/**
	*菜单标题
	*/
	@ApiModelProperty(name = "name", value = "菜单标题")
	@Check(nullable = false, title = "菜单标题")
	@Column(name = "name", nullable = true)
	private String name;
	
	/**
	*一级菜单跳转地址
	*/
	@ApiModelProperty(name = "redirect", value = "一级菜单跳转地址")
	@Column(name = "redirect", nullable = true)
	private String redirect;
	
	/**
	*权限策略1显示2禁用
	*/
	@ApiModelProperty(name = "permsType", value = "权限策略1显示2禁用")
	@Column(name = "perms_type", nullable = true)
	private Integer permsType;
	
	/**
	*菜单路径
	*/
	@ApiModelProperty(name = "menuUrl", value = "菜单路径")
	@Column(name = "menu_url", nullable = true)
	private String menuUrl;
	
	/**
	*组件路径
	*/
	@ApiModelProperty(name = "componentUrl", value = "组件路径")
	@Column(name = "component_url", nullable = true)
	private String componentUrl;

	/**
	 *组件名字
	 */
	@ApiModelProperty(name = "componentName", value = "组件名字")
	@Column(name = "component_name", nullable = true)
	private String componentName;

	
	/**
	*是否叶子节点:    1:是   0:不是
	*/
	@ApiModelProperty(name = "isLeaf", value = "是否叶子节点:    1:是   0:不是")
	@Column(name = "is_leaf", nullable = true)
	private Integer isLeaf;
	
	/**
	*菜单图标
	*/
	@ApiModelProperty(name = "icon", value = "菜单图标")
	@Column(name = "icon", nullable = true)
	private String icon;
	
	/**
	 * 子资源
	 */
	@ApiModelProperty(name = "childList", value = "子资源")
	@OneToMany(cascade = CascadeType.REFRESH, targetEntity = PrivResource.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id", insertable=false, updatable=false)
	@OrderBy("sortNo asc")
	private List<PrivResource> childList = new ArrayList<PrivResource>();
}