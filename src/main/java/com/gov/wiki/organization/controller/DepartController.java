/**
 * @Title: DepartController.java 
 * @Package com.insolu.spm.organization.feign.impl 
 * @Description: 部门管理控制器
 * @author cys 
 * @date 2019年11月5日 下午9:57:10 
 * @version V1.0 
 */
package com.gov.wiki.organization.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.gov.wiki.common.entity.MemberInfoReq;
import com.gov.wiki.common.entity.system.OrgCompany;
import com.gov.wiki.organization.service.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.entity.system.OrgDepart;
import com.gov.wiki.common.entity.system.OrgMember;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.organization.req.DepartReq;
import com.gov.wiki.organization.req.query.DepartQuery;
import com.gov.wiki.organization.service.IDepartService;
import com.gov.wiki.organization.service.IMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/depart")
@RestController
@Api(tags = "部门管理")
public class DepartController {
	
	/**
	 * 注入departService
	 */
	@Autowired
	private IDepartService departService;
	
	/**
	 * 注入memberService
	 */
	@Autowired
	private IMemberService memberService;

	/**
	 * @Title: saveOrUpdate 
	 * @Description: 新增或者修改部门信息
	 * @param 设定文件 
	 * @return ResultBean<OrgDepart>    返回类型 
	 * @throws
	 */
	@PostMapping("/save-update")
	@ApiOperation(value = "新增或者修改部门信息")
	@ControllerMonitor(description = "新增或者修改部门信息", operType = 2)
	public ResultBean<OrgDepart> saveOrUpdate(@RequestBody ReqBean<DepartReq> bean) {
		return departService.saveOrUpdateDepart(bean);
	}

	/**
	 * @Title: delById 
	 * @Description: 根据ID删除部门信息
	 * @param 设定文件 
	 * @return ResultBean    返回类型 
	 * @throws
	 */
	@PostMapping("/delById")
	@ApiOperation(value = "根据ID删除部门信息")
	@ControllerMonitor(description = "根据ID删除部门信息", operType = 4)
	public ResultBean delById(@RequestBody ReqBean<String> bean) {
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.PARAM_NULL, "ID");
		List<MemberInfoReq> byDepartId = memberService.findByDepartId(id);
		List<OrgDepart> idByParentId = departService.findIdByParentId(id);
		if(idByParentId.size()!=0) return ResultBean.error(-1,"部门拥有下级部门,无法删除");
		if(byDepartId.size()!=0) return ResultBean.error(-1,"部门下有人员，不允许删除！");
		departService.deleteById(id);

//		OrgDepart depart = departService.findById(id);
//		if(depart != null) {
//			List<OrgMember> mList = memberService.queryMemberByDepartPath(depart.getPath());
//			CheckUtil.check(mList == null || mList.isEmpty(), ResultCode.COMMON_ERROR, "部门下有人员，不允许删除！");
//			List<String> ids = departService.getDepartIdsByPath(depart.getPath());
//			if(ids == null || ids.isEmpty()) {
//				ids = new ArrayList<String>();
//				ids.add(depart.getId());
//			}
//			if(ids != null && !ids.isEmpty()) {
//				departService.batchDelByIds(ids);
//			}
//		}
		return new ResultBean();
	}

	/**
	 * @Title: findById 
	 * @Description: 根据ID查询部门信息
	 * @param 设定文件 
	 * @return ResultBean<OrgDepart>    返回类型 
	 * @throws
	 */
	@PostMapping("/findById")
	@ApiOperation(value = "根据ID查询部门信息")
	@ControllerMonitor(description = "根据ID查询部门信息")
	public ResultBean<OrgDepart> findById(@RequestBody ReqBean<String> bean) {
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.PARAM_NULL, "ID");
		OrgDepart depart = departService.findById(id);
		OrgMember byId = memberService.findById(depart.getDirector());
		List<String> inchargeleader=Arrays.asList(depart.getInChargeLeader().split(","));
		List<MemberInfoReq> inchargename =memberService.findNameByIds(inchargeleader);
		OrgMember byId1 = memberService.findById((depart.getAdministrator()));
		if(byId!=null){
			depart.setDirector(byId.getId());
			depart.setDirectorName(byId.getRealName());
		}
		if(byId1!=null){
			depart.setAdministrator(byId1.getId());
			depart.setAdministratorName(byId1.getRealName());
		}
		depart.setInChargeLeaderList(inchargename);
		return new ResultBean(depart);
	}

	/**
	 * @Title: findListByParams 
	 * @Description: 根据父节点查询部门信息
	 * @param 设定文件 
	 * @return ResultBean<List<OrgDepart>>    返回类型 
	 * @throws
	 */
	@PostMapping("/findListByParams")
	@ApiOperation(value = "根据父节点查询部门信息")
	@ControllerMonitor(description = "根据父节点查询部门信息")
	public ResultBean<List<OrgDepart>> findListByParams(@RequestBody ReqBean<DepartQuery> bean) {
		DepartQuery dq = bean.getBody();
		return new ResultBean(departService.findByParams(dq));
	}

	/**
	 * @Title: queryChildList 
	 * @Description: 查询子部门
	 * @param 设定文件 
	 * @return ResultBean<OrgDepart>    返回类型 
	 * @throws
	 */
	@PostMapping("/queryChildList")
	@ApiOperation(value = "查询子部门")
	@ControllerMonitor(description = "查询子部门")
	public ResultBean<List<OrgDepart>> queryChildList(@RequestBody ReqBean<String> bean) {
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.PARAM_NULL, "ID");
		OrgDepart d = departService.findById(id);
		if(d == null || StringUtils.isBlank(d.getPath())) {
			return new ResultBean<List<OrgDepart>>();
		}
		return new ResultBean<List<OrgDepart>>(departService.queryChildDepartByPath(d.getPath()));
	}

	/**
	 * @Title: queryChildListByRanges 
	 * @Description: 根据分管部门查询子部门
	 * @param 设定文件 
	 * @return ResultBean<List<OrgDepart>>    返回类型 
	 * @throws
	 */
	@PostMapping("/queryChildListByRanges")
	@ApiOperation(value = "根据分管部门查询子部门")
	@ControllerMonitor(description = "根据分管部门查询子部门")
	public ResultBean<List<OrgDepart>> queryChildListByRanges(@RequestBody ReqBean<List<String>> bean) {
		return departService.queryChildListByRanges(bean);
	}

	@PostMapping("/catalog")
	@ApiOperation(value = "获取部门目录")
	@ControllerMonitor(description = "获取部门目录")
	public ResultBean<List<HashMap<String,Object>>> catalog(@RequestBody ReqBean<List<String>> bean) {
		List<String> companyids=bean.getBody();
		List<HashMap<String,Object>> ans=new ArrayList<>();
		for (String companyid : companyids) {
			HashMap<String,Object> temp=new HashMap<>();
			List<Object[]> topid = departService.findIdByParentId("-1",companyid);
			temp.put("0",topid);
			List<Object[]> upMatterId = departService.groupParentId(companyid);
			for (Object[] s : upMatterId) {
				List<Object[]> idByUpmatterId = departService.findIdByParentId(s[0].toString(),companyid);
				temp.put(s[0].toString(),idByUpmatterId);
			}
			ans.add(temp);
		}
		return new ResultBean<>(ans);
	}
}