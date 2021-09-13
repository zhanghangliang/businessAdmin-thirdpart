package com.gov.wiki.organization.controller;

import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.entity.system.OrgCompany;
import com.gov.wiki.common.entity.system.OrgDepart;
import com.gov.wiki.common.redis.RedisManager;
import com.gov.wiki.common.utils.Constants;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.SessionUser;
import com.gov.wiki.organization.service.ICompanyService;
import com.gov.wiki.organization.service.IDepartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/company")
@RestController
@Api(tags = "公司管理")
public class CompanyController {
    @Autowired
    private ICompanyService companyService;

    @Autowired
    private IDepartService departService;

    @Autowired
    private RedisManager redisManager;

    @PostMapping("/savecompany")
    @ApiOperation(value = "新增或者修改公司信息")
    @ControllerMonitor(description = "新增或者修改公司信息", operType = 2)
    public ResultBean<OrgCompany> savecompany(@RequestBody ReqBean<OrgCompany> bean) {
        return new ResultBean<>(companyService.saveOrUpdate(bean.getBody()));
    }

    @GetMapping("/allcompany")
    @ApiOperation(value = "获取所有公司信息")
    @ControllerMonitor(description = "获取所有公司信息", operType = 1)
    public ResultBean<List<Object[]>> allcompany(HttpServletRequest request){
        String token = request.getHeader(Constants.TOKEN);
        SessionUser user = (SessionUser) redisManager.getSessionUser(token);
        return new ResultBean<>(companyService.findAllcompany(user.getCreater()));
    }

    @PostMapping("/deletecompany")
    @ApiOperation(value = "删除公司")
    @ControllerMonitor(description = "删除公司", operType = 1)
    public ResultBean<String> deletecompany(@RequestBody ReqBean<List<String>> bean){
        List<OrgDepart> byCompanyId = departService.findByCompanyId(bean.getBody().get(0));
        if(byCompanyId.size()!=0) return ResultBean.error(-1,"公司下还有部门,无法删除");
        companyService.deleteById(bean.getBody().get(0));
        OrgCompany byId = companyService.findById(bean.getBody().get(0));
        return byId==null?new ResultBean():ResultBean.error(-1,"删除失败");
    }

}
