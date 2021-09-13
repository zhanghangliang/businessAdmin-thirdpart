package com.gov.wiki.message.res;

import com.gov.wiki.common.entity.system.OrgRole;
import com.gov.wiki.common.res.BaseRes;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MessageRes extends BaseRes {

	/**
	*组ID
	*/
	private String groupId;
	
	/**
	*消息内容
	*/
	private String content;
	
	/**
	*文件ID
	*/
	private String folderId;
	
	
	/**
	*消息状态： 1已发送，2，已撤回
	*/
	private Integer status;
	
	/**
	*
	*/
	private String linkMessageId;
	
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
