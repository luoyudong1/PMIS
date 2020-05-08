package com.kthw.pmis.controller.checkPlan;

import com.kthw.common.DataTable;
import com.kthw.common.base.ErrCode;
import com.kthw.pmis._enum.SheetInfoType;
import com.kthw.pmis.entiy.Depot;
import com.kthw.pmis.entiy.DetectDevice;
import com.kthw.pmis.entiy.PlanCheck;
import com.kthw.pmis.entiy.PlanCheckSheet;
import com.kthw.pmis.entiy.ext.SheetInfoExt;
import com.kthw.pmis.helper.DepotHelper;
import com.kthw.pmis.mapper.common.DepotMapper;
import com.kthw.pmis.mapper.common.DetectDeviceMapper;
import com.kthw.pmis.mapper.common.PlanCheckMapper;
import com.kthw.pmis.mapper.common.PlanCheckSheetMapper;
import com.kthw.pmis.service.planCheckSheet.PlanCheckSheetService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping("/checkPlan/sheet")
public class CheckPlanSheetController {
    private static final Logger logger = LoggerFactory.getLogger(CheckPlanSheetController.class);
    @Autowired
    private PlanCheckSheetMapper planCheckSheetMapper;
    @Autowired
    private PlanCheckMapper planCheckMapper;
    @Autowired
    private PlanCheckSheetService planCheckSheetService;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private DetectDeviceMapper detectDeviceMapper;
    @Autowired
    private DepotHelper depotHelper;
    @ResponseBody
    @RequestMapping(value = "/getAllSheets", method = {RequestMethod.GET})
    public DataTable<PlanCheckSheet> sheetsList(HttpServletRequest request, Long depotId,Short flag) {
        logger.info("显示检修计划单据");
        List<PlanCheckSheet> list = new ArrayList<>();
        DataTable<PlanCheckSheet> dt = new DataTable<PlanCheckSheet>();
        int total = 0;
        try {
            Map<String, Object> params = new HashMap<>();
            if (depotId!=null) {
                List<Depot> childrens = depotHelper.getChildrens(depotId);
                params.put("depotIdList", childrens);
            }
            if(flag!=null){
                params.put("eqFlag", flag);
            }
            params.put("orderByClause", "flag asc,update_time desc");
            list = planCheckSheetMapper.selectByMap(params);
            dt.setRecordsTotal(total);
            dt.setRecordsFiltered(total);
        } catch (Exception e) {
            logger.error("getAllSheets error" + request);
        }
        dt.setData(list);
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;

    }

    /**
     * 新增检修计划
     *
     * @param planCheckSheet
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Map<String, Object> add(@RequestBody PlanCheckSheet planCheckSheet) {
        logger.info("新增检修计划单据");
        int code = 0;
        Map<String, Object> ret = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        try {
            //查询是否新增过
            params.put("eqDepotId", planCheckSheet.getDepotId());
            params.put("eqYear", planCheckSheet.getYear());
            params.put("eqMonth", planCheckSheet.getMonth());
            List<PlanCheckSheet> list = planCheckSheetMapper.selectByMap(params);
            if (list.size() > 0) {
                code = ErrCode.SHEET_ALREADY_EXISTS;
                ret.put("code", code);
                ret.put("msg", ErrCode.getMessage(code));
                return ret;
            }
            //新增planCheckSheet表
            planCheckSheet.setCreateTime(new Date());
            planCheckSheet.setFlag((short) 1);
            int flag = planCheckSheetMapper.insert(planCheckSheet);
            if (flag == 1) {
                ret.put("code", code);
                ret.put("msg", ErrCode.getMessage(code));
            }
            return ret;
        } catch (Exception e) {
            logger.error("新增检修计划单据出错");
        }
        code = ErrCode.ADD_ERROR;
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;

    }

    /**
     * 修改检修计划
     *
     * @param planCheckSheet
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, Object> update(@RequestBody PlanCheckSheet planCheckSheet) {
        logger.info("修改检修计划单据");
        int code = 0;
        Map<String, Object> ret = new HashMap<>();
        try {
            //修改faultHandle表
            if (planCheckSheet.getFlag() == 3) {
                Map<String, Object> params = new HashMap<>();
                PlanCheckSheet info=planCheckSheetMapper.selectByPrimaryKey(planCheckSheet.getSheetId());
                params.put("eqSheetId", planCheckSheet.getSheetId());
                params.put("status", 2);
                planCheckMapper.updateByMap(params);
                //下个月自动生成
                Calendar calendar = Calendar.getInstance();
//                calendar.setTime();
            }
            planCheckSheet.setUpdateTime(new Timestamp(new Date().getTime()));
            int flag = planCheckSheetMapper.updateByPrimaryKeySelective(planCheckSheet);
            if (flag == 1) {
                ret.put("code", code);
                ret.put("msg", ErrCode.getMessage(code));
            }
            return ret;
        } catch (Exception e) {
            logger.error("修改检修计划单据出错");
        }
        code = ErrCode.MODIFY_ERROR;
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;

    }

    /**
     * 删除检修计划单据
     *
     * @param planCheckSheet
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Map<String, Object> delete(@RequestBody PlanCheckSheet planCheckSheet) {
        logger.info("删除检修计划单据");
        int code = 0;
        Map<String, Object> ret = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        DetectDevice detectDevice = new DetectDevice();
        try {
            List<PlanCheck> list = new ArrayList<>();
            DataTable<PlanCheck> dt = new DataTable<PlanCheck>();
            //"删除检修计划单据
            params.put("eqSheetId",planCheckSheet.getSheetId());
            list=planCheckMapper.selectByMap(params);
            if(list.size()>0) {
                //删除单据下的全部探测站检修计划
                planCheckMapper.deleteBySheetId(planCheckSheet.getSheetId());
                for (PlanCheck planCheck:list){
                    //回退探测站的检修计划创建标志
                    detectDevice=detectDeviceMapper.selectByPrimaryKey(planCheck.getDetectDeviceId());
                    detectDevice.setPlanCheckEnable((short)(detectDevice.getPlanCheckEnable().intValue()-1));
                    detectDeviceMapper.updateByPrimaryKeySelective(detectDevice);
                }
            }
            int flag = planCheckSheetMapper.deleteByPrimaryKey(planCheckSheet.getSheetId());
            if (flag == 0) {
                code = ErrCode.DELETE_ERROR;
            }
        } catch (Exception e) {
            logger.error("删除检修计划单据出错");
        }
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;

    }

    @ResponseBody
    @RequestMapping(value = "/getMaxSheetId", method = {RequestMethod.GET})
    public synchronized String getMaxSheetId(@RequestParam String sheet_id, HttpServletRequest request) {
        logger.info("获取检修计划最大sheet_id");

        try {
            String maxSheetId = planCheckSheetService.getMaxSheetId(sheet_id);
            return maxSheetId;
        } catch (Exception e) {
            logger.error("getMaxSheetIerror" + request);
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/getDepotName", method = {RequestMethod.GET})
    public Depot getDepotName(Long depotId) {
        logger.info("获取检部门名称");
        Depot depot = new Depot();
        try {
            depot = depotMapper.selectByPrimaryKey(depotId);
        } catch (Exception e) {
            logger.error("获取检部门名称error");
        }
        return depot;
    }
}
