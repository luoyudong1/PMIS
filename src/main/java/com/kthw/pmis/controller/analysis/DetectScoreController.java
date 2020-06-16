package com.kthw.pmis.controller.analysis;

import com.kthw.common.DataTable;
import com.kthw.common.base.ErrCode;
import com.kthw.pmis.controller.checkPlan.CheckPlanController;
import com.kthw.pmis.entiy.DetectScore;
import com.kthw.pmis.service.analysis.DetectScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Transactional
@RequestMapping("/detectScore")
public class DetectScoreController {
    @Autowired
    private DetectScoreService detectScoreService;
    private static final Logger logger = LoggerFactory.getLogger(DetectScoreController.class);
    /**
     * 设置当月的分数
     *
     * @param year
     * @param month
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/setScore", method = RequestMethod.POST)
    public Map<String, Object> setScore(Integer year, Integer month) {
        logger.info("设置当月探测站分数");
        Map<String, Object> ret = new HashMap<>();
        int code = 0;

        detectScoreService.setMonthScore(year, month);//设置当月的分数

        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    /**
     * 获取当月的分数
     *
     * @param year
     * @param month
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getScore", method = RequestMethod.GET)
    public DataTable<DetectScore> getScore(HttpServletRequest request,Integer year, Integer month) {
        logger.info("获取当月探测站分数");
        Map<String, Object> ret = new HashMap<>();
        DataTable<DetectScore> dt = new DataTable<>();
        List<DetectScore> list = new ArrayList<>();
        list = detectScoreService.getMonthScore(year, month);//获取当月的分数
        dt.setRecordsTotal(list.size());
        dt.setRecordsFiltered(list.size());
        dt.setData(list);
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }
}
