package com.gov.wiki.business.service.impl;

import com.github.wenhao.jpa.PredicateBuilder;
import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.business.req.query.MonthQuery;
import com.gov.wiki.business.req.query.YearQuery;
import com.gov.wiki.business.res.AnnualDataRes;
import com.gov.wiki.business.res.RespondPromptlyRes;
import com.gov.wiki.business.service.DialogueService;
import com.gov.wiki.business.service.StatisticsUtilService;
import com.gov.wiki.common.entity.IdEntity;
import com.gov.wiki.common.entity.wechat.WxOperationRecord;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.DateUtils;
import com.gov.wiki.message.dao.McGroupDao;
import com.gov.wiki.message.dao.McGroupMemberDao;
import com.gov.wiki.message.entity.McGroup;
import com.gov.wiki.message.entity.McGroupMember;
import com.gov.wiki.wechat.dao.WxOperationRecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service("activeDialogueService")
public class DialogueServiceImpl extends BaseServiceImpl<McGroup, String, McGroupDao> implements DialogueService {

    @Autowired
    private StatisticsUtilService statisticsUtilService;

    @Autowired
    private WxOperationRecordDao wxOperationRecordDao;

    @Autowired
    private McGroupMemberDao mcGroupMemberDao;

    //数量
    long oneNum = 0L;
    long twoNum = 0L;
    long threeNum = 0L;
    long fourNum = 0L;

    /**
     * @param query :
     * @decription TODO(统计主动对话次数 - 统计人人对话次数)
     * @return: com.gov.wiki.business.res.AnnualDataRes
     * @author liusq
     * @date 2021/5/11 10:39
     */
    @Override
    public AnnualDataRes getActiveDialogue(YearQuery query) {
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
            PredicateBuilder<McGroup> builder = Specifications.and();
            builder.between("createTime", dates[0], dates[1])
                    .ne("status", 1);
            long num = this.baseRepository.count(builder.build());
            dataList.add(num);
        });

        res.setDateList(dateList);
        res.setDataList(dataList);
        return res;
    }

    /**
     * @param query :
     * @decription TODO(统计人机对话次数)
     * @return: com.gov.wiki.business.res.AnnualDataRes
     * @author liusq
     * @date 2021/5/11 11:04
     */
    @Override
    public AnnualDataRes getManMachineDialogue(YearQuery query) {
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
            builder.between("createTime", dates[0], dates[1])
                    .eq("recordType", 1);
            long num = wxOperationRecordDao.count(builder.build());
            dataList.add(num);
        });

        res.setDateList(dateList);
        res.setDataList(dataList);
        return res;
    }

    /**
     * @param query :
     * @decription TODO(统计及时响应率)
     * @return: com.gov.wiki.business.res.RespondPromptlyRes
     * @author liusq
     * @mail 2301409946@qq.com
     * @date 2021/5/11 11:40
     * @version V1.0
     */
    @Override
    public RespondPromptlyRes getRespondPromptly(MonthQuery query) {
        RespondPromptlyRes res = new RespondPromptlyRes();
        //日期格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //第一段区间
        double oneMin = 0.00, oneMax = 5.00;
        //第二段区间
        double twoMin = 5.00, twoMax = 10.00;
        //第三段区间
        double threeMin = 10.00, threeMax = 60.00;

        //数据
        Map<String, Long> dataMap = new HashMap<>();

        Calendar cal = Calendar.getInstance();
        //获取年份
        int year = cal.get(Calendar.YEAR);
        if (ObjectUtils.isEmpty(query.getMonth()) || query.getMonth() == 0) {
            //获取月份
            query.setMonth(cal.get(Calendar.MONTH) + 1);
        }
        //获取日
        int day = cal.get(Calendar.DATE);
        Date[] dates = new Date[0];
        try {
            dates = DateUtils.getRangeByMongth(simpleDateFormat.parse(year + "-" + query.getMonth() + "-" + day));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //组成员
        PredicateBuilder<McGroupMember> groupMember = Specifications.and();
        groupMember.between("createTime", dates[0], dates[1]);
        List<McGroupMember> groupMemberList = mcGroupMemberDao.findAll(groupMember.build());
        //组id
        List<String> groupIds = groupMemberList.parallelStream().map(McGroupMember::getGroupId).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(groupIds)) {
            dataMap.put(oneMin + "分钟~" + oneMax + "分钟", oneNum);
            dataMap.put(twoMin + "分钟~" + twoMax + "分钟", twoNum);
            dataMap.put(threeMin + "分钟~" + threeMax + "分钟", threeNum);
            dataMap.put(threeMax + "分钟以上", fourNum);
            res.setDataMap(dataMap);
            return res;
        }
        //群组
        PredicateBuilder<McGroup> builder = Specifications.and();
        builder.in("id", groupIds);
        Map<String, McGroup> groupMap = this.baseRepository.findAll(builder.build()).parallelStream().collect(Collectors.toMap(IdEntity::getId, s -> s));

        groupMemberList.forEach(s -> {
            Optional.ofNullable(groupMap.get(s.getGroupId())).ifPresent(group -> {
                //两者相差多少分钟
                double minute = differenceMinute(s.getCreateTime(), group.getCreateTime());
                if (minute >= oneMin && minute <= oneMax) {
                    oneNum++;
                } else if (minute > twoMin && minute <= twoMax) {
                    twoNum++;
                } else if (minute > threeMin && minute <= threeMax) {
                    threeNum++;
                } else {
                    fourNum++;
                }
            });
        });

        dataMap.put(oneMin + "分钟~" + oneMax + "分钟", oneNum);
        dataMap.put(twoMin + "分钟~" + twoMax + "分钟", twoNum);
        dataMap.put(threeMin + "分钟~" + threeMax + "分钟", threeNum);
        dataMap.put(threeMax + "分钟以上", fourNum);
        res.setDataMap(dataMap);

        //清零
        oneNum = 0L;
        twoNum = 0L;
        threeNum = 0L;
        fourNum = 0L;
        return res;
    }

    /**
     * @param beReduced:
     * @param less:
     * @decription TODO(计算两者相差多少分钟) less - beReduced
     * @return: int
     * @author liusq
     * @mail 2301409946@qq.com
     * @date 2021/5/11 14:18
     * @version V1.0
     */
    private double differenceMinute(Date beReduced, Date less) {
        //天
        long day = (less.getTime() - beReduced.getTime()) / (24 * 60 * 60 * 1000);
        //小时
        long hour = ((less.getTime() - beReduced.getTime()) / (60 * 60 * 1000)) % 24;
        //分钟
        long minute = ((less.getTime() - beReduced.getTime()) / 1000) % 60;
        //秒
        long second = ((less.getTime() - beReduced.getTime()) / (60 * 1000)) % 60;
        double secondD = (double) second / 60;
        //和
        return (double) (day * 24 * 60) + (hour * 60) + minute + secondD;
    }


    public static void main(String[] args) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        Date beReduced = df.parse("2018-10-22 01:00:00");
        Date less = df.parse("2018-10-22 02:01:01");

//        System.out.println((d2.getTime() - d1.getTime()) / (24 * 60 * 60 * 1000) + "天");
//        System.out.println((((d2.getTime() - d1.getTime()) / (60 * 60 * 1000)) % 24) + "小时");
//        System.out.println((((d2.getTime() - d1.getTime()) / 1000) % 60) + "分钟");
//        System.out.println(((d2.getTime() - d1.getTime()) / (60 * 1000)) % 60 + "秒");
    }
}