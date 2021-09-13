package com.gov.wiki.business.controller;

import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.business.req.AuditQuery;
import com.gov.wiki.business.service.*;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.entity.buss.*;
import com.gov.wiki.common.entity.system.SysFile;
import com.gov.wiki.common.enums.AuditEnum;
import com.gov.wiki.common.utils.BeanUtils;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.system.service.IFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/Audit")
@RestController
@Api(tags = "审核管理")
public class AuditController {
    @Autowired
    private BizAuditService bizAuditService;

    @Autowired
    private BizAuditOpinionService bizAuditOpinionService;

    @Autowired
    private BizMaterialAuditService bizMaterialAuditService;

    @Autowired
    private BizMaterialDepositoryService bizMaterialDepositoryService;

    @Autowired
    private BizMatterAuditMainService bizMatterAuditMainService;

    @Autowired
    private BizMatterDepositoryMainService bizMatterDepositoryMainService;

    @Autowired
    private BizMatterAuditSlaveService bizMatterAuditSlaveService;

    @Autowired
    private ISubjectAuditService subjectAuditService;

    @Autowired
    private IFileService fileService;

    /**
     * @Title: submitaudit
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param bean
     * @return ResultBean<String> 返回类型
     * @throws
     */
    @PostMapping
    @ApiOperation(value = "提交审核意见")
    @ControllerMonitor(description = "提交审核意见", operType = 3)
    public ResultBean<String> submitaudit(@RequestBody ReqBean<AuditQuery> bean) {
        AuditQuery body = bean.getBody();
        if (AuditEnum.Audittype.MATERIAL.getKey()==body.getObjectType()) {
            BizMaterialAudit bizMaterialAudit = bizMaterialAuditService.findById(body.getAuditedId());
            if (bizMaterialAudit.getAuditState() == AuditEnum.AuditState.ADOPT.getKey()) return ResultBean.error(-1, "不能修改审核通过的信息");
        } else if (AuditEnum.Audittype.MATTER.getKey()==body.getObjectType()) {
            BizMatterAuditMain bizMatterAuditMain = bizMatterAuditMainService.findById(body.getAuditedId());
            if (bizMatterAuditMain.getAuditState() == AuditEnum.AuditState.ADOPT.getKey()) return ResultBean.error(-1, "不能修改审核通过的信息");
        }
        else if(AuditEnum.Audittype.SUBJECT.getKey()==body.getObjectType()){
            BizSubjectAudit subjectAudit = subjectAuditService.findById(body.getAuditedId());
            //if (subjectAudit.getAuditState() == AuditEnum.AuditState.ADOPT.getKey()) return ResultBean.error(-1, "不能修改审核通过的信息");
        }
        else{
            return ResultBean.error(ResultCode.DATA_NOT_EXIST.getCode(),body.getObjectType().toString());
        }


        //更新审核表以及审核意见表信息
        BizAudit byAuditedId = bizAuditService.findByAuditedId(body.getAuditedId());
        if (byAuditedId == null) {
            //创建审核表数据
            BizAudit bizAudit = new BizAudit();
            if (AuditEnum.Audittype.MATERIAL.getKey()==body.getObjectType()) {
                bizAudit.setObjectType(AuditEnum.Audittype.MATERIAL.getKey());
            } else if (AuditEnum.Audittype.MATTER.getKey()==body.getObjectType()) {
                bizAudit.setObjectType(AuditEnum.Audittype.MATTER.getKey());
            }
            else if(AuditEnum.Audittype.SUBJECT.getKey()==body.getObjectType()){
                bizAudit.setObjectType(AuditEnum.Audittype.SUBJECT.getKey());
            }
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


        //更新审核资料(事项)的审核状态
        if (AuditEnum.Audittype.MATERIAL.getKey()==body.getObjectType()) {
            BizMaterialAudit bizMaterialAudit = bizMaterialAuditService.findById(body.getAuditedId());
            bizMaterialAudit.setAuditState(body.getAuditState());
            //如果审核通过则向资料库保存当前资料信息
            if (body.getAuditState() == AuditEnum.AuditState.ADOPT.getKey()) {
                BizMaterialDepository bizMaterialDepository = BeanUtils.copyProperties(bizMaterialAudit, BizMaterialDepository.class);
                //bizMaterialDepository.setId(bizMaterialAudit.getMaterialDepositoryId());
                if(StringUtils.isNotBlank(bizMaterialAudit.getMaterialDepositoryId())) {
                	bizMaterialDepositoryService.recoveryMaterial(bizMaterialAudit.getMaterialDepositoryId());
                	bizMaterialAudit.setHisDepositoryId(bizMaterialAudit.getMaterialDepositoryId());
                }
                List<SysFile> fList = fileService.findByReferenceId(bizMaterialAudit.getId());
                List<SysFile> sysFiles = new ArrayList<>();
                for (SysFile sysFile : fList) {
                    SysFile sysFile1 = new SysFile();
                    sysFile1.setFileSize(sysFile.getFileSize());
                    sysFile1.setFileName(sysFile.getFileName());
                    sysFile1.setFileSuffix(sysFile.getFileSuffix());
                    sysFile1.setFileUrl(sysFile.getFileUrl());
                    sysFile1.setThumbnailUrl(sysFile.getThumbnailUrl());
                    sysFile1.setSortNo(sysFile.getSortNo());
                    sysFile1.setMimeType(sysFile.getMimeType());
                    sysFile1.setAlias(sysFile.getAlias());
                    sysFiles.add(sysFile1);
                }
                bizMaterialDepository.setSysFiles(sysFiles);
                bizMaterialDepository = bizMaterialDepositoryService.saveOrUpdate(bizMaterialDepository);
                bizMaterialAudit.setMaterialDepositoryId(bizMaterialDepository.getId());
                bizMaterialDepositoryService.replaceMaterialReference(bizMaterialAudit.getHisDepositoryId(), bizMaterialAudit.getMaterialDepositoryId());
            }
            bizMaterialAuditService.saveOrUpdate(bizMaterialAudit);
            return new ResultBean("修改成功");
        } else if (AuditEnum.Audittype.MATTER.getKey()==body.getObjectType()) {
            BizMatterAuditMain bizMatterAuditMain = bizMatterAuditMainService.findById(body.getAuditedId());
            bizMatterAuditMain.setAuditState(body.getAuditState());
            if (body.getAuditState() == AuditEnum.AuditState.ADOPT.getKey()) {
                BizMatterDepositoryMain bizMatterDepositoryMain = new BizMatterDepositoryMain(bizMatterAuditMain);
                bizMatterDepositoryMain.setId(bizMatterAuditMain.getMatterDepositoryMainId());
                List<BizMatterAuditSlave> byMatterAuditId = bizMatterAuditSlaveService.findByMatterAuditId(bizMatterAuditMain.getId());
                List<BizMatterDepositorySlave> bizMatterDepositorySlaves = new ArrayList<>();
                for (BizMatterAuditSlave bizMatterAuditSlave : byMatterAuditId) {
                    BizMatterDepositorySlave bizMatterDepositorySlave = new BizMatterDepositorySlave();
                    bizMatterDepositorySlave.setMaterialDepositoryId(bizMatterAuditSlave.getMaterialDepositoryId());
                    bizMatterDepositorySlave.setNumber(bizMatterAuditSlave.getNumber());
                    bizMatterDepositorySlave.setInspect(bizMatterAuditSlave.getInspect());
                    bizMatterDepositorySlave.setCollect(bizMatterAuditSlave.getCollect());
                    bizMatterDepositorySlave.setNecessity(bizMatterAuditSlave.getNecessity());
                    bizMatterDepositorySlaves.add(bizMatterDepositorySlave);
                }
                bizMatterDepositoryMain.setBizMatterDepositorySlaves(bizMatterDepositorySlaves);
                bizMatterDepositoryMain = bizMatterDepositoryMainService.saveOrUpdate(bizMatterDepositoryMain);
                bizMatterAuditMain.setMatterDepositoryMainId(bizMatterDepositoryMain.getId());
            }
            bizMatterAuditMainService.saveOrUpdate(bizMatterAuditMain);
            return new ResultBean("修改成功");
        }
		/*
		 * else if(AuditEnum.Audittype.SUBJECT.getKey()==body.getObjectType()){
		 * BizSubjectAudit bizSubjectAudit =
		 * subjectAuditService.findById(body.getAuditedId());
		 * bizSubjectAudit.setAuditState(body.getAuditState()); if (body.getAuditState()
		 * == AuditEnum.AuditState.ADOPT.getKey()) { BizSubject bizSubject = new
		 * BizSubject(bizSubjectAudit);
		 * bizSubject.setId(bizSubjectAudit.getSubjectId());
		 * bizSubject=subjectService.saveOrUpdate(bizSubject); List<PrivMatterSubject>
		 * bySubjectId = matterSubjectService.findBySubjectId(body.getAuditedId());
		 * List<PrivMatterSubject> privMatterSubjects=new ArrayList<>(); for
		 * (PrivMatterSubject privMatterSubject : bySubjectId) { PrivMatterSubject
		 * privMatterSubject1 = new PrivMatterSubject();
		 * privMatterSubject1.setSituationId(privMatterSubject.getSituationId());
		 * privMatterSubject1.setUpSituationId(privMatterSubject.getUpSituationId());
		 * privMatterSubject1.setSubjectId(bizSubject.getId());
		 * privMatterSubjects.add(privMatterSubject1); }
		 * matterSubjectService.saveAll(privMatterSubjects); }
		 * subjectAuditService.saveOrUpdate(bizSubjectAudit); return new
		 * ResultBean("修改成功"); }
		 */

        return ResultBean.error(-1, "系统异常");
    }
}