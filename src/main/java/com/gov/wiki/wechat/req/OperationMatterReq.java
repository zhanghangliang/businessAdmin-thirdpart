package com.gov.wiki.wechat.req;

import com.gov.wiki.common.check.Check;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "OperationMatterReq", description = "操作事项请求对象")
public class OperationMatterReq {
    @ApiModelProperty(name = "upoeration", value = "上一记录编号")
    String upoeration;
    @ApiModelProperty(name = "situation", value = "主情形编号")
    @Check
    String situation;
    @ApiModelProperty(name = "subject", value = "主题")
    @Check
    String subject;
    @ApiModelProperty(name = "matterIds", value = "所选的子情形")
    List<String> matterIds;
    @ApiModelProperty(name = "operationType", value = "上一步为0,下一步为1")
    @Check
    Integer operationType;
}
