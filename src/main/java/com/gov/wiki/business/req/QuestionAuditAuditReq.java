package com.gov.wiki.business.req;

import com.gov.wiki.common.check.Check;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 问题库审核
 * @author wangmingwei
 *
 */
@Data
public class QuestionAuditAuditReq {

	@ApiModelProperty(value = "问题审核库id")
	@Check
	private String id;
	@ApiModelProperty(value = "审核状态，枚举：StatusEnum")
	@Check(title = "审核状态")
	private Integer status;
	
	@ApiModelProperty(value = "审批意见")
	private String opinion;
}
