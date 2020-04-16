package com.kthw.pmis.controller.checkPlan;

import com.kthw.common.DataTable;
import com.kthw.common.base.ErrCode;
import com.kthw.pmis.controller.faultHandle.FaultReportController;
import com.kthw.pmis.entiy.DetectDevice;
import com.kthw.pmis.entiy.FaultHandle;
import com.kthw.pmis.entiy.PlanCheck;
import com.kthw.pmis.entiy.PlanCheckSheet;
import com.kthw.pmis.mapper.common.DetectDeviceMapper;
import com.kthw.pmis.mapper.common.PlanCheckMapper;
import com.kthw.pmis.mapper.common.PlanCheckSheetMapper;
import com.kthw.pmis.service.planCheckSheet.PlanCheckSheetService;
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
    private PlanCheckSheetService planCheckSheetService;
    @Autowired
    private PlanCheckSheetMapper planCheckSheetMapper;
    @Autowired
    private DetectDeviceMapper detectDeviceMapper;

    /**
     * 显示检修计划
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public DataTable<PlanCheck> list(HttpServletRequest request) {
        logger.info("显示检修计划");
        DataTable<PlanCheck> dt = new DataTable<PlanCheck>();
        String depotId = request.getParameter("depotId");
        String status = request.getParameter("status");
        String queryTime = request.getParameter("queryTime");
        String queryTime2 = request.getParameter("queryTime2");
        String type = request.getParameter("type");
        String sheetId = request.getParameter("sheetId");
        try {
            Map<String, Object> params = new HashMap<>();
            List<PlanCheck> list = new ArrayList<>();
            //获取faultHandle表
            if (StringUtils.isNotBlank(sheetId)) {
                params.put("eqSheetId", sheetId);
            } else {
                if (StringUtils.isNotBlank(depotId)) {
                    params.put("eqDepotId", Long.valueOf(depotId));
                }
                if (StringUtils.isNotBlank(status)) {
                    params.put("eqStatus", Short.valueOf(status));
                }
                if (StringUtils.isNotBlank(type)) {
                    params.put("eqPlanType", type);
                }
                params.put("queryTime", queryTime);
                params.put("queryTime2", queryTime2);
            }
            params.put("orderByClause", "status asc,update_time desc");
            list = planCheckMapper.selectByMap(params);
            dt.setRecordsTotal(list.size());
            dt.setRecordsFiltered(list.size());
            dt.setData(list);
            dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        } catch (Exception e) {
            logger.error("显示检修计划出错");
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
        DetectDevice detectDevice = new DetectDevice();
        try {
            List<PlanCheck> list = new ArrayList<>();
            DataTable<PlanCheck> dt = new DataTable<PlanCheck>();
            //新增planCheck表
            Integer id = planCheckMapper.getMaxId();
            if (id == null) {
                id = 1;
            } else {
                id = id + 1;
            }
            planCheck.setId(id);
            planCheck.setStatus((short) 1);
            planCheckMapper.insert(planCheck);
            detectDevice = detectDeviceMapper.selectByPrimaryKey(planCheck.getDetectDeviceId());
            detectDevice.setPlanCheckEnable((short) (detectDevice.getPlanCheckEnable().intValue() + 1));
            detectDeviceMapper.updateByPrimaryKeySelective(detectDevice);
            ret.put("code", code);
            ret.put("msg", ErrCode.getMessage(code));
            return ret;
        } catch (Exception e) {
            logger.error("新增检修计划出错");
        }
        code = ErrCode.ADD_ERROR;
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
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
                PlanCheckSheet nextMonth = new PlanCheckSheet();
                Integer id = planCheckMapper.getMaxId();
                if (id == null) {
                    id = 1;
                } else {
                    id = id + 1;
                }
                //计算下个月的时间
                calendar.setTime(info.getPlanTime());
                if (info.getPlanType().equals("月检")) {
                    calendar.add(Calendar.MONTH, 1);
                }
                if (info.getPlanType().equals("双月检")) {
                    calendar.add(Calendar.MONTH, 2);
                }
                params.put("eqDepotId", planCheckSheet.getDepotId());
                params.put("eqYear", calendar.get(Calendar.YEAR));
                params.put("eqMonth", calendar.get(Calendar.MONTH)+1);
                List<PlanCheckSheet> list = planCheckSheetMapper.selectByMap(params);
                if (list.size() < 1) {//已存在直接插入单据下，返回
                    //插入新检修单据
                    Date date=new Date();
                    sheetId = planCheckSheetService.getMaxSheetId(planCheckSheet.getSheetId().substring(0, 6));
                    nextMonth.setSheetId(sheetId);
                    nextMonth.setDepotId(planCheckSheet.getDepotId());
                    nextMonth.setYear(calendar.get(Calendar.YEAR));
                    nextMonth.setMonth(calendar.get(Calendar.MONTH)+1);
                    nextMonth.setDepotName(planCheckSheet.getDepotName());
                    nextMonth.setCreateUser(planCheckSheet.getCreateUser());
                    nextMonth.setCreateTime(date);
                    nextMonth.setUpdateTime(date);
                    nextMonth.setVerifyUser(null);
                    nextMonth.setFlag((short) 1);
                    planCheckSheetMapper.insert(nextMonth);
                } else {
                    sheetId = list.get(0).getSheetId();//获取单据id

                }
                //插入下个月单据下的探测站检修计划
                info.setStatus((short) 1);
                info.setId(id);
                info.setPlanTime(calendar.getTime());
                info.setSheetId(sheetId);
                info.setStartTime(null);
                info.setEndTime(null);
                info.setCheckRecord(null);
                info.setRemark(null);
                planCheckMapper.insert(info);

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
            int flag = planCheckMapper.deleteByPrimaryKey(planCheck.getId());
            if (flag == 0) {
                code = ErrCode.DELETE_ERROR;
            }
            detectDevice = detectDeviceMapper.selectByPrimaryKey(planCheck.getDetectDeviceId());
            detectDevice.setPlanCheckEnable((short) (detectDevice.getPlanCheckEnable().intValue() - 1));
            detectDeviceMapper.updateByPrimaryKeySelective(detectDevice);
        } catch (Exception e) {
            logger.error("删除检修计划出错");
        }
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;

    }
}
