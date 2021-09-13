package com.gov.wiki.business.service.impl;

import com.github.wenhao.jpa.PredicateBuilder;
import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.business.req.query.YearQuery;
import com.gov.wiki.business.res.AnnualDataRes;
import com.gov.wiki.business.res.LiftRateRes;
import com.gov.wiki.business.service.StatisticsUtilService;
import com.gov.wiki.business.service.UserStatisticsService;
import com.gov.wiki.common.entity.wechat.WxMember;
import com.gov.wiki.common.entity.wechat.WxOperationRecord;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.DateUtils;
import com.gov.wiki.wechat.dao.WxMemberDao;
import com.gov.wiki.wechat.dao.WxOperationRecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("userStatisticsService")
public class UserStatisticsServiceImpl extends BaseServiceImpl<WxMember, String, WxMemberDao> implements UserStatisticsService {

    @Autowired
    private StatisticsUtilService statisticsUtilService;

    @Autowired
    private WxOperationRecordDao wxOperationRecordDao;

    /**
     * @param query :
     * @decription TODO(统计用户提升率)
     * @return: com.gov.wiki.business.res.AnnualDataRes
     * @author liusq
     * @date 2021/5/10 10:28
     */
    @Override
    public LiftRateRes getUserLiftRate(YearQuery query) {
        LiftRateRes res = new LiftRateRes();
        //日期格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //时间集
        List<String> dateList = statisticsUtilService.getMonthByYear(query.getYear());
        //新增用户数集
        List<Long> addUserNumList = new ArrayList<>();
        //提交资料人数集
        List<Long> submitDataList = new ArrayList<>();

        dateList.forEach(s -> {
            //获取某一个月份当前时间的范围
            Date[] dates = new Date[0];
            try {
                dates = DateUtils.getRangeByMongth(simpleDateFormat.parse(s + "-01"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //新增用户统计
            PredicateBuilder<WxMember> builder = Specifications.and();
            builder.between("createTime", dates[0], dates[1]);
            long num = this.baseRepository.count(builder.build());
            addUserNumList.add(num);

            //提交资料人数统计
            PredicateBuilder<WxOperationRecord> predicateBuilder = Specifications.and();
            predicateBuilder.between("createTime", dates[0], dates[1]);
            long count = wxOperationRecordDao.count(predicateBuilder.build());
            submitDataList.add(count);
        });

        res.setDateList(dateList);
        res.setAddUserNumList(addUserNumList);
        res.setSubmitDataList(submitDataList);
        return res;
    }

    /**
     * @param query :
     * @decription TODO(统计实名注册)
     * @return: com.gov.wiki.business.res.AnnualDataRes
     * @author liusq
     * @mail 2301409946@qq.com
     * @date 2021/5/10 15:20
     * @version V1.0
     */
    @Override
    public AnnualDataRes getVerified(YearQuery query) {
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
            PredicateBuilder<WxMember> builder = Specifications.and();
            builder.between("createTime", dates[0], dates[1])
                    .isNotNull("idCard");
            long num = this.baseRepository.count(builder.build());
            dataList.add(num);
        });

        res.setDateList(dateList);
        res.setDataList(dataList);
        return res;
    }

    /**
     * @param query :
     * @decription TODO(统计活跃用户)
     * @return: com.gov.wiki.business.res.AnnualDataRes
     * @author liusq
     * @mail 2301409946@qq.com
     * @date 2021/5/10 15:53
     * @version V1.0
     */
    @Override
    public AnnualDataRes getActiveUser(YearQuery query) {
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
            PredicateBuilder<WxMember> builder = Specifications.and();
            builder.between("updateTime", dates[0], dates[1]);
            long num = this.baseRepository.count(builder.build());
            dataList.add(num);
        });

        res.setDateList(dateList);
        res.setDataList(dataList);
        return res;
    }
}
