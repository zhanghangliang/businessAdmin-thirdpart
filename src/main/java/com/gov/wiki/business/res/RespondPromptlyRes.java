package com.gov.wiki.business.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@Data
@ApiModel(value = "RespondPromptlyRes", description = "及时响应率返回对象")
public class RespondPromptlyRes {

    @ApiModelProperty(name = "dataMap", value = "<区间,数据>")
    private Map<String, Long> dataMap;
}
