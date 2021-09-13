package com.gov.wiki.common.entity.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.gov.wiki.common.entity.IdEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: PrivMemberRole
 * @Description: 人员角色关系实体
 * @author cys
 * @date 2019年11月2日
 */
@Data
@Entity
@Table(name = "priv_member_role")
@ApiModel(value = "PrivMemberRole", description = "人员角色关系对象")
public class PrivMemberRole extends IdEntity{
	
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	*角色ID
	*/
	@ApiModelProperty(name = "roleId", value = "角色ID")
	@Column(name = "role_id", nullable = true)
	private String roleId;
	
	/**
	*人员ID
	*/
	@ApiModelProperty(name = "memberId", value = "人员ID")
	@Column(name = "member_id", nullable = true)
	private String memberId;

	public PrivMemberRole() {
	}

	public PrivMemberRole(String memberId,String roleId) {
		this.memberId=memberId;
		this.roleId=roleId;
	}
}