package com.gov.wiki.organization.res;

import com.gov.wiki.common.entity.system.OrgMember;
import com.gov.wiki.common.entity.system.OrgRole;
import com.gov.wiki.common.entity.system.PrivResource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "MemberRes", description = "人员信息返回结果")
public class MemberRes {

    @ApiModelProperty(value = "人员信息")
    Page<OrgMember> orgMembers;

    @ApiModelProperty(value = "所属部门名称")
    private String departname;
}
