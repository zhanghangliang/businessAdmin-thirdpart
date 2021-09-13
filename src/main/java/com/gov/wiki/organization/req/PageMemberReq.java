package com.gov.wiki.organization.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "PageMemberReq", description = "分页人员请求对象")
public class PageMemberReq {
    @ApiModelProperty(name = "companyId", value = "公司ID")
    private String companyId;

    @ApiModelProperty(name = "departId", value = "部门ID")
    private String departId;
}
