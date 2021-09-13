package com.gov.wiki.business.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "MatterSubjectReq", description = "主题情形请求对象")
public class MatterSubjectReq {
    @ApiModelProperty(name = "father", value = "主情形编号")
    private String father;

    @ApiModelProperty(name = "son", value = "子情形编号")
    private List<String> son;
}
