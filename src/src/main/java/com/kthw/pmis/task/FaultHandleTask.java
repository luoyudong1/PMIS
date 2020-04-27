package com.kthw.pmis.task;

import com.kthw.pmis.controller.checkPlan.CheckPlanController;
import com.kthw.pmis.entiy.FaultHandle;
import com.kthw.pmis.mapper.common.FaultHandleMapper;
import com.kthw.pmis.service.faultHandle.FaultHandleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FaultHandleTask {

    @Autowired
    private FaultHandleService faultHandleService;
    private static final Logger logger = LoggerFactory.getLogger(CheckPlanController.class);
    /**
     * 每天八点执行
     */
    //                   秒分时日 月周
    @Scheduled(cron = "0 0 8 ? * *")
    public void faultHandleFile() {
        logger.info("每天八点定时任务归档故障预报");
        faultHandleService.finished();
    }
}
