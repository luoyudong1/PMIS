package com.kthw.pmis.controller.checkPlan;

import com.kthw.pmis.mapper.common.PlanCheckSheetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/checkPlan/sheet")
public class CheckPlanSheetController {
    private static final Logger logger = LoggerFactory.getLogger(CheckPlanSheetController.class);
    @Autowired
    private PlanCheckSheetMapper planCheckSheetMapper;

    @ResponseBody
    @RequestMapping(value = "/getMaxSheetId", method = {RequestMethod.GET})
    public synchronized String getMaxSheetId(@RequestParam String sheet_id, HttpServletRequest request) {
        logger.info("获取检修计划最大sheet_id");
        String maxSheetId= planCheckSheetMapper.getMaxSheetId(sheet_id);
        try{if (null == maxSheetId) {
            maxSheetId = sheet_id + "00000001";
            return maxSheetId;

        } else {
            Integer serial = Integer.valueOf(maxSheetId.substring(7)) + 1;
            String str = String.valueOf(serial);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 8 - str.length(); ++i) {
                sb.append("0");
            }
            sb.append(serial);

            String sheet_id2 = sheet_id + sb;
            return sheet_id2;

        }
        } catch (Exception e) {
            logger.error("getMaxSheetId error" + request);
        }
        return  null;
    }

}
