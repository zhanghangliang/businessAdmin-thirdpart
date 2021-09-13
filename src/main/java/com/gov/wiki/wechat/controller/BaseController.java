package com.gov.wiki.wechat.controller;

import com.alibaba.excel.EasyExcel;
import com.gov.wiki.common.utils.DateUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

public class BaseController {
    /**
     * excel通过EasyExcel导出，导出时文件名会自动加上当前时间
     * @param response
     * @param clazz
     * 			类
     * @param data
     * 			导出的数据
     * @param fileName
     * 			导出的文件名称
     * @throws IOException
     */
    protected void exportExcel(HttpServletResponse response,
                               Class<?> clazz,
                               List<?> data,
                               String fileName) throws IOException {
        if(fileName == null) fileName = "";
        fileName += DateUtils.getDateFormat("yyyy-MM-dd");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream()).head(clazz).sheet("导出数据").doWrite(data);
    }
}
