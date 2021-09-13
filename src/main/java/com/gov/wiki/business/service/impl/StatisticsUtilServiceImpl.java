package com.gov.wiki.business.service.impl;

import com.gov.wiki.business.service.StatisticsUtilService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author liusq
 * @decription TODO(统计公用方法)
 * @date 2021/5/10 14:00
 */
@Service
public class StatisticsUtilServiceImpl implements StatisticsUtilService {
    /**
     * @param year : 年份
     * @decription TODO(获取某年的月份)
     * @return: java.util.List<java.lang.String>
     * @author liusq
     * @date 2021/5/10 14:03
     */
    @Override
    public List<String> getMonthByYear(int year) {
        //时间集
        List<String> dateList = new ArrayList<>();
        if (ObjectUtils.isEmpty(year)) {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
        }
        //递减
        for (int i = 12; i > 0; i--) {
            if (i < 10) {
                dateList.add(0, year + "-0" + i);
            } else {
                dateList.add(0, year + "-" + i);
            }
        }
        return dateList;
    }
}
