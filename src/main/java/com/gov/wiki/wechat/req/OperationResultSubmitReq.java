/**
 * @Title: OperationResultSubmitReq.java
 * @Package com.gov.wiki.wechat.req
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2020年12月16日
 * @version V1.0
 */
package com.gov.wiki.wechat.req;

import com.gov.wiki.common.check.Check;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: OperationResultSubmitReq
 * @Description: 用户操作记录资料提交对象
 * @author cys
 * @date 2020年12月16日
 */
@Data
@ApiModel(description = "用户操作记录资料提交对象")
public class OperationResultSubmitReq {
	
	@ApiModelProperty(value = "操作对象类型1-用户记录2-用户记录详情")
	@Check(nullable = false, title = "操作对象类型")
	private Integer operType;

	@ApiModelProperty(value = "操作对象ID")
    @Check(nullable = false, title = "操作对象ID")
	private String objectId;
	
	@ApiModelProperty(value = "结果资料附件")
	@Check(nullable = false, title = "结果资料附件")
	private String annex;
}