package com.gov.wiki.business.controller;

import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.business.req.AuditDeleteQuery;
import com.gov.wiki.business.req.MatterQuery;
import com.gov.wiki.business.req.MatterReq;
import com.gov.wiki.business.res.MatterRes;
import com.gov.wiki.business.res.SituationRes;
import com.gov.wiki.business.service.*;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.check.CheckClass;
import com.gov.wiki.common.entity.buss.*;
import com.gov.wiki.common.enums.AuditEnum;
import com.gov.wiki.common.enums.MatterEnum;
import com.gov.wiki.common.redis.RedisManager;
import com.gov.wiki.common.utils.*;
import com.gov.wiki.system.service.IFileService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequestMapping("/Matter")
@RestController
@Api(tags = "事项管理")
public class MatterController {
    @Autowired
    private BizMatterAuditMainService bizMatterAuditMainService;
    @Autowired
    private BizMatterDepositoryMainService bizMatterDepositoryMainService;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private BizAuditService bizAuditService;
    @Autowired
    private BizAuditOpinionService bizAuditOpinionService;
    @Autowired
    private PrivMatterSubjectService matterSubjectService;
    @Autowired
    private IFileService fileService;

    /**
     * @Title: allAuditMatter
     * @Description: 分页查询审核事项
     * @param bean
     * @param request
     * @return ResultBean<PageInfo> 返回类型
     * @throws
     */
    @PostMapping("/pageAuditMatter")
    @ApiOperation(value = "分页查询审核事项")
    @ControllerMonitor(description = "分页查询审核事项")
    public ResultBean<PageInfo> pageAuditMatter(@RequestBody ReqBean<MatterQuery> bean) {
        return bizMatterAuditMainService.pageAuditMetter(bean);
    }

    @PostMapping("/saveOrUpdateMatter")
    @ApiOperation(value = "新增或更新事项")
    @ControllerMonitor(description = "新增或更新事项", operType = 2)
    public ResultBean<String> addmaterial(@RequestBody ReqBean<MatterReq> bean) {
        MatterReq body = bean.getBody();
        BizMatterAuditMain bizMatterAuditMain = body.getBizMatterAuditMain();
        String upmatterId = bizMatterAuditMain.getUpmatterId();
        BizMatterDepositoryMain upmatter = null;
        //判断情形和事项是否同级
        
        if (StringUtils.isNotBlank(upmatterId) && !upmatterId.equals("-1")) {
            upmatter = bizMatterDepositoryMainService.findById(upmatterId);
            List<BizMatterDepositoryMain> byUpmatterId = bizMatterDepositoryMainService.findByUpmatterId(upmatterId);
            /*0822 wmw  取消事项与情形同级的判断
            for (BizMatterDepositoryMain matterDepositoryMain : byUpmatterId) {
                CheckUtil.check(matterDepositoryMain.getAttribute()==bizMatterAuditMain.getAttribute(),ResultCode.COMMON_ERROR,"事项与情形同级");
            }
            */
        }
        //判断是否已经通过审核
        if (bizMatterAuditMain.getId() != null) {
            BizMatterAuditMain bizMatterAuditMain1 = bizMatterAuditMainService.findById(bizMatterAuditMain.getId());
            if (bizMatterAuditMain1!=null && bizMatterAuditMain1.getAuditState() == 2) return ResultBean.error(-1, "无法修改已通过的数据");
        }
        //判断是否事项选择情形为父级
        if (upmatter != null) {
            /*CheckUtil.check(!(upmatter.getAttribute() == MatterEnum.Attribute.Situation.getKey() 
            		&& bizMatterAuditMain.getAttribute() != MatterEnum.Attribute.Situation.getKey()), 
            		ResultCode.COMMON_ERROR, "事项无法选择情形为父级");*/
        }
        //如果上级为空则自己为最顶级
        if (StringUtils.isBlank(upmatterId)) {
            bizMatterAuditMain.setUpmatterId("-1");
        }
        //设置从表
        bizMatterAuditMain.setBizMatterAuditSlaves(body.getBizMatterAuditSlaveList());
        bizMatterAuditMain = bizMatterAuditMainService.saveOrUpdate(bizMatterAuditMain);
        return new ResultBean<>();
    }

    @PostMapping("/editMaterialMatter")
    @ApiOperation(value = "查询修改事项")
    @ControllerMonitor(description = "查询修改事项", operType = 3)
    public ResultBean<HashMap<String, Object>> editMaterialMatter(@RequestBody ReqBean<String> bean) {
        HashMap<String, Object> map = new HashMap<>();
        BizMatterAuditMain main = bizMatterAuditMainService.findById(bean.getBody());
        map.put("main", main);
        return new ResultBean<>(map);
    }

    @PostMapping
    @ApiOperation(value = "查看审核库详情")
    @ControllerMonitor(description = "查看审核库详情")
    public ResultBean<HashMap<String, Object>> MatterDetails(@RequestBody ReqBean<String> bean) {
        HashMap<String, Object> map = new HashMap<>();
        BizMatterDepositoryMain main = bizMatterDepositoryMainService.findById(bean.getBody());
        if(main != null && StringUtils.isNotBlank(main.getProcessFlow())) {
        	main.setProcessFlowFiles(fileService.findByReferenceId(main.getProcessFlow()));
        }
        List<BizMatterDepositoryMain> subordinate = bizMatterDepositoryMainService.findByUpmatterId(bean.getBody());
        if(subordinate != null && !subordinate.isEmpty()) {
        	for(BizMatterDepositoryMain sub:subordinate) {
        		if(sub != null && StringUtils.isNotBlank(sub.getProcessFlow())) {
        			sub.setProcessFlowFiles(fileService.findByReferenceId(sub.getProcessFlow()));
                }
        	}
        }
        map.put("main", main);
        map.put("subordinate", subordinate);
        return new ResultBean<>(map);
    }

    @PostMapping("/depositoryedit")
    @ApiOperation(value = "查看事项库详情")
    @ControllerMonitor(description = "查看事项库详情")
    public ResultBean<HashMap<String, Object>> depositoryedit(@RequestBody ReqBean<String> bean) {
        HashMap<String, Object> map = new HashMap<>();
        BizMatterDepositoryMain main = bizMatterDepositoryMainService.findById(bean.getBody());
        if(main != null && StringUtils.isNotBlank(main.getProcessFlow())) {
        	main.setProcessFlowFiles(fileService.findByReferenceId(main.getProcessFlow()));
        }
        map.put("main", main);
        return new ResultBean<>(map);
    }

    @GetMapping
    @ApiOperation(value = "获取所有上级事项名称和编号")
    @ControllerMonitor(description = "获取所有上级事项名称和编号")
    public ResultBean<HashMap<String, Object>> findAllUpMatter(HttpServletRequest request) {
        String token = request.getHeader(Constants.TOKEN);
        SessionUser user = (SessionUser) redisManager.getSessionUser(token);
        HashMap<String, Object> ans = new HashMap<>();
        List<Object[]> topid = bizMatterDepositoryMainService.findIdByUpmatterId("-1", user.getCreater());
        ans.put("0", topid);
        List<Object[]> upMatterId = bizMatterDepositoryMainService.groupUpMatterId(user.getCreater());
        for (Object[] s : upMatterId) {
            List<Object[]> idByUpmatterId = bizMatterDepositoryMainService.findIdByUpmatterId(s[0].toString(), user.getCreater());
            ans.put(s[0].toString(), idByUpmatterId);
        }
        return new ResultBean<>(ans);
    }

    @PostMapping("/deleteAllMatter")
    @ApiOperation(value = "批量删除事项库数据")
    @ControllerMonitor(description = "批量删除事项库数据", operType = 3)
    public ResultBean<String> deleteAllMatter(@RequestBody ReqBean<List<String>> bean) {
        List<BizMatterDepositoryMain> findsonbyids = bizMatterDepositoryMainService.findsonbyids(bean.getBody());
        if (findsonbyids.size() != 0) return ResultBean.error(-1, "选择删除的事项含有下级事项,无法删除");
        List<BizMatterDepositoryMain> byIds = bizMatterDepositoryMainService.findByIds(bean.getBody());
        List<BizMatterAuditMain> auditMains = new ArrayList<>();
        for (BizMatterDepositoryMain byId : byIds) {
            BizMatterAuditMain bizMatterAuditMain = new BizMatterAuditMain();
            bizMatterAuditMain.setCommitmentTime(byId.getCommitmentTime());
            bizMatterAuditMain.setUpmatterId("-2");
            bizMatterAuditMain.setMatterName(byId.getMatterName());
            bizMatterAuditMain.setDepartment(byId.getDepartment());
            bizMatterAuditMain.setKeyDescription(byId.getKeyDescription());
            bizMatterAuditMain.setMatterDescription(byId.getMatterDescription());
            bizMatterAuditMain.setMatterDepositoryMainId(byId.getId());
            bizMatterAuditMain.setSortnum(byId.getSortnum());
            bizMatterAuditMain.setSkipped(byId.getSkipped());
            bizMatterAuditMain.setAttribute(byId.getAttribute());
            bizMatterAuditMain.setAuditState(AuditEnum.AuditState.PENDING.getKey());
            bizMatterAuditMain.setOperationStatus(2);
            bizMatterAuditMain.setTerritory(byId.getTerritory());
            bizMatterAuditMain.setProcessFlow(byId.getProcessFlow());
            auditMains.add(bizMatterAuditMain);
        }
        bizMatterAuditMainService.saveAll(auditMains);
        return new ResultBean<>();
    }

    @PostMapping("/AuditDeleteMatter")
    @ApiOperation(value = "审核删除事项库数据")
    @ControllerMonitor(description = "审核删除事项库数据", operType = 4)
    public ResultBean<String> AuditDeleteMatter(@RequestBody ReqBean<AuditDeleteQuery> bean) {
        AuditDeleteQuery body = bean.getBody();
        CheckClass.check(body);
        BizMatterAuditMain byId = bizMatterAuditMainService.findById(body.getAuditedId());
        CheckUtil.check(byId!=null,ResultCode.COMMON_ERROR,"数据不存在");
        if(body.getAuditState()==AuditEnum.AuditState.ADOPT.getKey()){
            bizMatterDepositoryMainService.deleteById(byId.getMatterDepositoryMainId());
            matterSubjectService.deleteBySituationId(byId.getMatterDepositoryMainId());
            byId.setAuditState(AuditEnum.AuditState.ADOPT.getKey());
        }else if(body.getAuditState()==AuditEnum.AuditState.REFUES.getKey()){
            byId.setAuditState(AuditEnum.AuditState.REFUES.getKey());
            BizAudit byAuditedId = bizAuditService.findByAuditedId(body.getAuditedId());
            if (byAuditedId == null) {
                //创建审核表数据
                BizAudit bizAudit = new BizAudit();
                bizAudit.setObjectType(AuditEnum.Audittype.MATTER.getKey());
                bizAudit.setAuditedId(body.getAuditedId());
                bizAuditService.saveOrUpdate(bizAudit);
                //创建审核意见表数据
                BizAuditOpinion bizAuditOpinion = new BizAuditOpinion();
                BizAudit byAuditedId2 = bizAuditService.findByAuditedId(body.getAuditedId());
                bizAuditOpinion.setAuditId(byAuditedId2.getId());
                bizAuditOpinion.setAuditOpinion(body.getAuditOpinion());
                bizAuditOpinion = bizAuditOpinionService.saveOrUpdate(bizAuditOpinion);
            } else {
                //若已经存在审核表 则只需要修改意见表的数据
                BizAuditOpinion byAuditId = bizAuditOpinionService.findByAuditId(byAuditedId.getId());
                byAuditId.setAuditOpinion(body.getAuditOpinion());
                byAuditId = bizAuditOpinionService.saveOrUpdate(byAuditId);
            }
        }
        bizMatterAuditMainService.saveOrUpdate(byId);
        return new ResultBean<>();
    }


    @PostMapping("/online")
    @ApiOperation(value = "事项库下线")
    @ControllerMonitor(description = "事项库下线", operType = 3)
    public ResultBean<BizMatterDepositoryMain> online(@RequestBody ReqBean<String> bean) {
        BizMatterDepositoryMain byId = bizMatterDepositoryMainService.findById(bean.getBody());
        byId.setOnline(!byId.getOnline());
        return new ResultBean<>(bizMatterDepositoryMainService.saveOrUpdate(byId));
    }

    @PostMapping("/findsituation")
    @ApiOperation(value = "查找子情形和目录")
    @ControllerMonitor(description = "查找子情形和目录", operType = 1)
    public ResultBean<SituationRes> findsituation(@RequestBody ReqBean<String> bean, HttpServletRequest request) {
        String token = request.getHeader(Constants.TOKEN);
        SessionUser user = (SessionUser) redisManager.getSessionUser(token);
        List<MatterRes> matterRes = new ArrayList<>();

        BizMatterDepositoryMain byId = bizMatterDepositoryMainService.findById(bean.getBody());
        SituationRes situationRes = new SituationRes();
        if(byId.getAttribute()==MatterEnum.Attribute.Situation.getKey()){
            Specification<BizMatterDepositoryMain> specification = Specifications.<BizMatterDepositoryMain>and()
                    .eq("attribute", MatterEnum.Attribute.Situation.getKey())
                    .eq("upmatterId", bean.getBody())
                    .in("createBy", user.getCreater())
                    .build();
            List<BizMatterDepositoryMain> all = bizMatterDepositoryMainService.findAll(specification);
            situationRes.setSituation(all);
        }
        else{
            Specification<BizMatterDepositoryMain> specification = Specifications.<BizMatterDepositoryMain>and()
                    .eq("upmatterId", bean.getBody())
                    .in("createBy", user.getCreater())
                    .build();
            List<BizMatterDepositoryMain> all = bizMatterDepositoryMainService.findAll(specification);
            situationRes.setCatalog(all);
        }
        return new ResultBean<>(situationRes);
    }
    
    /**
     * @Title: pageDepositoryMetter
     * @Description: 分页查询事项库数据
     * @param bean
     * @return ResultBean<PageInfo> 返回类型
     * @throws
     */
    @PostMapping("/pageDepositoryMetter")
    @ApiOperation(value = "分页查询事项库数据")
    @ControllerMonitor(description = "分页查询事项库数据")
    public ResultBean<PageInfo> pageDepositoryMetter(@RequestBody ReqBean<MatterQuery> bean) {
    	return bizMatterDepositoryMainService.pageDepositoryMetter(bean);
    }
    
    /**
     * @Title: queryDepositoryByParent
     * @Description: 根据父级ID查询子事项
     * @param bean
     * @return ResultBean<List<BizMatterDepositoryMain>> 返回类型
     * @throws
     */
    @PostMapping("/queryDepositoryByParent")
    @ApiOperation(value = "根据父级ID查询子事项")
    @ControllerMonitor(description = "根据父级ID查询子事项")
    public ResultBean<List<BizMatterDepositoryMain>> queryDepositoryByParent(@RequestBody ReqBean<String> bean){
    	return new ResultBean<List<BizMatterDepositoryMain>>(bizMatterDepositoryMainService.queryChildsByParent(bean.getBody()));
    }
    
    @PostMapping("/findById")
    @ApiOperation(value = "根据id查询事项")
    @ControllerMonitor(description = "根据id查询事项", operType = 1)
    public ResultBean<BizMatterDepositoryMain> findsituation(@RequestBody ReqBean<String> bean) {
    	CheckUtil.notNull(bean.getBody(), ResultCode.PARAM_NULL, "id");
    	BizMatterDepositoryMain main = bizMatterDepositoryMainService.findById(bean.getBody());
    	if(main != null && StringUtils.isNotBlank(main.getProcessFlow())) {
        	main.setProcessFlowFiles(fileService.findByReferenceId(main.getProcessFlow()));
        }
    	return new ResultBean<BizMatterDepositoryMain>(main);
    }
}