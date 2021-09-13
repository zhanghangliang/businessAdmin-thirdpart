package com.gov.wiki.common.entity.system;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gov.wiki.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName: OrgMember
 * @Description: 人员实体
 * @author cys
 * @date 2019年11月2日
 */
@Data
@Entity
@Table(name = "org_member")
@Where(clause = "del_flag != 1")
@ApiModel(value = "OrgMember", description = "人员对象")
@DynamicInsert
@Accessors(chain = true)
public class OrgMember extends BaseEntity{
	
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int STATUS_ENABLE = 1;
	
	public static final int STATUS_UNABLE = 0;
	
	/**
	*人员编号
	*/
	@ApiModelProperty(name = "code", value = "人员编号")
	@Column(name = "code", nullable = true)
	private String code;
	
	/**
	*人员类型(枚举ID)
	*/
	@NotFound(action = NotFoundAction.IGNORE)
	@ApiModelProperty(name = "memberType", value = "人员类型")
	@ManyToOne(cascade = CascadeType.REFRESH, optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "member_type", insertable = true, updatable = true)
	private SysDictItem memberType;
	
	/**
	*办公电话
	*/
	@ApiModelProperty(name = "officePhone", value = "办公电话")
	@Column(name = "office_phone", nullable = true)
	private String officePhone;
	
	/**
	*邮箱
	*/
	@ApiModelProperty(name = "email", value = "邮箱")
	@Column(name = "email", nullable = true)
	private String email;
	
	/**
	*照片
	*/
	@ApiModelProperty(name = "photo", value = "照片")
	@Column(name = "photo", nullable = true)
	private String photo;
	
	/**
	*出生日期
	*/
	@ApiModelProperty(name = "birthday", value = "出生日期")
	@Column(name = "birthday", nullable = true)
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;
	
	/**
	*手机号
	*/
	@ApiModelProperty(name = "mobile", value = "手机号")
	@Column(name = "mobile", nullable = true)
	private String mobile;
	
	/**
	*人员账号状态 0-停用 1-启用
	*/
	@ApiModelProperty(name = "status", value = "人员账号状态 0-停用 1-启用")
	@Column(name = "status", nullable = true)
	private Integer status;
	
	/**
	*入职时间
	*/
	@ApiModelProperty(name = "entryTime", value = "入职时间")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "entry_time", nullable = true)
	private Date entryTime;
	
	/**
	*登录名
	*/
	@ApiModelProperty(name = "username", value = "登录名")
	@Column(name = "username", nullable = true)
	private String username;
	
	/**
	*工作地
	*/
	@ApiModelProperty(name = "workplace", value = "工作地")
	@Column(name = "workplace", nullable = true)
	private String workplace;
	
	/**
	*性别 1-男 2-女
	*/
	@ApiModelProperty(name = "sex", value = "性别 1-男 2-女")
	@Column(name = "sex", nullable = true)
	private Integer sex;
	
	/**
	*账号类型 0-超级管理员,1-单位管理员,2-普通人员
	*/
	@ApiModelProperty(name = "accountType", value = "账号类型 1-管理员,2-普通人员")
	@Column(name = "account_type", nullable = true)
	private Integer accountType;
	
	/**
	*姓名
	*/
	@ApiModelProperty(name = "realName", value = "姓名")
	@Column(name = "real_name", nullable = true)
	private String realName;
	
	/**
	*描述
	*/
	@ApiModelProperty(name = "description", value = "描述")
	@Column(name = "description", nullable = true)
	private String description;
	
	/**
	*部门
	*/
	@NotFound(action = NotFoundAction.IGNORE)
	@ApiModelProperty(name = "depart", value = "部门")
	@ManyToOne(cascade = CascadeType.REFRESH, optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "depart_id", insertable = true, updatable = true)
	private OrgDepart depart;
	
	/**
	*人员状态(枚举ID)
	*/
	@ApiModelProperty(name = "memberState", value = "人员状态")
	private Integer memberState;
	
	/**
	*排序号
	*/
	@ApiModelProperty(name = "sortNo", value = "排序号")
	@Column(name = "sort_no", nullable = true)
	private Integer sortNo;
	
	/**
	*密码
	*/
	@ApiModelProperty(name = "password", value = "密码")
	@JsonIgnore
	@Column(name = "password", nullable = true)
	private String password;
	
	/**
	*副岗(岗位ID，英文逗号隔开)
	*/
	@NotFound(action = NotFoundAction.IGNORE)
	@ApiModelProperty(name = "deputyPost", value = "副岗")
	@ManyToOne(cascade = CascadeType.REFRESH, optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "deputy_post", insertable = true, updatable = true)
	private OrgPost deputyPost;
	
	/**
	*职务级别
	*/
	@NotFound(action = NotFoundAction.IGNORE)
	@ApiModelProperty(name = "job", value = "职务级别")
	@ManyToOne(cascade = CascadeType.REFRESH, optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "job_id", insertable = true, updatable = true)
	private OrgJob job;
	
	/**
	*主要岗位
	*/
	@NotFound(action = NotFoundAction.IGNORE)
	@ApiModelProperty(name = "mainPost", value = "主要岗位")
	@ManyToOne(cascade = CascadeType.REFRESH, optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "main_post", insertable = true, updatable = true)
	private OrgPost mainPost;
	
	/**
	*用户角色信息
	*/
	@ApiModelProperty(name = "roleList", value = "用户角色信息")
	@Transient
	private List<OrgRole> roleList;

	@ApiModelProperty(name = "companyId", value = "公司编号")
	private String companyId;

	private String openId;

	private String headimgurl;
	
	public static final int SEX_MALE = 1;
	public static final int SEX_FEMALE = 2;
}