package com.gov.wiki.task;

import com.gov.wiki.common.utils.Constants;
import com.gov.wiki.message.dao.McGroupDao;
import com.gov.wiki.message.service.IGroupService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;

/**
 * 定时任务处理每一天的评价信息
 */
@Component
@Slf4j
public class RegularEvaluationTask {


    @Autowired
    IGroupService groupService;

    /**
     * 自动评价任务
     */
    @Scheduled(fixedDelay = 3600*1000)
    public void submitEvaluation(){
        log.debug("开始自动评价");
        groupService.autoEvaluationTask(null);
        log.debug("本次自动评价完成");
    }
}
