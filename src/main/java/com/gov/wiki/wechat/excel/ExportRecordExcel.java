package com.gov.wiki.wechat.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;


@Data
@ApiModel(value = "ExportRecordExcel",description = "产品配件类")
public class ExportRecordExcel {
    /**
     * 序号
     * 材料名称
     * 材料类型
     * 是否必要
     * 材料验收
     * 材料数量
     * 材料备注
     */

    /**
     * 序号
     */
    @ExcelProperty(index = 0,value = "序号")
    private Integer sort;

    /**
     * 材料名称
     */
    @ExcelProperty(index = 1,value = "材料名称")
    private String materialName;


    /**
     * 资料类型
     * 0：证照，批文，证明，表单，其他
     */

    @ExcelProperty(index = 2,value = "材料类型")
    private String materialType;

    /**
     * 必要性0-非必要 1-必要
     */
    @ExcelProperty(index = 3,value = "是否必要")
    private String necessitystr;

    /**
     * 材料验收
     */
    @ExcelProperty(index = 4,value = "材料验收")
    private String checkAccept;


    /**
     * 材料数量
     */
    @ExcelProperty(index = 5,value = "材料数量")
    private Integer qty;

    /**
     * 材料备注
     */
    @ExcelProperty(index = 6,value = "材料备注")
    private String remark;

}
