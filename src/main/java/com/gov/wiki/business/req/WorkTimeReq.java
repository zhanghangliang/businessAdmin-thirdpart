package com.gov.wiki.business.req;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class WorkTimeReq {

	private String id;
	
	/**
     * 创建时间
     */
    @CreationTimestamp
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @JsonIgnore
    private Date createTime;
    
    
    @CreationTimestamp
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "dutyTime", value = "生效时间")
    @JsonIgnore
    private Date dutyTime;


    /**
     * 状态，1上班，2休息
     */
    @ApiModelProperty(value = "状态，1上班，2休息")
    private Integer status;

    /**
     * 时间段，24小时制，用小时来表示，多个时间段用‘,’分割
     */
    @ApiModelProperty(value = "时间段，24小时制，用小时来表示，多个时间段用‘,’分割")
    private String period;
}
