package com.gov.wiki.business.controller;

import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.business.req.query.MonthQuery;
import com.gov.wiki.business.req.query.YearQuery;
import com.gov.wiki.business.res.AnnualDataRes;
import com.gov.wiki.business.res.LiftRateRes;
import com.gov.wiki.business.res.RespondPromptlyRes;
import com.gov.wiki.business.service.DialogueService;
import com.gov.wiki.business.service.MaterialPreTrialService;
import com.gov.wiki.business.service.UserStatisticsService;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.ReqBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "统计模块")
@RequestMapping("/statistics")
@RestController
public class StatisticsController {

    @Autowired
    private UserStatisticsService userStatisticsService;

    @Autowired
    private MaterialPreTrialService materialPreTrialService;

    @Autowired
    private DialogueService dialogueService;

    /**
     * @param bean:
     * @decription TODO(用户提升率统计 - 新增用户)
     * @return: com.gov.wiki.common.beans.ResultBean<com.gov.wiki.business.res.AnnualDataRes>
     * @author liusq
     * @date 2021/5/10 9:44
     */
    @PostMapping("/getUserLiftRate")
    @ApiOperation(value = "统计用户提升率")
    @ControllerMonitor(description = "统计用户提升率", operType = 1)
    public ResultBean<LiftRateRes> getUserLiftRate(@Validated @RequestBody ReqBean<YearQuery> bean) {
        CheckUtil.notNull(bean, ResultCode.PARAM_NULL, "参数");
        LiftRateRes liftRateRes = userStatisticsService.getUserLiftRate(bean.getBody());
        return new ResultBean<>(liftRateRes);
    }

    /**
     * @param bean:
     * @decription TODO(统计实名注册)
     * @return: com.gov.wiki.common.beans.ResultBean<com.gov.wiki.business.res.AnnualDataRes>
     * @author liusq
     * @date 2021/5/10 15:32
     */
    @PostMapping("/getVerified")
    @ApiOperation(value = "统计实名注册")
    @ControllerMonitor(description = "统计实名注册", operType = 1)
    public ResultBean<AnnualDataRes> getVerified(@Validated @RequestBody ReqBean<YearQuery> bean) {
        CheckUtil.notNull(bean, ResultCode.PARAM_NULL, "参数");
        AnnualDataRes verified = userStatisticsService.getVerified(bean.getBody());
        return new ResultBean<>(verified);
    }

    /**
     * @param bean:
     * @decription TODO(统计活跃用户)
     * @return: com.gov.wiki.common.beans.ResultBean<com.gov.wiki.business.res.AnnualDataRes>
     * @author liusq
     * @date 2021/5/10 15:55
     */
    @PostMapping("/getActiveUser")
    @ApiOperation(value = "统计活跃用户")
    @ControllerMonitor(description = "统计活跃用户", operType = 1)
    public ResultBean<AnnualDataRes> getActiveUser(@Validated @RequestBody ReqBean<YearQuery> bean) {
        CheckUtil.notNull(bean, ResultCode.PARAM_NULL, "参数");
        AnnualDataRes activeUser = userStatisticsService.getActiveUser(bean.getBody());
        return new ResultBean<>(activeUser);
    }

    /**
     * @param bean:
     * @decription TODO(统计材料预审并发量)
     * @return: com.gov.wiki.common.beans.ResultBean<com.gov.wiki.business.res.AnnualDataRes>
     * @author liusq
     * @date 2021/5/11 10:20
     */
    @PostMapping("/getMaterialPreTrial")
    @ApiOperation(value = "统计材料预审并发量")
    @ControllerMonitor(description = "统计材料预审并发量", operType = 1)
    public ResultBean<AnnualDataRes> getMaterialPreTrial(@Validated @RequestBody ReqBean<YearQuery> bean) {
        CheckUtil.notNull(bean, ResultCode.PARAM_NULL, "参数");
        AnnualDataRes activeUser = materialPreTrialService.getMaterialPreTrial(bean.getBody());
        return new ResultBean<>(activeUser);
    }

    /**
     * @param bean:
     * @decription TODO(统计主动对话次数 - 统计人人对话次数)
     * @return: com.gov.wiki.common.beans.ResultBean<com.gov.wiki.business.res.AnnualDataRes>
     * @author liusq
     * @date 2021/5/11 10:48
     */
    @PostMapping("/getActiveDialogue")
    @ApiOperation(value = "统计主动对话次数-统计人人对话次数")
    @ControllerMonitor(description = "统计主动对话次数-统计人人对话次数", operType = 1)
    public ResultBean<AnnualDataRes> getActiveDialogue(@Validated @RequestBody ReqBean<YearQuery> bean) {
        CheckUtil.notNull(bean, ResultCode.PARAM_NULL, "参数");
        AnnualDataRes activeUser = dialogueService.getActiveDialogue(bean.getBody());
        return new ResultBean<>(activeUser);
    }

    /**
     * @param bean:
     * @decription TODO(统计人机对话次数)
     * @return: com.gov.wiki.common.beans.ResultBean<com.gov.wiki.business.res.AnnualDataRes>
     * @author liusq
     * @date 2021/5/11 11:12
     */
    @PostMapping("/getManMachineDialogue")
    @ApiOperation(value = "统计人机对话次数")
    @ControllerMonitor(description = "统计人机对话次数", operType = 1)
    public ResultBean<AnnualDataRes> getManMachineDialogue(@Validated @RequestBody ReqBean<YearQuery> bean) {
        CheckUtil.notNull(bean, ResultCode.PARAM_NULL, "参数");
        AnnualDataRes activeUser = dialogueService.getManMachineDialogue(bean.getBody());
        return new ResultBean<>(activeUser);
    }

    /**
     * @param bean:
     * @decription TODO(统计及时响应率)
     * @return: com.gov.wiki.common.beans.ResultBean<com.gov.wiki.business.res.RespondPromptlyRes>
     * @author liusq
     * @date 2021/5/11 16:20
     */
    @PostMapping("/getRespondPromptly")
    @ApiOperation(value = "统计及时响应率")
    @ControllerMonitor(description = "统计及时响应率", operType = 1)
    public ResultBean<RespondPromptlyRes> getRespondPromptly(@Validated @RequestBody ReqBean<MonthQuery> bean) {
        CheckUtil.notNull(bean, ResultCode.PARAM_NULL, "参数");
        RespondPromptlyRes activeUser = dialogueService.getRespondPromptly(bean.getBody());
        return new ResultBean<>(activeUser);
    }
}