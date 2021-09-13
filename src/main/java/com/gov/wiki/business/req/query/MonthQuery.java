package com.gov.wiki.business.req.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @author liusq
 * @decription TODO(传入月份)
 * @date 2021/5/11 15:56
 */
@Data
@ApiModel(value = "YearQuery", description = "月份统计")
public class MonthQuery {

    @ApiModelProperty(value = "月份")
    @Size(min = 1, max = 2, message = "请输入正确的月份")
    private Integer month;
}
