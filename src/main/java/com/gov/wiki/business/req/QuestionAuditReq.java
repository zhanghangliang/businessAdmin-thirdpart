/**
 * @Title: QuestionAuditReq.java
 * @Package com.gov.wiki.business.req
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2020年8月25日
 * @version V1.0
 */
package com.gov.wiki.business.req;

import java.util.List;

import javax.persistence.Column;

import com.gov.wiki.business.enums.StatusEnum;
import com.gov.wiki.common.check.Check;
import com.gov.wiki.common.entity.buss.BizQuestionAudit;
import com.gov.wiki.common.utils.JSONUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: QuestionAuditReq
 * @Description: 问题审批请求对象
 * @author cys
 * @date 2020年8月25日
 */
@Data
@ApiModel(value = "QuestionAuditReq", description = "问题审批请求对象")
public class QuestionAuditReq {
	/**
	 * 唯一标识
	 */
	@ApiModelProperty(value = "唯一标识")
	private String id;
	
	/**
	*题目名称
	*/
	@Check(title = "题目名称", nullable = true)
	@ApiModelProperty(value = "题目名称")
	private String name;
	
	/**
	*描述
	*/
	@ApiModelProperty(value = "描述")
	private String description;
	
	/**
	*选择策略 1-串行 2-并行
	*/
	@ApiModelProperty(value = "选择策略 1-串行 2-并行")
	private Integer strategy;
	
	/**
	*关联题库ID
	*/
	@ApiModelProperty(value = "关联题库ID")
	private String releaseId;
	
	/**
	*操作类型
	*/
	@ApiModelProperty(value = "操作类型")
	private Integer operType;
	
	@ApiModelProperty(value = "状态")
	private Integer status;
	
	@ApiModelProperty(value = "备注")	
	private String remark;
	
	@ApiModelProperty(value = "操作原因")	
	private String operReason;
	
	/**
	*问题选项信息
	*/
	@ApiModelProperty(value = "问题选项信息")
	private List<QuestionOpinionAuditReq> opinionList;
	
	public BizQuestionAudit toEntity() {
		BizQuestionAudit q = new BizQuestionAudit();
		q.setId(JSONUtil.strToNull(this.id));
		q.setDelFlag(false);
		q.setDescription(description);
		q.setName(JSONUtil.strToNull(this.name));
		q.setOperType(this.operType);
		q.setStrategy(strategy);
		q.setReleaseId(JSONUtil.strToNull(this.releaseId));
		q.setStatus(StatusEnum.save.getValue());
		q.setRemark(this.remark);
		q.setOperReason(this.operReason);
		return q;
	}
}