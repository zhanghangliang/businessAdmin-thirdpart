package com.gov.wiki.business.res;

import com.gov.wiki.common.entity.buss.BizMatterDepositoryMain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "SituationRes", description = "目录情形返回对象")
public class SituationRes {
    @ApiModelProperty(name = "son", value = "目录")
    private List<BizMatterDepositoryMain> catalog;
    @ApiModelProperty(name = "son", value = "子情形")
    private List<BizMatterDepositoryMain> situation;
}
