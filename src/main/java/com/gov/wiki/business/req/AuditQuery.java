package com.gov.wiki.business.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AuditQuery", description = "审核查询对象")
public class AuditQuery {
    @ApiModelProperty(name = "audited_id", value = "被审核编号")
    private String auditedId;
    @ApiModelProperty(name = "object_type", value = "被审核文件类型")
    private Integer objectType;
    @ApiModelProperty(name = "audit_state", value = "审核状态(0为待审,1为拒绝,2为通过)")
    private Integer auditState;
    @ApiModelProperty(name = "audit_opinion", value = "审核意见")
    private String auditOpinion;
}
