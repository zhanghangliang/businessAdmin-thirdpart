package com.gov.wiki.common.entity.buss;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.gov.wiki.common.entity.BaseEntity;
import com.gov.wiki.common.utils.BeanUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
*主题材料审核表
*/
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Where(clause = "del_flag != 1")
@Table(name = "biz_subject_material_audit")
@SQLDelete(sql = "update biz_subject_material_audit set del_flag=1 where id=?")
@SQLDeleteAll(sql = "update biz_subject_material_audit set del_flag=1 where id=?")
@ApiModel(value = "BizSubjectMaterialAudit", description = "主题材料审核表")
public class BizSubjectMaterialAudit extends BaseEntity{
	
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	*备注
	*/
	@Column(name = "remark", nullable = true)
	@ApiModelProperty(value = "备注")
	private String remark;
	
	/**
	*所属主题审核记录
	*/
	@Column(name = "subject_audit_id", nullable = true)
	@ApiModelProperty(value = "所属主题审核记录")
	private String subjectAuditId;
	
	/**
	*资料ID
	*/
	@Column(name = "material_id", nullable = true)
	@ApiModelProperty(value = "资料ID")
	private String materialId;
	
	/**
	*必要性0-非必要 1-必要
	*/
	@Column(name = "necessity", nullable = true)
	@ApiModelProperty(value = "必要性0-非必要 1-必要")
	private Boolean necessity;
	
	/**
	*材料数量
	*/
	@Column(name = "qty", nullable = true)
	@ApiModelProperty(value = "材料数量")
	private Integer qty;
	
	/**
	*验收
	*/
	@Column(name = "check_accept", nullable = true)
	@ApiModelProperty(value = "验收")
	private String checkAccept;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ApiModelProperty(value = "资料")
	@ManyToOne(cascade = CascadeType.REFRESH, optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "material_id", insertable = false, updatable = false)
	private BizMaterialDepository material;
}