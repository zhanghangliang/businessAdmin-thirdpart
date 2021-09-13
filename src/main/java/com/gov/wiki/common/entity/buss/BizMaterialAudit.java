package com.gov.wiki.common.entity.buss;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;
import com.gov.wiki.common.entity.BaseEntity;
import com.gov.wiki.common.entity.system.SysDictItem;
import com.gov.wiki.common.entity.system.SysFile;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
*资料审核表
*/
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Where(clause = "del_flag != 1")
@Table(name = "biz_material_audit")
@SQLDelete(sql = "update biz_material_audit set del_flag=1 where id=?")
@SQLDeleteAll(sql = "update biz_material_audit set del_flag=1 where id=?")
public class BizMaterialAudit extends BaseEntity{
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;

	/**
	*是否前置
	*/
	@Column(name = "front", nullable = true)
	@ApiModelProperty(value = "是否前置")
	private Boolean front;
	
	/**
	*前置描述
	*/
	@Column(name = "front_description", nullable = true)
	@ApiModelProperty(value = "前置描述")
	private String frontDescription;
	
	/**
	*资料类别,例如T1
	*/
	@Column(name = "material_category", nullable = true)
	@ApiModelProperty(value = "资料类别")
	private String materialCategory;
	
	/**
	*资料库ID
	*/
	@Column(name = "material_depository_id", nullable = true)
	@ApiModelProperty(value = "资料库id")
	private String materialDepositoryId;
	
	/**
	*历史资料库ID
	*/
	@Column(name = "his_depository_id", nullable = true)
	@ApiModelProperty(value = "历史资料库id")
	private String hisDepositoryId;
	
	/**
	*资料说明
	*/
	@Column(name = "material_description", nullable = true)
	@ApiModelProperty(value = "资料说明")
	private String materialDescription;
	
	/**
	*资料名称
	*/
	@Column(name = "material_name", nullable = true)
	@ApiModelProperty(value = "资料名称")
	private String materialName;
	
	/**
	*资料来源
	*/
	@Column(name = "material_source", nullable = true)
	@ApiModelProperty(value = "资料来源")
	private String materialSource;
	
	/**
	*资料类型,例如:证件
	*/
	@Column(name = "material_type", nullable = true)
	@ApiModelProperty(value = "资料类型")
	private Integer materialType;
	/**
	*资料编号
	*/
	@Column(name = "material_id", nullable = true)
	@ApiModelProperty(value = "资料编号")
	private String materialId;
	
	/**
	*审核状态(0为待审,1为拒绝,2为通过)
	*/
	@Column(name = "audit_state", nullable = false)
	@ApiModelProperty(value = "审核状态(0为待审,1为拒绝,2为通过)")
	private Integer auditState;
	
	/**
	*操作状态(0表示新建,1表示修改,2表示删除)
	*/
	@Column(name = "operation_status", nullable = true)
	@ApiModelProperty(value = "操作状态(0表示新建,1表示修改,2表示删除)")
	private Integer operationStatus;
	
	/**
	*受理标准
	*/
	@Column(name = "standard", nullable = true)
	@ApiModelProperty(value = "受理标准")
	private String standard;
	
	/**
	*去向
	*/
	@Column(name = "whereabouts", nullable = true)
	@ApiModelProperty(value = "去向")
	private String whereabouts;
	
	/**
	*标记是否处于删除状态
	*/
	@Column(name = "sign", nullable = true)
	@ApiModelProperty(value = "是否删除")	
	private Boolean sign;
	
	@Column(name = "remark", nullable = true)
	@ApiModelProperty(value = "备注")	
	private String remark;
	
	@NotFound(action = NotFoundAction.IGNORE)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "reference_id", referencedColumnName = "id")
	@ApiModelProperty(value = "关联文件")
    private List<SysFile> sysFiles;
	
	/**
	 * 修改次数
	 */
	@Column(name = "modify_times", nullable = true)
	@ApiModelProperty(value = "修改次数")	
	private Integer modifyTimes;
	
	/**
	 * 版本号
	 */
	@Column(name = "version", nullable = true)
	@ApiModelProperty(value = "版本号")	
	private String version;
	
	/**
	 * 行政职责分类
	 */
	@Column(name = "duty_type", nullable = true)
	@ApiModelProperty(value = "行政职责分类")	
	private String dutyType;
	
	/**
	*行政职责分类数据字典项
	*/
	@NotFound(action = NotFoundAction.IGNORE)
	@ApiModelProperty(name = "dutyTypeItem", value = "行政职责分类数据字典项")
	@ManyToOne(cascade = CascadeType.REFRESH, optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "duty_type", insertable = false, updatable = false)
	private SysDictItem dutyTypeItem;
	
	/**
	 * 操作原因
	 */
	@Column(name = "oper_reason", nullable = true)
	@ApiModelProperty(value = "操作原因")	
	private String operReason;
}