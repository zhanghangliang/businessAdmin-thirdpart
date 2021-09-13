package com.gov.wiki.business.req;

import com.gov.wiki.organization.req.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "StudyQuery", description = "学习资料查询对象")
public class StudyQuery extends BaseQuery{
	
    @ApiModelProperty(value = "父级ID")
    private String parentId;

    @ApiModelProperty(value = "标题")
    private String name;

    @ApiModelProperty(value = "分类")
    private Integer type;
}
