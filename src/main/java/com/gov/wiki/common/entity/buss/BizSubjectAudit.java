package com.gov.wiki.common.entity.buss;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;
import com.gov.wiki.common.entity.BaseEntity;
import com.gov.wiki.common.entity.system.SysFile;
import com.gov.wiki.common.utils.BeanUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
*主题审核记录
*/
@Setter
@Getter
@ToString
@Entity
@DynamicUpdate
@DynamicInsert
@Where(clause = "del_flag != 1")
@Table(name = "biz_subject_audit")
@SQLDelete(sql = "update biz_subject_audit set del_flag=1 where id=?")
@SQLDeleteAll(sql = "update biz_subject_audit set del_flag=1 where id=?")
@ApiModel(value = "BizSubjectAudit", description = "主题审核记录")
public class BizSubjectAudit extends BaseEntity{
	
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	*名称
	*/
	@Column(name = "name", nullable = true)
	@ApiModelProperty(value = "名称")
	private String name;
	
	/**
	*关键描述
	*/
	@Column(name = "key_description", nullable = true)
	@ApiModelProperty(value = "关键描述")
	private String keyDescription;
	
	/**
	*描述
	*/
	@Column(name = "description", nullable = true)
	@ApiModelProperty(value = "描述")
	private String description;
	
	/**
	*主题类型
	*/
	@Column(name = "subject_type", nullable = true)
	@ApiModelProperty(value = "主题类型")
	private String subjectType;
	
	@ApiModelProperty(value = "关联id")
	@Column(name = "release_id", nullable = true)
	private String releaseId;
	
	@ApiModelProperty(value = "历史关联id")
	@Column(name = "his_release_id", nullable = true)
	private String hisReleaseId;
	
	/**
	*所属地
	*/
	@Column(name = "territory", nullable = true)
	@ApiModelProperty(value = "所属地")
	private String territory;
	
	/**
	*办理流程
	*/
	@Column(name = "process_flow", nullable = true)
	@ApiModelProperty(value = "办理流程")
	private String processFlow;
	
	@Transient
	@ApiModelProperty(value = "关联文件")
    private List<SysFile> sysFiles;
	
	/**
	*状态
	*/
	@Column(name = "status", nullable = true)
	@ApiModelProperty(value = "状态")
	private Integer status;
	
	/**
	*操作类型
	*/
	@Column(name = "oper_type", nullable = true)
	@ApiModelProperty(value = "操作类型")
	private Integer operType;
	
	@ApiModelProperty(value = "备注")	
	private String remark;
	
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
	
	@ApiModelProperty(value = "主题大类1-一事一次办 2-单一事项")
	@Column(name = "major_category", nullable = true)
	private Integer majorCategory;
	
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@Cascade(value = {CascadeType.ALL})
	@JoinColumn(name = "subject_audit_id")
	@ApiModelProperty(value = "主题材料列表")
	private Set<BizSubjectMaterialAudit> materials;
	
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@Cascade(value = {CascadeType.ALL})
	@JoinColumn(name = "subject_audit_id")
	@ApiModelProperty(value = "主题问题关系清单列表")
	@OrderBy(clause = "sort asc")
	private Set<BizSubjectQaRelationshipAudit> relationShips;
	
	/**
	 * 操作原因
	 */
	@Column(name = "oper_reason", nullable = true)
	@ApiModelProperty(value = "操作原因")	
	private String operReason;

	public BizSubject toSubject() {
		BizSubject subject = BeanUtils.copyProperties(this, BizSubject.class);
		//subject.setId(releaseId);
		subject.setRelationShips(toRelationShips());
		if(subject.getMaterials() != null) {
			for (BizSubjectMaterial b : subject.getMaterials()) {
				b.setId(null);
			}
		}
		return subject;
	}

	private Set<BizSubjectQaRelationship> toRelationShips() {
		Set<BizSubjectQaRelationship> set = new HashSet<BizSubjectQaRelationship>();
		if(relationShips == null || relationShips.isEmpty()) return set;
		for (BizSubjectQaRelationshipAudit o : relationShips) {
			set.add(o.toRelationShip());
		}
		return set;
	}

	public void merge(BizSubjectAudit t) {
		BeanUtils.copyProperties(t, this, false, "materials","relationShips");
		if(this.materials != null) {
			materials.clear();
			this.materials.addAll(t.getMaterials());			
		} else {
			materials = t.getMaterials();
		}
		if(this.relationShips != null) {
			this.relationShips.clear();
		}
	}
}