package com.gov.wiki.organization.controller;

import com.github.wenhao.jpa.PredicateBuilder;
import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.entity.system.OrgCustomer;
import com.gov.wiki.common.entity.wechat.WxMember;
import com.gov.wiki.common.redis.RedisManager;
import com.gov.wiki.common.utils.*;
import com.gov.wiki.organization.service.ICustomerService;
import com.gov.wiki.wechat.service.WxMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/Customer")
@RestController
@Api(tags = "客户管理")
public class CustomerController {

    @Autowired
    private WxMemberService memberService;


    @PostMapping("/allCustomer")
    @ApiOperation(value = "查询所有客户")
    @ControllerMonitor(description = "查询所有客户", operType = 1)
    public ResultBean<Page<WxMember>> allCustomer(@RequestBody ReqBean<String> bean, HttpServletRequest request) {
        return new ResultBean<>(memberService.pageByName(bean));
    }

    @PostMapping("/deletbyIds")
    @ApiOperation(value = "批量删除客户")
    @ControllerMonitor(description = "批量删除客户", operType = 4)
    public ResultBean deletbyIds(@RequestBody ReqBean<List<String>> bean) {
        memberService.batchDelete(bean.getBody());
        WxMember byId = memberService.findById(bean.getBody().get(0));
        return byId==null? new ResultBean():ResultBean.error(-1,"删除失败");
    }

    @PostMapping("/editState")
    @ApiOperation(value = "修改客户状态")
    @ControllerMonitor(description = "修改客户状态", operType = 3)
    public ResultBean<WxMember> editState(@RequestBody ReqBean<WxMember> bean) {
        WxMember byId = memberService.findById(bean.getBody().getId());
        byId.setState(bean.getBody().getState());
        return new ResultBean<>(memberService.saveOrUpdate(byId));
    }
}