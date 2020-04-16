package com.kthw.pmis.task;

import com.kthw.pmis.controller.checkPlan.CheckPlanController;
import com.kthw.pmis.entiy.FaultHandle;
import com.kthw.pmis.mapper.common.FaultHandleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FaultHandleTask {

    @Autowired
    private FaultHandleMapper faultHandleMapper;
    private static final Logger logger = LoggerFactory.getLogger(CheckPlanController.class);
    /**
     * 每天八点执行
     */
    //                   秒分时日 月周
    @Scheduled(cron = "0 0 8 ? * *")
    public void faultHandleFile()
    {
        logger.info("每天八点定时任务归档故障预报");
        Date date=new Date();
        List<FaultHandle> list=new ArrayList<>();
        Map<String,Object> params=new HashMap<>();
        params.put("eqCompleteFlag",7);
        params.put("eqFinished",0);
        params.put("queryTime2",date);
        list=faultHandleMapper.selectByMap(params);
        for(FaultHandle handle:list){
            handle.setFinished(1);
        }
        faultHandleMapper.batchUpdate(list);
    }
}
