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
import com.gov.wiki.common.utils.BeanUtils;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.Constants;
import com.gov.wiki.common.utils.JSONUtil;
import com.gov.wiki.common.utils.JwtUtil;
import com.gov.wiki.common.utils.PasswordUtil;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.SessionUser;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.organization.res.CheckCodeRes;
import com.gov.wiki.wechat.req.CheckCodeReq;
import com.gov.wiki.wechat.req.PlatformInfoRep;
import com.gov.wiki.wechat.req.WxMemberReq;
import com.gov.wiki.wechat.service.WxMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping(value = "/Wx")
@Api(tags = "微信用户管理")
@Slf4j
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
        log.info("saveOrUpdate接收到请求体" + JSONUtil.toJSONString(req));
        if (StringUtils.isNotBlank(req.getMobile())) {
            oldMember = memberService.findByMobile(req.getMobile());
        }
        CheckUtil.check(!(oldMember != null && !req.getIdCard().equals(oldMember.getIdCard())), ResultCode.COMMON_ERROR, "手机号已被注册");
        CheckUtil.check(!(req.getPassword() == null && req.getId() == null), ResultCode.COMMON_ERROR, "密码为空");
        CheckUtil.check(StringUtils.isIdCard(req.getIdCard()), ResultCode.COMMON_ERROR, "身份证号码错误");
        CheckUtil.check(StringUtils.isNotBlank(req.getRealName()), ResultCode.COMMON_ERROR, "真实姓名错误");
        if (StringUtils.isNotBlank(req.getId()) && oldMember == null) {
            oldMember = memberService.findById(req.getId());
        }
        WxMember wxMember = BeanUtils.copyProperties(req, WxMember.class);
        if (oldMember != null) {
            if (req.getPassword() == null || "".equals(req.getPassword())) {
                req.setPassword(oldMember.getPassword());
            } else {
                req.setPassword(PasswordUtil.getSaltMD5(req.getPassword()));
            }
            wxMember.setHeadimgurl(oldMember.getHeadimgurl());
            wxMember.setId(oldMember.getId());
            wxMember.setOpenid(oldMember.getOpenid());
        }
        WxMember wxMember1 = memberService.saveOrUpdate(wxMember);
        log.info(JSONUtil.toJSONString(wxMember1));

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
        return new ResultBean<>(wxMember1);
    }

    @PostMapping(value = "/checkcode")
    @ApiOperation(value = "校验验证码")
    @ControllerMonitor(description = "校验验证码", operType = 1)
    public ResultBean<CheckCodeRes> checkcode(@RequestBody ReqBean<CheckCodeReq> bean) {
        CheckCodeReq body = bean.getBody();
        String code = (String) redisManager.get(body.getMobile());
        CheckCodeRes checkCodeRes = new CheckCodeRes();
        if (code != null && code.equals(body.getCode())) {
            checkCodeRes.setCheckstate(CheckCodeEnum.CheckState.SUCCESS.getKey());
        } else {
            checkCodeRes.setCheckstate(CheckCodeEnum.CheckState.FAIL.getKey());
        }
        return new ResultBean<>(checkCodeRes);
    }

    @PostMapping(value = "/checkcodeLogin")
    @ApiOperation(value = "校验验证码登录")
    @ControllerMonitor(description = "校验验证码登录", operType = 1)
    public ResultBean<CheckCodeRes> checkcodeLogin(@RequestBody ReqBean<CheckCodeReq> bean) {
        CheckCodeReq body = bean.getBody();
        String code = (String) redisManager.get(body.getMobile());
        CheckCodeRes checkCodeRes = new CheckCodeRes();
        if (code != null && code.equals(body.getCode())) {
            checkCodeRes.setCheckstate(CheckCodeEnum.CheckState.SUCCESS.getKey());
        } else {
            checkCodeRes.setCheckstate(CheckCodeEnum.CheckState.FAIL.getKey());
        }
        return new ResultBean<>(checkCodeRes);
    }

    @PostMapping("loginByPlatform")
    @ApiOperation(value = "利用平台信息登录")
    @ControllerMonitor(description = "利用平台信息登录", operType = 1)
    public ResultBean<CheckCodeRes> loginByPlatform(@RequestBody ReqBean<PlatformInfoRep> bean) {
        PlatformInfoRep req = bean.getBody();
        log.info("loginByPlatform接收到请求体" + JSONUtil.toJSONString(req));
        WxMember oldMember = memberService.findByMobile(req.getUsername());

        if (oldMember == null) {
            oldMember = new WxMember();
            oldMember.setOpenid(req.getUuid());
            oldMember.setPassword(req.getUserPassword());
            oldMember.setName(req.getRealName());
            oldMember.setSex(req.getSex());
            oldMember.setMobile(req.getUsername());
            oldMember.setRealName(req.getRealName());
            oldMember.setIdCard(req.getIdNumber());
            memberService.saveOrUpdate(oldMember);
        }

        OrgMember orgMember = new OrgMember();
        orgMember.setRealName(oldMember.getRealName());
        orgMember.setId(oldMember.getId());
        orgMember.setPassword(oldMember.getPassword());
        orgMember.setSex(oldMember.getSex());
        orgMember.setCreateTime(new Date());
        orgMember.setOpenId(oldMember.getOpenid());
        orgMember.setUsername(oldMember.getRealName());
        orgMember.setMobile(oldMember.getMobile());

        String token = JwtUtil.sign(oldMember.getId(), oldMember.getName(), oldMember.getOpenid());
        SessionUser sessionUser = SessionUser.create(token, orgMember, null, null);
        redisManager.hSessionUser(token, sessionUser);

        CheckCodeRes checkCodeRes = new CheckCodeRes();
        checkCodeRes.setToken(token);
        checkCodeRes.setCheckstate(CheckCodeEnum.CheckState.SUCCESS.getKey());
        return new ResultBean<>(checkCodeRes);
    }


    @GetMapping("deleteByMobile")
    @ApiOperation(value = "删除用户mobile")
    @ControllerMonitor(description = "删除用户", operType = 1)
    public ResultBean<CheckCodeRes> deleteByMobile(@RequestParam String mobile) {
        memberService.deleteByMobile(mobile);
        return new ResultBean<>();
    }

    @GetMapping("deleteByOpenId")
    @ApiOperation(value = "删除用户Openid")
    @ControllerMonitor(description = "删除用户", operType = 1)
    public ResultBean<CheckCodeRes> deleteByOpenId(@RequestParam String openId) {
        memberService.deleteByOpenId(openId);
        return new ResultBean<>();
    }

    @GetMapping("deleteTokenSession")
    @ApiOperation(value = "删除redis")
    @ControllerMonitor(description = "删除redis", operType = 1)
    public ResultBean<CheckCodeRes> deleteTokenSession(@RequestParam String token) {
        redisManager.delSessionUser(token);
        return new ResultBean<>();
    }
}
