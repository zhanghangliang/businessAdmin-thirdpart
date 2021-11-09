package com.gov.wiki.wechat.controller;

import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.business.req.query.SubjectQuery;
import com.gov.wiki.business.service.BizMatterDepositoryMainService;
import com.gov.wiki.business.service.ISubjectService;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.entity.buss.BizMatterDepositoryMain;
import com.gov.wiki.common.entity.buss.BizSubject;
import com.gov.wiki.common.entity.wechat.WxSearchRecord;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.ReqHeader;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.system.service.IFileService;
import com.gov.wiki.wechat.res.WxSearchRecordRes;
import com.gov.wiki.wechat.service.WxSearchRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/Wxsearch")
@Api(tags = "搜索管理")
public class SearchController {
    @Autowired
    private WxSearchRecordService searchRecordService;
    @Autowired
	private IFileService fileService;
    @Autowired
    private BizMatterDepositoryMainService bizMatterDepositoryMainService;
    @Autowired
    private ISubjectService subjectService;


    @PostMapping(value = "/getAllsearch")
    @ApiOperation(value = "获取用户和热门搜索记录")
    @ControllerMonitor(description = "获取用户和热门搜索记录", operType = 1)
    public ResultBean<WxSearchRecordRes> getAllsearch(@RequestBody ReqBean<String> bean){
        Specification<WxSearchRecord> specification = Specifications.<WxSearchRecord>and()
                .eq("searchType",1)
                .build();
        Specification<WxSearchRecord> specification1 = Specifications.<WxSearchRecord>and()
                .eq(StringUtils.isNotBlank(bean.getBody()),"memberId",bean.getBody())
                .eq("searchType",0)
                .build();
        Page<WxSearchRecord> all = searchRecordService.findAll(specification,bean.getHeader().getPageable());
        Page<WxSearchRecord> user = searchRecordService.findAll(specification1,bean.getHeader().getPageable());
        WxSearchRecordRes searchRecordRes=new WxSearchRecordRes();
        searchRecordRes.setAll(all);
        searchRecordRes.setUser(user);
        return new ResultBean<>(searchRecordRes);
    }

    @PostMapping(value = "/delrecord")
    @ApiOperation(value = "清空用户搜索记录")
    @ControllerMonitor(description = "清空用户搜索记录", operType = 4)
    public ResultBean<String> delrecord(@RequestBody ReqBean<String> bean){
        searchRecordService.deleteByMemberId(bean.getBody());
        List<WxSearchRecord> byMemberId = searchRecordService.findByMemberId(bean.getBody());
        CheckUtil.check(byMemberId.size()==0, ResultCode.COMMON_ERROR,"删除失败");
        return new ResultBean<>("删除成功");
    }

    @PostMapping(value = "/addsearchhistory")
    @ApiOperation(value = "搜索")
    @ControllerMonitor(description = "搜索", operType = 2)
    public ResultBean<Page<BizSubject>> addsearchhistory(@RequestBody ReqBean<WxSearchRecord> bean){
        WxSearchRecord body = bean.getBody();
        ReqBean<SubjectQuery> bean1 = new ReqBean<SubjectQuery>();
        bean1.setHeader(bean.getHeader());
        SubjectQuery q = new SubjectQuery();
        q.setRecyclingMark(false);
        q.setKeywords(body.getSearchContent());
        q.setMajorCategorys(Arrays.asList(1));
        Page<BizSubject> all = subjectService.page(bean1);
        body.setSearchType(0);
        searchRecordService.saveOrUpdate(body);
        WxSearchRecord bySearchContent = searchRecordService.findBySearchContent(body.getSearchContent());
        if(bySearchContent==null){
            bySearchContent = new WxSearchRecord();
            bySearchContent.setMemberId(body.getMemberId());
            bySearchContent.setCount(1);
            bySearchContent.setSearchType(1);
            bySearchContent.setSearchContent(body.getSearchContent());
        }
        else{
            bySearchContent.setCount(bySearchContent.getCount()+1);
        }
        searchRecordService.saveOrUpdate(bySearchContent);
        return new ResultBean<>(all);
    }
}
