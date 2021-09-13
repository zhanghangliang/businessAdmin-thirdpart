package com.gov.wiki.wechat.listener;

import com.gov.wiki.wechat.excel.ExportRecordExcel;

public class ExportRecordExcelListener extends BaseExcelListener<ExportRecordExcel> {
    public static final String TEMPLATE_NAME="材料清单.xls";
    @Override
    public void setTemplatName(String templatName) {this.templatName=TEMPLATE_NAME;
    }
}
