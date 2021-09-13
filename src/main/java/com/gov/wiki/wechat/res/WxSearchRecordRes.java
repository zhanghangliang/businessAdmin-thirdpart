package com.gov.wiki.wechat.res;


import com.gov.wiki.common.entity.wechat.WxOperationRecord;
import com.gov.wiki.common.entity.wechat.WxSearchRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@ApiModel(value = "WxSearchRecordRes", description = "搜索返回结果")
public class WxSearchRecordRes {
    @ApiModelProperty(value = "热门搜索")
    private Page<WxSearchRecord> all;
    @ApiModelProperty(value = "用户搜索")
    private Page<WxSearchRecord> user;
}
