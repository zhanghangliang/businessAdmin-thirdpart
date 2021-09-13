package com.gov.wiki.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "MemberInfoReq", description = "分配资源参数对象")
public class MemberInfoReq {
    @ApiModelProperty(value = "角色ID")
    private String id;
    @ApiModelProperty(value = "角色姓名")
    private String name;
    @ApiModelProperty(value = "公司编号")
    private String companyId;

    public MemberInfoReq(String id, String name,String companyId) {
        this.id = id;
        this.name = name;
        this.companyId=companyId;
    }
}
