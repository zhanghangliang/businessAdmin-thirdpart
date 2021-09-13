/**
 * @Title: SysConfigReq.java 
 * @Package com.gov.wiki.system.req
 * @Description: 系统配置请求对象
 * @author cys 
 * @date 2019年12月8日 下午4:10:53 
 * @version V1.0 
 */
package com.gov.wiki.system.req;

import com.gov.wiki.common.entity.system.SysConfig;
import com.gov.wiki.common.utils.JSONUtil;
import com.gov.wiki.common.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@ApiModel(value = "SysConfigReq", description = "系统配置请求对象")
@Accessors(chain = true)
public class SysConfigReq {
	/**
	 * 主键
	 */
	@ApiModelProperty(name = "id", value = "唯一标识")
	private String id;
	
	/**
	*系统中文名称
	*/
	@ApiModelProperty(name = "cnName", value = "系统中文名称")
	private String cnName;
	
	/**
	*系统英文名称
	*/
	@ApiModelProperty(name = "enName", value = "系统英文名称")
	private String enName;
	
	/**
	*轮播图
	*/
	@ApiModelProperty(name = "carousel", value = "轮播图")
	private String carousel;
	
	/**
	*系统LOGO
	*/
	@ApiModelProperty(name = "logo", value = "系统LOGO")
	private String logo;
	
	/**
	*备注
	*/
	@ApiModelProperty(name = "remarks", value = "备注")
	private String remarks;
	
	public SysConfig toEntity() {
		SysConfig c = new SysConfig();
		if(StringUtils.isNotBlank(this.id)) {
			c.setId(this.id);
		}
		c.setCarousel(JSONUtil.strToNull(this.carousel));
		c.setCnName(JSONUtil.strToNull(this.cnName));
		c.setEnName(JSONUtil.strToNull(this.enName));
		c.setDelFlag(false);
		c.setLogo(JSONUtil.strToNull(this.logo));
		c.setRemarks(JSONUtil.strToNull(this.remarks));
		return c;
	}
}