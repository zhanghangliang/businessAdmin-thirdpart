package com.gov.wiki.business.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import com.gov.wiki.common.check.Check;

@Data
@ApiModel(value = "StudyReq", description = "学习资料请求对象")
public class StudyReq {
    @ApiModelProperty(name = "id", value = "唯一标识")
    private String id;

    @ApiModelProperty(name = "name", value = "名称")
    @Check(nullable = false, title = "名称")
    private String name;
    
    @ApiModelProperty(value = "类型 1-文件夹 2-文件")
    @Check(nullable = false, title = "资料类型")
    private Integer type;

    @ApiModelProperty(name = "classification", value = "分类")
    private Integer classification;

    @ApiModelProperty(name = "studyDescribe", value = "描述")
    private String studyDescribe;

    @ApiModelProperty(name = "lengthOfStudy", value = "学习时长")
    private String lengthOfStudy;

    @ApiModelProperty(name = "learners", value = "学习人员")
    private String learners;

    @ApiModelProperty(name = "online", value = "启用禁用")
    private Boolean online;

    @ApiModelProperty(name = "companyId", value = "公司ID")
    private String companyId;

    @ApiModelProperty(name = "sysFiles", value = "上传的文件")
    private List<String> fileIds;
    
    @ApiModelProperty(name = "permissionType", value = "权限类型 1-私有，2-部分公开，3-公开")
    private Integer permissionType;
    
    @ApiModelProperty(name = "permissionRange", value = "部分公开人员范围")
    private String permissionRange;
    
    @ApiModelProperty(value = "父级")
    private String parentId;
    
    @ApiModelProperty(value = "长编码")
    private String path;
    
    @ApiModelProperty(value = "序号")
    private Integer seq;

    @ApiModelProperty(value = "关键字")
    private String keywords;
}