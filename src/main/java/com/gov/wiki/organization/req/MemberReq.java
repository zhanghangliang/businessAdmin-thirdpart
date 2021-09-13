package com.gov.wiki.organization.req;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gov.wiki.common.entity.system.OrgDepart;
import com.gov.wiki.common.entity.system.OrgJob;
import com.gov.wiki.common.entity.system.OrgMember;
import com.gov.wiki.common.entity.system.OrgPost;
import com.gov.wiki.common.entity.system.OrgRole;
import com.gov.wiki.common.entity.system.SysDictItem;
import com.gov.wiki.common.enums.StatusEnum;
import com.gov.wiki.common.utils.StringUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: OrgMember
 * @Description: 人员实体
 * @author cys
 * @date 2019年11月2日
 */
@Data
@ApiModel(value = "MemberReq", description = "人员请求对象")
public class MemberReq {
	
	/**
	 * 主键
	 */
	@ApiModelProperty(name = "id", value = "唯一标识")
	private String id;
	
	/**
	*人员编号
	*/
	@ApiModelProperty(name = "code", value = "人员编号")
	private String code;
	
//	/**
//	*人员类型(枚举ID)
//	*/
//	@ApiModelProperty(name = "memberType", value = "人员类型")
//	private String memberType;
	
//	/**
//	*办公电话
//	*/
//	@ApiModelProperty(name = "officePhone", value = "办公电话")
//	private String officePhone;
	
//	/**
//	*邮箱
//	*/
//	@ApiModelProperty(name = "email", value = "邮箱")
//	private String email;
	
//	/**
//	*照片
//	*/
//	@ApiModelProperty(name = "photo", value = "照片")
//	private String photo;
	
//	/**
//	*出生日期
//	*/
//	@ApiModelProperty(name = "birthday", value = "出生日期")
//	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
//	@DateTimeFormat(pattern = "yyyy-MM-dd")
//	private Date birthday;
	
//	/**
//	*手机号
//	*/
//	@ApiModelProperty(name = "mobile", value = "手机号")
//	private String mobile;
	
	/**
	*人员账号状态 0-停用 1-启用
	*/
	@ApiModelProperty(name = "status", value = "人员账号状态 0-停用 1-启用")
	private Integer status = StatusEnum.ENABLE.getValue();
	
//	/**
//	*入职时间
//	*/
//	@ApiModelProperty(name = "entryTime", value = "入职时间")
//	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
//	@DateTimeFormat(pattern = "yyyy-MM-dd")
//	private Date entryTime;
	
	/**
	*登录名
	*/
	@ApiModelProperty(name = "username", value = "登录名")
	private String username;
	
//	/**
//	*工作地
//	*/
//	@ApiModelProperty(name = "workplace", value = "工作地")
//	private String workplace;
	
//	/**
//	*性别 1-男 2-女
//	*/
//	@ApiModelProperty(name = "sex", value = "性别 1-男 2-女")
//	private Integer sex;
	
//	/**
//	*账号类型 0-超级管理员,1-单位管理员,2-普通人员
//	*/
//	@ApiModelProperty(name = "accountType", value = "账号类型 0-超级管理员,1-单位管理员,2-普通人员")
//	private Integer accountType;
	
	/**
	*姓名
	*/
	@ApiModelProperty(name = "realName", value = "姓名")
	private String realName;
	
//	/**
//	*描述
//	*/
//	@ApiModelProperty(name = "description", value = "描述")
//	private String description;
//
	/**
	*部门
	*/
	@ApiModelProperty(name = "depart", value = "部门")
	private String departId;
	
	/**
	*人员状态(枚举ID)
	*/
	@ApiModelProperty(name = "memberState", value = "人员状态")
	private Integer memberState;
	
	/**
	*排序号
	*/
	@ApiModelProperty(name = "sortNo", value = "排序号")
	private Integer sortNo;
	
	/**
	*密码
	*/
	@ApiModelProperty(name = "password", value = "密码")
	private String password;

	@ApiModelProperty(name = "companyId", value = "公司编号")
	private String companyId;
	
//	/**
//	*副岗(岗位ID，英文逗号隔开)
//	*/
//	@ApiModelProperty(name = "deputyPost", value = "副岗")
//	private String deputyPost;
	
//	/**
//	*职务级别
//	*/
//	@ApiModelProperty(name = "jobId", value = "职务级别")
//	private String jobId;
	
//	/**
//	*主要岗位
//	*/
//	@ApiModelProperty(name = "mainPost", value = "主要岗位")
//	private String mainPost;
	
	/**
	*用户角色信息
	*/
	@ApiModelProperty(name = "roleIds", value = "用户角色信息")
	private List<String> roleList;
	
	public OrgMember toEntity() {
		OrgMember m = new OrgMember();
		m.setCompanyId(this.companyId);
//		m.setAccountType(accountType);
//		m.setBirthday(birthday);
		m.setCode(code);
		m.setDelFlag(false);
		if(StringUtils.isNotBlank(this.departId)) {
			OrgDepart depart = new OrgDepart();
			depart.setId(this.departId);
			m.setDepart(depart);
		}
//		if(StringUtils.isNotBlank(this.deputyPost)) {
//			OrgPost dPost = new OrgPost();
//			dPost.setId(this.deputyPost);
//			m.setDeputyPost(dPost);
//		}
//		m.setDescription(this.description);
//		m.setEmail(this.email);
//		m.setEntryTime(this.entryTime);
		m.setId(this.id);
//		if(StringUtils.isNotBlank(this.jobId)) {
//			OrgJob job = new OrgJob();
//			job.setId(this.jobId);
//			m.setJob(job);
//		}
//		if(StringUtils.isNotBlank(this.mainPost)) {
//			OrgPost mainPost = new OrgPost();
//			mainPost.setId(this.mainPost);
//			m.setMainPost(mainPost);
//		}
//		if(StringUtils.isNotBlank(this.memberState)) {
//			SysDictItem stateItem = new SysDictItem();
//			stateItem.setId(this.memberState);
//			m.setMemberState(stateItem);
//		}
//		if(StringUtils.isNotBlank(this.memberType)) {
//			SysDictItem memberTypeItem = new SysDictItem();
//			memberTypeItem.setId(this.memberType);
//			m.setMemberType(memberTypeItem);
//		}
//		m.setMobile(this.mobile);
//		m.setOfficePhone(this.officePhone);
		m.setPassword(this.password);
//		m.setPhoto(this.photo);
		m.setRealName(this.realName);
//		m.setSex(this.sex);
		m.setSortNo(this.sortNo);
		m.setStatus(this.status);
		m.setUsername(this.username);
		m.setMemberState(this.memberState);
//		m.setWorkplace(this.workplace);
		List<OrgRole> roleList = new ArrayList<OrgRole>();
		if(this.getRoleList() != null && !this.getRoleList().isEmpty()) {
			for(String roleId:this.getRoleList()) {
				if(StringUtils.isBlank(roleId)) {
					continue;
				}
				OrgRole r = new OrgRole();
				r.setId(roleId);
				roleList.add(r);
			}
		}
		m.setRoleList(roleList);
		return m;
	}
}