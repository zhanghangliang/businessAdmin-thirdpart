package com.gov.wiki.business.service.impl;

import com.github.wenhao.jpa.PredicateBuilder;
import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.business.req.query.YearQuery;
import com.gov.wiki.business.res.AnnualDataRes;
import com.gov.wiki.business.service.MaterialPreTrialService;
import com.gov.wiki.business.service.StatisticsUtilService;
import com.gov.wiki.common.entity.wechat.WxOperationRecord;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.DateUtils;
import com.gov.wiki.wechat.dao.WxOperationRecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("materialPreTrialService")
public class MaterialPreTrialServiceImpl extends BaseServiceImpl<WxOperationRecord, String, WxOperationRecordDao> implements MaterialPreTrialService {

    @Autowired
    private StatisticsUtilService statisticsUtilService;

    /**
     * @param query :
     * @decription TODO(统计材料预审并发量)
     * @return: com.gov.wiki.business.res.AnnualDataRes
     * @author liusq
     * @date 2021/5/11 10:13
     */
    @Override
    public AnnualDataRes getMaterialPreTrial(YearQuery query) {
        AnnualDataRes res = new AnnualDataRes();
        //日期格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //时间集
        List<String> dateList = statisticsUtilService.getMonthByYear(query.getYear());
        //数据
        List<Long> dataList = new ArrayList<>();

        dateList.forEach(s -> {
            //获取某一个月份当前时间的范围
            Date[] dates = new Date[0];
            try {
                dates = DateUtils.getRangeByMongth(simpleDateFormat.parse(s + "-01"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            PredicateBuilder<WxOperationRecord> builder = Specifications.and();
            builder.between("createTime", dates[0], dates[1]);
            long num = this.baseRepository.count(builder.build());
            dataList.add(num);
        });

        res.setDateList(dateList);
        res.setDataList(dataList);
        return res;
    }
}