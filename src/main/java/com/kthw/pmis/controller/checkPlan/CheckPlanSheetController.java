package com.kthw.pmis.controller.checkPlan;

import com.kthw.common.DataTable;
import com.kthw.common.base.ErrCode;
import com.kthw.pmis._enum.SheetInfoType;
import com.kthw.pmis.entiy.Depot;
import com.kthw.pmis.entiy.DetectDevice;
import com.kthw.pmis.entiy.PlanCheck;
import com.kthw.pmis.entiy.PlanCheckSheet;
import com.kthw.pmis.entiy.bo.PlanCheckAddBO;
import com.kthw.pmis.entiy.dto.DetectDeviceDTO;
import com.kthw.pmis.entiy.dto.DetectManageDTO;
import com.kthw.pmis.entiy.ext.SheetInfoExt;
import com.kthw.pmis.helper.DepotHelper;
import com.kthw.pmis.mapper.common.DepotMapper;
import com.kthw.pmis.mapper.common.DetectDeviceMapper;
import com.kthw.pmis.mapper.common.PlanCheckMapper;
import com.kthw.pmis.mapper.common.PlanCheckSheetMapper;
import com.kthw.pmis.service.checkPlan.CheckPlanService;
import com.kthw.pmis.service.detectManage.DetectManageService;
import com.kthw.pmis.service.planCheckSheet.PlanCheckSheetService;
import com.kthw.pmis.util.plan.PlanCheckUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
    private DepotHelper depotHelper;
    @Autowired
    private DetectManageService detectManageService;
    @Autowired
    private CheckPlanService checkPlanService;

    @ResponseBody
    @RequestMapping(value = "/getAllSheets", method = {RequestMethod.GET})
    public DataTable<PlanCheckSheet> sheetsList(HttpServletRequest request, Long depotId, Short flag) {
        logger.info("显示检修计划单据");
        List<PlanCheckSheet> list = new ArrayList<>();
        DataTable<PlanCheckSheet> dt = new DataTable<PlanCheckSheet>();
        int total = 0;
        try {
            Map<String, Object> params = new HashMap<>();
            if (depotId != null) {
                List<Depot> childrens = depotHelper.getChildrens(depotId);
                params.put("depotIdList", childrens);
            }
            if (flag != null) {
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
        List<PlanCheck> planChecks = new ArrayList<>();
        List<PlanCheck> oldPlanChecks1 = new ArrayList<>();
        List<PlanCheck> oldPlanChecks2 = new ArrayList<>();
        List<PlanCheckSheet> oldPlanList1 = new ArrayList<>();
        List<PlanCheckSheet> oldPlanList2 = new ArrayList<>();
        PlanCheckAddBO planCheckAddBO=new PlanCheckAddBO();
        try {
            //查询是否新增过
            params.put("eqDepotId", planCheckSheet.getDepotId());
            params.put("eqYear", planCheckSheet.getYear());
            params.put("eqMonth", planCheckSheet.getMonth());
            List<PlanCheckSheet> list = planCheckSheetMapper.selectByMap(params);
            if (list.size() > 0) {//新增过则返回
                code = ErrCode.SHEET_ALREADY_EXISTS;
                ret.put("code", code);
                ret.put("msg", ErrCode.getMessage(code));
                return ret;
            }

            //获取上个月的检修记录
            Calendar calendar = Calendar.getInstance();
            calendar.set(planCheckSheet.getYear(), planCheckSheet.getMonth() - 1, 1);
            calendar.add(Calendar.MONTH, -1);
            Integer oldYear = calendar.get(Calendar.YEAR);
            Integer oldMonth = calendar.get(Calendar.MONTH) + 1;
            params.put("eqYear", oldYear);
            params.put("eqMonth", oldMonth);
            oldPlanList1 = planCheckSheetMapper.selectByMap(params);

            //获取上上个月的检修记录
            calendar.add(Calendar.MONTH, -1);
            oldYear = calendar.get(Calendar.YEAR);
            oldMonth = calendar.get(Calendar.MONTH) + 1;
            params.put("eqYear", oldYear);
            params.put("eqMonth", oldMonth);
            oldPlanList2 = planCheckSheetMapper.selectByMap(params);


            Integer id = planCheckMapper.getMaxId();
            if (id == null) {
                id = 1;
            } else {
                id++;
            }
            List<DetectDeviceDTO> detectDeviceList = detectManageService.listDetect(planCheckSheet.getDepotId(), 1);

            if (oldPlanList1.size() > 0) {//不是第一次新建
                params.clear();
                params.put("eqSheetId", oldPlanList1.get(0).getSheetId());
                params.put("orderByClause", "detect_device_id asc");
                oldPlanChecks1 = planCheckMapper.selectByMap(params);//上个月的检修记录
                if (oldPlanList2.size() > 0) {
                    params.put("eqSheetId", oldPlanList2.get(0).getSheetId());
                    params.put("eqPlanType", "双月检");
                    params.put("orderByClause", "detect_device_id asc");
                    oldPlanChecks2 = planCheckMapper.selectByMap(params);//上上个月的检修记录
                }
                //增加检修探测站详情，返回相应数据list及类型条数
                planCheckAddBO = PlanCheckUtil.addDataByDetect(detectDeviceList, id, planCheckSheet, oldPlanChecks1, oldPlanChecks2);//批量增加

            } else {//第一次新建，返回相应数据list及类型条数
                planCheckAddBO  = PlanCheckUtil.addDataByDetectFisrt(detectDeviceList, id, planCheckSheet);//批量增加

            }
            //获取批量插入数据
            planChecks=planCheckAddBO.getList();
            checkPlanService.batchInsert(planChecks);//批量插入
            //新增planCheckSheet表，设置半月检，双月检，月检数量
            planCheckSheet.setHalfMonthCount(planCheckAddBO.getHalfMonthCount());
            planCheckSheet.setBiMonthCount(planCheckAddBO.getBiMonthCount());
            planCheckSheet.setSingleMonthCount(planCheckAddBO.getSingleMonthCount());
            planCheckSheet.setTotal(planChecks.size());
            planCheckSheet.setCompleteCount(0);
            planCheckSheet.setCreateTime(new Date());
            planCheckSheet.setFlag((short) 1);
            int flag = checkPlanService.insertSheet(planCheckSheet);
            if (flag == 1) {
                ret.put("code", code);
                ret.put("msg", ErrCode.getMessage(code));
            }
            ret.put("code", code);
            ret.put("msg", ErrCode.getMessage(code));
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
                PlanCheckSheet info = planCheckSheetMapper.selectByPrimaryKey(planCheckSheet.getSheetId());
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
            params.put("eqSheetId", planCheckSheet.getSheetId());
            list = planCheckMapper.selectByMap(params);
            if (list.size() > 0) {
                //删除单据下的全部探测站检修计划
                planCheckMapper.deleteBySheetId(planCheckSheet.getSheetId());
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

    /**

     */
    @ResponseBody
    @RequestMapping(value = "/getDetect", method = {RequestMethod.GET})
    public DetectManageDTO getDetect(Long depotId, String sheetId) {
        logger.info("获取可增加的探测站");
        Map<String, Object> params = new HashMap<>();
        List<DetectDeviceDTO> list = new ArrayList<>();
        DetectManageDTO ret = new DetectManageDTO();
        Set<String> lines=new HashSet<>();
        Set<String> detectDeviceType=new HashSet<>();
        try {
            //获取的可增加的探测站
            list=checkPlanService.getDetect(depotId,sheetId);
            for (DetectDeviceDTO device:list){
                lines.add(device.getLineName());
                detectDeviceType.add(device.getDeviceTypeName());
            }
            //返回的可增加的探测站
            ret.setList(list);
            ret.setLines(lines);
            ret.setDetectDeviceType(detectDeviceType);
        } catch (Exception e) {
            logger.error("获取可增加的探测站");
        }
        return ret;
    }


}
