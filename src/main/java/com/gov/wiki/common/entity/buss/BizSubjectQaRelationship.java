package com.gov.wiki.common.entity.buss;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;

import com.gov.wiki.common.entity.BaseEntity;
import com.gov.wiki.common.utils.BeanUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
*主题问答关系
*/
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@DynamicUpdate
@DynamicInsert
@Where(clause = "del_flag != 1")
@Table(name = "biz_subject_qa_relationship")
@SQLDelete(sql = "update biz_subject_qa_relationship set del_flag=1 where id=?")
@SQLDeleteAll(sql = "update biz_subject_qa_relationship set del_flag=1 where id=?")
@ApiModel(value = "BizSubjectQaRelationship", description = "主题问答关系")
public class BizSubjectQaRelationship extends BaseEntity{
	
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	*所属主题
	*/
	@Column(name = "subject_id", nullable = true)
	@ApiModelProperty(value = "所属主题")
	private String subjectId;
	
	/**
	*选项ID
	*/
	@Column(name = "opinion_id", nullable = true)
	@ApiModelProperty(value = "选项ID")
	private String opinionId;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToOne(cascade = CascadeType.REFRESH, optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "opinion_id", insertable = false, updatable = false)
	private BizQuestionOpinion opinion;
	
	/**
	*下一问题ID
	*/
	@Column(name = "next_question_id", nullable = true)
	@ApiModelProperty(value = "下一问题ID")
	private String nextQuestionId;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToOne(cascade = CascadeType.REFRESH, optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "next_question_id", insertable = false, updatable = false)
	private BizQuestion question;
	
	/**
	*上一选项ID
	*/
	@Column(name = "pre_opinion_id", nullable = true)
	@ApiModelProperty(value = "上一选项ID")
	private String preOpinionId;
	
	/**
	*上一级关系id
	*/
	@Column(name = "pre_relationship_id", nullable = true ,length=32)
	private String preRelationshipId;
	
	@Column(name = "sort")
	private Integer sort;
	
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "pre_relationship_id")
	@Cascade(value = {org.hibernate.annotations.CascadeType.ALL})
	@ApiModelProperty(value = "下一级选项id")
	@OrderBy(clause = "sort asc")
	private Set<BizSubjectQaRelationship> preRelationships;

	public BizSubjectQaRelationshipAudit toRelationShip() {
		BizSubjectQaRelationshipAudit relationship = BeanUtils.copyProperties(this, BizSubjectQaRelationshipAudit.class);
		relationship.setRelationshipId(this.getId()); 
		relationship.setId(null);
		relationship.setPreRelationships(toRelationShips());
		return relationship;
	}

	private Set<BizSubjectQaRelationshipAudit> toRelationShips() {
		Set<BizSubjectQaRelationshipAudit> set = new HashSet<BizSubjectQaRelationshipAudit>();
		if(preRelationships == null || preRelationships.isEmpty()) return set;
		for (BizSubjectQaRelationship o : preRelationships) {
			set.add(o.toRelationShip());
		}
		return set;
	}
}