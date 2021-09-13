/**
 * @Title: ILogService.java 
 * @Package com.gov.wiki.system.service
 * @Description: 日志处理接口
 * @author cys 
 * @date 2019年11月5日 下午9:07:47 
 * @version V1.0 
 */
package com.gov.wiki.system.service;

import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.entity.system.SysLog;
import com.gov.wiki.common.service.IBaseService;
import com.gov.wiki.common.utils.PageInfo;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.system.req.query.LogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

public interface ILogService extends IBaseService<SysLog, String>{

	/**
	 * @Title: pageList 
	 * @Description: 分页查询日志信息
	 * @param 设定文件 
	 * @return ResultBean<PageInfo>    返回类型 
	 * @throws
	 */
	ResultBean<PageInfo> pageList(ReqBean<LogQuery> bean);
	
	/**
	 * @Title: queryLastLoginLog 
	 * @Description: 获取人员最后登陆信息
	 * @param 设定文件 
	 * @return SysLog    返回类型 
	 * @throws
	 */
	SysLog queryLastLoginLog();

	Page<SysLog> findAll(Specification specification, PageRequest pageRequest);
}
