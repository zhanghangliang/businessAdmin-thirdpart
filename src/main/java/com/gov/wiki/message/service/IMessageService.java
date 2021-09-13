package com.gov.wiki.message.service;

import java.util.List;

import com.gov.wiki.common.res.PageResult;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.message.req.MemberMessageHistoryReq;
import org.springframework.transaction.annotation.Transactional;

import com.gov.wiki.common.service.IBaseService;
import com.gov.wiki.common.utils.SessionUser;
import com.gov.wiki.message.entity.McMessage;
import com.gov.wiki.message.req.GroupComment;
import com.gov.wiki.message.req.ReadMessageReq;
import com.gov.wiki.message.req.SendMessageReq;
import com.gov.wiki.message.res.GroupRes;
import com.gov.wiki.message.res.MessageRes;

public interface IMessageService extends IBaseService<McMessage, String>{

	McMessage sendMessage(SendMessageReq sendMessageReq, SessionUser user);

	List<MessageRes> readMessage(ReadMessageReq body);

	@Transactional(readOnly = true)
	List<MessageRes> pull(ReadMessageReq body);

	List<MessageRes> queryHistoryMessage(ReqBean<MemberMessageHistoryReq> bean);

	boolean hasNeedReadMessage();
}
