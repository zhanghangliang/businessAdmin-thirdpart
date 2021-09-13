package com.gov.wiki.message.service;

import com.gov.wiki.common.res.PageResult;
import com.gov.wiki.common.service.IBaseService;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.SessionUser;
import com.gov.wiki.message.entity.McGroup;
import com.gov.wiki.message.req.GroupComment;
import com.gov.wiki.message.req.GroupQuery;
import com.gov.wiki.message.res.GroupRes;

import java.util.Date;

public interface IGroupService extends IBaseService<McGroup, String> {


	PageResult<GroupRes> queryGroup(ReqBean<GroupQuery> bean);

	void joinToGroup(String body);

	McGroup addGroup(SessionUser user);

	void closeGroup(String body);

	void comment(GroupComment body);

	long count(GroupQuery query);

	void autoEvaluationTask(Date date);
}
