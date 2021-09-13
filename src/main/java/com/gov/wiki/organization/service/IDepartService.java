/**
 * @Title: IDepartService.java
 * @Package com.gov.wiki.organization.service
 * @Description: 部门管理处理接口
 * @author cys
 * @date 2019年11月1日
 * @version V1.0
 */
package com.gov.wiki.organization.service;

import java.util.List;

import javax.transaction.Transactional;

import org.aspectj.weaver.ast.Or;
import org.springframework.web.bind.annotation.RequestBody;

import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.entity.system.OrgDepart;
import com.gov.wiki.common.entity.system.OrgMember;
import com.gov.wiki.common.service.IBaseService;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.organization.req.DepartReq;
import com.gov.wiki.organization.req.query.DepartQuery;

public interface IDepartService extends IBaseService<OrgDepart, String> {
	/**
	 * @Title: saveOrUpdateDepart 
	 * @Description: 新增或者修改部门
	 * @param 设定文件 
	 * @return ResultBean<OrgDepart>    返回类型 
	 * @throws
	 */
	@Transactional
	ResultBean<OrgDepart> saveOrUpdateDepart(@RequestBody ReqBean<DepartReq> bean);

	/**
	 * @Title: findByParams 
	 * @Description: 根据查询条件查询部门信息
	 * @param 设定文件 
	 * @return List<OrgDepart>    返回类型 
	 * @throws
	 */
	List<OrgDepart> findByParams(DepartQuery dq);
	
	/**
	 * @Title: checkDepartNameExist 
	 * @Description: 判断部门名称是否存在
	 * @param 设定文件 
	 * @return boolean    返回类型 
	 * @throws
	 */
	boolean checkDepartNameExist(OrgDepart depart);
	
	/**
	 * @Title: getDepartIdsByPath 
	 * @Description: 获取部门主键列表
	 * @param 设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	List<String> getDepartIdsByPath(String path);
	
	/**
	 * @Title: batchDelByIds 
	 * @Description: 根据部门主键批量删除部门信息
	 * @param 设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	void batchDelByIds(List<String> ids);
	
	/**
	 * @Title: getMaxSeq 
	 * @Description: 获取最大序号
	 * @param 设定文件 
	 * @return int    返回类型 
	 * @throws
	 */
	int getMaxSeq();
	
	/**
	 * @Title: queryDepartPath 
	 * @Description: 获取部门路径
	 * @param 设定文件 
	 * @return String    返回类型 
	 * @throws
	 */
	String queryDepartPath(String departId);
	
	/**
	 * @Title: queryChildDepartByPath 
	 * @Description: 获取子部门
	 * @param 设定文件 
	 * @return List<OrgDepart>    返回类型 
	 * @throws
	 */
	List<OrgDepart> queryChildDepartByPath(String path);
	
	List<String> queryChildUsers(OrgMember member);
	/**
	 * 查询当前用户子部门的用户ID
	 * @param i
	 * 		0 包含当前部门
	 * 		1 不包含当前部门
	 * 		2 不包含当前部门，但包含当前用户
	 * @return
	 */
	List<String> queryChildUsers(OrgMember member,int i);

	/**
	 * 查询当前用户子部门的用户信息
	 * @param i
	 * 		0 包含当前部门
	 * 		1 不包含当前部门
	 * 		2 不包含当前部门，但包含当前用户
	 * @return
	 */
	List<OrgMember> queryChildUsersInfo(OrgMember member, int i);
	
	/**
	 * @Title: queryDepartRangeByUser 
	 * @Description: 根据用户查询用户管辖部门长编码
	 * @param 设定文件 
	 * @return List<String>    返回类型 
	 * @throws
	 */
	List<String> queryDepartRangeByUser(String userId);
	
	/**
	 * @Title: queryChildListByRanges 
	 * @Description: 根据分管部门查询子部门
	 * @param 设定文件 
	 * @return ResultBean<List<OrgDepart>>    返回类型 
	 * @throws
	 */
	ResultBean<List<OrgDepart>> queryChildListByRanges(ReqBean<List<String>> bean);

	List<String> queryChildUserByDepartIds(List<String> ids);

	List<Object[]> findfirstlevel(String companyid);

	List<Object[]> findsecondlevel(String parentid);

	List<Object[]> groupParentId(String companyid);

	List<Object[]> findIdByParentId(String parentid,String companyid);

	List<OrgDepart> findByCompanyId(String companyid);

	List<OrgDepart> findIdByParentId(String parentid);

	OrgDepart findByDirector(String director);

	OrgDepart findByInChargeLeaderLike(String incharge);
}