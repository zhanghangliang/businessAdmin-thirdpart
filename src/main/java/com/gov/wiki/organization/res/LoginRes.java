package com.gov.wiki.organization.res;

import java.util.List;
import com.gov.wiki.common.entity.system.OrgMember;
import com.gov.wiki.common.entity.system.OrgRole;
import com.gov.wiki.common.entity.system.PrivResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "LoginRes", description = "登录返回结果")
public class LoginRes {

	@ApiModelProperty(value = "人员信息")
	private OrgMember member;
	
	@ApiModelProperty(value = "登录人员角色信息")
	private List<OrgRole> roles;
	
	@ApiModelProperty(value = "登录人员树形菜单")
	private List<PrivResource> treeResources;
}
