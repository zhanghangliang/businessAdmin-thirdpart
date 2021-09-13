package com.gov.wiki.wechat.controller;

import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.business.service.BizMatterDepositoryMainService;
import com.gov.wiki.business.service.ISubjectService;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.entity.buss.BizMatterDepositoryMain;
import com.gov.wiki.common.entity.buss.BizSubject;
import com.gov.wiki.common.entity.wechat.WxOperationRecord;
import com.gov.wiki.common.entity.wechat.WxOperationRecordCount;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.config.WxConfig;
import com.gov.wiki.wechat.service.WxOperationRecordCountService;
import com.gov.wiki.wechat.service.WxOperationRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.sql.rowset.Predicate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/WxIndex")
@Api(tags = "微信首页管理")
public class WxIndexController {

    @Autowired
    private ISubjectService subjectService;

    @Autowired
    private WxOperationRecordCountService recordCountService;

    @Autowired
    private WxConfig wxConfig;

    @PostMapping(value = "/popularservices")
    @ApiOperation(value = "热门服务")
    @ControllerMonitor(description = "热门服务", operType = 1)
    public ResultBean<Page<WxOperationRecordCount>> popularservices(@RequestBody ReqBean<String> bean){
    	
        return new ResultBean<>(recordCountService.findEffectiveAll(bean.getHeader().getPageable()));
    }

	/*
	 * @PostMapping(value = "/findbysubjecttype")
	 * 
	 * @ApiOperation(value = "查询分类主题")
	 * 
	 * @ControllerMonitor(description = "查询分类主题", operType = 1) public
	 * ResultBean<List<BizSubject>> findbysubjecttype(@RequestBody ReqBean<Integer>
	 * bean){ Specification<BizSubject> specification =
	 * Specifications.<BizSubject>and() .eq("subjectType",bean.getBody())
	 * .eq("online",true) .build(); return new
	 * ResultBean<>(subjectService.findAll(specification)); }
	 */
}