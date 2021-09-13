package com.gov.wiki.common.entity.buss;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;
import com.gov.wiki.common.entity.BaseEntity;
import com.gov.wiki.common.utils.BeanUtils;
import com.graphbuilder.org.apache.harmony.awt.gl.Crossing.QuadCurve;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
*问题审核表
*/
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Where(clause = "del_flag != 1")
@Table(name = "biz_question_audit")
@SQLDelete(sql = "update biz_question_audit set del_flag=1 where id=?")
@SQLDeleteAll(sql = "update biz_question_audit set del_flag=1 where id=?")
@ApiModel(value = "BizQuestionAudit", description = "问题审核表")
public class BizQuestionAudit extends BaseEntity{
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	*题目名称
	*/
	@Column(name = "name", nullable = true)
	@ApiModelProperty(value = "题目名称")
	private String name;
	
	/**
	*描述
	*/
	@Column(name = "description", nullable = true)
	@ApiModelProperty(value = "描述")
	private String description;
	
	/**
	*关联题库ID
	*/
	@Column(name = "release_id", nullable = true)
	@ApiModelProperty(value = "关联题库ID")
	private String releaseId;
	
	/**
	*关联历史题库ID
	*/
	@Column(name = "his_release_id", nullable = true)
	@ApiModelProperty(value = "关联历史题库ID")
	private String hisReleaseId;
	
	/**
	*操作类型
	*/
	@Column(name = "oper_type", nullable = true)
	@ApiModelProperty(value = "操作类型")
	private Integer operType;
	/**
	*选择策略 1-串行 2-并行
	*/
	@Column(name = "strategy", nullable = true)
	@ApiModelProperty(value = "选择策略 1-串行 2-并行")
	private Integer strategy;
	/**
	*状态
	*/
	@Column(name = "status", nullable = true)
	@ApiModelProperty(value = "状态")
	private Integer status;
	
	@Column(name = "remark", nullable = true)
	@ApiModelProperty(value = "备注")	
	private String remark;
	
	/**
	 * 问题选项列表
	 */
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "question_audit_id",insertable = false, updatable = false)
	@ApiModelProperty(value = "问题选项列表")
	@OrderBy("sort_no ASC")
	@NotFound(action = NotFoundAction.IGNORE)
	@Fetch(FetchMode.SELECT)
	private List<BizQuestionOpinionAudit> opinionList;
	
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
	 * 操作原因
	 */
	@Column(name = "oper_reason", nullable = true)
	@ApiModelProperty(value = "操作原因")	
	private String operReason;
	
	public BizQuestion toQuestion() {
		BizQuestion question = BeanUtils.copyProperties(this, BizQuestion.class);
		//if(this.releaseId != null) question.setId(releaseId);
		question.setOpinionList(toOpinionList());
		return question;
	}

	private List<BizQuestionOpinion> toOpinionList() {
		List<BizQuestionOpinion> list = new ArrayList<BizQuestionOpinion>();
		if(opinionList == null || opinionList.isEmpty()) return list;
		for (BizQuestionOpinionAudit o : opinionList) {
			list.add(o.toOpinion());
		}
		return list;
	}
}