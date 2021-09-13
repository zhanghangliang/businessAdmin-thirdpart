package com.gov.wiki.common.entity.buss;

import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OrderBy;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;
import com.gov.wiki.common.entity.BaseEntity;
import com.gov.wiki.common.entity.system.SysFile;
import com.gov.wiki.common.utils.BeanUtils;
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

/**
*主题库
*/
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "biz_subject")
@Where(clause = "del_flag != 1")
@SQLDelete(sql = "update biz_subject set del_flag=1 where id=?")
@SQLDeleteAll(sql = "update biz_subject set del_flag=1 where id=?")
@ApiModel(value = "BizSubject", description = "主题库")
public class BizSubject extends BaseEntity{
	
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	*名称
	*/
	@Column(name = "name", nullable = true)
	@ApiModelProperty(value = "主题名称")
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
	
	@Column(name = "remark", nullable = true ,length=255)
	@ApiModelProperty(value = "备注")	
	private String remark;
	
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@Cascade(value = {CascadeType.ALL})
	@JoinColumn(name = "subject_id")
	@ApiModelProperty(value = "主题材料列表")
	private Set<BizSubjectMaterial> materials;
	
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "subject_id")
	@Cascade(value = {CascadeType.ALL})
	@ApiModelProperty(value = "主题问题关系清单列表")
	@OrderBy(clause = "sort asc")
	private Set<BizSubjectQaRelationship> relationShips;
	
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
	 * 回收标志
	 */
	@Column(name = "recycling_mark", nullable = true)
	@ApiModelProperty(value = "回收标志")	
	private Boolean recyclingMark;
	
	@ApiModelProperty(value = "主题大类1-一事一次办 2-单一事项")
	@Column(name = "major_category", nullable = true)
	private Integer majorCategory;

	public BizSubjectAudit toAudit() {
		BizSubjectAudit audit = BeanUtils.copyProperties(this, BizSubjectAudit.class);
		audit.setRelationShips(toRelationShips());
		audit.setId(null);
		audit.setReleaseId(this.getId());
		if(audit.getMaterials() != null) {
			for (BizSubjectMaterialAudit b : audit.getMaterials()) {
				b.setId(null);
			}
		}
		return audit;
	}

	private Set<BizSubjectQaRelationshipAudit> toRelationShips() {
		Set<BizSubjectQaRelationshipAudit> set = new HashSet<BizSubjectQaRelationshipAudit>();
		if(relationShips == null || relationShips.isEmpty()) return set;
		for (BizSubjectQaRelationship o : relationShips) {
			set.add(o.toRelationShip());
		}
		return set;
	}
}