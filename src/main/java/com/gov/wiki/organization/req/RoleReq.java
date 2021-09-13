package com.gov.wiki.organization.req;

import com.gov.wiki.common.entity.system.OrgRole;
import com.gov.wiki.common.enums.StatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: OrgRole
 * @Description: 角色请求对象
 * @author cys
 * @date 2019年11月2日
 */
@Data
@ApiModel(value = "RoleReq", description = "角色请求对象")
public class RoleReq {
	/**
	 * 主键
	 */
	@ApiModelProperty(name = "id", value = "唯一标识")
	private String id;

	/**
	*是否默认角色 0-否 1-是
	*/
	@ApiModelProperty(name = "defaultVal", value = "是否默认角色 0-否 1-是")
	private Integer defaultVal = 0;
	
	/**
	*描述
	*/
	@ApiModelProperty(name = "description", value = "描述")
	private String description;
	
	/**
	*角色名称
	*/
	@ApiModelProperty(name = "name", value = "角色名称")
	private String name;
	
	/**
	*状态 0-停用 1-启用
	*/
	@ApiModelProperty(name = "status", value = "状态 0-停用 1-启用")
	private Integer status = StatusEnum.ENABLE.getValue();
	
	/**
	*角色编码
	*/
	@ApiModelProperty(name = "code", value = "角色编码")
	private String code;
	
	/**
	*排序号
	*/
	@ApiModelProperty(name = "sortNo", value = "排序号")
	private Integer sortNo;
	
	public OrgRole toEntity() {
		OrgRole r = new OrgRole();
		r.setDefaultVal(this.defaultVal);
		r.setCode(this.code);
		r.setDelFlag(false);
		r.setDescription(this.description);
		r.setId(this.id);
		r.setName(this.name);
		r.setSortNo(this.sortNo);
		r.setStatus(this.status);
		return r;
	}
}