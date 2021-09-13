package com.gov.wiki.business.res;

import com.gov.wiki.common.entity.buss.BizMatterDepositoryMain;
import com.gov.wiki.common.entity.buss.BizSubjectAudit;
import com.gov.wiki.common.entity.buss.PrivMatterSubject;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "SubjectAuditRes", description = "审核主题返回对象")
public class SubjectAuditRes {

    private BizSubjectAudit subjectAudit;

    private List<BizMatterDepositoryMain> matterDepositoryMains;
}
