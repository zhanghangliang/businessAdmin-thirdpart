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

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.gov.wiki.common.entity.BaseEntity;
import com.gov.wiki.common.entity.system.SysFile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: BizMatterDepositoryMain
 * @Description: 事项库
 * @author cys
 * @date 2020年8月19日
 */
@Entity
@Data
@Table(name = "biz_matter_depository_main")
@ApiModel(value = "BizMatterDepositoryMain", description = "事项库")
public class BizMatterDepositoryMain extends BaseEntity {
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(name = "commitmentTime", value = "承诺时限")
	@Column(name = "commitment_time")
	private String commitmentTime;
	
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
	
	@ApiModelProperty(name = "attribute", value = "事项属性(1-主题，2-基础事项,3-事项，4-情形)")
	@Column(name = "attribute")
	private Integer attribute;
	
	@ApiModelProperty(name = "upmatterName", value = "上级事项名称")
	@Column(name = "upmatter_name")
	private String upmatterName;
	
	@ApiModelProperty(name = "online", value = "是否上线")
	@Column(name = "online")
	private Boolean online;
	
	@ApiModelProperty(name = "operationStatus", value = "是否处于删除状态")
	@Column(name = "operation_status")
	private Boolean operationStatus;
	
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
	
	@ApiModelProperty(name = "childNums", value = "子事项个数")
	@Formula("(select count(*) from biz_matter_depository_main d where d.upmatter_id=id)")
	private Integer childNums;

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "matter_depository_id", referencedColumnName = "id")
	private List<BizMatterDepositorySlave> bizMatterDepositorySlaves;
	
	@Transient
	@ApiModelProperty(value = "处理流程文件")
    private List<SysFile> processFlowFiles;
	
	@ApiModelProperty(name = "subjectType", value = "主题类型")
	@Column(name = "subject_type")
	private String subjectType;

	public BizMatterDepositoryMain() {
	}

	public BizMatterDepositoryMain(BizMatterAuditMain bizMatterAuditMain) {
		this.commitmentTime = bizMatterAuditMain.getCommitmentTime();
		this.upmatterId = bizMatterAuditMain.getUpmatterId();
		this.matterName = bizMatterAuditMain.getMatterName();
		this.department = bizMatterAuditMain.getDepartment();
		this.keyDescription = bizMatterAuditMain.getKeyDescription();
		this.matterDescription = bizMatterAuditMain.getMatterDescription();
		this.sortnum = bizMatterAuditMain.getSortnum();
		this.skipped = bizMatterAuditMain.getSkipped();
		this.attribute = bizMatterAuditMain.getAttribute();
		this.upmatterName = bizMatterAuditMain.getUpmatterName();
		this.online = true;
		this.operationStatus = false;
		this.territory = bizMatterAuditMain.getTerritory();
		this.alias = bizMatterAuditMain.getAlias();
		this.strategy = bizMatterAuditMain.getStrategy();
		this.processFlow = bizMatterAuditMain.getProcessFlow();
		this.subjectType = bizMatterAuditMain.getSubjectType();
	}
}