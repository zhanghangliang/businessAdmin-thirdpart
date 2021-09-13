package com.gov.wiki.message.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.utils.*;
import com.gov.wiki.message.req.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.check.CheckClass;
import com.gov.wiki.common.redis.RedisManager;
import com.gov.wiki.common.res.PageResult;
import com.gov.wiki.message.entity.McGroup;
import com.gov.wiki.message.res.GroupRes;
import com.gov.wiki.message.res.MessageRes;
import com.gov.wiki.message.service.IGroupService;
import com.gov.wiki.message.service.IMessageService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/message")
@Slf4j
public class MessageController {

	@Autowired
	private IMessageService messageService;
	@Autowired
	private IGroupService groupService;
	
	@Autowired
	private RedisManager redisManager;
	
	@PostMapping(value = "/read")
	public ResultBean<List<MessageRes>> readMessage(@RequestBody ReqBean<ReadMessageReq> bean){
		CheckClass.check(bean.getBody());
		return new ResultBean<List<MessageRes>>(messageService.readMessage(bean.getBody()));
	}

	@PostMapping(value = "/hasNewMessage")
	public ResultBean<Boolean> hasNewMessage(){
		boolean result = messageService.hasNeedReadMessage();
		if(!result) {
			GroupQuery query = new GroupQuery();
			query.setQueryCanJoin(true);
			long count = groupService.count(query);
			result = count != 0;
		}
		return new ResultBean<Boolean>(result);
	}
	
	/**
	 * 一次性拉取所有消息
	 * @param bean
	 * @return
	 */
	@PostMapping(value = "/pull")
	public ResultBean<List<MessageRes>> pull(@RequestBody ReqBean<ReadMessageReq> bean){
		CheckClass.check(bean.getBody());
		return new ResultBean<List<MessageRes>>(messageService.pull(bean.getBody()));
	}
	
	/**
	 *   新添加一个组，然后把客服加进去
	 * @param bean
	 * @return
	 */
	@PostMapping(value = "/linkcustomer")
	public ResultBean<GroupRes> linkcustomer(HttpServletRequest request){
		String token = request.getHeader(Constants.TOKEN);
        SessionUser user = (SessionUser) redisManager.getSessionUser(token);
		McGroup group = groupService.addGroup(user);
		log.info("group:{}",group.getId());
		//向组中发送客户加入会话的消息
		
		SendMessageReq sendMessageReq = new SendMessageReq();
		String msg=String.format("客户%s加入会话",user.getMember().getRealName());
		sendMessageReq.setContent(msg);
		sendMessageReq.setGroupId(group.getId());
		sendMessageReq.setMessageType(7);
		doSendMessage(sendMessageReq, request);
		return new ResultBean<GroupRes>(BeanUtils.copyProperties(group, GroupRes.class));
	}
	/**
	 * 查询可以 添加分组的列表
	 * @return
	 */
	@PostMapping(value = "/queryGroup")
	@ApiOperation(value = "查询聊天列表")
	public ResultBean<PageResult<GroupRes>> queryGroup(@RequestBody ReqBean<GroupQuery> bean){
		return new ResultBean<PageResult<GroupRes>>(groupService.queryGroup(bean));
	}


	/**
	 * 查询聊天历史记录
	 */
	@PostMapping(value = "/queryHistory")
	@ApiOperation(value = "查询历史记录")
	public ResultBean<List<MessageRes>> queryHistoryMessage(@RequestBody ReqBean<MemberMessageHistoryReq> bean){
		CheckUtil.notNull(bean, ResultCode.COMMON_ERROR,"对象不能为空");
		CheckUtil.notEmpty(bean.getBody().getId(),ResultCode.COMMON_ERROR,"用户ID不能为空");
		return new ResultBean<>(messageService.queryHistoryMessage(bean));
	}




	/**
	 * 加入系统中,返回群组中的一次性消息
	 * @return
	 */
	@PostMapping(value = "/addGroup")
	@ApiOperation(value = "加入群组，然后返回之前的所有消息")
	public ResultBean<List<MessageRes>> addGroup(@RequestBody ReqBean<String> bean,HttpServletRequest request){
		groupService.joinToGroup(bean.getBody());
		String token = request.getHeader(Constants.TOKEN);
		SessionUser user = (SessionUser) redisManager.getSessionUser(token);
		ReadMessageReq req = new ReadMessageReq();
		req.setGroupId(bean.getBody());
		//发送消息{客服xxx加入会话}
		SendMessageReq sendMessageReq = new SendMessageReq();
		String msg=String.format("客服%s加入会话",user.getMember().getRealName());
		sendMessageReq.setContent(msg);
		sendMessageReq.setGroupId(bean.getBody());
		sendMessageReq.setMessageType(8);
		doSendMessage(sendMessageReq, request);
		return new ResultBean<List<MessageRes>>(messageService.pull(req));
	}
	@PostMapping(value = "/closeGroup")
	@ApiOperation(value = "关闭群组（会话）")
	public ResultBean<String> closeGroup(@RequestBody ReqBean<String> bean,HttpServletRequest request){
		SendMessageReq sendMessageReq = new SendMessageReq();
		String msg= "客服关闭会话";
		sendMessageReq.setContent(msg);
		sendMessageReq.setGroupId(bean.getBody());
		sendMessageReq.setMessageType(6);
		doSendMessage(sendMessageReq, request);
		groupService.closeGroup(bean.getBody());
		return new ResultBean<String>();
	}
	
	@PostMapping(value = "/sendMessage")
	public ResultBean<List<MessageRes>> sendMessage(@RequestBody ReqBean<SendMessageReq> bean,HttpServletRequest request){
		doSendMessage(bean.getBody(), request);
		ReadMessageReq read = new ReadMessageReq();
		read.setGroupId(bean.getBody().getGroupId());
		return new ResultBean<List<MessageRes>>(messageService.readMessage(read));
	}

	private void doSendMessage(SendMessageReq bean, HttpServletRequest request) {
		String token = request.getHeader(Constants.TOKEN);
        SessionUser user = (SessionUser) redisManager.getSessionUser(token);
		messageService.sendMessage(bean,user);
	}
	@PostMapping(value = "/comment")
	public ResultBean<String> comment(@RequestBody ReqBean<GroupComment> bean){
		CheckClass.check(bean.getBody());
		groupService.comment(bean.getBody());
		return new ResultBean<String>();
	}
}
