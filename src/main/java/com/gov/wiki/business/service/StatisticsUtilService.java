package com.gov.wiki.business.service;

import java.util.List;

/**
 * @author liusq
 * @decription TODO(统计公用方法)
 * @date 2021/5/10 14:00
 */
public interface StatisticsUtilService {

    /**
     * @param year: 年份
     * @decription TODO(获取某年的月份)
     * @return: java.util.List<java.lang.String>
     * @author liusq
     * @date 2021/5/10 14:03
     */
    List<String> getMonthByYear(int year);
}
