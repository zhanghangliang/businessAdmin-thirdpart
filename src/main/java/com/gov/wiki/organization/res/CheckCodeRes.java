package com.gov.wiki.organization.res;

import com.gov.wiki.common.entity.system.OrgMember;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "CheckCodeRes", description = "检查验证码结果")
public class CheckCodeRes {
    @ApiModelProperty(value = "结果")
    private Integer checkstate;
}
