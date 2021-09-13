/**
 * @Copyright: 成都北诺星科技有限公司  All rights reserved.Notice 官方网站：http://www.beinuoxing.com
 */
package com.gov.wiki.wechat.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.check.CheckClass;
import com.gov.wiki.common.exception.CheckException;
import com.gov.wiki.common.utils.CheckUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class BaseExcelListener<E>
        extends AnalysisEventListener<E> {

    /**
     * 人员列表Excel模版表头所占行数，默认3行
     */
    protected int templatHeadRowds = 0;
    /**
     * 模板名称
     */
    protected String templatName = "";

    protected int readCount = 0;
    protected List<E> list = new ArrayList<E>();

    protected List<String> errorMsg = new ArrayList<String>();


    public BaseExcelListener() {
        super();
    }

    public BaseExcelListener(int templatHeadRowds) {
        super();
        this.templatHeadRowds = templatHeadRowds;
    }

    @Override
    public void invoke(E data, AnalysisContext context) {
        // TODO Auto-generated method stub
        Integer rowNum = context.readRowHolder().getRowIndex();
        if (rowNum > templatHeadRowds - 1) {
            CheckClass.check(data);
            readCount++;
            list.add(data);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) {
        //未到解析行数的错误不抛
        Integer rowNum = context.readRowHolder().getRowIndex();
        if (rowNum <= templatHeadRowds - 1) {
            return;
        }
        rowNum++;
        String msg = null;
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
            msg = "第%s行，第%s列解析异常";
            msg = String.format(msg, excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex());
            log.error(msg);
        } else if (exception instanceof CheckException) {
            CheckException checkException = (CheckException) exception;
            msg = "第%s行解析错误，原因：%s";
            msg = String.format(msg, rowNum, checkException.getMessage1());
        } else {
            //其他异常
            msg = "第%s行解析错误";
            msg = String.format(msg, context.readRowHolder().getRowIndex());
            log.error(msg, exception);
        }
        CheckUtil.check(false, ResultCode.COMMON_ERROR, msg);
    }

    public String getTemplatName() {
        return templatName;
    }

    public abstract void setTemplatName(String templatName);

    public List<E> getList() {
        return this.list;
    }
}
