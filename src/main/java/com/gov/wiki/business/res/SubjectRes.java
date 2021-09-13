package com.gov.wiki.business.res;

import com.gov.wiki.common.entity.buss.BizMatterDepositoryMain;
import com.gov.wiki.common.entity.buss.BizSubject;
import com.gov.wiki.common.entity.buss.PrivMatterSubject;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "SubjectRes", description = "主题返回对象")
public class SubjectRes {
    private BizSubject subjectAudit;

    private List<BizMatterDepositoryMain> matterDepositoryMains;
}
