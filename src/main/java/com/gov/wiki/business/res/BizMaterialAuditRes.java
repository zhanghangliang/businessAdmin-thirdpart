package com.gov.wiki.business.res;

import com.gov.wiki.common.entity.buss.BizMaterialAudit;
import com.gov.wiki.common.entity.buss.BizMaterialDepository;
import com.gov.wiki.common.entity.system.SysFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "BizMaterialAuditRes", description = "审核返回对象")
public class BizMaterialAuditRes {
    @ApiModelProperty(name = "bizMaterialAudit", value = "资料对象")
    private BizMaterialDepository bizMaterialAudit;
    @ApiModelProperty(name = "operationStatus", value = "文件表")
    private List<SysFile> files;
}
