package com.kthw.pmis.serviceimpl.analysis;

import com.kthw.pmis.entiy.*;
import com.kthw.pmis.mapper.common.DetectDeviceMapper;
import com.kthw.pmis.mapper.common.DetectScoreMapper;
import com.kthw.pmis.mapper.common.FaultHandleMapper;
import com.kthw.pmis.mapper.common.PlanCheckMapper;
import com.kthw.pmis.service.analysis.DetectScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DetectScoreServiceImpl implements DetectScoreService {
    @Autowired
    private DetectDeviceMapper detectDeviceMapper;
    @Autowired
    private FaultHandleMapper faultHandleMapper;
    @Autowired
    private PlanCheckMapper planCheckMapper;
    @Autowired
    private DetectScoreMapper detectScoreMapper;
    /**
     *设置探测站每月的评分
     *
     * @param year
     * @param month
     */
    @Override
    public void setMonthScore(Integer year, Integer month) {
        Map<String, Object> params = new HashMap<>();
        List<DetectScore> list = new ArrayList<>();
        params.put("orderByCause", "detect_device_id asc");
        params.put("eqFaultEnable", 1);
        List<DetectDeviceWithDepot> detectDeviceList = detectDeviceMapper.listDetectDeviceWithDepot(params);//获取需要评分的探测站

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 25);
        params.put("queryTime", calendar.getTime());

        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DATE, 26);
        params.put("queryTime2", calendar.getTime());
        params.put("eqType", "设备故障");
        List<FaultHandle> faultHandleList = faultHandleMapper.selectByMap(params);//获取当月的设备故障


        //获取当月的检修计划
        List<PlanCheck> planCheckList = planCheckMapper.selectByMap(params);
        for (DetectDeviceWithDepot detectDevice : detectDeviceList) {//获取每月的探测站
            DetectScore detectScore = new DetectScore();
            detectScore.setDetectDeviceId(detectDevice.getDetectDeviceId());
            detectScore.setDetectDeviceName(detectDevice.getDetectDeviceName());
            detectScore.setDetectDeviceType(detectDevice.getDeviceTypeName());
            detectScore.setYear(year);
            detectScore.setMonth(month);



            int dayCount = 0;//白天数量
            int nightCount = 0;//晚上数量
            Float stopTime = 0f;//初始化故障停时
            Double stopTimeDedution = 0d;//超时扣分
            for (FaultHandle faultHandle : faultHandleList) {//插入故障停时评分
                if (faultHandle.getDetectDeviceId() > detectDevice.getDetectDeviceId()) {//故障探测站id大于当前探测站id
                    break;
                } else if (faultHandle.getDetectDeviceId().equals(detectDevice.getDetectDeviceId())) {//当前探测站
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTime(faultHandle.getHaultStartTime());
                    Integer hour = calendar1.get(Calendar.HOUR);
                    //故障停时相差小时
                    Float faultStopTime = faultHandle.getFaultStopTime();
                    String faultLevelType = faultHandle.getFaultLevelType();
                    //故障A/B/C级别
                    switch (faultLevelType) {
                        case "A":
                            if (faultStopTime > 48) {
                                stopTimeDedution = (24 * 0.25) + (faultStopTime - 48) % 2 * 0.5;
                            } else {
                                stopTimeDedution = faultStopTime % 2 * 0.25;
                            }
                            break;
                        case "B":
                            if (hour < 18 && hour > 8) {
                                if (faultStopTime > 6) {
                                    stopTimeDedution = (3 * 0.25) + (faultStopTime - 6) % 2 * 0.5;
                                } else {
                                    stopTimeDedution = faultStopTime % 2 * 0.25;
                                }
                            } else {
                                if (faultStopTime > 12) {
                                    stopTimeDedution = (6 * 0.25) + (faultStopTime - 12) % 2 * 0.5;
                                } else {
                                    stopTimeDedution = faultStopTime % 2 * 0.25;
                                }
                            }
                            break;
                        case "C":
                            if (faultStopTime > 72) {
                                stopTimeDedution = (36 * 0.25) + (faultStopTime - 72) % 2 * 0.5;
                            } else {
                                stopTimeDedution = faultStopTime % 2 * 0.25;
                            }
                            break;
                        default:
                            break;

                    }
/**
 * 统计白天黑夜次数
 */
                    if (hour < 18 && hour > 8) {
                        dayCount++;
                    } else if (hour > 18 || hour < 8) {
                        nightCount++;
                    }


                    stopTime += faultStopTime;//停时累加

                }

            }

/**
 * 设备巡检
 */
            Float noEnoughTime = 0f;//初始化时间不足扣分
            Float unCheck = 0f;//初始化漏检不足扣分
            Float unReport = 0f;//初始化漏报不足扣分
            for (PlanCheck planCheck : planCheckList) {//插入检修计划
                if (planCheck.getDetectDeviceId() > detectDevice.getDetectDeviceId()) {//检修计划探测站id大于当前探测站id
                    break;
                } else if (planCheck.getDetectDeviceId().equals(detectDevice.getDetectDeviceId())) {//当前探测站
                    if (planCheck.getStatus() == 6) {//完成的检修计划

                    } else {//未完成的检修计划
                        unCheck = 20f;
                    }

                }

            }

            detectScore.setDayFaultCount(dayCount);//白天数量
            detectScore.setNightFaultCount(nightCount);//晚上数量
            detectScore.setTotalFaultCount(dayCount + nightCount);//总计
            if (detectDevice.getManagePerCapita() != null)
                detectScore.setStopTime(stopTime * detectDevice.getManagePerCapita());//插入故障停时
            detectScore.setStopTimeDeduction(-stopTimeDedution.floatValue());//故障停时扣分
            detectScore.setUncheckDedution1(-unCheck);//设备巡检漏检扣分
            list.add(detectScore);
        }
        detectScoreMapper.batchInsert(list);//批量增加当月的分数
    }

    /**
     * 获取当月的分数
     * @param year
     * @param month
     * @return
     */
    @Override
    public List<DetectScore> getMonthScore(Integer year, Integer month) {
        Map<String,Object> params=new HashMap<>();
        params.put("eqYear",year);
        params.put("eqMonth",month);
        return detectScoreMapper.selectByMap(params);

    }
}
