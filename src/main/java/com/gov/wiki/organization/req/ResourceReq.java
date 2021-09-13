package com.gov.wiki.organization.req;

import com.gov.wiki.common.entity.system.PrivResource;
import com.gov.wiki.common.entity.system.SysFile;
import com.gov.wiki.common.enums.StatusEnum;
import com.gov.wiki.common.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;

/**
 * @ClassName: PrivResource
 * @Description: 资源实体
 * @author cys
 * @date 2019年11月2日
 */
@Data
@ApiModel(value = "ResourceReq", description = "资源请求对象")
public class ResourceReq {
	/**
	 * 主键
	 */
	@ApiModelProperty(name = "id", value = "唯一标识")
	private String id;

	@ApiModelProperty(name = "menuId", value = "菜单id")
	@Column(name = "menu_id", nullable = true)
	private String menuId;

//	/**
//	*菜单类型(1:菜单:2:按钮权限)
//	*/
//	@ApiModelProperty(name = "menuType", value = "菜单类型(1:菜单:2:按钮权限)")
//	private Integer menuType;
	
//	/**
//	*所属模块ID
//	*/
//	@ApiModelProperty(name = "moduleId", value = "所属模块ID")
//	private String moduleId;
	
//	/**
//	*是否隐藏路由: 0否,1是
//	*/
//	@ApiModelProperty(name = "hidden", value = "是否隐藏路由: 0否,1是，默认否")
//	private Integer hidden = 0;
	
	/**
	*父id
	*/
	@ApiModelProperty(name = "parentId", value = "父id")
	private String parentId;
	
//	/**
//	*组件名字
//	*/
//	@ApiModelProperty(name = "componentName", value = "组件名字")
//	private String componentName;
	
	/**
	*状态 0-停用 1-启用
	*/
	@ApiModelProperty(name = "status", value = "状态 0-停用 1-启用")
	private Integer status = StatusEnum.ENABLE.getValue();
	
//	/**
//	*菜单权限编码
//	*/
//	@ApiModelProperty(name = "perms", value = "菜单权限编码")
//	private String perms;
	
	/**
	*描述
	*/
	@ApiModelProperty(name = "description", value = "描述")
	private String description;
	
	/**
	*排序号
	*/
	@ApiModelProperty(name = "sortNo", value = "排序号")
	private Integer sortNo;
	
//	/**
//	*是否路由菜单: 0:不是  1:是（默认值1）
//	*/
//	@ApiModelProperty(name = "isRoute", value = "是否路由菜单: 0:不是  1:是（默认值1）")
//	private Integer isRoute = 1;
	
	/**
	*菜单标题
	*/
	@ApiModelProperty(name = "name", value = "菜单标题")
	private String name;
	
//	/**
//	*一级菜单跳转地址
//	*/
//	@ApiModelProperty(name = "redirect", value = "一级菜单跳转地址")
//	private String redirect;
	
//	/**
//	*权限策略1显示2禁用
//	*/
//	@ApiModelProperty(name = "permsType", value = "权限策略1显示2禁用")
//	private Integer permsType;
	
	/**
	*路径
	*/
	@ApiModelProperty(name = "menu_url", value = "路径菜单")
	private String menuUrl;

	@ApiModelProperty(name = "component_url", value = "组件菜单")
	private String componentUrl;
	
//	/**
//	*组件
//	*/
//	@ApiModelProperty(name = "component", value = "组件")
//	private String component;
	
//	/**
//	*是否叶子节点:    1:是   0:不是
//	*/
//	@ApiModelProperty(name = "isLeaf", value = "是否叶子节点:    1:是   0:不是")
//	private Integer isLeaf;
	
	/**
	*菜单图标
	*/
	@ApiModelProperty(name = "icon", value = "菜单图标")
	private String icon;
	
	public PrivResource toEntity() {
		PrivResource re = new PrivResource();
		re.setParentId(this.parentId);
		re.setName(this.name);
		re.setMenuId(this.menuId);
		re.setMenuUrl(this.menuUrl);
		re.setComponentUrl(this.componentUrl);
		re.setDescription(this.description);
		re.setSortNo(this.sortNo);
		re.setStatus(this.status);
		re.setId(this.id);
		re.setIcon(this.icon);
		return re;
	}
}