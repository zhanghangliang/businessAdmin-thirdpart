package com.gov.wiki.organization.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.entity.system.OrgDepart;
import com.gov.wiki.common.entity.system.OrgMember;
import com.gov.wiki.common.entity.system.PrivMemberRole;
import com.gov.wiki.common.entity.system.PrivResource;
import com.gov.wiki.common.entity.wechat.WxMember;
import com.gov.wiki.common.redis.RedisManager;
import com.gov.wiki.common.res.WxMemberBasicRes;
import com.gov.wiki.common.utils.BeanUtils;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.Constants;
import com.gov.wiki.common.utils.JwtUtil;
import com.gov.wiki.common.utils.MyHttpUtils;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.SessionUser;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.config.WxConfig;
import com.gov.wiki.organization.req.LoginReq;
import com.gov.wiki.organization.res.LoginRes;
import com.gov.wiki.organization.service.IDepartService;
import com.gov.wiki.organization.service.IMemberRoleService;
import com.gov.wiki.organization.service.IMemberService;
import com.gov.wiki.organization.service.IResourceService;
import com.gov.wiki.organization.service.IRoleResourceService;
import com.gov.wiki.system.service.IFileService;
import com.gov.wiki.wechat.service.WxMemberService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;

/**
 * @ClassName: LoginController
 * @Description: ?????????????????????
 * @author cys
 * @date 2019???12???10???
 */
@RestController
@Api(tags = "????????????")
@Slf4j
public class LoginController {

	/**
	 * ??????redisManager
	 */
	@Autowired
	private RedisManager redisManager;
	/**
	 * ??????memberService
	 */
	@Autowired
	private IMemberService memberService;
	/**
	 * ??????departService
	 */
	@Autowired
	private IDepartService departService;

	@Autowired
	private IFileService fileService;

	@Autowired
	private IResourceService resourceService;

	@Autowired
	private IMemberRoleService memberRoleService;
	@Autowired
	private IRoleResourceService roleResourceService;

	@Autowired
	private WxMemberService wxmemberService;

	@Autowired
	private WxConfig wxConfig;
	@Autowired
	private WxMpService wxMpService;
	private Map<String, String> map = new HashMap<String, String>();

	/**
	 * ??????????????????
	 */
	public static final boolean needUserInfo = true;
	/**
	 * @Title: login
	 * @Description: ????????????
	 * @param req
	 * @return ResultBean<String> ????????????
	 * @throws
	 */
	@PostMapping(value = "/login")
	@ApiOperation(value = "????????????")
	@ControllerMonitor(description = "????????????", operType = 5)
	public ResultBean<String> login(@RequestBody ReqBean<LoginReq> bean){
		LoginReq req = bean.getBody();
		LoginRes res = memberService.userLogin(req);
		OrgMember member = res.getMember();
		String token = JwtUtil.sign(member.getId(), member.getUsername(), "");
		List<String> paths = new ArrayList<String>();
		if(member.getAccountType() != null && member.getAccountType() == 1) {// ?????????
			paths.add("0000");
		}else {
			List<String> pathResult = departService.queryDepartRangeByUser(member.getId());
			if(pathResult != null) {
				paths.addAll(pathResult);
			}
		}
		OrgDepart byDirector = departService.findByDirector(member.getId());
		OrgDepart byInChargeLeaderLike = departService.findByInChargeLeaderLike(member.getId());
		List<String> creater=new ArrayList<>();
		if (byDirector != null) {
			creater.add(member.getId());
			String[] split = byDirector.getInChargeLeader().split(",");
			for (String s : split) {
				creater.add(s);
			}
			creater.add(byDirector.getAdministrator());
		}
		else if(byInChargeLeaderLike!=null){
			creater.add(member.getId());
			creater.add(byInChargeLeaderLike.getAdministrator());
		}
		else{
			creater.add(member.getId());
		}
		SessionUser sessionUser = SessionUser.create(token, member, res.getTreeResources(),res.getRoles());
		sessionUser.setDepartRange(paths);
		sessionUser.setCreater(creater);
		redisManager.hSessionUser(token, sessionUser);
		return new ResultBean<String>(token);
	}

	/**
	 * ??????????????????
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/getUserInfo")
	@ApiOperation(value = "??????????????????")
	@ControllerMonitor(description = "??????????????????")
	public ResultBean<SessionUser> getUserInfo(HttpServletRequest request){
		String token = request.getHeader(Constants.TOKEN);
		SessionUser user = (SessionUser) redisManager.getSessionUser(token);
		List<PrivMemberRole> byMemberId = memberRoleService.findByMemberId(user.getCreater());
		List<String> roleids=new ArrayList<>();
		for (PrivMemberRole privMemberRole : byMemberId) {
			roleids.add(privMemberRole.getRoleId());
		}
		List<String> resourceids = roleResourceService.findByRoleIds(roleids);

		List<PrivResource> treeResources = user.getTreeResources();
		HashMap<String,String> icon=new HashMap<>();
		for (PrivResource treeResource : treeResources) {
			icon.put(treeResource.getName(),treeResource.getIcon());
		}
		for(int i=0;i<treeResources.size();i++){
			treeResources.get(i).setChildList(resourceService.findByParentId(treeResources.get(i).getId(),resourceids));
		}
		user.setIcon(icon);
		return new ResultBean<SessionUser>(user);
	}



	/**
	 * @Title: logout
	 * @Description: ????????????
	 * @param request
	 * @return ResultBean ????????????
	 * @throws
	 */
	@PostMapping(value = "/logout")
	@ApiOperation(value = "????????????")
	@ControllerMonitor(description = "????????????", operType = 7)
	public ResultBean logout(HttpServletRequest request) {
		String token = request.getHeader(Constants.TOKEN);
		if(StringUtils.isNotBlank(token)) {
			redisManager.delSessionUser(token);
		}
		return new ResultBean();
	}


	@PostMapping(value = "/wxlogin")
	@ApiOperation(value = "??????????????????")
	@ControllerMonitor(description = "??????????????????", operType = 5)
	public ResultBean<String> wxlogin(@RequestBody ReqBean<LoginReq> bean) {
		WxMember wxMember = wxmemberService.externalLogin(bean.getBody());
		String token = JwtUtil.sign(wxMember.getId(), wxMember.getMobile(), "");
		OrgMember orgMember = new OrgMember();
		orgMember.setId(wxMember.getId());
		orgMember.setPassword(wxMember.getPassword());
		orgMember.setRealName(wxMember.getRealName());
		if(StringUtils.isNotBlank(wxMember.getRealName())) {
			orgMember.setRealName(wxMember.getRealName());
		}
		orgMember.setSex(wxMember.getSex());
		orgMember.setMobile(wxMember.getMobile());
		orgMember.setCreateTime(wxMember.getCreateTime());
		orgMember.setOpenId(wxMember.getOpenid());
		orgMember.setHeadimgurl(wxMember.getHeadimgurl());
		SessionUser sessionUser = SessionUser.create(token, orgMember, null,null);
		redisManager.hSessionUser(token, sessionUser);
		return new ResultBean<>(token);
	}

	@PostMapping(value = "/wxgetUserInfo")
	@ApiOperation(value = "????????????????????????")
	@ControllerMonitor(description = "????????????????????????")
	public ResultBean<SessionUser> wxgetUserInfo(HttpServletRequest request){
		String token = request.getHeader(Constants.TOKEN);
		SessionUser user = (SessionUser) redisManager.getSessionUser(token);
		return new ResultBean<>(user);
	}
	@PostMapping(value = "/wxgetBasicUserInfo")
	@ApiOperation(value = "????????????????????????")
	@ControllerMonitor(description = "????????????????????????")
	public ResultBean<WxMemberBasicRes> wxgetBasicUserInfo(HttpServletRequest request){
		String token = request.getHeader(Constants.TOKEN);
		SessionUser user = (SessionUser) redisManager.getSessionUser(token);
		WxMember wxMember = wxmemberService.findById(user.getMember().getId());
		WxMemberBasicRes res = BeanUtils.copyProperties(wxMember, WxMemberBasicRes.class);
		res.setRealName(res.getRealName().charAt(0) + "*");
		res.setIdCard(res.getIdCard().substring(0, 6) + "********"+res.getIdCard().substring(res.getIdCard().length()-4));
		return new ResultBean<>(res);
	}





	@GetMapping(value = "/codelogin")
	@ApiOperation(value = "??????????????????")
	@ControllerMonitor(description = "??????????????????", operType = 1)
	public void codelogin(@RequestParam(value = "code", required = true) String code,String state
			,HttpServletResponse response) throws IOException {
		CheckUtil.notEmpty(code,ResultCode.COMMON_ERROR,"code??????");
		CheckUtil.notEmpty(state,ResultCode.COMMON_ERROR,"state??????");
		// ??????Code??????Openid
		String openidUrl = wxConfig.getOpenidurl() + "appid=" + wxConfig.getAppid() + "&secret=" + wxConfig.getAppsecret() + "&code=" + code + "&grant_type=authorization_code";
		String openidMsg = MyHttpUtils.doPost(openidUrl, "");
		// ??????????????????
		JSONObject result = JSON.parseObject(openidMsg);
		CheckUtil.notNull(result, ResultCode.COMMON_ERROR,"????????????????????????");
		// ??????????????????????????????
		String access_token = result.getString("access_token");
		// ??????????????????
		String openid = result.getString("openid");
		JSONObject userInfo = getUserInfo(access_token, openid);
		CheckUtil.notNull(userInfo, ResultCode.COMMON_ERROR,"??????????????????????????????");
		WxMember wxMember = wxmemberService.findByOpenId(userInfo.getString("openid"));
		log.info(userInfo.toJSONString());
		if(wxMember==null){
			wxMember = new WxMember();
			wxMember.setName(userInfo.getString("nickname"));
			wxMember.setSex(Integer.valueOf(userInfo.getString("sex")));
			wxMember.setHeadimgurl(userInfo.getString("headimgurl"));
			wxMember.setOpenid(userInfo.getString("openid"));
			wxMember = wxmemberService.saveOrUpdate(wxMember);
		}
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
		sessionUser = (SessionUser) redisManager.getSessionUser(token);
		String redirectUrl = map.get(state);
		redirectUrl = appendToken(redirectUrl,token);
		map.remove(state);
		log.info("??????url???{}",redirectUrl);
		response.sendRedirect(redirectUrl);
	}

	private JSONObject getUserInfo(String access_token, String openid) {
		if(!needUserInfo) {
			JSONObject json = new JSONObject();
			json.put("nickname", "????????????");
			json.put("sex", "0");
			json.put("openid", openid);
			return json;
		}
		// ??????????????????
		String userInfoUrl = wxConfig.getUserinfourl() + "access_token=" + access_token + "&openid=" + openid + "&lang=zh_CN";
		String userInfoMsg = MyHttpUtils.doPost(userInfoUrl, "");
		// ??????????????????
		JSONObject userInfo = JSON.parseObject(userInfoMsg);
		return userInfo;
	}

	private String appendToken(String redirectUrl,String token) {
		if(StringUtils.isBlank(redirectUrl))return "";
		if(redirectUrl.indexOf("?") >0) {
			return redirectUrl +="&token="+token;
		}
		return redirectUrl += "?token="+token;
	}

	@PostMapping(value = "/getcodeUrl")
	@ApiOperation(value = "??????codeUrl")
	@ControllerMonitor(description = "??????codeUrl", operType = 1)
	public ResultBean<String> getcode(@RequestBody ReqBean<String> bean) {
		String stat = Math.random()+"";
		String url = bean.getBody();
		CheckUtil.notEmpty(url, ResultCode.PARAM_NULL, "body");
		map.put(stat, url);
		url = wxMpService.oauth2buildAuthorizationUrl(wxConfig.getRedirecturi(), needUserInfo ? "snsapi_userinfo" : "snsapi_base", stat);
		log.info("url:{}",url);
		return new ResultBean<>(url);
	}
	/**
	 * ?????????????????????
	 * ????????????
	 * @param request
	 * @param response
	 * @param echostr
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @throws Exception
	 */
	@RequestMapping(value = "/echo",method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public void echo(HttpServletRequest request, HttpServletResponse response, String echostr, String signature,
			String timestamp, String nonce) throws Exception {
		log.info("???????????????echoStr:{},singture:{},time:{},nonce:{}",echostr,signature,timestamp,nonce);
		PrintWriter out = response.getWriter();
		if (wxMpService.checkSignature(timestamp, nonce, signature)) {
			log.info("????????????????????????");
			if (StringUtils.isNotBlank(echostr)) {
				//????????????
				out.print(echostr);
				return;
			} else {
				//????????????
				String encryptType = StringUtils.isBlank(request.getParameter("encrypt_type")) ? "raw"
						: request.getParameter("encrypt_type");
				WxMpXmlMessage inMessage = null;
				log.info("???????????????{}",encryptType);
				if ("raw".equals(encryptType)) {
					// ?????????????????????
					inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
				} else if ("aes".equals(encryptType)) {
					// ???aes???????????????
					String msgSignature = request.getParameter("msg_signature");
					inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(), wxMpService.getWxMpConfigStorage(),
							timestamp, nonce, msgSignature);
				} else {
					response.getWriter().println("???????????????????????????");
					return;
				}
				if (inMessage != null) {
					//???????????????????????????
					log.info("?????????{}",inMessage);
				}

			}

		} else{
			log.info("????????????????????????");
			response.getWriter().println("????????????");
		}
		out.close();
		out = null;
	}

	@GetMapping(value = "/getechostr")
	@ApiOperation(value = "??????echostr")
	@ControllerMonitor(description = "??????echostr", operType = 1)
	public String getechostr(String signature,String timestamp,String nonce,String echostr) {
		wxMpService.checkSignature(timestamp,nonce,signature);
		return echostr;
	}
}
