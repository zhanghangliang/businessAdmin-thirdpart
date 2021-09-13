/**
 * @Title: MaterialExistRes.java
 * @Package com.gov.wiki.business.res
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2020年11月25日
 * @version V1.0
 */
package com.gov.wiki.business.res;

import com.gov.wiki.common.entity.system.SysDictItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: MaterialExistRes
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author cys
 * @date 2020年11月25日
 */
@Data
public class MaterialExistRes {
	/**
	*唯一标识
	*/
	@ApiModelProperty(value = "唯一标识")
	private String id;
	
	/**
	*是否前置
	*/
	@ApiModelProperty(value = "是否前置")
	private Boolean front;
	
	/**
	*前置描述
	*/
	@ApiModelProperty(value = "前置描述")
	private String frontDescription;
	
	/**
	*资料类别,例如T1
	*/
	@ApiModelProperty(value = "资料类别")
	private String materialCategory;
	
	/**
	*资料说明
	*/
	@ApiModelProperty(value = "资料说明")
	private String materialDescription;
	
	/**
	*资料名称
	*/
	@ApiModelProperty(value = "资料名称")
	private String materialName;
	
	/**
	*资料来源
	*/
	@ApiModelProperty(value = "资料来源")
	private String materialSource;
	
	/**
	*资料类型,例如:证件
	*/
	@ApiModelProperty(value = "资料类型")
	private Integer materialType;
	
	/**
	*资料编号
	*/
	@ApiModelProperty(value = "资料编号")
	private String materialId;
	
	/**
	*受理标准
	*/
	@ApiModelProperty(value = "受理标准")
	private String standard;
	
	/**
	*去向
	*/
	@ApiModelProperty(value = "去向")
	private String whereabouts;
	
	@ApiModelProperty(value = "备注")	
	private String remark;

	/**
	 * 修改次数
	 */
	@ApiModelProperty(value = "修改次数")	
	private Integer modifyTimes;
	
	/**
	 * 版本号
	 */
	@ApiModelProperty(value = "版本号")	
	private String version;
	
	/**
	 * 行政职责分类
	 */
	@ApiModelProperty(value = "行政职责分类")	
	private String dutyType;
	
	/**
	*行政职责分类数据字典项
	*/
	@ApiModelProperty(name = "dutyTypeItem", value = "行政职责分类数据字典项")
	private SysDictItem dutyTypeItem;
}