/**
 * @Title: QuestionOpinionAuditReq.java
 * @Package com.gov.wiki.business.req
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2020年8月25日
 * @version V1.0
 */
package com.gov.wiki.business.req;

import java.util.List;

import com.gov.wiki.common.check.Check;
import com.gov.wiki.common.entity.buss.BizQuestionOpinionAudit;
import com.gov.wiki.common.utils.JSONUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: QuestionOpinionAuditReq
 * @Description: 问题选项审批请求对象
 * @author cys
 * @date 2020年8月25日
 */
@Data
@ApiModel(value = "QuestionOpinionAuditReq", description = "问题选项审批请求对象")
public class QuestionOpinionAuditReq {
	/**
	 * 唯一标识
	 */
	@ApiModelProperty(value = "唯一标识")
	private String id;
	
	/**
	*选项名称
	*/
	@Check(title = "选项名称", nullable = true)
	@ApiModelProperty(value = "选项名称")
	private String name;
	
	/**
	*描述
	*/
	@ApiModelProperty(value = "描述")
	private String description;
	
	/**
	*办理部门
	*/
	@ApiModelProperty(value = "办理部门")
	private String handleDepart;
	
	/**
	*承诺时限
	*/
	@ApiModelProperty(value = "承诺时限")
	private String commitmentTime;
	
	/**
	*所属地
	*/
	@ApiModelProperty(value = "所属地")
	private String territory;
	
	/**
	*关联问题库选项ID
	*/
	@ApiModelProperty(value = "关联问题库选项ID")
	private String opinionId;
	
	/**
	*问题审核表ID
	*/
	@ApiModelProperty(value = "问题审核表ID")
	private String questionAuditId;
	
	/**
	*排序号
	*/
	@ApiModelProperty(value = "排序号")
	private Integer sortNo;
	
	/**
	*题库ID
	*/
	@ApiModelProperty(value = "题库ID")
	private String questionId;
	
	/**
	*是否强制提醒 0-否 1-是
	*/
	@ApiModelProperty(value = "是否强制提醒 0-否 1-是")
	private Boolean forceRemind;
	
	/**
	*强制提醒内容
	*/
	@ApiModelProperty(value = "强制提醒内容")
	private String remindContent;
	
	/**
	*选项材料
	*/
	@ApiModelProperty(value = "选项材料")
	private List<OpinionMaterialAuditReq> materialList;
	
	public BizQuestionOpinionAudit toEntity() {
		BizQuestionOpinionAudit op = new BizQuestionOpinionAudit();
		op.setId(JSONUtil.strToNull(this.id));
		op.setCommitmentTime(JSONUtil.strToNull(this.commitmentTime));
		op.setDelFlag(false);
		op.setDescription(JSONUtil.strToNull(this.description));
		op.setHandleDepart(JSONUtil.strToNull(this.handleDepart));
		op.setName(JSONUtil.strToNull(this.name));
		op.setOpinionId(JSONUtil.strToNull(this.opinionId));
		op.setTerritory(JSONUtil.strToNull(this.territory));
		op.setSortNo(this.sortNo);
		op.setQuestionId(JSONUtil.strToNull(this.questionId));
		op.setQuestionAuditId(JSONUtil.strToNull(this.questionAuditId));
		op.setRemindContent(JSONUtil.strToNull(this.remindContent));
		op.setForceRemind(this.forceRemind);
		return op;
	}
}