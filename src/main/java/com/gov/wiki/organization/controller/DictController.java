package com.gov.wiki.organization.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.check.CheckClass;
import com.gov.wiki.common.entity.system.SysDict;
import com.gov.wiki.common.entity.system.SysDictItem;
import com.gov.wiki.common.enums.StatusEnum;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.PageInfo;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.system.req.DictItemReq;
import com.gov.wiki.system.req.DictReq;
import com.gov.wiki.system.req.query.DictQuery;
import com.gov.wiki.system.service.IDictItemService;
import com.gov.wiki.system.service.IDictService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @ClassName: DictController
 * @Description: 数据字典管理控制器
 * @author cys
 * @date 2019年11月12日 下午3:58:51
 */
@RequestMapping("/dict")
@RestController
@Api(tags = "数据字典管理")
public class DictController{

	@Autowired
	private IDictService dictService;

	@Autowired
	private IDictItemService dictItemService;

	/**
	 * @Title: saveOrUpdateDict
	 * @Description: 新增或者修改数据字典
	 * @param 设定文件
	 * @return ResultBean<SysDict>    返回类型
	 * @throws
	 */
	@PostMapping("/dict-save-update")
	@ApiOperation(value = "新增或者修改数据字典")
	@ControllerMonitor(description = "新增或者修改数据字典", operType = 8)
	public ResultBean<SysDict> saveOrUpdateDict(@RequestBody ReqBean<DictReq> bean) {
		DictReq req = bean.getBody();
		SysDict dict = req == null?null:req.toEntity();
		CheckUtil.notNull(dict, ResultCode.COMMON_ERROR, "参数不存在！");
		CheckClass.check(dict);
		if(StringUtils.isNotBlank(dict.getId())) {
			SysDict dict2 = dictService.findById(dict.getId());
			if(dict2 != null) {//修改
				dict2.setDescription(dict.getDescription());
				dict2.setDictName(dict.getDictName());
				dict = dict2;
			} else {//新增
				CheckUtil.check(!dictService.existDictCode(dict), ResultCode.COMMON_ERROR, "数据字典编码重复！");
			}
		} else {//新增
			CheckUtil.check(!dictService.existDictCode(dict), ResultCode.COMMON_ERROR, "数据字典编码重复！");
		}
		return dictService.saveOrUpdateEntity(dict);
	}

	/**
	 * @Title: delDictById
	 * @Description: 根据ID删除数据字典信息
	 * @param 设定文件
	 * @return ResultBean    返回类型
	 * @throws
	 */
	@PostMapping("/delDictById")
	@ApiOperation(value = "根据ID删除数据字典信息")
	@ControllerMonitor(description = "根据ID删除数据字典信息", operType = 4)
	public ResultBean delDictById(@RequestBody ReqBean<String> bean) {
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.COMMON_ERROR, "参数不存在！");
		SysDict dict = dictService.findById(id);
		if(dict != null) {
			dict.setDelFlag(true);
			dictService.saveOrUpdate(dict);
		}
		return new ResultBean();
	}

	/**
	 * @Title: batchDelDict
	 * @Description: 批量删除数据字典信息
	 * @param 设定文件
	 * @return ResultBean    返回类型
	 * @throws
	 */
	@PostMapping("/batchDelDict")
	@ApiOperation(value = "批量删除数据字典信息")
	@ControllerMonitor(description = "批量删除数据字典信息", operType = 4)
	public ResultBean batchDelDict(@RequestBody ReqBean<List<String>> bean) {
		List<String> ids = bean.getBody();
		CheckUtil.notNull(ids, ResultCode.COMMON_ERROR, "参数不存在！");
		List<SysDict> dictList = dictService.findByIds(ids);
		if(dictList != null && !dictList.isEmpty()) {
			for(SysDict d:dictList) {
				d.setDelFlag(true);
			}
			dictService.saveAll(dictList);
		}
		return new ResultBean();
	}

	/**
	 * @Title: queryDictList
	 * @Description: 根据条件查询数据字典信息
	 * @param 设定文件
	 * @return ResultBean<List<SysDict>>    返回类型
	 * @throws
	 */
	@PostMapping("/queryDictList")
	@ApiOperation(value = "根据条件查询数据字典信息")
	@ControllerMonitor(description = "根据条件查询数据字典信息")
	public ResultBean<List<SysDict>> queryDictList(@RequestBody ReqBean<DictQuery> bean) {
		return dictService.queryDictList(bean);
	}

	/**
	 * @Title: pageDictList
	 * @Description: 分页查询数据字典信息
	 * @param 设定文件
	 * @return ResultBean<PageInfo>    返回类型
	 * @throws
	 */
	@PostMapping("/pageDictList")
	@ApiOperation(value = "分页查询数据字典信息")
	@ControllerMonitor(description = "分页查询数据字典信息")
	public ResultBean<PageInfo> pageDictList(@RequestBody ReqBean<DictQuery> bean) {
		return dictService.pageDictList(bean);
	}

	/**
	 * @Title: findDictById
	 * @Description: 根据ID获取数据字典信息
	 * @param 设定文件
	 * @return ResultBean<SysDict>    返回类型
	 * @throws
	 */
	@PostMapping("/findDictById")
	@ApiOperation(value = "根据ID获取数据字典信息")
	@ControllerMonitor(description = "根据ID获取数据字典信息")
	public ResultBean<SysDict> findDictById(@RequestBody ReqBean<String> bean) {
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.COMMON_ERROR, "参数不存在！");
		SysDict dict = dictService.findById(id);
		return new ResultBean(dict);
	}

	/**
	 * @Title: findDictByCode
	 * @Description: 根据编码获取数据字典信息
	 * @param 设定文件
	 * @return ResultBean<SysDict>    返回类型
	 * @throws
	 */
	@PostMapping("/findDictByCode")
	@ApiOperation(value = "根据编码获取数据字典信息")
	@ControllerMonitor(description = "根据编码获取数据字典信息")
	public ResultBean<SysDict> findDictByCode(@RequestBody ReqBean<String> bean) {
		String code = bean.getBody();
		CheckUtil.notEmpty(code, ResultCode.COMMON_ERROR, "参数不存在！");
		SysDict dict = dictService.getByCode(code);
		return new ResultBean(dict);
	}

	/**
	 * @Title: saveOrUpdateItem
	 * @Description: 新增或者修改数据字典项
	 * @param 设定文件
	 * @return ResultBean<SysDictItem>    返回类型
	 * @throws
	 */
	@PostMapping("/item-save-update")
	@ApiOperation(value = "新增或者修改数据字典项")
	@ControllerMonitor(description = "新增或者修改数据字典项", operType = 8)
	public ResultBean<SysDictItem> saveOrUpdateItem(@RequestBody ReqBean<DictItemReq> bean) {
		DictItemReq req = bean.getBody();
		SysDictItem dictItem = req == null?null:req.toEntity();
		CheckUtil.notNull(dictItem, ResultCode.COMMON_ERROR, "参数不存在！");
		CheckClass.check(dictItem);
		return dictItemService.saveOrUpdateEntity(dictItem);
	}

	/**
	 * @Title: findDictItemById
	 * @Description: 根据ID查询数据字典项
	 * @param 设定文件
	 * @return ResultBean<SysDictItem>    返回类型
	 * @throws
	 */
	@PostMapping("/findDictItemById")
	@ApiOperation(value = "根据ID查询数据字典项")
	@ControllerMonitor(description = "根据ID查询数据字典项")
	public ResultBean<SysDictItem> findDictItemById(@RequestBody ReqBean<String> bean) {
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.COMMON_ERROR, "参数不存在！");
		SysDictItem item = dictItemService.findById(id);
		return new ResultBean(item);
	}

	/**
	 * @Title: findDictItemByDictCode
	 * @Description: 根据数据字典编码或者数据字典项
	 * @param 设定文件
	 * @return ResultBean<List<SysDictItem>>    返回类型
	 * @throws
	 */
	@PostMapping("/findDictItemByDictCode")
	@ApiOperation(value = "根据数据字典编码或者数据字典项")
	@ControllerMonitor(description = "根据数据字典编码或者数据字典项")
	public ResultBean<List<SysDictItem>> findDictItemByDictCode(@RequestBody ReqBean<String> bean) {
		String code = bean.getBody();
		CheckUtil.notEmpty(code, ResultCode.COMMON_ERROR, "参数不存在！");
		List<SysDictItem> itemList = new ArrayList<SysDictItem>();
		SysDict dict = dictService.getByCode(code);
		if(dict != null && dict.getItems() != null && !dict.getItems().isEmpty()) {
			for(SysDictItem i:dict.getItems()) {
				if(i.getStatus() != null && i.getStatus() == StatusEnum.ENABLE.getValue()) {
					itemList.add(i);
				}
			}
		}
		return new ResultBean(itemList);
	}

	/**
	 * @Title: findDictItemByDictId
	 * @Description: 根据数据字典ID查询数据字典项
	 * @param 设定文件
	 * @return ResultBean<List<SysDictItem>>    返回类型
	 * @throws
	 */
	@PostMapping("/findDictItemByDictId")
	@ApiOperation(value = "根据数据字典ID查询数据字典项")
	@ControllerMonitor(description = "根据数据字典ID查询数据字典项")
	public ResultBean<List<SysDictItem>> findDictItemByDictId(@RequestBody ReqBean<String> bean) {
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.COMMON_ERROR, "参数不存在！");
		List<SysDictItem> itemList = new ArrayList<SysDictItem>();
		SysDict dict = dictService.findById(id);
		if(dict != null && dict.getItems() != null && !dict.getItems().isEmpty()) {
			itemList.addAll(dict.getItems());
		}
		return new ResultBean(itemList);
	}

	/**
	 * @Title: delDictItemById
	 * @Description: 根据数据字典项ID删除数据字典项
	 * @param 设定文件
	 * @return ResultBean    返回类型
	 * @throws
	 */
	@PostMapping("/delDictItemById")
	@ApiOperation(value = "根据数据字典项ID删除数据字典项")
	@ControllerMonitor(description = "根据数据字典项ID删除数据字典项", operType = 4)
	public ResultBean delDictItemById(@RequestBody ReqBean<String> bean) {
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.COMMON_ERROR, "参数不存在！");
		SysDictItem item = dictItemService.findById(id);
		if(item != null) {
			item.setDelFlag(true);
			dictItemService.saveOrUpdate(item);
		}
		return new ResultBean();
	}

	/**
	 * @Title: batchDelDictItemById
	 * @Description: 根据数据字典项ID批量删除数据字典项
	 * @param 设定文件
	 * @return ResultBean    返回类型
	 * @throws
	 */
	@PostMapping("/batchDelDictItemById")
	@ApiOperation(value = "根据数据字典项ID批量删除数据字典项")
	@ControllerMonitor(description = "根据数据字典项ID批量删除数据字典项", operType = 4)
	public ResultBean batchDelDictItemById(@RequestBody ReqBean<List<String>> bean) {
		List<String> ids = bean.getBody();
		CheckUtil.notNull(ids, ResultCode.COMMON_ERROR, "参数不存在！");
		List<SysDictItem> itemList = dictItemService.findByIds(ids);
		if(itemList != null && !itemList.isEmpty()) {
			for(SysDictItem i:itemList) {
				i.setDelFlag(true);
			}
			dictItemService.saveAll(itemList);
		}
		return new ResultBean();
	}
	
	/**
	 * @Title: findDictItemByItemCode
	 * @Description: 根据数据字典值编码查询数据字典值信息
	 * @param bean
	 * @return ResultBean<SysDictItem> 返回类型
	 * @throws
	 */
	@PostMapping("/findDictItemByItemCode")
	@ApiOperation(value = "根据数据字典值编码查询数据字典值信息")
	@ControllerMonitor(description = "根据数据字典值编码查询数据字典值信息")
	public ResultBean<SysDictItem> findDictItemByItemCode(@RequestBody ReqBean<String> bean) {
		String code = bean.getBody();
		CheckUtil.notEmpty(code, ResultCode.COMMON_ERROR, "参数不存在！");
		return new ResultBean(dictItemService.queryByItemCode(code));
	}
}