package com.gov.wiki.common.entity.buss;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.OrderBy;

import com.gov.wiki.common.entity.BaseEntity;
import com.gov.wiki.common.utils.BeanUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;

import static com.gov.wiki.common.utils.BeanUtils.copyProperties;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
*主题问答关系审核表
*/
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@DynamicUpdate
@DynamicInsert
@Where(clause = "del_flag != 1")
@Table(name = "biz_subject_qa_relationship_audit")
@SQLDelete(sql = "update biz_subject_qa_relationship_audit set del_flag=1 where id=?")
@SQLDeleteAll(sql = "update biz_subject_qa_relationship_audit set del_flag=1 where id=?")
@ApiModel(value = "BizSubjectQaRelationshipAudit", description = "主题问答关系审核表")
public class BizSubjectQaRelationshipAudit extends BaseEntity{
	
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	*所属主题审核
	*/
	@Column(name = "subject_audit_id", nullable = true)
	@ApiModelProperty(value = "所属主题审核")
	private String subjectAuditId;
	
	/**
	*选项ID
	*/
	@Column(name = "opinion_id", nullable = true)
	@ApiModelProperty(value = "选项ID")
	private String opinionId;
	
	/**
	*下一问题ID
	*/
	@Column(name = "next_question_id", nullable = true)
	@ApiModelProperty(value = "下一问题ID")
	private String nextQuestionId;
	
	@ApiModelProperty(value = "下一问题对象")
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(cascade = CascadeType.REFRESH, optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "next_question_id", insertable = false, updatable = false)
	private BizQuestion nextQuestion;
	
	/**
	*上一选项ID
	*/
	@Column(name = "pre_opinion_id", nullable = true)
	@ApiModelProperty(value = "上一选项ID")
	private String preOpinionId;
	
	@ApiModelProperty(value = "上一个选项")
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(cascade = CascadeType.REFRESH, optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "pre_opinion_id", insertable = false, updatable = false)
	private BizQuestionOpinion preOpinion;
	
	/**
	*关系ID
	*/
	@Column(name = "relationship_id", nullable = true)
	@ApiModelProperty(value = "关系ID")
	private String relationshipId;
	
	/**
	*上一级关系id
	*/
	@Column(name = "pre_relationship_id", nullable = true ,length=32)
	private String preRelationshipId;
	@Column(name = "sort")
	@ApiModelProperty(value = "序号")
	private Integer sort;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "pre_relationship_id")
	@ApiModelProperty(value = "下一级选项问题关系对象")
	@OrderBy(clause = "sort asc")
	private Set<BizSubjectQaRelationshipAudit> preRelationships;

	public BizSubjectQaRelationship toRelationShip() {
		BizSubjectQaRelationship relationship = copyProperties(this, BizSubjectQaRelationship.class);
		//relationship.setId(relationshipId);
		relationship.setPreRelationships(toRelationShips());
		return relationship;
	}
	
	private Set<BizSubjectQaRelationship> toRelationShips() {
		Set<BizSubjectQaRelationship> set = new HashSet<BizSubjectQaRelationship>();
		if(preRelationships == null || preRelationships.isEmpty()) return set;
		for (BizSubjectQaRelationshipAudit o : preRelationships) {
			set.add(o.toRelationShip());
		}
		return set;
	}
}