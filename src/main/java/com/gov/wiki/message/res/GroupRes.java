package com.gov.wiki.message.res;

import java.util.List;

import javax.persistence.Column;

import com.gov.wiki.common.res.BaseRes;
import com.gov.wiki.message.entity.McGroupMember;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 组管理
 * @author wangmingwei
 *
 */
@Setter
@Getter
public class GroupRes extends BaseRes {

	/**
	*群名称
	*/
	@ApiModelProperty(value = "群名称")
	private String name;
	
	/**
	*群组状态  1.等待客服加入。
	*	2.正常聊天中。
	*	3.聊天关闭
	*
	*/
	@Column(name = "status", nullable = true)
	@ApiModelProperty(value = "*群组状态  1.等待客服加入。\n" + 
			"	*	2.正常聊天中。\n" + 
			"	*	3.聊天关闭")
	private Integer status;
	@ApiModelProperty(value = "群成员")
	private List<McGroupMember> members;
}
