package com.gov.wiki.business.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.wenhao.jpa.PredicateBuilder;
import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.business.req.StudyQuery;
import com.gov.wiki.business.req.StudyReq;
import com.gov.wiki.business.res.StudyRes;
import com.gov.wiki.business.service.BizStudyRecordService;
import com.gov.wiki.business.service.BizStudyService;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.check.CheckClass;
import com.gov.wiki.common.entity.MemberInfoReq;
import com.gov.wiki.common.entity.buss.BizStudy;
import com.gov.wiki.common.entity.buss.BizStudyRecord;
import com.gov.wiki.common.redis.RedisManager;
import com.gov.wiki.common.res.PageResult;
import com.gov.wiki.common.utils.BeanUtils;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.Constants;
import com.gov.wiki.common.utils.JwtUtil;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.SessionUser;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.organization.service.IMemberService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/Study")
@RestController
@Api(tags = "学习管理")
public class StudyController {

    @Autowired
    private BizStudyService studyService;

    @Autowired
    private IMemberService memberService;

    @Autowired
    private BizStudyRecordService studyRecordService;

    @Autowired
    private RedisManager redisManager;

    /**
     * @Title: saveOrUpdate
     * @Description: 新增或更新学习资料信息
     * @param bean
     * @return ResultBean<StudyRes> 返回类型
     * @throws
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value = "新增或更新学习资料信息")
    @ControllerMonitor(description = "新增或更新学习资料信息", operType = 2)
    public ResultBean<StudyRes> saveOrUpdate(@RequestBody ReqBean<StudyReq> bean){
        return new ResultBean<>(studyService.saveOrUpdate(bean.getBody()));
    }


    @PostMapping("/findAll")
    @ApiOperation(value = "分页查找学习资料")
    @ControllerMonitor(description = "分页查找学习资料", operType = 1)
    public ResultBean<PageResult<BizStudy>> findAll(@RequestBody ReqBean<StudyQuery> bean){
    	String userId = JwtUtil.getUserId();
    	CheckUtil.notEmpty(userId, ResultCode.COMMON_ERROR, "用户未登录！");
    	StudyQuery query = bean.getBody() == null?new StudyQuery():bean.getBody();
    	PredicateBuilder<BizStudy> builder = Specifications.and();
    	builder.predicate(Specifications.or()
    		.eq("createBy", userId)
    		.predicate(Specifications.and()
    			.eq("online", true)
    			.predicate(Specifications.or()
    				.predicate(Specifications.and()
    					.eq("permissionType", 2)
    					.like("permissionRange", "%" + userId + "%")
    					.build())
    				.eq("permissionType", 3)
    				.build())
    			.build())
    		.build());
    	if(StringUtils.isNotBlank(query.getKeywords())) {
    		builder.like("name", "%" + query.getKeywords() + "%");
    	}
        Page<BizStudy> all = studyService.findAll(builder.build(), bean.getHeader().getPageable());
        List<BizStudy> content = all.getContent();
        for (BizStudy bizStudy : content) {
            String[] split = bizStudy.getLearners().split(",");
            List<String> strings = Arrays.asList(split);
            List<MemberInfoReq> nameById = memberService.findNameByIds(strings);
            bizStudy.setLearnersname(nameById);
            if(StringUtils.isNotBlank(bizStudy.getPermissionRange())) {
            	String[] memberIds = bizStudy.getPermissionRange().split(",");
            	bizStudy.setPermissionRangeName(memberService.findNamesByIds(Arrays.asList(memberIds)));
            }
        } 
        PageResult<BizStudy> result = new PageResult<BizStudy>(all);
        result.setCurrentPage(all.getNumber()+1);
        result.setDataList(all.getContent());
        return new ResultBean<>(result);
    }

    /**
     * @Title: delete
     * @Description: 批量删除
     * @param bean
     * @return ResultBean<String> 返回类型
     * @throws
     */
    @PostMapping("/delete")
    @ApiOperation(value = "批量删除")
    @ControllerMonitor(description = "批量删除", operType = 4)
    public ResultBean<String> delete(@RequestBody ReqBean<List<String>> bean){
    	studyService.depthDelete(bean.getBody());
        return new ResultBean<>();
    }
    
    /**
     * @Title: findByParams
     * @Description: 根据参数查询学习资料
     * @param bean
     * @return ResultBean<List<StudyRes>> 返回类型
     * @throws
     */
    @PostMapping("/findByParams")
    @ApiOperation(value = "根据参数查询学习资料")
    @ControllerMonitor(description = "根据参数查询学习资料")
    public ResultBean<List<StudyRes>> findByParams(@RequestBody ReqBean<StudyQuery> bean){
    	return new ResultBean<List<StudyRes>>(studyService.findByParams(bean.getBody()));
    }
    
    /**
     * @Title: findById
     * @Description: 根据id查询学习资料
     * @param bean
     * @return ResultBean<StudyRes> 返回类型
     * @throws
     */
    @PostMapping("/findById")
    @ApiOperation(value = "根据id查询学习资料")
    @ControllerMonitor(description = "根据id查询学习资料", operType = 4)
    public ResultBean<StudyRes> findById(@RequestBody ReqBean<String> bean){
    	CheckUtil.notNull(bean.getBody(), ResultCode.PARAM_NULL,"id");
    	BizStudy s = studyService.findById(bean.getBody());
    	StudyRes studyRes = BeanUtils.copyProperties(s, StudyRes.class);
    	if(StringUtils.isNotBlank(studyRes.getPermissionRange())) {
    		String[] memberIds = studyRes.getPermissionRange().split(",");
         	studyRes.setPermissionRangeName(memberService.findNamesByIds(Arrays.asList(memberIds)));
    	}
    	return new ResultBean<StudyRes>(studyRes);
    }

    @PostMapping("/addrecord")
    @ApiOperation(value = "新增或更新学习记录")
    @ControllerMonitor(description = "新增或更新学习记录", operType = 2)
    public ResultBean<BizStudyRecord> addrecord(@RequestBody ReqBean<BizStudyRecord> bean){
        BizStudyRecord body = bean.getBody();
        CheckClass.check(body);
        BizStudyRecord byMemberIdAndStudyId = studyRecordService.findByMemberIdAndStudyId(body.getMemberId(), body.getStudyId());
        if(byMemberIdAndStudyId!=null){
            body.setId(byMemberIdAndStudyId.getId());
        }
        BizStudyRecord bizStudyRecord = studyRecordService.saveOrUpdate(bean.getBody());
        return new ResultBean<>(bizStudyRecord);
    }

    @PostMapping("/findAllrecord")
    @ApiOperation(value = "分页查找学习记录")
    @ControllerMonitor(description = "分页查找学习记录", operType = 1)
    public ResultBean<Page<BizStudyRecord>> findAllrecord(@RequestBody ReqBean<String> bean, HttpServletRequest request){
        String token = request.getHeader(Constants.TOKEN);
        SessionUser user = (SessionUser) redisManager.getSessionUser(token);
        Specification<BizStudyRecord> specification = Specifications.<BizStudyRecord>and()
                .in("createBy", user.getCreater())
                .build();
        return new ResultBean<>(studyRecordService.findAll(specification,bean.getHeader().getPageable()));
    }

    @PostMapping("/findByName")
    @ApiOperation(value = "根据标题查询学习资料")
    @ControllerMonitor(description = "根据标题查询学习资料", operType = 1)
    public ResultBean<Integer> findByName(@RequestBody ReqBean<StudyQuery> bean, HttpServletRequest request){
        String token = request.getHeader(Constants.TOKEN);
        SessionUser user = (SessionUser) redisManager.getSessionUser(token);
        CheckUtil.notNull(bean.getBody(), ResultCode.COMMON_ERROR,"参数不能为空");
        StudyQuery body = bean.getBody();
        Specification<BizStudy> specification = Specifications.<BizStudy>and()
                .in("createBy", user.getCreater())
                .eq(StringUtils.isNotBlank(body.getName()),"name",body.getName())
                .eq(StringUtils.isNotBlank(body.getParentId()),"parentId",body.getParentId())
                .eq(body.getType()!=null,"type",body.getType())
                .build();
        return new ResultBean<>(studyService.findCountByName(specification).intValue());
    }

}