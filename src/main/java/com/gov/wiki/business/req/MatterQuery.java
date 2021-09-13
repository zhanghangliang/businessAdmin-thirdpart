package com.gov.wiki.business.req;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "MatterQuery", description = "事项查询对象")
public class MatterQuery {
    @ApiModelProperty(name = "name", value = "事项名称")
    private String name;
    @ApiModelProperty(name = "keyword", value = "关键描述")
    private String keyword;
    @ApiModelProperty(name = "attribute", value = "1-主题，2-基础事项，3-事项，4-情形")
    private List<Integer> attribute;
    
    @ApiModelProperty(name = "subjectType", value = "主题类型")
    private String subjectType;
}
