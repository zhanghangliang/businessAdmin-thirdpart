/**
 * @Title: IDictService.java 
 * @Package com.gov.wiki.system.service
 * @Description: 数据字典管理service接口
 * @author cys 
 * @date 2019年11月5日 下午9:07:01 
 * @version V1.0 
 */
package com.gov.wiki.system.service;

import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.entity.system.SysDict;
import com.gov.wiki.common.service.IBaseService;
import com.gov.wiki.common.utils.PageInfo;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.system.req.query.DictQuery;

public interface IDictService extends IBaseService<SysDict, String>{

	/**
	 * 通过字段code获取字典
	 * @param code
	 * @return
	 */
	SysDict getByCode(String code);
	
	/**
	 * @Title: existDictCode 
	 * @Description: 判断数据字典名称是否存在
	 * @param 设定文件 
	 * @return boolean    返回类型 
	 * @throws
	 */
	boolean existDictCode(SysDict dict);
	
	/**
	 * @Title: queryDictList 
	 * @Description: 查询数据字典信息
	 * @param 设定文件 
	 * @return ResultBean<List<SysDict>>    返回类型 
	 * @throws
	 */
	ResultBean<List<SysDict>> queryDictList(@RequestBody ReqBean<DictQuery> bean);
	
	/**
	 * @Title: pageDictList 
	 * @Description: 分页查询数据字典信息
	 * @param 设定文件 
	 * @return ResultBean<PageInfo>    返回类型 
	 * @throws
	 */
	ResultBean<PageInfo> pageDictList(@RequestBody ReqBean<DictQuery> bean);
}