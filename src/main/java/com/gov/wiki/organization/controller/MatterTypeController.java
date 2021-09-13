package com.gov.wiki.organization.controller;

import com.alibaba.fastjson.JSON;
import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.business.service.BizMatterTypeService;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.entity.buss.BizMatterType;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.ReqBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/mattertype")
@RestController
@Api(tags = "事项枚举管理")
public class MatterTypeController {
    @Autowired
    private BizMatterTypeService matterTypeService;

    @PostMapping(value = "/saveorupdate")
    @ApiOperation(value = "保存或修改")
    @ControllerMonitor(description = "保存或修改", operType = 2)
    public ResultBean<BizMatterType> saveorupdate(@RequestBody ReqBean<BizMatterType> bean){
        BizMatterType body = bean.getBody();
        BizMatterType byKey = matterTypeService.findByKey(body.getMatterKey());
        if(byKey!=null){
            CheckUtil.check(byKey.getId().equals(body.getId()), ResultCode.COMMON_ERROR,"键值已存在");
        }
        BizMatterType matterType = matterTypeService.saveOrUpdate(bean.getBody());
        return new ResultBean<>(matterType);
    }

    @PostMapping(value = "/delete")
    @ApiOperation(value = "删除")
    @ControllerMonitor(description = "删除", operType = 4)
    public ResultBean<String> delete(@RequestBody ReqBean<List<String>> bean){
        matterTypeService.batchDelete(bean.getBody());
        return new ResultBean<>();
    }
}
