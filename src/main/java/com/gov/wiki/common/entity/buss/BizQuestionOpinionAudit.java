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

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;

import com.gov.wiki.common.entity.BaseEntity;
import com.gov.wiki.common.utils.BeanUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
*问题选项审核表
*/
@Setter
@Getter
@ToString(callSuper = true)
@Entity
@DynamicUpdate
@DynamicInsert
@Where(clause = "del_flag != 1")
@Table(name = "biz_question_opinion_audit")
@SQLDelete(sql = "update biz_question_opinion_audit set del_flag=1 where id=?")
@SQLDeleteAll(sql = "update biz_question_opinion_audit set del_flag=1 where id=?")
@ApiModel(value = "BizQuestionOpinionAudit", description = "问题选项审核表")
public class BizQuestionOpinionAudit extends BaseEntity{
	
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	*选项名称
	*/
	@Column(name = "name", nullable = true)
	@ApiModelProperty(value = "选项名称")
	private String name;
	
	/**
	*描述
	*/
	@Column(name = "description", nullable = true)
	@ApiModelProperty(value = "描述")
	private String description;
	
	
	/**
	*办理部门
	*/
	@Column(name = "handle_depart", nullable = true)
	@ApiModelProperty(value = "办理部门")
	private String handleDepart;
	
	/**
	*承诺时限
	*/
	@Column(name = "commitment_time", nullable = true)
	@ApiModelProperty(value = "承诺时限")
	private String commitmentTime;
	
	/**
	*所属地
	*/
	@Column(name = "territory", nullable = true)
	@ApiModelProperty(value = "所属地")
	private String territory;
	
	/**
	*关联问题库选项ID
	*/
	@Column(name = "opinion_id", nullable = true)
	@ApiModelProperty(value = "关联问题库选项ID")
	private String opinionId;
	
	/**
	*问题审核表ID
	*/
	@Column(name = "question_audit_id", nullable = true)
	@ApiModelProperty(value = "问题审核表ID")
	private String questionAuditId;
	
	/**
	*排序号
	*/
	@Column(name = "sort_no", nullable = true)
	@ApiModelProperty(value = "排序号")
	private Integer sortNo;
	
	/**
	*题库ID
	*/
	@Column(name = "question_id", nullable = true)
	@ApiModelProperty(value = "题库ID")
	private String questionId;
	
	/**
	*是否强制提醒 0-否 1-是
	*/
	@ApiModelProperty(value = "是否强制提醒 0-否 1-是")
	@Column(name = "force_remind", nullable = true)
	private Boolean forceRemind;
	
	/**
	*强制提醒内容
	*/
	@ApiModelProperty(value = "强制提醒内容")
	@Column(name = "remind_content", nullable = true)
	private String remindContent;
	
	/**
	 * 问题选项材料列表
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "opinion_audit_id")
	@ApiModelProperty(value = "问题选项材料列表")
	private List<BizOpinionMaterialAudit> materialList;

	public BizQuestionOpinion toOpinion() {
		BizQuestionOpinion opinion = BeanUtils.copyProperties(this, BizQuestionOpinion.class);
		//if(opinionId != null) opinion.setId(opinionId);
		return opinion;
	}
}