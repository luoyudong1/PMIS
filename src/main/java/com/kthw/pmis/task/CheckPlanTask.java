package com.kthw.pmis.task;

import com.kthw.pmis.controller.checkPlan.CheckPlanController;
import com.kthw.pmis.entiy.FaultHandle;
import com.kthw.pmis.entiy.PlanCheck;
import com.kthw.pmis.mapper.common.PlanCheckMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CheckPlanTask {
    @Autowired
    private PlanCheckMapper planCheckMapper;
    private static final Logger logger = LoggerFactory.getLogger(CheckPlanController.class);
    /**
     * 每天八点执行
     */
    //                   秒分时日 月周
    @Scheduled(cron = "0 0 8 ? * *")
    public void finished()
    {
        logger.info("每天八点定时任务归档检修计划");
        Date date=new Date();
        List<PlanCheck> list=new ArrayList<>();
        Map<String,Object> params=new HashMap<>();
        params.put("eqStatus",6);
        params.put("eqFinished",0);
        params.put("queryTime2",date);
        list=planCheckMapper.selectByMap(params);
        for(PlanCheck planCheck:list){
            planCheck.setFinished(1);
        }
        planCheckMapper.batchUpdate(list);
    }

}
