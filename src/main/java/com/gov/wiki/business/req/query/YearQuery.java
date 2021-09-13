package com.gov.wiki.business.req.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @author liusq
 * @decription TODO(传入年份)
 * @date 2021/5/10 14:17
 */
@Data
@ApiModel(value = "YearQuery", description = "年份统计")
public class YearQuery {

    @ApiModelProperty(value = "年份")
    @Size(min = 4, max = 4, message = "请输入正确的年份")
    private Integer year;
}