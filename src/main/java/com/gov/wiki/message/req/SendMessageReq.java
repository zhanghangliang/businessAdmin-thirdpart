package com.gov.wiki.message.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SendMessageReq {

	@ApiModelProperty(value = "消息内容")
	private String content;
	@ApiModelProperty(value = "组ID")
	private String groupId;
	
	@ApiModelProperty(value = "文件夹id")
	private String folderId;
	
	/**
	 * 
	 */
	@ApiModelProperty(value = "消息类型：1 文本消息 2 图片消息 3 视频 4 外连接 5 等待对话 6 关闭会话 7 客户加入会话 8 客服加入会话")
	private Integer messageType;

	/**
	 * 消息链接
	 */
	@ApiModelProperty(value = "消息链接")
	private String messageUrl;
}
