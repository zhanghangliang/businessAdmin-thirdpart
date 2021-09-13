package com.gov.wiki.wechat.res;

import com.gov.wiki.common.entity.buss.BizMatterDepositoryMain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "SubjecteRes", description = "主题返回结果")
public class SubjecteRes {
    @ApiModelProperty(name = "situation", value = "父情形")
    BizMatterDepositoryMain situation;
    @ApiModelProperty(name = "sonSituation", value = "子情形")
    List<BizMatterDepositoryMain> sonSituation;
}
