package com.kthw.pmis.controller.system;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.kthw.pmis.entiy.Depot;
import com.kthw.pmis.mapper.common.DepotMapper;
import com.kthw.pmis.mapper.common.SheetInfoMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kthw.common.base.BaseController;
import com.kthw.common.base.ErrCode;
import com.kthw.pmis.model.TableView;
import com.kthw.pmis.model.system.MessageInfo;
import com.kthw.pmis.model.system.User;
import com.kthw.pmis.service.system.MessageInfoService;

/**
 * 描述:消息处理Controller.
 */
@Controller
@RequestMapping(value = "/system/messageInfo")
public class
MessageInfoController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(MessageInfoController.class);

    @Autowired
    private MessageInfoService messageInfoService;

    @RequestMapping(value = "getMessageInfoList", method = {RequestMethod.GET})
    @ResponseBody
    public TableView<MessageInfo> getMessageInfoList(HttpServletRequest request, HttpSession httpSession) {
        logger.info("查询用户消息列表");
        User user = (User) httpSession.getAttribute("AUTH_USER");
        String userId = user.getUser_id();
        TableView<MessageInfo> view = new TableView<MessageInfo>();
        view.setData(messageInfoService.getMessageBydepotId(request));
        return view;
    }

    @RequestMapping(value = "updateMessageState", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, String> updateMessageState(@RequestParam Map<String, String> params, HttpServletRequest request, HttpSession httpSession) {
        int code = 0;
        logger.info("处理消息");
        int message_id = Integer.parseInt(params.get("message_id"));
        try {
            messageInfoService.updateMessageInfo(message_id);
        } catch (Exception e) {
            e.printStackTrace();
            code = ErrCode.DB_ERROR;
        }
        Map<String, String> ret = new HashMap<>();
        if (code != 0) {
            ret.put("msg", "数据库连接错误");
        }
        ret.put("code", code + "");
        return ret;
    }


}
