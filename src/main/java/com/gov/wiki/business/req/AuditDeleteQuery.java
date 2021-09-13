package com.gov.wiki.business.req;

import com.gov.wiki.common.check.Check;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AuditDeleteQuery", description = "审核删除对象")
public class AuditDeleteQuery {
    @ApiModelProperty(name = "audited_id", value = "被审核编号")
    @Check
    private String auditedId;
    @ApiModelProperty(name = "audit_state", value = "审核状态(0为待审,1为拒绝,2为通过)")
    @Check
    private Integer auditState;
    @ApiModelProperty(name = "audit_opinion", value = "审核意见")
    private String auditOpinion;
}
