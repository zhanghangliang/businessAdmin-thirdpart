package com.gov.wiki.business.req;

import java.util.List;
import com.gov.wiki.common.check.Check;
import com.gov.wiki.common.entity.buss.BizMaterialAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "MaterialReq", description = "资料请求对象")
public class MaterialReq {
    @ApiModelProperty(name = "id", value = "唯一标识")
    private String id;
    
    @ApiModelProperty(name = "materialId", value = "审核表编号")
    @Check
    private String materialId;
    
    @ApiModelProperty(name = "materialDepositoryId", value = "资料库编号")
    private String materialDepositoryId;
    
    @ApiModelProperty(name = "materialType", value = "资料类型,例如:证件")
    private Integer materialType;
    
    @ApiModelProperty(name = "materialName", value = "资料名称")
    @Check(nullable = false, title = "资料名称")
    private String materialName;
    
    @ApiModelProperty(name = "materialCategory", value = "资料类别,例如T1")
    private String materialCategory;
    
    @ApiModelProperty(name = "materialSource", value = "资料来源")
    private String materialSource;
    
    @ApiModelProperty(name = "materialDescription", value = "资料说明")
    private String materialDescription;
    
    @ApiModelProperty(name = "front", value = "是否前置")
    private Boolean front;
    
    @ApiModelProperty(value = "前置描述")
    private String frontDescription;
    
    @ApiModelProperty(name = "auditState", value = "审核状态")
    private Integer auditState;
    
    @ApiModelProperty(name = "operationStatus", value = "操作状态")
    private Integer operationStatus;
    
    @ApiModelProperty(name = "sysFile", value = "文件表")
    private List<String> sysFile;
    
    @ApiModelProperty(name = "standard", value = "受理标准")
    private String standard;
    
    @ApiModelProperty(name = "whereabouts", value = "去向")
    private String whereabouts;
    
	@ApiModelProperty(value = "备注")	
	private String remark;
	
	@ApiModelProperty(value = "行政职责分类")	
	private String dutyType;
	
	@ApiModelProperty(value = "操作原因")	
	private String operReason;

    public BizMaterialAudit toEntity(){
        BizMaterialAudit bizMaterialAudit=new BizMaterialAudit();
        bizMaterialAudit.setId(this.id);
        bizMaterialAudit.setMaterialId(this.materialId);
        bizMaterialAudit.setMaterialDepositoryId(this.materialDepositoryId);
        bizMaterialAudit.setMaterialType(this.materialType);
        bizMaterialAudit.setMaterialName(this.materialName);
        bizMaterialAudit.setMaterialCategory(this.materialCategory);
        bizMaterialAudit.setMaterialSource(this.materialSource);
        bizMaterialAudit.setMaterialDescription(this.materialDescription);
        bizMaterialAudit.setFront(this.front);
        bizMaterialAudit.setAuditState(this.auditState);
        bizMaterialAudit.setOperationStatus(this.operationStatus);
        bizMaterialAudit.setStandard(this.standard);
        bizMaterialAudit.setWhereabouts(this.whereabouts);
        bizMaterialAudit.setFrontDescription(this.frontDescription);
        bizMaterialAudit.setRemark(this.remark);
        bizMaterialAudit.setDutyType(this.dutyType);
        bizMaterialAudit.setOperReason(this.operReason);
        return bizMaterialAudit;
    }
}