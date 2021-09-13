package com.gov.wiki.business.res;

import com.gov.wiki.common.entity.buss.BizMatterDepositoryMain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "MatterRes", description = "事项返回对象")
public class MatterRes {
    @ApiModelProperty(name = "father", value = "父情形")
    private BizMatterDepositoryMain father;
    @ApiModelProperty(name = "son", value = "子情形")
    private List<BizMatterDepositoryMain> son;
}
