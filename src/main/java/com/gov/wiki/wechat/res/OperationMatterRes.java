package com.gov.wiki.wechat.res;

import com.gov.wiki.common.entity.buss.BizMatterDepositoryMain;
import com.gov.wiki.common.entity.wechat.WxOperationRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "OperationMatterRes", description = "操作事项返回结果")
public class OperationMatterRes {
    @ApiModelProperty(name = "wxOperationRecord", value = "记录")
    WxOperationRecord wxOperationRecord;
    @ApiModelProperty(name = "situation", value = "父情形")
    BizMatterDepositoryMain situation;
    @ApiModelProperty(name = "matterDepositoryMains", value = "事项数据")
    List<BizMatterDepositoryMain> matterDepositoryMains;
}
