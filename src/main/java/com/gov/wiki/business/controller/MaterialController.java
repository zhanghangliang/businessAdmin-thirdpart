package com.gov.wiki.business.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.business.enums.StatusEnum;
import com.gov.wiki.business.req.AuditDeleteQuery;
import com.gov.wiki.business.req.DataDelReq;
import com.gov.wiki.business.req.MaterialQuery;
import com.gov.wiki.business.req.MaterialReq;
import com.gov.wiki.business.req.query.MaterialExistQuery;
import com.gov.wiki.business.res.BizMaterialAuditRes;
import com.gov.wiki.business.res.BizMaterialDepositoryRes;
import com.gov.wiki.business.res.MaterialExistRes;
import com.gov.wiki.business.service.BizAuditOpinionService;
import com.gov.wiki.business.service.BizAuditService;
import com.gov.wiki.business.service.BizMaterialAuditService;
import com.gov.wiki.business.service.BizMaterialDepositoryService;
import com.gov.wiki.business.service.BizMatterAuditSlaveService;
import com.gov.wiki.business.service.BizMatterDepositorySlaveService;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.check.CheckClass;
import com.gov.wiki.common.entity.buss.BizAudit;
import com.gov.wiki.common.entity.buss.BizAuditOpinion;
import com.gov.wiki.common.entity.buss.BizMaterialAudit;
import com.gov.wiki.common.entity.buss.BizMaterialDepository;
import com.gov.wiki.common.entity.system.SysFile;
import com.gov.wiki.common.enums.AuditEnum;
import com.gov.wiki.common.utils.BeanUtils;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.config.FileConfig;
import com.gov.wiki.system.service.IFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @ClassName: MaterialController
 * @Description: 资料管理控制器
 * @author cys
 * @date 2020年11月24日
 */
@RequestMapping("/Material")
@RestController
@Api(tags = "资料管理")
public class MaterialController {
    @Autowired
    private BizMaterialAuditService bizMaterialAuditService;

    @Autowired
    private BizMaterialDepositoryService bizMaterialDepositoryService;

    @Autowired
    private BizMatterAuditSlaveService matterAuditSlaveService;

    @Autowired
    private BizMatterDepositorySlaveService matterDepositorySlaveService;

    @Autowired
    private IFileService fileService;

    @Autowired
    private FileConfig customPro;

    @Autowired
    private BizAuditService bizAuditService;
    @Autowired
    private BizAuditOpinionService bizAuditOpinionService;

    /**
     * @Title: pageAuditMaterialList
     * @Description: 分页查询资料审核记录
     * @param bean
     * @param request
     * @return ResultBean<Page<BizMaterialAudit>> 返回类型
     * @throws
     */
    @PostMapping("/allAuditMaterial")
    @ApiOperation(value = "分页查询资料审核记录")
    @ControllerMonitor(description = "分页查询资料审核记录")
    public ResultBean<Page<BizMaterialAudit>> pageAuditMaterialList(@RequestBody ReqBean<MaterialQuery> bean) {
        return new ResultBean<>(bizMaterialAuditService.pageList(bean));
    }

    /**
     * @Title: pageMaterialList
     * @Description: 分页查询资料库信息
     * @param bean
     * @param request
     * @return ResultBean<Page<BizMaterialDepository>> 返回类型
     * @throws
     */
    @PostMapping("/allMaterial")
    @ApiOperation(value = "分页查询资料库信息")
    @ControllerMonitor(description = "分页查询资料库信息")
    public ResultBean<Page<BizMaterialDepository>> pageMaterialList(@RequestBody ReqBean<MaterialQuery> bean) {
        return new ResultBean<>(bizMaterialDepositoryService.pageList(bean));
    }

    /**
     * @Title: saveOrUpdateAuditMaterial
     * @Description: 新增或修改资料审核记录
     * @param bean
     * @return ResultBean<String> 返回类型
     * @throws
     */
    @PostMapping("/saveOrUpdateMaterial")
    @ApiOperation(value = "新增或修改资料审核记录")
    @ControllerMonitor(description = "新增或修改资料审核记录", operType = 2)
    public ResultBean<BizMaterialAudit> saveOrUpdateAuditMaterial(@RequestBody ReqBean<MaterialReq> bean) {
        return new ResultBean<BizMaterialAudit>(bizMaterialAuditService.saveOrUpdate(bean.getBody()));
    }

    /**
     * @Title: queryMaterial
     * @Description: 根据编号查询资料
     * @param bean
     * @return ResultBean<BizMaterialAuditRes> 返回类型
     * @throws
     */
    @PostMapping("/Material")
    @ApiOperation(value = "根据编号查询资料")
    @ControllerMonitor(description = "根据编号查询资料")
    public ResultBean<BizMaterialAuditRes> queryMaterial(@RequestBody ReqBean<String> bean) {
        BizMaterialAuditRes bizMaterialAuditRes = new BizMaterialAuditRes();
        BizMaterialDepository audit = bizMaterialDepositoryService.findById(bean.getBody());
        CheckUtil.notNull(audit, ResultCode.DATA_NOT_EXIST, "数据");
        List<SysFile> files = fileService.findByReferenceId(audit.getId());
        bizMaterialAuditRes.setBizMaterialAudit(audit);
        bizMaterialAuditRes.setFiles(files);
        return new ResultBean<BizMaterialAuditRes>(bizMaterialAuditRes);
    }
    
    /**
     * @Title: findMaterialHistoryById
     * @Description: 根据资料维护或审核Id查询资料
     * @param bean
     * @return ResultBean<BizMaterialAudit> 返回类型
     * @throws
     */
    @PostMapping("/findMaterialHistoryById")
    @ApiOperation(value = "根据资料维护或审核Id查询资料")
    @ControllerMonitor(description = "根据资料维护或审核Id查询资料")
    public ResultBean<BizMaterialAudit> findMaterialHistoryById(@RequestBody ReqBean<String> bean) {
    	BizMaterialAudit audit = bizMaterialAuditService.findById(bean.getBody());
    	return new ResultBean<BizMaterialAudit>(audit);
    }

    /**
     * @Title: deleteAllMaterial
     * @Description: 批量删除资料库数据
     * @param bean
     * @return ResultBean<String> 返回类型
     * @throws
     */
    @PostMapping("/deleteAllMaterial")
    @ApiOperation(value = "批量删除资料库数据")
    @ControllerMonitor(description = "批量删除资料库数据", operType = 3)
    public ResultBean<String> deleteAllMaterial(@RequestBody ReqBean<DataDelReq> bean) {
    	DataDelReq req = bean.getBody();
    	CheckUtil.notNull(req, ResultCode.COMMON_ERROR, "请求参数不存在！");
    	List<String> ids = req.getDelIds();
    	CheckUtil.check(ids != null && !ids.isEmpty(), ResultCode.COMMON_ERROR, "待删除记录不存在！");
        List<BizMaterialDepository> materialDepositorys = bizMaterialDepositoryService.findByIds(ids);
        List<BizMaterialAudit> bizSubjectAudits = new ArrayList<>();
        for (BizMaterialDepository materialDepository : materialDepositorys) {
            BizMaterialAudit audit = BeanUtils.copyProperties(materialDepository, BizMaterialAudit.class);
            audit.setMaterialDepositoryId(materialDepository.getId());
            audit.setOperationStatus(2);
            audit.setOperReason(req.getReason());
            audit.setAuditState(StatusEnum.save.getValue());
            bizSubjectAudits.add(audit);
        }
        bizMaterialAuditService.saveAll(bizSubjectAudits);
        return new ResultBean<>();
    }

    /**
     * @Title: AuditDeleteMaterial
     * @Description: 审核删除资料库数据
     * @param bean
     * @return ResultBean<String> 返回类型
     * @throws
     */
    @PostMapping("/AuditDeleteMaterial")
    @ApiOperation(value = "审核删除资料库数据")
    @ControllerMonitor(description = "审核删除资料库数据", operType = 4)
    public ResultBean<String> AuditDeleteMaterial(@RequestBody ReqBean<AuditDeleteQuery> bean) {
        AuditDeleteQuery body = bean.getBody();
        CheckClass.check(body);
        BizMaterialAudit byId = bizMaterialAuditService.findById(body.getAuditedId());
        CheckUtil.check(byId!=null,ResultCode.COMMON_ERROR,"数据不存在");
        if(body.getAuditState()==AuditEnum.AuditState.ADOPT.getKey()){
            bizMaterialDepositoryService.deleteById(byId.getMaterialDepositoryId());
            matterAuditSlaveService.deleteByMaterialDepositoryId(byId.getMaterialDepositoryId());
            matterDepositorySlaveService.deleteByMaterialDepositoryId(byId.getMaterialDepositoryId());
            byId.setAuditState(AuditEnum.AuditState.ADOPT.getKey());
            byId.setOperationStatus(2);
        }else if(body.getAuditState()==AuditEnum.AuditState.REFUES.getKey()){
            byId.setAuditState(AuditEnum.AuditState.REFUES.getKey());
            BizAudit byAuditedId = bizAuditService.findByAuditedId(body.getAuditedId());
            if (byAuditedId == null) {
                //创建审核表数据
                BizAudit bizAudit = new BizAudit();
                bizAudit.setObjectType(AuditEnum.Audittype.SUBJECT.getKey());
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
        bizMaterialAuditService.saveOrUpdate(byId);

        return new ResultBean<>();
    }

    /**
     * @Title: MaterialDepositoryById
     * @Description: 根据编号查询资料库数据
     * @param bean
     * @return ResultBean<BizMaterialDepositoryRes> 返回类型
     * @throws
     */
    @PostMapping("/MaterialDepositoryById")
    @ApiOperation(value = "根据编号查询资料库数据")
    @ControllerMonitor(description = "根据编号查询资料库数据")
    public ResultBean<BizMaterialDepositoryRes> MaterialDepositoryById(@RequestBody ReqBean<String> bean) {
        BizMaterialDepositoryRes bizMaterialDepositoryRes = new BizMaterialDepositoryRes();
        BizMaterialDepository byId = bizMaterialDepositoryService.findById(bean.getBody());
        List<SysFile> byReferenceId = fileService.findByReferenceId(byId.getId());
        bizMaterialDepositoryRes.setBizMaterialDepository(byId);
        bizMaterialDepositoryRes.setFiles(byReferenceId);
        return new ResultBean<BizMaterialDepositoryRes>(bizMaterialDepositoryRes);
    }
    
    /**
     * @Title: queryExistMaterials
     * @Description: 查询存在名称资料信息
     * @param bean
     * @return ResultBean<List<MaterialExistRes>> 返回类型
     * @throws
     */
    @PostMapping("/queryExistMaterials")
    @ApiOperation(value = "查询存在名称资料信息")
    @ControllerMonitor(description = "查询存在名称资料信息")
    public ResultBean<List<MaterialExistRes>> queryExistMaterials(@RequestBody ReqBean<MaterialExistQuery> bean){
    	List<MaterialExistRes> resList = new ArrayList<MaterialExistRes>();
    	MaterialExistQuery query = bean.getBody();
    	if(query != null && StringUtils.isNotBlank(query.getKeywords())) {
    		List<MaterialExistRes> bmdList = bizMaterialDepositoryService.queryExistList(query);
    		if(bmdList != null && !bmdList.isEmpty()) {
    			resList.addAll(bmdList);
    		}
    		List<MaterialExistRes> bmaList = bizMaterialAuditService.queryExistList(query);
    		if(bmaList != null && !bmaList.isEmpty()) {
    			resList.addAll(bmaList);
    		}
    	}
    	return new ResultBean<List<MaterialExistRes>>(resList);
    }

    @GetMapping("/materialdownload")
    @ApiOperation(value = "资料文件下载")
    @ControllerMonitor(description = "资料文件下载")
    public ResultBean download(@RequestParam(value = "materialId", required = true) String materialId,
                               HttpServletRequest request, HttpServletResponse response) throws IOException {
        BizMaterialAudit byId = bizMaterialAuditService.findById(materialId);

        List<SysFile> byReferenceId=new ArrayList<>();
        if(byId.getAuditState()==2){
            byReferenceId = fileService.findByReferenceId(byId.getMaterialDepositoryId());
        }else{
            byReferenceId = fileService.findByReferenceId(materialId);
        }
        //压缩zip
        String filePath = "/home/govwiki/file" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".zip";
        File file1 = new File(filePath);
        if (!file1.getParentFile().exists()) {
            file1.getParentFile().mkdirs();
        }
        response.reset();
        response.setContentType("content-type:octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((filePath).getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        ZipArchiveOutputStream zous = new ZipArchiveOutputStream(out);
        zous.setUseZip64(Zip64Mode.AsNeeded);


        List<File> fileList = new ArrayList<>();
        for (SysFile s : byReferenceId) {
            String savePath = customPro.getSavePath();
            String showPrefix = customPro.getShowPrefix();
            File f = new File(savePath+s.getFileUrl().substring(showPrefix.length()+1));
            fileList.add(f);
        }
        for (File file : fileList) {
            String fileName = file.getName();
            InputStream inputStream = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            if (baos != null) {
                baos.flush();
            }
            byte[] bytes = baos.toByteArray();

            //设置文件名
            ZipArchiveEntry entry = new ZipArchiveEntry(fileName);
            zous.putArchiveEntry(entry);
            zous.write(bytes);
            zous.closeArchiveEntry();
            if (baos != null) {
                baos.close();
            }
        }
        if(zous!=null) {
            zous.close();
        }
        downloadExport(filePath, request, response);
        for (File file : fileList) {
            if(file.exists()) file.delete();
        }
        return new ResultBean();
    }

    protected void downloadExport(String filePath,HttpServletRequest request,
                                  HttpServletResponse response) throws IOException {
        String fileName;
        if(filePath.lastIndexOf("\\") >= 0) {
            fileName = filePath.substring(filePath.lastIndexOf("\\")+1);
        } else if(filePath.lastIndexOf("/") >= 0) {
            fileName = filePath.substring(filePath.lastIndexOf("/")+1);
        } else {
            fileName = filePath;
        }
        download(fileName, response, filePath);
    }

    private void download(String templetName, HttpServletResponse response, String fileUrl)
            throws FileNotFoundException, IOException, UnsupportedEncodingException {
        File file = new File(fileUrl);
        InputStream fis = new BufferedInputStream(new FileInputStream(file.getAbsolutePath()));
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        // 清空response
        response.reset();
        // 设置response的Header
        String mineType = fileUrl.substring(fileUrl.lastIndexOf("."));
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(templetName,"UTF-8"));
        response.addHeader("Content-Length", "" + file.length());
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());

        response.setContentType(mineType);
        toClient.write(buffer);
        toClient.flush();
        toClient.close();
    }
}