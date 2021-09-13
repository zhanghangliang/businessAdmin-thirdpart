package com.gov.wiki.common.entity.buss;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.gov.wiki.common.entity.BaseEntity;
import com.gov.wiki.common.entity.system.SysFile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Entity
@Data
@Table(name = "biz_matter_audit_main")
@ApiModel(value = "BizMatterAuditMain", description = "事项审核主表")
public class BizMatterAuditMain extends BaseEntity {
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(name = "commitmentTime", value = "承诺时限")
	@Column(name = "commitment_time")
	private String commitmentTime;
	
	@ApiModelProperty(name = "matterDepositoryMainId", value = "事项库编号")
	@Column(name = "matter_depository_main_id")
	private String matterDepositoryMainId;
	
	@ApiModelProperty(name = "upmatterId", value = "上级事项")
	@Column(name = "upmatter_id")
	private String upmatterId;
	
	@ApiModelProperty(name = "matterName", value = "事项名称")
	@Column(name = "matter_name")
	private String matterName;
	
	@ApiModelProperty(name = "department", value = "办理部门")
	@Column(name = "department")
	private String department;
	
	@ApiModelProperty(name = "keyDescription", value = "关键描述")
	@Column(name = "key_description")
	private String keyDescription;
	
	@ApiModelProperty(name = "matterDescription", value = "事项描述")
	@Column(name = "matter_description")
	private String matterDescription;
	
	@ApiModelProperty(name = "sortnum", value = "排序号")
	@Column(name = "sortnum")
	private String sortnum;
	
	@ApiModelProperty(name = "skipped", value = "是否跳过")
	@Column(name = "skipped")
	private Boolean skipped;
	
	@ApiModelProperty(name = "attribute", value = "事项属性(1为基础 0为事项 3为情形)")
	@Column(name = "attribute")
	private Integer attribute;
	
	@ApiModelProperty(name = "auditState", value = "审核状态")
	@Column(name = "audit_state")
	private Integer auditState;
	
	@ApiModelProperty(name = "upmatterName", value = "上级事项名称")
	@Column(name = "upmatter_name")
	private String upmatterName;
	
	@ApiModelProperty(name = "operationStatus", value = "操作状态")
	@Column(name = "operation_status")
	private Integer operationStatus;
	
	@ApiModelProperty(name = "territory", value = "所属地")
	@Column(name = "territory")
	private String territory;
	
	@ApiModelProperty(name = "alias", value = "事项别名")
	@Column(name = "alias")
	private String alias;

	@ApiModelProperty(name = "strategy", value = "选择策略1-串行，2-并行")
	@Column(name = "strategy")
	private Integer strategy;

	@ApiModelProperty(name = "processFlow", value = "处理流程")
	@Column(name = "process_flow")
	private String processFlow;
	
	@ApiModelProperty(value = "处理流程文件")
	@Transient
    private List<SysFile> processFlowFiles;
	
	@ApiModelProperty(name = "subjectType", value = "主题类型")
	@Column(name = "subject_type")
	private String subjectType;

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "matter_audit_id", referencedColumnName = "id")
	@Fetch(FetchMode.SUBSELECT)
	private List<BizMatterAuditSlave> bizMatterAuditSlaves;

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "audited_id", referencedColumnName = "id")
	@Fetch(FetchMode.SUBSELECT)
	private List<BizAudit> bizAudit;
}