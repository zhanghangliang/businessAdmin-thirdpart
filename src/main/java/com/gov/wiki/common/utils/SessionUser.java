package com.gov.wiki.common.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gov.wiki.common.entity.system.OrgMember;
import com.gov.wiki.common.entity.system.OrgRole;
import com.gov.wiki.common.entity.system.PrivResource;
import com.gov.wiki.common.entity.system.SysFile;
import com.gov.wiki.common.entity.wechat.WxMember;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SessionUser implements Serializable {

	private static final long serialVersionUID = -5415025908925318023L;

	private String token;
	@ApiModelProperty(value = "用户信息")
	private OrgMember member;
	@ApiModelProperty(value = "资源树")
	private List<PrivResource> treeResources;
	@ApiModelProperty(value = "角色code")
	private List<String> roleCodes;
	@ApiModelProperty(value = "icon")
	HashMap<String,String> icon;
	@ApiModelProperty(value = "creater")
	List<String> creater;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date expireDate;

	/**
	 * 当前人员管辖部门范围，长编码
	 */
	@ApiModelProperty(value = "当前人员分管部门，长编码")
	private List<String> departRange;

	public static SessionUser create(String token, OrgMember member, List<PrivResource> treeResources,
			List<OrgRole> roles) {
		List<String> roleCodes = new ArrayList<String>();
		if (roles != null && !roles.isEmpty()) {
			for (OrgRole o : roles) {
				roleCodes.add(o.getCode());
			}
		}
		return new SessionUser().setToken(token).setMember(member).setTreeResources(treeResources)
				.setExpireDate(new Date(System.currentTimeMillis() + Constants.USER_EXPIRATION_DATE))
				.setRoleCodes(roleCodes);
	}
}