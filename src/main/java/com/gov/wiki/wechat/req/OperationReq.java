package com.gov.wiki.wechat.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

import com.gov.wiki.common.check.Check;

@Data
@ApiModel(description = "操作请求对象")
public class OperationReq {
    @ApiModelProperty(value = "主题id")
    @Check(nullable = false, title = "主题")
	private String subjectId;
    
    @ApiModelProperty(value = "最后一层关系id")
    private String relationShipId;
    
    @ApiModelProperty(value = "选择的选项的id")
    @Check(nullable = false, title = "选择的选项的id")
    private List<String> opinionIds;
    
    
    private String serviceRecordId;
    
    @ApiModelProperty(value = "是否提交审核")
    private Boolean hasSubmit = false;
    
}
