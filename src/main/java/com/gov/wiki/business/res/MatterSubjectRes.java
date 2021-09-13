package com.gov.wiki.business.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "MatterSubjectRes", description = "主题情形返回对象")
public class MatterSubjectRes {
    @ApiModelProperty(name = "father", value = "主情形编号")
    private String father;

    private String fatherName;

    @ApiModelProperty(name = "son", value = "子情形编号")
    private List<String> son;
}
