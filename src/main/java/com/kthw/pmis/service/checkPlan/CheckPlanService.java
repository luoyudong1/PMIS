package com.kthw.pmis.service.checkPlan;

import com.kthw.pmis.entiy.PlanCheck;
import com.kthw.pmis.entiy.PlanCheckSheet;
import com.kthw.pmis.entiy.dto.DetectDeviceDTO;

import java.util.List;

public interface CheckPlanService {
    void finished();

    int batchInsert(List<PlanCheck> list);//批量插入

    int insertSheet(PlanCheckSheet planCheckSheet);

    List<DetectDeviceDTO> getDetect(Long depotId,String sheetId);
}
