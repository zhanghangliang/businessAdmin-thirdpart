package com.gov.wiki.business.res;

import com.gov.wiki.common.entity.buss.BizMaterialDepository;
import com.gov.wiki.common.entity.system.SysFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "BizMaterialDepositoryRes", description = "审核返回对象")
public class BizMaterialDepositoryRes {
    @ApiModelProperty(name = "operationStatus", value = "资料库表")
    private BizMaterialDepository bizMaterialDepository;
    @ApiModelProperty(name = "operationStatus", value = "文件表")
    private List<SysFile> files;

}
