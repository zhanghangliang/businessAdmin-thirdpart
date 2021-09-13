package com.gov.wiki.message.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 对话评价
 * @author wangmingwei
 *
 */
@Data
public class GroupComment {

	@ApiModelProperty(value = "群组id")
	private String id;
	@ApiModelProperty(value = "评分等级")
	private Integer star;
	@ApiModelProperty(value = "评价内容")
	private String comment;
}
