package com.gov.wiki.business.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liusq
 * @decription TODO(年度数据返回对象)
 * @date 2021/5/10 9:42
 */
@Data
@ApiModel(value = "AnnualDataRes", description = "年度数据返回对象")
public class AnnualDataRes {

    @ApiModelProperty(name = "dateList", value = "时间集")
    private List<String> dateList;

    @ApiModelProperty(name = "dataList", value = "数据")
    private List<Long> dataList;
}
