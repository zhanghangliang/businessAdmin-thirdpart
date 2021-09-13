package com.gov.wiki.business.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liusq
 * @decription TODO(用户提升率统计返回对象)
 * @date 2021/5/10 17:28
 */
@Data
@ApiModel(value = "LiftRateRes", description = "用户提升率统计返回对象")
public class LiftRateRes {
    @ApiModelProperty(name = "dateList", value = "时间集")
    private List<String> dateList;

    @ApiModelProperty(name = "addUserNumList", value = "新增用户数集")
    private List<Long> addUserNumList;

    @ApiModelProperty(name = "submitDataList", value = "提交资料人数集")
    private List<Long> submitDataList;
}