package com.gov.wiki.organization.controller;

import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.business.service.BizMaterialSourceService;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.entity.buss.BizMaterialSource;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.ReqBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/materialsource")
@RestController
@Api(tags = "资料来源枚举管理")
public class MaterialSourceController {
    @Autowired
    private BizMaterialSourceService materialSourceService;

    @PostMapping(value = "/saveorupdate")
    @ApiOperation(value = "保存或修改")
    @ControllerMonitor(description = "保存或修改", operType = 2)
    public ResultBean<BizMaterialSource> saveorupdate(@RequestBody ReqBean<BizMaterialSource> bean){
        BizMaterialSource body = bean.getBody();
        BizMaterialSource byKey = materialSourceService.findByKey(body.getMaterialKey());
        if(byKey!=null){
            CheckUtil.check(byKey.getId().equals(body.getId()), ResultCode.COMMON_ERROR,"键值已存在");
        }
        BizMaterialSource matterType = materialSourceService.saveOrUpdate(bean.getBody());
        return new ResultBean<>(matterType);
    }

    @PostMapping(value = "/delete")
    @ApiOperation(value = "删除")
    @ControllerMonitor(description = "删除", operType = 4)
    public ResultBean<String> delete(@RequestBody ReqBean<List<String>> bean){
        materialSourceService.batchDelete(bean.getBody());
        return new ResultBean<>();
    }
}
