package com.kthw.pmis.serviceimpl.checkPlan;

import com.kthw.pmis.entiy.FaultHandle;
import com.kthw.pmis.entiy.PlanCheck;
import com.kthw.pmis.entiy.PlanCheckSheet;
import com.kthw.pmis.entiy.dto.DetectDeviceDTO;
import com.kthw.pmis.entiy.dto.DetectManageDTO;
import com.kthw.pmis.mapper.common.PlanCheckMapper;
import com.kthw.pmis.mapper.common.PlanCheckSheetMapper;
import com.kthw.pmis.service.checkPlan.CheckPlanService;
import com.kthw.pmis.service.detectManage.DetectManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class CheckPlanServiceImpl implements CheckPlanService {
    @Autowired
    private PlanCheckMapper planCheckMapper;
    @Autowired
    private DetectManageService detectManageService;
    @Autowired
    private PlanCheckSheetMapper planCheckSheetMapper;

    @Override
    public void finished() {
        Date date = new Date();
        List<PlanCheck> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("eqStatus", 6);
        params.put("eqFinished", 0);
        params.put("ltUpdateTime", date);
        list = planCheckMapper.selectByMap(params);
        for (PlanCheck planCheck : list) {
            planCheck.setFinished(1);
        }
        planCheckMapper.batchUpdate(list);

    }

    @Override
    public int insertSheet(PlanCheckSheet planCheckSheet) {

        planCheckSheetMapper.insert(planCheckSheet);
        return 1;
    }

    @Override
    public int batchInsert(List<PlanCheck> list) {
        planCheckMapper.batchInsert(list);
        return 1;
    }

    @Override
    public List<DetectDeviceDTO> getDetect(Long depotId, String sheetId) {
        Map<String, Object> params = new HashMap<>();
        List<DetectDeviceDTO> list = new ArrayList<>();
        List<PlanCheck> planChecks = new ArrayList<>();
        List<PlanCheck> oldPlanChecks = new ArrayList<>();
        List<PlanCheckSheet> oldPlanChecksSheets = new ArrayList<>();
        PlanCheck planCheck = new PlanCheck();
        //获取本月的检修单据
        PlanCheckSheet planCheckSheet = planCheckSheetMapper.selectByPrimaryKey(sheetId);
        //获取上个月的检修单据
        Calendar calendar = Calendar.getInstance();
        calendar.set(planCheckSheet.getYear(), planCheckSheet.getMonth() - 1, 1);
        calendar.add(Calendar.MONTH, -1);
        Integer oldYear = calendar.get(Calendar.YEAR);
        Integer oldMonth = calendar.get(Calendar.MONTH) + 1;
        params.put("eqYear", oldYear);
        params.put("eqMonth", oldMonth);
        oldPlanChecksSheets = planCheckSheetMapper.selectByMap(params);
        //获取部门所管理的探测站
        list = detectManageService.listDetect(depotId, 1);

        //获取本月检修记录
        params.clear();
        params.put("eqSheetId", sheetId);
        params.put("orderByClause", "detect_device_id asc");
        planChecks = planCheckMapper.selectByMap(params);

        //获取上个月的检修记录
        if (oldPlanChecksSheets.size() > 0) {
            params.clear();
            params.put("eqSheetId", oldPlanChecksSheets.get(0).getSheetId());
            params.put("orderByClause", "detect_device_id asc");
            oldPlanChecks = planCheckMapper.selectByMap(params);
        }

        Iterator<DetectDeviceDTO> it=list.iterator();
        while(it.hasNext()) {
            DetectDeviceDTO device = it.next();
            for (int i = 0; i < planChecks.size(); i++) {//遍历本月的检修计划
                planCheck = planChecks.get(i);
                if (device.getDetectDeviceId().equals(planCheck.getDetectDeviceId())) {
                    if (device.getPlanCheckType().equals("月检") || device.getPlanCheckType().equals("双月检")) {//月检或者双月检已存在
                        it.remove();
                        break;
                    } else if (device.getPlanCheckType().equals("半月检")) {//半月检已存在
                        planCheck = planChecks.get(++i);
                        if (planCheck.getDetectDeviceId().equals(device.getDetectDeviceId())) {
//                          //两个半月检//
                            it.remove();
                            break;

                        }

                    }
                }
            }

            if (!device.getDetectDeviceId().equals(planCheck.getDetectDeviceId())) {
                if (device.getPlanCheckType().equals("双月检")) {
                    for (int k = 0; k < oldPlanChecks.size(); k++) {
                        PlanCheck beforeOneMonth = oldPlanChecks.get(k);
                        if (device.getDetectDeviceId().equals(beforeOneMonth.getDetectDeviceId())) {
//                            list.remove(device);
                            it.remove();
                            break;
                        }

                    }
                }

            }
        }


        return list;
    }
}
