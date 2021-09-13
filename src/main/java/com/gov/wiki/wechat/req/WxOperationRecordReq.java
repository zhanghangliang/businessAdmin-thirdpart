package com.gov.wiki.wechat.req;

import com.gov.wiki.common.buss.vo.SimpleSubjectRes;
import com.gov.wiki.common.entity.system.SysFile;
import com.gov.wiki.common.entity.wechat.WxMember;
import com.gov.wiki.common.entity.wechat.WxOperationRecordResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel(value = "WxOperationRecordReq",description = "")
public class WxOperationRecordReq {


        @ApiModelProperty(value = "id")
        private String id;

        @ApiModelProperty(value = "选项结果")
        private String options;

        @ApiModelProperty(value = "预审不通过原因")
        private String reason;

        @ApiModelProperty(value = "操作记录类型：1服务，2预审")
        private Integer recordType;

        @ApiModelProperty(value = "审核状态：0 保存， 1 提交待审， 2 审核中 3 审核通过，4 审核不通过（可以修改）")
        private Integer auditStatus;

        @ApiModelProperty(value = "是否转预审，只有自助服务才可以转预审。默认未转预审0,不转，1转")
        private Integer changeAudit;

}
