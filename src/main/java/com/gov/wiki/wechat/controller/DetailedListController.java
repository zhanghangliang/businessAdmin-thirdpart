package com.gov.wiki.wechat.controller;

import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.business.service.BizMatterTypeService;
import com.gov.wiki.business.service.ISubjectService;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.entity.buss.BizMatterType;
import com.gov.wiki.common.entity.buss.BizSubject;
import com.gov.wiki.wechat.res.DetailRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/Wxdetailedlist")
@Api(tags = "微信服务清单管理")
public class DetailedListController {
    @Autowired
    private ISubjectService subjectService;

    @Autowired
    private BizMatterTypeService bizMatterTypeService;

	/*
	 * @PostMapping(value = "/detailedlist")
	 * 
	 * @ApiOperation(value = "服务清单")
	 * 
	 * @ControllerMonitor(description = "服务清单", operType = 1) public
	 * ResultBean<List<DetailRes>> detailedlist(){ List<DetailRes> ans=new
	 * ArrayList<>(); List<BizMatterType> all1 = bizMatterTypeService.findAll(); for
	 * (BizMatterType sc : all1) { Specification<BizSubject> specification =
	 * Specifications.<BizSubject>and() .eq("online",true)
	 * .eq("subjectType",sc.getMatterKey()) .build(); List<BizSubject> all =
	 * subjectService.findAll(specification); DetailRes detailRes = new DetailRes();
	 * detailRes.setName(sc.getMatterValue()); detailRes.setDepositoryMains(all);
	 * ans.add(detailRes); } return new ResultBean<>(ans); }
	 */
}
