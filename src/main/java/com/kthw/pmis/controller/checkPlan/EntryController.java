package com.kthw.pmis.controller.checkPlan;

import com.kthw.common.DataTable;
import com.kthw.pmis.entiy.Depot;
import com.kthw.pmis.entiy.PlanCheck;
import com.kthw.pmis.helper.DepotHelper;
import com.kthw.pmis.helper.PlanCheckHelper;
import com.kthw.pmis.mapper.common.PlanCheckMapper;
import com.kthw.pmis.mapper.common.PlanCheckSheetMapper;
import com.kthw.pmis.service.checkPlan.CheckPlanService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@Transactional
@RequestMapping("/checkPlan/entry")
public class EntryController {
    private static final Logger logger = LoggerFactory.getLogger(EntryController.class);
    @Autowired
    private PlanCheckMapper planCheckMapper;
    @Autowired
    private DepotHelper depotHelper;

    /**
     * 显示未兑现检修计划
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listUnfulfilled", method = RequestMethod.GET)
    public DataTable<PlanCheck> listUnfulfilled(HttpServletRequest request, Long depotId, Integer year, Integer month) {
        logger.info("显示未兑现检修计划");
        DataTable<PlanCheck> dt = new DataTable<PlanCheck>();
        String dispatcher = request.getParameter("dispatcher");
        String detectDeviceName = request.getParameter("detectDeviceName");
        try {
            Map<String, Object> params = new HashMap<>();
            List<PlanCheck> list = new ArrayList<>();
            //获取当日检修计划

            //获取未开始的检修计划
            //前一天
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month - 2, 26, 0, 0, 0);
            if (depotId != null) {
                List<Depot> childrens = depotHelper.getChildrens(depotId);
                params.put("depotIdList", childrens);

            }
            params.put("queryTime", calendar.getTime());

            if (StringUtils.isNotBlank(dispatcher)) {
                params.put("eqDispatcher", Integer.valueOf(dispatcher));
            }
            if (StringUtils.isNotBlank(detectDeviceName)) {
                params.put("likeDetectDeviceName", detectDeviceName + "%");
            }
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DATE, 25);
            if (date.getTime() < calendar.getTime().getTime()) {
                calendar.setTime(date);
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
            }
            params.put("queryTime2", calendar.getTime());
            params.put("gtStatus", 1);
            params.put("ltStatus", 6);
            params.put("orderByClause", "status desc,plan_time desc,update_time desc");
            list.addAll(planCheckMapper.selectByMap(params));


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

}
