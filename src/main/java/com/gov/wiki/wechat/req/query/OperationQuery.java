package com.gov.wiki.wechat.req.query;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "OperationQuery", description = "操作查询对象")
public class OperationQuery {
    @ApiModelProperty(name = "memberId", value = "人员编号")
    private String memberId;
    
    @ApiModelProperty(value = "查询状态")
    private List<Integer> statusList;
    

    @ApiModelProperty(value = "查询类型 1，自助服务，2，预审")
    private Integer recordType;
}
