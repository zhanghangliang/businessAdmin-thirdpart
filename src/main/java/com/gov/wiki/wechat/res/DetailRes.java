package com.gov.wiki.wechat.res;

import com.gov.wiki.common.entity.buss.BizMatterDepositoryMain;
import com.gov.wiki.common.entity.buss.BizSubject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "DetailRes", description = "服务列表返回结果")
public class DetailRes {
    @ApiModelProperty(value = "分类名")
    private String name;
    @ApiModelProperty(value = "内容")
    private List<BizSubject> depositoryMains;
}
