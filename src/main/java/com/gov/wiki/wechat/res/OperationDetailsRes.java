package com.gov.wiki.wechat.res;

import com.gov.wiki.common.entity.buss.BizMatterDepositorySlave;
import com.gov.wiki.common.entity.system.SysFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "OperationDetailsRes", description = "搜索返回结果")
public class OperationDetailsRes {
    @ApiModelProperty(value = "资料id")
    private String id;
    @ApiModelProperty(value = "资料名称")
    private String name;
    @ApiModelProperty(value = "文件信息")
    private List<SysFile> sysFiles;
}
