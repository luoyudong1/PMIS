package com.kthw.pmis.controller.checkPlan;

import com.kthw.common.DataTable;
import com.kthw.common.base.ErrCode;
import com.kthw.pmis.entiy.*;
import com.kthw.pmis.helper.DepotHelper;
import com.kthw.pmis.helper.PlanCheckHelper;
import com.kthw.pmis.mapper.common.FaultInfoMapper;
import com.kthw.pmis.mapper.common.PlanCheckMapper;
import com.kthw.pmis.mapper.common.PlanCheckSheetMapper;
import com.kthw.pmis.service.checkPlan.CheckPlanService;
import com.mysql.jdbc.CacheAdapter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

@Controller
@Transactional
@RequestMapping("/checkPlan/checkPlan")
public class CheckPlanController {
    private static final Logger logger = LoggerFactory.getLogger(CheckPlanController.class);
    @Autowired
    private PlanCheckMapper planCheckMapper;
    @Autowired
    private PlanCheckSheetMapper planCheckSheetMapper;
    @Autowired
    private DepotHelper depotHelper;
    @Autowired
    private PlanCheckHelper planCheckHelper;
    @Autowired
    private CheckPlanService checkPlanService;

    /**
     * 显示检修计划
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listOnDay", method = RequestMethod.GET)
    public DataTable<PlanCheck> listOnDay(HttpServletRequest request) {
        logger.info("显示当日检修计划");
        DataTable<PlanCheck> dt = new DataTable<PlanCheck>();
        String depotId = request.getParameter("depotId");
        String queryTime = request.getParameter("queryTime");
        String queryTime2 = request.getParameter("queryTime2");
        String dispatcher = request.getParameter("dispatcher");
        String detectDeviceName = request.getParameter("detectDeviceName");
        try {
            Map<String, Object> params = new HashMap<>();
            List<PlanCheck> list = new ArrayList<>();
            Depot depot = null;
            List<Depot> childrens = new ArrayList<>();

            //获取当日检修计划
            if (StringUtils.isNotBlank(depotId)) {
                //获取未开始的检修计划
                depot = depotHelper.getDepot(Long.valueOf(depotId));
                childrens = depotHelper.getChildrens(Long.valueOf(depotId));
                params.put("depotIdList", childrens);
            }
            params.put("queryTime", queryTime);
            params.put("queryTime2", queryTime2);
            params.put("eqFinished", 0);
            params.put("eqStatus", (short) 2);
            if (StringUtils.isNotBlank(dispatcher)) {
                params.put("eqDispatcher", Integer.valueOf(dispatcher));
            }
            if (StringUtils.isNotBlank(detectDeviceName)) {
                params.put("likeDetectDeviceName", "%" + detectDeviceName + "%");
            }
            params.put("orderByClause", "update_time desc,status asc");
            list.addAll(planCheckMapper.selectByMap(params));

            //获取开始但未完成的检修计划
            params.clear();
            if (StringUtils.isNotBlank(depotId)) {
                params.put("depotIdList", childrens);
            }
            params.put("gtStatus", (short) 2);
            params.put("eqFinished", 0);
            if (StringUtils.isNotBlank(dispatcher)) {
                params.put("eqDispatcher", Integer.valueOf(dispatcher));
            }
            if (StringUtils.isNotBlank(detectDeviceName)) {
                params.put("likeDetectDeviceName", detectDeviceName);
            }
            params.put("orderByClause", "update_time desc,status asc");
            list.addAll(planCheckMapper.selectByMap(params));
            list = planCheckHelper.sortNewList(list, depot);

            dt.setRecordsTotal(list.size());
            dt.setRecordsFiltered(list.size());
            dt.setData(list);
            dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        } catch (
                Exception e) {
            logger.error("显示当日检修计划出错");
        }
        return dt;

    }

    /**
     * 显示未兑现检修计划
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listUnfulfilled", method = RequestMethod.GET)
    public DataTable<PlanCheck> listUnfulfilled(HttpServletRequest request,Long depotId,Integer year,Integer month) {
        logger.info("显示未兑现检修计划");
        DataTable<PlanCheck> dt = new DataTable<PlanCheck>();
        try {
            Map<String, Object> params = new HashMap<>();
            List<PlanCheck> list = new ArrayList<>();
            //获取当日检修计划
            if (depotId!=null) {
                //获取未开始的检修计划
                //前一天
                Date date=new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month-2,26,0,0,0);

                List<Depot> childrens = depotHelper.getChildrens(depotId);
                params.put("depotIdList", childrens);
                params.put("queryTime", calendar.getTime());


                calendar.add(Calendar.MONTH,1);
                calendar.set(Calendar.DATE,25);
                if(date.getTime()<calendar.getTime().getTime()){
                    calendar.setTime(date);
                    calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)-1);
                }
                params.put("queryTime2", calendar.getTime());
                params.put("eqStatus", (short) 2);
                params.put("orderByClause", "update_time desc,status asc");
                list.addAll(planCheckMapper.selectByMap(params));

            }
            dt.setRecordsTotal(list.size());
            dt.setRecordsFiltered(list.size());
            dt.setData(list);
            dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        } catch (
                Exception e) {
            logger.error("显示未兑现检修计划出错");
        }
        return dt;

    }

    /**
     * 根据sheetid显示检修计划
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getPlanBySheetId", method = RequestMethod.GET)
    public DataTable<PlanCheck> getPlanBySheetId(HttpServletRequest request) {
        logger.info("根据sheetid显示检修计划");
        DataTable<PlanCheck> dt = new DataTable<PlanCheck>();
        String sheetId = request.getParameter("sheetId");
        String detectDeviceName = request.getParameter("detectDeviceName");
        try {
            Map<String, Object> params = new HashMap<>();
            List<PlanCheck> list = new ArrayList<>();
            //探测站名称
            if (StringUtils.isNotBlank(detectDeviceName)) {
                params.put("likeDetectDeviceName", detectDeviceName);
            }
            //获取faultHandle表
            if (StringUtils.isNotBlank(sheetId)) {
                params.put("eqSheetId", sheetId);
                params.put("orderByClause", "status asc,create_time desc,plan_time desc");
                list = planCheckMapper.selectByMap(params);
            }

            dt.setRecordsTotal(list.size());
            dt.setRecordsFiltered(list.size());
            dt.setData(list);
            dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        } catch (Exception e) {
            logger.error("根据sheetid显示检修计划出错");
        }
        return dt;

    }

    /**
     * 新增检修计划
     *
     * @param planCheck
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public synchronized Map<String, Object> add(@RequestBody PlanCheck planCheck) {
        logger.info("新增检修计划");
        int code = 0;
        Map<String, Object> ret = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        try {
            if (planCheck != null && StringUtils.isNotBlank(planCheck.getSheetId())) {
                //新增planCheck表
                params.put("eqSheetId", planCheck.getSheetId());
                params.put("eqDetectDeviceId", planCheck.getDetectDeviceId());
                List<PlanCheck> records = planCheckMapper.selectByMap(params);//获取检修记录
                if (records.size() == 0
                        || (records.size() == 1 && planCheck.getPlanType().equals("半月检"))
                        || planCheck.getPlanType().equals("临时检修")) {//记录为空或者有一次半月检才能增加

                    Integer id = planCheckMapper.getMaxId();
                    if (id == null) {
                        id = 1;
                    } else {
                        id = id + 1;
                    }
                    //插入检修计划
                    planCheck.setId(id);
                    planCheck.setFinished(0);
                    planCheckMapper.insert(planCheck);
/**
 * 更新单据的相应记录数
 */
                    PlanCheckSheet planCheckSheet = planCheckSheetMapper.selectByPrimaryKey(planCheck.getSheetId());
                    switch (planCheck.getPlanType()) {
                        case "半月检":
                            if (planCheckSheet.getHalfMonthCount() != null)
                                planCheckSheet.setHalfMonthCount(planCheckSheet.getHalfMonthCount() + 1);
                            break;
                        case "月检":
                            if (planCheckSheet.getSingleMonthCount() != null)
                                planCheckSheet.setSingleMonthCount(planCheckSheet.getSingleMonthCount() + 1);
                            break;
                        case "双月检":
                            if (planCheckSheet.getBiMonthCount() != null)
                                planCheckSheet.setBiMonthCount(planCheckSheet.getBiMonthCount() + 1);
                            break;
                        case "临时检修":
                            if (planCheckSheet.getTemporaryCount() != null)
                                planCheckSheet.setTemporaryCount(planCheckSheet.getTemporaryCount() + 1);
                            break;
                    }
                    planCheckSheet.setTotal(planCheckSheet.getTotal() + 1);
                    planCheckSheetMapper.updateByPrimaryKeySelective(planCheckSheet);

                    ret.put("code", code);
                    ret.put("msg", ErrCode.getMessage(code));
                    return ret;
                } else {//当前探测站检修记录已存在，无法增加
                    code = ErrCode.PLAN_CHECK_EXIST;
                    ret.put("code", code);
                    ret.put("msg", ErrCode.getMessage(code));
                    return ret;
                }
            }
        } catch (Exception e) {
            logger.error("新增检修计划出错");
        }
        code = ErrCode.ADD_ERROR;
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;

    }

    /**
     * 延期检修计划
     *
     * @param planCheck
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delay", method = RequestMethod.POST)
    public synchronized Map<String, Object> delay(@RequestBody PlanCheck planCheck) {
        logger.info("延期检修计划");
        int code = 0;
        Map<String, Object> ret = new HashMap<>();
        try {
            PlanCheck info = planCheckMapper.selectByPrimaryKey(planCheck.getId());
            if (info.getStatus() == (short) 2) {
                planCheck.setOldPlanTime(info.getPlanTime());//原检修计划时间
                planCheckMapper.updateByPrimaryKeySelective(planCheck);
                ret.put("code", code);
                ret.put("msg", ErrCode.getMessage(code));
                return ret;
            }
            ret.put("code", code);
            ret.put("msg", ErrCode.getMessage(code));
            return ret;
        } catch (Exception e) {
            logger.error("延期检修计划出错");
            code = ErrCode.DELAY_ERROR;
            ret.put("code", code);
            ret.put("msg", ErrCode.getMessage(code));
        }
        return ret;

    }

    /**
     * 重置检修计划
     *
     * @param planCheck
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/undo", method = RequestMethod.POST)
    public Map<String, Object> undo(@RequestBody PlanCheck planCheck) {
        logger.info("重置检修计划");
        int code = 0;
        Map<String, Object> ret = new HashMap<>();
        try {
            PlanCheck info = planCheckMapper.selectByPrimaryKey(planCheck.getId());
            info.setStatus((short) 2);
            info.setStartTime(null);
            info.setEndTime(null);
            info.setVerifyDate1(null);
            info.setVerifyDate2(null);
            info.setVerifyDate3(null);
            info.setVerifyDate4(null);
            info.setVerifyUser(null);
            info.setVerifyUser1(null);
            info.setVerifyUser2(null);
            info.setVerifyUser3(null);
            info.setVerifyUser4(null);
            info.setCheckRecord(null);
            info.setRemark(null);
            planCheckMapper.updateByPrimaryKey(info);
            ret.put("code", code);
            ret.put("msg", ErrCode.getMessage(code));
            return ret;
        } catch (Exception e) {
            logger.error("重置检修计划出错");
            code = ErrCode.DELAY_ERROR;
            ret.put("code", code);
            ret.put("msg", ErrCode.getMessage(code));
        }
        return ret;

    }

    /**
     * 修改检修计划
     *
     * @param planCheck
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, Object> update(@RequestBody PlanCheck planCheck) {
        logger.info("修改检修计划");
        int code = 0;
        Map<String, Object> ret = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        String sheetId = null;
        Calendar calendar = Calendar.getInstance();

        try {

            //修改planCheck表
            planCheck.setUpdateTime(new Timestamp(new Date().getTime()));
            int flag = planCheckMapper.updateByPrimaryKeySelective(planCheck);

            if (flag == 1) {
                ret.put("code", code);
                ret.put("msg", ErrCode.getMessage(code));
            }
            //获取检修计划
            PlanCheck info = planCheckMapper.selectByPrimaryKey(planCheck.getId());
            //检修完成标志
            if (planCheck.getStatus() != null && planCheck.getStatus() == 6) {
                PlanCheckSheet planCheckSheet = planCheckSheetMapper.selectByPrimaryKey(info.getSheetId());

                //更新检修完成记录数
                planCheckSheet.setCompleteCount(planCheckSheet.getCompleteCount() + 1);
                planCheckSheetMapper.updateByPrimaryKeySelective(planCheckSheet);


            }
            return ret;
        } catch (Exception e) {
            logger.error("修改检修计划出错");
        }
        code = ErrCode.MODIFY_ERROR;
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;

    }

    /**
     * 删除检修计划
     *
     * @param planCheck
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Map<String, Object> delete(@RequestBody PlanCheck planCheck) {
        logger.info("删除检修计划");
        int code = 0;
        Map<String, Object> ret = new HashMap<>();
        DetectDevice detectDevice = new DetectDevice();
        try {
            List<PlanCheck> list = new ArrayList<>();
            DataTable<PlanCheck> dt = new DataTable<PlanCheck>();
            //新增planCheck表
            PlanCheck info = planCheckMapper.selectByPrimaryKey(planCheck.getId());
            int flag = planCheckMapper.deleteByPrimaryKey(planCheck.getId());
            if (flag == 0) {
                code = ErrCode.DELETE_ERROR;
            }
            PlanCheckSheet planCheckSheet = planCheckSheetMapper.selectByPrimaryKey(info.getSheetId());
            switch (info.getPlanType()) {
                case "半月检":
                    if (planCheckSheet.getHalfMonthCount() != null && planCheckSheet.getHalfMonthCount() > 1)
                        planCheckSheet.setHalfMonthCount(planCheckSheet.getHalfMonthCount() - 1);
                    break;
                case "月检":
                    if (planCheckSheet.getSingleMonthCount() != null && planCheckSheet.getSingleMonthCount() > 1)
                        planCheckSheet.setSingleMonthCount(planCheckSheet.getSingleMonthCount() - 1);
                    break;
                case "双月检":
                    if (planCheckSheet.getBiMonthCount() != null && planCheckSheet.getBiMonthCount() > 1)
                        planCheckSheet.setBiMonthCount(planCheckSheet.getBiMonthCount() - 1);
                    break;
                case "临时检修":
                    if (planCheckSheet.getTemporaryCount() != null && planCheckSheet.getTemporaryCount() > 1)
                        planCheckSheet.setTemporaryCount(planCheckSheet.getTemporaryCount() - 1);
                    break;
            }
            planCheckSheet.setTotal(planCheckSheet.getTotal() - 1);
            planCheckSheetMapper.updateByPrimaryKeySelective(planCheckSheet);
        } catch (Exception e) {
            logger.error("删除检修计划出错");
        }
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;

    }

    /**
     * 归档检修计划
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/finished", method = RequestMethod.GET)
    public Map<String, Object> finished() {
        logger.info("归档检修计划");
        int code = 0;
        Map<String, Object> ret = new HashMap<>();
        try {

            checkPlanService.finished();
            ret.put("code", code);
            ret.put("msg", ErrCode.getMessage(code));
            return ret;
        } catch (Exception e) {
            logger.error("归档检修计划出错");
        }
        code = ErrCode.MODIFY_ERROR;
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;

    }

}
