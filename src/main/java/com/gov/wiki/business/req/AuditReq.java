package com.gov.wiki.business.req;

import com.gov.wiki.common.check.Check;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AuditReq {
    
    @ApiModelProperty(value = "审核库id")
	@Check
	private String id;
	@ApiModelProperty(value = "审核状态(0为待审,1为同意,2为拒绝)")
	@Check(title = "审核状态")
	private Integer status;
	
	@ApiModelProperty(value = "审批意见")
	private String opinion;
	
	@ApiModelProperty(value = "审核人id")
	private String auditorId;
}
