package com.kthw.pmis.controller.checkPlan;

import com.kthw.common.DataTable;
import com.kthw.common.base.ErrCode;
import com.kthw.pmis._enum.SheetInfoType;
import com.kthw.pmis.entiy.Depot;
import com.kthw.pmis.entiy.PlanCheck;
import com.kthw.pmis.entiy.PlanCheckSheet;
import com.kthw.pmis.entiy.ext.SheetInfoExt;
import com.kthw.pmis.mapper.common.DepotMapper;
import com.kthw.pmis.mapper.common.PlanCheckSheetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/checkPlan/sheet")
public class CheckPlanSheetController {
    private static final Logger logger = LoggerFactory.getLogger(CheckPlanSheetController.class);
    @Autowired
    private PlanCheckSheetMapper planCheckSheetMapper;
    @Autowired
    private DepotMapper depotMapper;


    @ResponseBody
    @RequestMapping(value = "/getAllSheets", method = {RequestMethod.GET})
    public DataTable<PlanCheckSheet> sheetsList( HttpServletRequest request,Long depotId) {
        logger.info("显示检修计划单据");
        List<PlanCheckSheet> list = new ArrayList<>();
        DataTable<PlanCheckSheet> dt = new DataTable<PlanCheckSheet>();
        int total = 0;
        try {
            Map<String,Object> params=new HashMap<>();
            params.put("eqDepotId",depotId);

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
        try {
            //新增planCheckSheet表
            planCheckSheet.setCreateTime(new Date());
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
    @ResponseBody
    @RequestMapping(value = "/getMaxSheetId", method = {RequestMethod.GET})
    public synchronized String getMaxSheetId(@RequestParam String sheet_id, HttpServletRequest request) {
        logger.info("获取检修计划最大sheet_id");
        String maxSheetId = planCheckSheetMapper.getMaxSheetId(sheet_id);
        try {
            if (null == maxSheetId) {
                maxSheetId = sheet_id + "00000001";
                return maxSheetId;

            } else {
                Integer serial = Integer.valueOf(maxSheetId.substring(7)) + 1;
                StringBuffer sb = new StringBuffer();
                String str = String.valueOf(serial);
                for (int i = 0; i < 8 - str.length(); ++i) {
                    sb.append("0");
                }
                sb.append(serial);
                String sheet_id2 = sheet_id + sb;
                return sheet_id2;
            }
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
