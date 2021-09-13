package com.gov.wiki.business.req.query;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WorkTimeQuery {

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM")
    @DateTimeFormat(pattern = "yyyy-MM")
    @ApiModelProperty(name = "queryMonth", value = "查询月份")
	private Date queryMonth;
	
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "queryDay", value = "查询日期")
	private Date queryDay;
}
