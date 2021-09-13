/**
 * @Title: DistribResourceReq.java 
 * @Package com.insolu.spm.organization.req 
 * @Description: 分配资源参数对象
 * @author cys 
 * @date 2019年11月8日 下午9:06:32 
 * @version V1.0 
 */
package com.gov.wiki.organization.req;

import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "DistribResourceReq", description = "分配资源参数对象")
public class DistribResourceReq {

	@ApiModelProperty(value = "角色ID")
	private String roleId;
	
	@ApiModelProperty(value = "资源ID")
	private List<String> resourceIds;
}