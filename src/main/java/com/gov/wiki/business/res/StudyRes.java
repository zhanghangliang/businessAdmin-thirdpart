package com.gov.wiki.business.res;

import com.gov.wiki.common.entity.system.SysFile;
import com.gov.wiki.common.res.BaseRes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
@ApiModel(value = "StudyRes", description = "学习资料返回对象")
public class StudyRes extends BaseRes{

    @ApiModelProperty(name = "name", value = "名称")
    private String name;

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

    @ApiModelProperty(name = "sysFiles", value = "上传的文件")
    private List<SysFile> sysFiles;

    @ApiModelProperty(name = "learnersname", value = "学习人员姓名")
    private List<String> learnersname;
    
    @ApiModelProperty(name = "permissionType", value = "权限类型 1-私有，2-部分公开，3-公开")
    private Integer permissionType;
    
    @ApiModelProperty(name = "permissionRange", value = "部分公开范围人员")
    private String permissionRange;
    
    @ApiModelProperty(name = "permissionRangeName", value = "部分公开范围人员名称")
    private String permissionRangeName;
    
    @ApiModelProperty(value = "类型 1-文件夹 2-文件")
    private Integer type;
    
    @ApiModelProperty(value = "父级")
    private String parentId;
    
    @ApiModelProperty(value = "长编码")
    private String path;
    
    @ApiModelProperty(value = "序号")
    private Integer seq;
    
    @ApiModelProperty(value = "叶子节点数")
    private int leaf;
    
    @ApiModelProperty(value = "父级名称")
    private String parentName;

    @ApiModelProperty(value = "关键字")
    private String keywords;
}