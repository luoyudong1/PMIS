package com.kthw.pmis.serviceimpl.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kthw.pmis.entiy.Depot;
import com.kthw.pmis.mapper.common.DepotMapper;
import com.kthw.pmis.mapper.common.DetectPartsMapper;
import com.kthw.pmis.mapper.common.SheetInfoMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kthw.common.base.ErrCode;
import com.kthw.pmis.mapper.system.MessageInfoMapper;
import com.kthw.pmis.model.system.MessageInfo;
import com.kthw.pmis.service.system.MessageInfoService;

import javax.servlet.http.HttpServletRequest;

@Service
public class MessageInfoServiceImpl implements MessageInfoService {

    private static Logger logger = LoggerFactory.getLogger(MessageInfoServiceImpl.class);

    @Autowired
    private MessageInfoMapper messageInfoMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private SheetInfoMapper sheetInfoMapper;
    @Autowired
    private DetectPartsMapper detectPartsMapper;

    @Override
    public List<MessageInfo> getMessageInfoByUser(String userId) {
        List<MessageInfo> messgaeInfos = null;
        try {
            messgaeInfos = messageInfoMapper.getMessageInfoByUser(userId);
        } catch (Exception e) {
            logger.error("getMessages error: ", e);
        }
        return messgaeInfos;
    }

    @Override
    public int updateMessageInfo(int message_id) {
        int errCode = 0;
        try {
            messageInfoMapper.updateMessageInfo(message_id);
        } catch (Exception e) {
            logger.error("message processing error: ", e);
            errCode = ErrCode.DB_ERROR;
        }
        return errCode;
    }
    private List<MessageInfo> getTestMessage(Long depotId){
        List<MessageInfo> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("depotId",depotId);
        params.put("sheetType", (short) 9);
        int count = sheetInfoMapper.getNotReceiptSheetInfo(params);
        if (count > 0) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage_info("所未接收车间返修的单据为" + count + "条");
            list.add(messageInfo);
        }
        count = sheetInfoMapper.getNotVerifySheetInfo(params);
        if (count > 0) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage_info("所未审核车间返修的单据为" + count + "条");
            list.add(messageInfo);
        }
        return list;
    }
    private List<MessageInfo> getWorkShopMessage(Long depotId){
        List<MessageInfo> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        //车间未接收所调拨的单据数量
        params.put("depotId", Long.valueOf(depotId));
        params.put("sheetType", (short) 10);
        int noReceiptCount = sheetInfoMapper.getNotReceiptSheetInfo(params);
        if (noReceiptCount > 0) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage_info("车间未接收所调拨的单据为" + noReceiptCount + "条");
            list.add(messageInfo);
        }
        //车间未审核所调拨的单据数量
        int noVerifyCount = sheetInfoMapper.getNotVerifySheetInfo(params);
        if (noVerifyCount > 0) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage_info("车间接收所调拨未审核的单据为" + noVerifyCount + "条");
            list.add(messageInfo);
        }
        return list;
    }
    private List<MessageInfo> getDepotMessage(Long depotId){
        List<MessageInfo> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        //车间未接收所调拨的单据数量
        params.clear();
        params.put("depotId", Long.valueOf(depotId));
        List<String> detectDeviceList = detectPartsMapper.getPartsUnstall(params);
        if (detectDeviceList.size() > 0) {
            for (String detectDevice : detectDeviceList) {
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setMessage_info("探测站" + detectDevice + "有配件未安装");
                list.add(messageInfo);
            }
        }
        return list;
    }
    //根据部门获取消息
    @Override
    public List<MessageInfo> getMessageBydepotId(HttpServletRequest request) {

        List<MessageInfo> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        try {
            String depotId = request.getParameter("depotId");
            if (StringUtils.isNotBlank(depotId)) {
                Depot depot = depotMapper.selectByPrimaryKey(Long.valueOf(depotId));
                switch (depot.getDepotLevel()) {
                    case 2://检测所单据未接收信息
                       list.addAll(getTestMessage(Long.valueOf(depotId)));
                        break;
                    case 4://车间单据未接收信息
                       list.addAll(getWorkShopMessage(Long.valueOf(depotId)));
                        break;
                    case 5://班组探测站配件未安装信息
                        list.addAll(getDepotMessage(Long.valueOf(depotId)));
                        break;

                }
            }
        } catch (Exception e) {
            logger.error("getMessageBydepotId error: ", e);
        }
        return list;
    }

    @Override
    public int insertMessageInfo(List<MessageInfo> list) {
        int errCode = 0;
        try {
            messageInfoMapper.insertMessageInfo(list);
        } catch (Exception e) {
            logger.error("insert message error: ", e);
            errCode = ErrCode.DB_ERROR;
        }
        return errCode;
    }

    @Override
    public List<MessageInfo> getMessageReceiveUser(Map<String, String> params) {
        List<MessageInfo> messageInfos = messageInfoMapper.getMessageConfig(params);
        if (messageInfos.size() == 0) {
            try {
                messageInfos = messageInfoMapper.getUserByDepart(params);
            } catch (Exception e) {
                logger.error("getMessagesUser error: ", e);
            }
        }
        String type = params.get("type");
        switch (type) {
            case "1":
                for (MessageInfo messageInfo : messageInfos) {
                    messageInfo.setMessage_info("调度命令");
                    messageInfo.setMessage_type(1);
                    messageInfo.setMessage_priority(3);
                }
                break;
            case "2":
                for (MessageInfo messageInfo : messageInfos) {
                    messageInfo.setMessage_info("天气预警命令");
                    messageInfo.setMessage_type(2);
                    messageInfo.setMessage_priority(3);
                }
                break;
            case "3":
                for (MessageInfo messageInfo : messageInfos) {
                    messageInfo.setMessage_info("应急预案");
                    messageInfo.setMessage_type(3);
                    messageInfo.setMessage_priority(1);
                }
                break;
            default:
                break;

        }
        return messageInfos;
    }


}