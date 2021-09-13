/**
 * @Title: SysConfigFeignImpl.java
 * @Package com.gov.wiki.organization.controller
 * @Description: 系统信息配置管理控制器
 * @author cys
 * @date 2019年12月8日 下午4:26:18
 * @version V1.0
 */
package com.gov.wiki.organization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.check.CheckClass;
import com.gov.wiki.common.entity.system.SysConfig;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.system.req.SysConfigReq;
import com.gov.wiki.system.service.ISysConfigService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/sys-config")
@RestController
@Api(tags = "系统信息管理")
public class SysConfigController {

	/**
	 * 注入sysConfigService
	 */
	@Autowired
	private ISysConfigService sysConfigService;

	/**
	 * @Title: saveOrUpdate
	 * @Description: 新增或者修改系统配置信息
	 * @param 设定文件
	 * @return ResultBean<SysConfig>    返回类型
	 * @throws
	 */
	@PostMapping("/save-update")
	@ApiOperation(value = "新增或者修改系统配置信息")
	@ControllerMonitor(description = "新增或者修改系统配置信息", operType = 2)
	public ResultBean<SysConfig> saveOrUpdate(@RequestBody ReqBean<SysConfigReq> bean) {
		SysConfigReq req = bean.getBody();
		SysConfig config = req == null?null:req.toEntity();
		CheckUtil.notNull(config, ResultCode.COMMON_ERROR, "参数不存在！");
		CheckClass.check(config);
		SysConfig old = sysConfigService.findOneConfig(config.getId());
		if(old != null) {
			config.setId(old.getId());
		}
		return sysConfigService.saveOrUpdateEntity(config);
	}

	/**
	 * @Title: delById
	 * @Description: 根据ID删除系统配置信息
	 * @param 设定文件
	 * @return ResultBean    返回类型
	 * @throws
	 */
	@PostMapping("/delById")
	@ApiOperation(value = "根据ID删除系统配置信息")
	@ControllerMonitor(description = "根据ID删除系统配置信息", operType = 4)
	public ResultBean delById(@RequestBody ReqBean<String> bean) {
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.COMMON_ERROR, "参数不存在！");
		sysConfigService.deleteById(id);
		return new ResultBean();
	}

	/**
	 * @Title: querySysConfigInfo
	 * @Description: 查询系统配置信息
	 * @param 设定文件
	 * @return ResultBean<SysConfig>    返回类型
	 * @throws
	 */
	@PostMapping("/querySysConfigInfo")
	@ApiOperation(value = "查询系统配置信息")
	@ControllerMonitor(description = "查询系统配置信息")
	public ResultBean<SysConfig> querySysConfigInfo() {
		SysConfig config = sysConfigService.findOneConfig("");
		return new ResultBean(config);
	}
	/**
	 * 根据事项类型获取编码
	 * @return
	 */
	@PostMapping("/getCodeByMaterial")
	@ApiOperation(value = "根据事项类型查询编码")
	@ControllerMonitor(description = "根据事项类型查询编码")
	public ResultBean<String> getCodeByMaterial(@RequestBody ReqBean<Integer> bean) {
		CheckUtil.notNull(bean, ResultCode.PARAM_NULL, "参数");
		return new ResultBean<String>(sysConfigService.getCodeByMaterial(bean.getBody()));
	}
}