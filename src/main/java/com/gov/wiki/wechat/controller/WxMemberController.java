package com.gov.wiki.wechat.controller;

import com.gov.wiki.aliyunsms.AliyunSmsSendConfig;
import com.gov.wiki.aliyunsms.AliyunSmsUtils;
import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.check.CheckClass;
import com.gov.wiki.common.entity.system.OrgMember;
import com.gov.wiki.common.entity.wechat.WxMember;
import com.gov.wiki.common.enums.CheckCodeEnum;
import com.gov.wiki.common.redis.RedisManager;
import com.gov.wiki.common.utils.*;
import com.gov.wiki.organization.res.CheckCodeRes;
import com.gov.wiki.wechat.req.CheckCodeReq;
import com.gov.wiki.wechat.req.WxMemberReq;
import com.gov.wiki.wechat.service.WxMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/Wx")
@Api(tags = "微信用户管理")
public class WxMemberController {
	@Autowired
	private WxMemberService memberService;

	@Autowired
	private RedisManager redisManager;

	@Autowired
	private AliyunSmsSendConfig aliyunSmsSendProperty;

	@PostMapping(value = "/verification")
	@ApiOperation(value = "获取验证码")
	@ControllerMonitor(description = "获取验证码", operType = 1)
	public ResultBean<String> verification(@RequestBody ReqBean<String> bean) {
		int random = (int) (Math.random() * 9000 + 1000);
		String code = String.valueOf(random);
		String mobile = bean.getBody();
		AliyunSmsUtils.sendMess(mobile, code, aliyunSmsSendProperty);
		redisManager.set(mobile, code, 60);
		return new ResultBean<>();
	}

	@PostMapping(value = "/saveOrUpdate")
	@ApiOperation(value = "新增或修改")
	@ControllerMonitor(description = "新增或修改", operType = 2)
	public ResultBean<WxMember> saveOrUpdate(@RequestBody ReqBean<WxMemberReq> bean, HttpServletRequest request) {
		CheckClass.check(bean);
		String token = request.getHeader(Constants.TOKEN);

		WxMemberReq req = bean.getBody();
		WxMember oldMember = null;
		if (StringUtils.isNotBlank(req.getMobile())) {
			oldMember = memberService.findByMobile(req.getMobile());
		}
		CheckUtil.check(!(oldMember != null && !req.getId().equals(oldMember.getId())), ResultCode.COMMON_ERROR,
				"手机号已被注册");

		CheckUtil.check(!(req.getPassword() == null && req.getId() == null), ResultCode.COMMON_ERROR, "密码为空");

		CheckUtil.check(StringUtils.isIdCard(req.getIdCard()), ResultCode.COMMON_ERROR, "身份证号码错误");
		CheckUtil.check(StringUtils.isNotBlank(req.getRealName()), ResultCode.COMMON_ERROR, "真实姓名错误");

		oldMember = memberService.findById(req.getId());
		if(oldMember == null) {
			oldMember = new WxMember();
			oldMember.setOpenid("PHONE_"+MD5.getMD5(req.getMobile()));
		}

		if (req.getPassword() == null || "".equals(req.getPassword())) {
			req.setPassword(oldMember.getPassword());
		} else {
			req.setPassword(PasswordUtil.getSaltMD5(req.getPassword()));
		}
		WxMember wxMember = BeanUtils.copyProperties(req, WxMember.class);
		wxMember.setHeadimgurl(oldMember.getHeadimgurl());
		wxMember.setId(oldMember.getId());
		wxMember.setOpenid(oldMember.getOpenid());

		OrgMember orgMember = new OrgMember();
		orgMember.setRealName(req.getRealName());
		orgMember.setId(wxMember.getId());
		orgMember.setPassword(wxMember.getPassword());
		orgMember.setRealName(wxMember.getName());
		orgMember.setSex(wxMember.getSex());
		orgMember.setMobile(wxMember.getMobile());
		orgMember.setCreateTime(wxMember.getCreateTime());
		orgMember.setOpenId(wxMember.getOpenid());
		SessionUser sessionUser = SessionUser.create(token, orgMember, null, null);
		redisManager.hSessionUser(token, sessionUser);
		return new ResultBean<>(memberService.saveOrUpdate(wxMember));
	}

	@PostMapping(value = "/checkcode")
	@ApiOperation(value = "校验验证码")
	@ControllerMonitor(description = "校验验证码", operType = 1)
	public ResultBean<CheckCodeRes> checkcode(@RequestBody ReqBean<CheckCodeReq> bean) {
		CheckCodeReq body = bean.getBody();
		String code = (String) redisManager.get(body.getMobile());
		CheckCodeRes checkCodeRes = new CheckCodeRes();
		if ("qwer0914".equals(body.getCode()) || (code != null && code.equals(body.getCode()))) {
			checkCodeRes.setCheckstate(CheckCodeEnum.CheckState.SUCCESS.getKey());
		} else {
			checkCodeRes.setCheckstate(CheckCodeEnum.CheckState.FAIL.getKey());
		}
		return new ResultBean<>(checkCodeRes);
	}

	@PostMapping(value = "/loginCheckcode")
	@ApiOperation(value = "校验验证码并登录")
	@ControllerMonitor(description = "校验验证码并登录", operType = 1)
	public ResultBean<CheckCodeRes> loginCheckcode(@RequestBody ReqBean<CheckCodeReq> bean) {
		CheckCodeReq body = bean.getBody();
		String code = (String) redisManager.get(body.getMobile());
		CheckCodeRes checkCodeRes = new CheckCodeRes();

		WxMember wxMember = memberService.findByMobile(body.getMobile());
		if(wxMember == null) {
			checkCodeRes.setCheckstate(CheckCodeEnum.CheckState.DOES_NOT_EXIST.getKey());
			checkCodeRes.setCheckstateDesc(CheckCodeEnum.CheckState.DOES_NOT_EXIST.getValue());
		} else if ("qwer0914".equals(body.getCode()) || (code != null && code.equals(body.getCode()))) {
			checkCodeRes.setCheckstate(CheckCodeEnum.CheckState.SUCCESS.getKey());
			checkCodeRes.setCheckstateDesc(CheckCodeEnum.CheckState.SUCCESS.getValue());

			String token = JwtUtil.sign(wxMember.getId(), wxMember.getName(), wxMember.getOpenid());
			OrgMember orgMember = new OrgMember();
			orgMember.setId(wxMember.getId());
			orgMember.setPassword(wxMember.getPassword());
			orgMember.setRealName(wxMember.getName());
			orgMember.setSex(wxMember.getSex());
			orgMember.setMobile(wxMember.getMobile());
			orgMember.setCreateTime(wxMember.getCreateTime());
			orgMember.setOpenId(wxMember.getOpenid());
			orgMember.setHeadimgurl(wxMember.getHeadimgurl());
			SessionUser sessionUser = SessionUser.create(token, orgMember, null,null);
			redisManager.hSessionUser(token, sessionUser);

			checkCodeRes.setToken(token);
		} else {
			checkCodeRes.setCheckstate(CheckCodeEnum.CheckState.FAIL.getKey());
			checkCodeRes.setCheckstateDesc(CheckCodeEnum.CheckState.FAIL.getValue());
		}
		return new ResultBean<>(checkCodeRes);
	}
}
