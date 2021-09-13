package com.gov.wiki.organization.controller;

import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.business.service.BizMaterialSourceService;
import com.gov.wiki.business.service.BizMatterTypeService;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.entity.buss.BizMaterialSource;
import com.gov.wiki.common.entity.buss.BizMatterType;
import com.gov.wiki.common.entity.system.OrgCompany;
import com.gov.wiki.common.enums.AuditEnum;
import com.gov.wiki.common.enums.MaterialEnum;
import com.gov.wiki.common.enums.MatterEnum;
import com.gov.wiki.common.enums.StudyEnum;
import com.gov.wiki.common.utils.ReqBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequestMapping("/enum")
@RestController
@Api(tags = "枚举获取")
public class EnumController {
    @Autowired
    private BizMatterTypeService matterTypeService;
    @Autowired
    private BizMaterialSourceService materialSourceService;
    @PostMapping("/audit")
    @ApiOperation(value = "审核枚举类获取")
    @ControllerMonitor(description = "审核枚举类获取", operType = 1)
    public ResultBean<List<HashMap<String,Object>>> audit(@RequestBody ReqBean<String> bean) {
        String body = bean.getBody();
        List<HashMap<String,Object>> ans=new ArrayList<>();
        switch (body){
            //审核状态
            case "AuditState":
                AuditEnum.AuditState[] auditStates = AuditEnum.AuditState.values();
                for (AuditEnum.AuditState value : auditStates) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id",value.getKey());
                    map.put("value",value.getValue());
                    ans.add(map);
                }
                break;
            //审核类型
            case "Audittype":
                AuditEnum.Audittype[] audittypes = AuditEnum.Audittype.values();
                for (AuditEnum.Audittype value : audittypes) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id",value.getKey());
                    map.put("value",value.getValue());
                    ans.add(map);
                }
                break;
        }
        return new ResultBean(ans);
    }


    @PostMapping("/material")
    @ApiOperation(value = "资料枚举类获取")
    @ControllerMonitor(description = "资料枚举类获取", operType = 1)
    public ResultBean<List<HashMap<String,Object>>> material(@RequestBody ReqBean<String> bean) {
        String body = bean.getBody();
        List<HashMap<String,Object>> ans=new ArrayList<>();
        switch (body){
            //文件类型
            case "MaterialType":
                MaterialEnum.MaterialType[] auditStates = MaterialEnum.MaterialType.values();
                for (MaterialEnum.MaterialType value : auditStates) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id",value.getKey());
                    map.put("value",value.getValue());
                    ans.add(map);
                }
                break;
            //操作状态
            case "OperationStatus":
                MaterialEnum.OperationStatus[] audittypes = MaterialEnum.OperationStatus.values();
                for (MaterialEnum.OperationStatus value : audittypes) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id",value.getKey());
                    map.put("value",value.getValue());
                    ans.add(map);
                }
                break;
            case "StudyType":
                StudyEnum.StudyType[] studyTypes=StudyEnum.StudyType.values();
                for (StudyEnum.StudyType value : studyTypes) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id",value.getKey());
                    map.put("value",value.getValue());
                    ans.add(map);
                }
                break;
            case "MaterialSource":
                List<BizMaterialSource> all = materialSourceService.findAll();
                for (BizMaterialSource matterType : all) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id",matterType.getMaterialKey());
                    map.put("value",matterType.getMaterialValue());
                    map.put("realid",matterType.getId());
                    ans.add(map);
                }
                break;
        }
        return new ResultBean(ans);
    }

    @PostMapping("/matter")
    @ApiOperation(value = "事项枚举类获取")
    @ControllerMonitor(description = "事项枚举类获取", operType = 1)
    public ResultBean<List<HashMap<String,Object>>> matter(@RequestBody ReqBean<String> bean) {
        String body = bean.getBody();
        List<HashMap<String,Object>> ans=new ArrayList<>();
        switch (body){
            //所属地
            case "Territory":
                MatterEnum.Territory[] auditStates = MatterEnum.Territory.values();
                for (MatterEnum.Territory value : auditStates) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id",value.getKey());
                    map.put("value",value.getValue());
                    ans.add(map);
                }
                break;
            case "MatterType":
                List<BizMatterType> all = matterTypeService.findAll();
                for (BizMatterType matterType : all) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id",matterType.getMatterKey());
                    map.put("value",matterType.getMatterValue());
                    map.put("describe",matterType.getMatterDescribe());
                    map.put("realid",matterType.getId());
                    ans.add(map);
                }
                break;
            case "Attribute":
                MatterEnum.Attribute[] attributes = MatterEnum.Attribute.values();
                for (MatterEnum.Attribute value : attributes) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id",value.getKey());
                    map.put("value",value.getValue());
                    ans.add(map);
                }
                break;
        }
        return new ResultBean(ans);
    }
}
