/**   
 * @Copyright:  成都北诺星科技有限公司  All rights reserved.Notice 官方网站：http://www.beinuoxing.com
 */
package com.gov.wiki.common.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
public class IdRes {

	@ApiModelProperty(value = "主键ID")
	private String id;
}
