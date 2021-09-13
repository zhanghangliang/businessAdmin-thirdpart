package com.gov.wiki.message.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.gov.wiki.common.res.PageResult;
import com.gov.wiki.common.utils.*;
import com.gov.wiki.message.req.MemberMessageHistoryReq;
import com.gov.wiki.organization.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.PredicateBuilder;
import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.entity.system.SysFile;
import com.gov.wiki.common.redis.RedisManager;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.config.FileConfig;
import com.gov.wiki.message.dao.McGroupDao;
import com.gov.wiki.message.dao.McMessageDao;
import com.gov.wiki.message.entity.McGroup;
import com.gov.wiki.message.entity.McGroupMember;
import com.gov.wiki.message.entity.McMessage;
import com.gov.wiki.message.req.ReadMessageReq;
import com.gov.wiki.message.req.SendMessageReq;
import com.gov.wiki.message.res.MessageRes;
import com.gov.wiki.message.service.IMessageService;
import com.gov.wiki.system.dao.SysFileDao;
import com.gov.wiki.watermaker.Position;
import com.gov.wiki.watermaker.WaterMarkForImage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
public class MessageServiceImpl extends BaseServiceImpl<McMessage, String, McMessageDao> implements IMessageService {

	/**
	 * 用户过期时间
	 */
	Map<String, Long> userExpiresTime = new ConcurrentHashMap<String, Long>();
	@Autowired
	private RedisManager redisManager;

	@Autowired
	private McGroupDao groupDao;
	@Autowired
	private SysFileDao fileDao;

	@Autowired
	private FileConfig customPro;

	@Autowired
	IRoleService roleService;

	@Override
	public List<MessageRes> readMessage(ReadMessageReq body) {
		String userId = JwtUtil.getUserId();
		List<MessageRes> messages = new ArrayList<MessageRes>();
		McMessage message = (McMessage) redisManager.lLeftPro(getRedisKey(userId));
		while (message != null) {
			messages.add(BeanUtils.copyProperties(message, MessageRes.class));
			message = (McMessage) redisManager.lLeftPro(getRedisKey(userId));
		}
		return messages;
	}

	@Override
	public McMessage sendMessage(SendMessageReq req, SessionUser user) {
		String userId = JwtUtil.getUserId();
		updateUserExpiresTime(userId);
		// 获取组信息，
		Optional<McGroup> optional = groupDao.findById(req.getGroupId());
		CheckUtil.check(optional.isPresent(), ResultCode.COMMON_ERROR, "群组不存在");
		List<McGroupMember> members = optional.get().getMembers();
		McMessage message = BeanUtils.copyProperties(req, McMessage.class);
		// 查询是否存在文件
		if (StringUtils.isNotBlank(message.getFolderId())) addWaterMaker(message);
		message.setCreateBy(user.getMember().getId());
		message.setCreateTime(new Date());
		message.setCreateName(user.getMember().getRealName());
		McMessage mcMessage = this.baseRepository.saveAndFlush(message);
		saveToCache(mcMessage, members);
		return mcMessage;
	}

	/**
	 * 消息中的图片添加水印
	 * 
	 * @param message
	 */
	private void addWaterMaker(McMessage message) {
		SysFile file = new SysFile();
		file.setReferenceId(message.getFolderId());
		WaterMarkForImage waterMarkForImg = new WaterMarkForImage();
		String text = null;
		try {
			text = new String("图片仅供预审参考，不用作其他任何用途".getBytes(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("添加水印失败",e);
		}
		Integer fontStyle = Font.BOLD;
		Float alpha = 0.5F;
		Color waterMarkFontColor = Color.YELLOW;
		Integer waterMarkFontDegree = null;
		List<SysFile> files = fileDao.findAll(Example.of(file));
		String tempFile = "temp";
		for (SysFile f : files) {
			String filePath = customPro.getSavePath() + f.getFileUrl();
			filePath = filePath.replace(customPro.getShowPrefix(), "");
			log.info("加水印的文件地址：{}", filePath);
			String result = waterMarkForImg.markImageBySingleLineText(filePath, customPro.getSavePath(), tempFile,
					"文泉驿微米黑", fontStyle, Position.CENTER, alpha, waterMarkFontColor, text, waterMarkFontDegree);
			FileUtils.copyFile(result, filePath);
			log.info("给文件[{}]添加水印结果：{}", f.getId(), result);
		}
	}


	private void saveToCache(McMessage message, List<McGroupMember> members) {
		String userId = JwtUtil.getUserId();
		if (!CollectionUtils.isEmpty(members)) {
			for (McGroupMember m : members) {
				if (m.getUserId().equals(userId))
					continue;
				if (userExpiresTime.containsKey(m.getUserId())) {
					redisManager.lSet(getRedisKey(m.getUserId()), message);
				}
			}
		}

	}

	private String getRedisKey(String userId) {
		String key = "MESSAGE:CACHE:USERID";
		return key.replace("USERID", userId);
	}

	private void updateUserExpiresTime(String userId) {
		long time = System.currentTimeMillis() + 20 * 60 * 1000;
		userExpiresTime.put(userId, time);
		redisManager.expire(userId, 20 * 60);
		// 数据删除

	}

	@Override
	public List<MessageRes> pull(ReadMessageReq body) {
		String userId = JwtUtil.getUserId();
		PredicateBuilder<McMessage> builder = Specifications.and();
		builder.eq("groups.members.userId", userId);
		builder.eq(StringUtils.isNotBlank(body.getGroupId()), "groups.id", body.getGroupId());
		List<McMessage> list = this.baseRepository.findAll(builder.build());
		return BeanUtils.listCopy(list, MessageRes.class);
	}

	@Override
	public List<MessageRes> queryHistoryMessage(ReqBean<MemberMessageHistoryReq> bean) {
		MemberMessageHistoryReq body = bean.getBody();
		PredicateBuilder<McMessage> builder = Specifications.and();
		builder.eq("groups.createBy", body.getId());
		Sort sort = new Sort(Sort.Direction.ASC, "createTime");
		List<McMessage> mcMessages = this.baseRepository.findAll(builder.build(), sort);
		for (McMessage mcMessage : mcMessages) {
			this.getEntityManager().detach(mcMessage);
		}
		return BeanUtils.listCopy(mcMessages, MessageRes.class);
	}

	@Override
	public boolean hasNeedReadMessage() {
		String userId = JwtUtil.getUserId();
		return redisManager.hasKey(getRedisKey(userId));
	}

}
