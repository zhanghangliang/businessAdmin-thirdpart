package com.gov.wiki.message.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.PredicateBuilder;
import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.redis.RedisManager;
import com.gov.wiki.common.res.PageResult;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.BeanUtils;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.JwtUtil;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.SessionUser;
import com.gov.wiki.message.dao.McGroupDao;
import com.gov.wiki.message.dao.McGroupMemberDao;
import com.gov.wiki.message.entity.McGroup;
import com.gov.wiki.message.entity.McGroupMember;
import com.gov.wiki.message.req.GroupComment;
import com.gov.wiki.message.req.GroupQuery;
import com.gov.wiki.message.res.GroupRes;
import com.gov.wiki.message.service.IGroupService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class GroupServiceImpl 
	extends BaseServiceImpl<McGroup, String, McGroupDao> 
	implements IGroupService {

	@Autowired
	private McGroupMemberDao groupMemberDao;
	@Autowired
	private RedisManager redisManager;
	
	@Override
	public McGroup addGroup(SessionUser user) {
		String userId = JwtUtil.getUserId();
		
		//查询当前客户是否有未关闭的会话，如果有，则返回未关闭的会话。
		PredicateBuilder<McGroup> builder = Specifications.and();
		builder.eq("createBy",user.getMember().getId()).ne("status","3");
		Sort sort=new Sort(Sort.Direction.DESC,"createTime");
		List<McGroup> mcGroups = this.baseRepository.findAll(builder.build(), sort);
		if (CollectionUtils.isNotEmpty(mcGroups) && mcGroups.size()>0){
			return mcGroups.get(0);
		}
		McGroup group = new McGroup();
		group.setName(user.getMember().getRealName());
		group.setStatus(1);
		this.baseRepository.saveAndFlush(group);

		//addMembers
		McGroupMember member = new McGroupMember();
		member.setUserId(userId);
		member.setGroupId(group.getId());
		member.setHeadUrl(user.getMember().getHeadimgurl());
		groupMemberDao.save(member);
		return group;
	}

	@Override
	public PageResult<GroupRes> queryGroup(ReqBean<GroupQuery> bean) {
		String userId = JwtUtil.getUserId();
		PredicateBuilder<McGroup> builder = Specifications.and();
		GroupQuery query = bean.getBody();
		if(query == null) query = new GroupQuery();
		if(query.getQueryMyList() != null) {
			builder.eq("members.userId", userId);
		}
		if(query.getQueryCanJoin() != null) {
			builder.eq("status", 1);
		}
		Page<McGroup> page = this.baseRepository.findAll(builder.build(), bean.getHeader().getPageable());
		return BeanUtils.pageCopy(page, GroupRes.class);
	}

	@Override
	public void joinToGroup(String body) {
		String userId = JwtUtil.getUserId();
		McGroup group = this.findById(body);
		CheckUtil.notNull(group, ResultCode.DATA_NOT_EXIST, "聊天会话");
		McGroupMember groupMember = new McGroupMember();
		groupMember.setGroupId(group.getId());
		long count = groupMemberDao.count(Example.of(groupMember));
		CheckUtil.check(count<2,ResultCode.COMMON_ERROR,"当前用户已经被接");
		McGroupMember member = new McGroupMember();
		member.setGroupId(body);
		member.setUserId(userId);
		group.setStatus(2);
		this.baseRepository.save(group);
		groupMemberDao.save(member);
	}

	@Override
	public void closeGroup(String body) {
		McGroup group = findById(body);
		CheckUtil.notNull(group, ResultCode.DATA_NOT_EXIST, "会话");
		group.setStatus(3);
		this.saveOrUpdate(group);
	}

	@Override
	public void comment(GroupComment req) {
		McGroup group = findById(req.getId());
		CheckUtil.notNull(group, ResultCode.DATA_NOT_EXIST, "会话");
		group.setScore(req.getStar());
		group.setComment(req.getComment());
		this.saveOrUpdate(group);
	}

	@Override
	public long count(GroupQuery query) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void autoEvaluationTask(Date date) {
		Date startTime = null;
		Date endTime = null;
		Calendar instance = Calendar.getInstance();
		if (date == null) {
			Date currentTime = new Date();
			instance.setTime(currentTime);
			instance.add(Calendar.HOUR_OF_DAY, -24);
			endTime = instance.getTime();
			instance.setTime(currentTime);
			instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), 1, 0, 0, 0);
			instance.set(Calendar.MILLISECOND, 0);
			startTime = instance.getTime();
		} else {
			startTime = date;
			endTime = new Date();
		}
		PredicateBuilder<McGroup> builder = Specifications.and();
		builder.eq("delFlag", false).eq("status", 3)
				.le("createTime", endTime)
				.ge("createTime", startTime);
		List<McGroup> list = this.baseRepository.findAll(builder.build());
		log.debug("检测到未评价数量:"+list.size()+"条");
		list.forEach(data -> {
			data.setScore(5);
			data.setComment("该用户未评价");
			data.setStatus(3);
			super.saveOrUpdate(data);
		});
	}

}
