package com.gov.wiki.business.res;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "SlavesRes", description = "从表返回结果")
public class SlavesRes {
    //主键
    private String id;
    //编号
    private String materialId;
    //类型
    private Integer materialType;
    //前置
    private Boolean front;
    //名称
    private String materialName;

    //数量
    private Integer number;
    //验证
    private Boolean inspect;
    //收取
    private Integer collect;
    //必要性
    private Boolean necessity;

    private String materialDepositoryId;

    public SlavesRes() {
    }

    public SlavesRes(String id,String materialId, Integer materialType, Boolean front, String materialName, Integer number, Boolean inspect, Integer collect, Boolean necessity,String materialDepositoryId) {
        this.id=id;
        this.materialId = materialId;
        this.materialType = materialType;
        this.front = front;
        this.materialName = materialName;
        this.number = number;
        this.inspect = inspect;
        this.collect = collect;
        this.necessity = necessity;
        this.materialDepositoryId=materialDepositoryId;
    }
}
