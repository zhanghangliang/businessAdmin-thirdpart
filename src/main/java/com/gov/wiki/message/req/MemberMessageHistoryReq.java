package com.gov.wiki.message.req;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.gov.wiki.common.utils.DateUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Setter
@Getter
@ApiModel(value = "MemberMessageHistoryReq",description = "用户历史消息查询")
public class MemberMessageHistoryReq {

    @ApiModelProperty(value = "用户ID",name = "id")
    private String id;

    @ApiModelProperty(value = "查询开始时间",name = "startDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startData;

    @ApiModelProperty(value = "查询结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endData;

	@Override
	public String toString() {
		return "MemberMessageHistoryReq [id=" + id + ", startData=" + DateUtil.dateToString(startData, "yyyy-MM-dd HH:mm:ss") + ", endData=" + DateUtil.dateToString(endData, "yyyy-MM-dd HH:mm:ss") + "]";
	}
    
    
    
}
