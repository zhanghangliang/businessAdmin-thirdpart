package com.gov.wiki.business.req;

import com.gov.wiki.common.entity.buss.BizMatterAuditMain;
import com.gov.wiki.common.entity.buss.BizMatterAuditSlave;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "MatterReq", description = "事项请求对象")
public class MatterReq {
    @ApiModelProperty(name = "主表信息", value = "主表信息")
    private BizMatterAuditMain bizMatterAuditMain;

    @ApiModelProperty(name = "从表信息", value = "从表信息")
    private List<BizMatterAuditSlave> bizMatterAuditSlaveList;
}
