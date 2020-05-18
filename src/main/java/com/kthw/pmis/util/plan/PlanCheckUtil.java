package com.kthw.pmis.util.plan;

import com.kthw.pmis.entiy.DetectDevice;
import com.kthw.pmis.entiy.PlanCheck;
import com.kthw.pmis.entiy.PlanCheckSheet;
import com.kthw.pmis.entiy.bo.PlanCheckAddBO;
import com.kthw.pmis.entiy.dto.DetectDeviceDTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PlanCheckUtil {
    /**
     * 第一次添加下个月的检修记录
     *
     * @param list
     * @param id
     * @param planCheckSheet
     * @return
     */
    public static PlanCheckAddBO addDataByDetectFisrt(List<DetectDeviceDTO> list, Integer id, PlanCheckSheet planCheckSheet) {
        int biMonthCount=0;
        int singleMonthCount=0;
        int halfMonthCount=0;
        PlanCheckAddBO planCheckAddBO=new PlanCheckAddBO();
        List<PlanCheck> planChecks = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(planCheckSheet.getYear(), planCheckSheet.getMonth() - 1, 1);//本月的1号
        Date date = calendar.getTime();
        for (int i = 0; i < list.size(); i++) {
            DetectDeviceDTO device = list.get(i);
            if (device.getPlanCheckType().equals("月检")) {//月检加1次
                planChecks.add(addPlanCheck(id, planCheckSheet, date, device));
                singleMonthCount++;
                id++;
            }else if (device.getPlanCheckType().equals("双月检")) {//双月检加1次
                planChecks.add(addPlanCheck(id, planCheckSheet, date, device));
                id++;
                biMonthCount++;
            } else if (device.getPlanCheckType().equals("半月检")) {//半月检加两次
                planChecks.add(addPlanCheck(id, planCheckSheet, date, device));
                id++;
                halfMonthCount++;
                planChecks.add(addPlanCheck(id, planCheckSheet, date, device));
                id++;
                halfMonthCount++;
            }

        }
        //返回参数封装
        planCheckAddBO.setBiMonthCount(biMonthCount);
        planCheckAddBO.setHalfMonthCount(halfMonthCount);
        planCheckAddBO.setList(planChecks);
        planCheckAddBO.setSingleMonthCount(singleMonthCount);
        return planCheckAddBO;
    }

    /**
     * 第二次添加下个月的检修记录
     *
     * @param list
     * @param id
     * @param planCheckSheet
     * @param oldPlanChecks1
     * @param oldPlanChecks2
     * @return
     */
    public static PlanCheckAddBO addDataByDetect(List<DetectDeviceDTO> list, Integer id, PlanCheckSheet planCheckSheet, List<PlanCheck> oldPlanChecks1, List<PlanCheck> oldPlanChecks2) {
        int biMonthCount=0;
        int singleMonthCount=0;
        int halfMonthCount=0;
        PlanCheckAddBO planCheckAddBO=new PlanCheckAddBO();
        List<PlanCheck> planChecks = new ArrayList<>();
        PlanCheck planCheck = new PlanCheck();
        Calendar calendar = Calendar.getInstance();
        Calendar old = Calendar.getInstance();
        calendar.set(planCheckSheet.getYear(), planCheckSheet.getMonth() - 1, 1);//本月的1号
        Date date= new Date();
        for (int i = 0; i < list.size(); i++) {
            DetectDeviceDTO device = list.get(i);
            //非双月检遍历上个月
            for (int j = 0; j < oldPlanChecks1.size(); j++) {//遍历上个月的检修记录
                planCheck = oldPlanChecks1.get(j);
                old.setTime(planCheck.getPlanTime());
                calendar.set(Calendar.DATE,1);
                if (device.getDetectDeviceId().equals(planCheck.getDetectDeviceId())) {
                    calendar.set(Calendar.DATE, old.get(Calendar.DATE));
                    date = calendar.getTime();
                    System.out.println(calendar.getTime());
                    if (device.getPlanCheckType().equals("月检")) {//正常增加
                        planChecks.add(addPlanCheck(id, planCheckSheet, date, device));
                        id++;
                        singleMonthCount++;
                        break;
                    } else if (device.getPlanCheckType().equals("半月检")) {//正常增加
                        planChecks.add(addPlanCheck(id, planCheckSheet, date, device));
                        id++;
                        halfMonthCount++;
                        old.setTime(oldPlanChecks1.get(++j).getPlanTime());
                        calendar.set(Calendar.DATE, old.get(Calendar.DATE));
                        date = calendar.getTime();
                        planChecks.add(addPlanCheck(id, planCheckSheet, date, device));
                        id++;
                        halfMonthCount++;
                        break;
                    } else if (device.getPlanCheckType().equals("双月检")) {
                        break;
                    }
                }
            }
            ///
            //上个月没有双月检
            if (!device.getDetectDeviceId().equals(planCheck.getDetectDeviceId())
                    && device.getPlanCheckType().equals("双月检")) {
                for (int k = 0; k < oldPlanChecks2.size(); k++) {//遍历上上个月的检修记录
                    planCheck = oldPlanChecks2.get(k);
                    if (device.getDetectDeviceId().equals(planCheck.getDetectDeviceId())) {
                        old.setTime(planCheck.getPlanTime());
                        calendar.set(Calendar.DATE, old.get(Calendar.DATE));
                        date = calendar.getTime();
                        break;
                    }

                }
                //新增双月检
                planChecks.add(addPlanCheck(id, planCheckSheet, date, device));
                id++;
                biMonthCount++;

            }

        }
        //返回参数封装
        planCheckAddBO.setBiMonthCount(biMonthCount);
        planCheckAddBO.setHalfMonthCount(halfMonthCount);
        planCheckAddBO.setList(planChecks);
        planCheckAddBO.setSingleMonthCount(singleMonthCount);
        return planCheckAddBO;
    }

    public static PlanCheck addPlanCheck(Integer id, PlanCheckSheet planCheckSheet, Date date, DetectDeviceDTO device) {
        PlanCheck planCheck = new PlanCheck();
        planCheck.setId(id);
        planCheck.setSheetId(planCheckSheet.getSheetId());
        planCheck.setPlanTime(date);
        planCheck.setFinished(0);
        planCheck.setStatus((short) 1);
        planCheck.setDispatcher(device.getDispatcher());
        planCheck.setDetectDepotId(device.getDepotId());
        planCheck.setDetectDepotName(device.getDepotName());
        planCheck.setPlanType(device.getPlanCheckType());
        planCheck.setDetectDeviceType(device.getDeviceTypeName());
        planCheck.setDetectDeviceId(device.getDetectDeviceId());
        planCheck.setDetectDeviceName(device.getDetectDeviceName());
        return planCheck;
    }
}
