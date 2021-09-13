package com.gov.wiki.common.entity.wechat;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.gov.wiki.common.entity.buss.BizAuditOpinion;
import org.hibernate.annotations.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gov.wiki.common.buss.vo.SimpleSubjectRes;
import com.gov.wiki.common.entity.BaseEntity;
import com.gov.wiki.common.entity.system.OrgMember;
import com.gov.wiki.common.entity.system.SysFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Entity
@Table(name = "wx_operation_record")
@Where(clause = "del_flag != 1")
@SQLDelete(sql = "update wx_operation_record set del_flag=1 where id=?")
@SQLDeleteAll(sql = "update wx_operation_record set del_flag=1 where id=?")
@ApiModel(value = "WxOperationRecord", description = "用户操作记录表")
@DynamicInsert
public class WxOperationRecord extends BaseEntity {

    /**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "操作人员ID")
	@Column(name = "member_id", nullable = true)
    private String memberId;
	
	/**
	 * 操作人员
	 */
	@NotFound(action = NotFoundAction.IGNORE)
	@ApiModelProperty(value = "操作人员")
	@ManyToOne(cascade = CascadeType.REFRESH, optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id", insertable = false, updatable = false)
	private WxMember member;
    
	@ApiModelProperty(value = "选项结果")
    @Column(name = "options", nullable = true)
    private String options;

	@ApiModelProperty(value = "操作状态")
    @Column(name = "status", nullable = true)
    private Integer status;

	@ApiModelProperty(value = "资料附件")
    @Column(name = "material_annex", nullable = true)
    private String materialAnnex;
	
	@NotFound(action = NotFoundAction.IGNORE)
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "reference_id", referencedColumnName = "material_annex", insertable = false, updatable = false)
	@ApiModelProperty(value = "资料附件文件列表")
	@OrderBy("sort_no ASC")
    private List<SysFile> materialAnnexFiles;

	@ApiModelProperty(value = "主题ID")
    @Column(name = "subject_id", nullable = true)
    private String subjectId;

	
	@ApiModelProperty(value = "操作主题")
	@Transient
	private SimpleSubjectRes subject;
    
	@ApiModelProperty(value = "预审不通过原因")
    @Column(name = "reason", nullable = true)
    private String reason;

	@ApiModelProperty(value = "操作记录类型：1服务，2预审")
	@Column(name = "record_type",nullable = true)
	private Integer recordType;

	@ApiModelProperty(value = "审核状态：0 保存， 1 提交待审， 2 审核中 3 审核通过，4 审核不通过（可以修改）")
	@Column(name = "audit_status",nullable = true)
	private Integer auditStatus;

	@ApiModelProperty(value = "是否转预审，只有自助服务才可以转预审。默认未转预审")
	@Column(name = "change_audit",nullable = true)
	private Integer changeAudit;
	
	@Formula("(select o.username from org_member o where o.id="
			+ "(select a.create_by from wx_operation_record_audit a where a.record_id=id order by create_time desc limit 0,1))")
	private String auditName;
	
	
	@ApiModelProperty(value = "操作记录列表")
	@Transient
	private List<WxOperationRecordAudit> wxOperationRecordAuditList;

	/**
	*操作结果资料详情
	*/
	@ApiModelProperty(value = "操作结果资料详情")
	@Transient
	private List<WxOperationRecordResult> detailList;
	
	
}