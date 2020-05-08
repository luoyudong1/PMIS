package com.kthw.pmis.serviceimpl.system;

import com.kthw.common.base.ErrCode;
import com.kthw.pmis.entiy.Depot;
import com.kthw.pmis.mapper.common.*;
import com.kthw.pmis.mapper.system.MessageInfoMapper;
import com.kthw.pmis.model.system.MessageInfo;
import com.kthw.pmis.service.system.MessageInfoService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    @Autowired
    private FaultHandleMapper faultHandleMapper;
    @Autowired
    private PlanCheckMapper planCheckMapper;

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

    /**
     *
     * PMIS系统各登录用户根据部门及角色获取消息
     * @param depotId
     * @return
     */
    //获取 机辆所库管人员 未接收车间返修的单据数量和所内检修出库未接收的单据数量
    private List<MessageInfo> getTestMessage(Long depotId){
        List<MessageInfo> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        //所未接收车间返修的单据数量(机辆所库管)
        params.put("depotId",depotId);
        params.put("sheetType", (short) 9);
        int count = sheetInfoMapper.getNotReceiptSheetInfo(params);
        if (count > 0) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage_info(" 所未接收车间返修的单据为" + count + "条");
            messageInfo.setMessage_state(count);
            messageInfo.setMessage_id(0);
            list.add(messageInfo);
        }
        //机辆所库管未接收所内检修出库审核数量     //ZZF.ADD
        params.put("depotId",depotId);
        params.put("sheetType", (short) 7);
        count = sheetInfoMapper.getNotVerifySendSheetInfo(params);
        if (count > 0) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage_info(" 所内未审核所内检修出库单据为" + count + "条");
            messageInfo.setMessage_state(count);
            messageInfo.setMessage_id(1);
            list.add(messageInfo);

        }
        return list;
    }

    //获取 机辆所审核人员 未审核车间返修的单据和所未审核所配送到车间调拨的单据数量              ZZF.ADD
    private List<MessageInfo> getTestMessageRole(Long depotId){
        List<MessageInfo> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        //所未审核车间返修的单据数量
        params.put("depotId",depotId);
        params.put("sheetType", (short) 9);
        int count = sheetInfoMapper.getNotVerifySheetInfo(params);
        if (count > 0) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage_info(" 所未审核车间返修的单据为" + count + "条");
            messageInfo.setMessage_state(count);
            messageInfo.setMessage_id(0);
            list.add(messageInfo);
        }
        ///所未审核所配送到车间调拨的单据数量
        params.put("depotId",depotId);
        params.put("sheetType", (short) 10);
        count = sheetInfoMapper.getNotVerifySendSheetInfo(params);
        if (count > 0) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage_info(" 所未审核所配送到车间调拨单据为" + count + "条");
            messageInfo.setMessage_state(count);
            messageInfo.setMessage_id(1);
            list.add(messageInfo);
        }
        //所未审核所报废配送到车间调拨的单据数量
        params.put("depotId",depotId);
        params.put("sheetType", (short) 5);
        count = sheetInfoMapper.getNotVerifySendSheetInfo(params);
        if (count > 0) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage_info(" 所未审核所报废配送到车间调拨单据为" + count + "条");
            messageInfo.setMessage_state(count);
            messageInfo.setMessage_id(2);
            list.add(messageInfo);
        }
        return list;
    }

    //获取机辆所维修人员未接收所内送修单            ZZF.ADD
    private List<MessageInfo> getFixMessage(Long depotId) {
        List<MessageInfo> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        //机辆所维修人员未接收所内送修单数量
        params.put("depotId",depotId);
        params.put("sheetType", (short) 6);
        int count = sheetInfoMapper.getNotVerifySendSheetInfo(params);
        if (count > 0) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage_info(" 所内未审核的送修单据为" + count + "条");
            messageInfo.setMessage_state(count);
            messageInfo.setMessage_id(0);
            list.add(messageInfo);
        }
        return list;
    }

    //获取车间录入人员未接收所调拨的单据数量
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
            messageInfo.setMessage_state(noReceiptCount);
            messageInfo.setMessage_id(0);
            list.add(messageInfo);
        }
        params.put("depotId", Long.valueOf(depotId));
        params.put("sheetType", (short) 5);
        noReceiptCount = sheetInfoMapper.getNotReceiptSheetInfo(params);
        if (noReceiptCount > 0) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage_info("车间未接收所报废调拨的单据为" + noReceiptCount + "条");
            messageInfo.setMessage_state(noReceiptCount);
            messageInfo.setMessage_id(1);
            list.add(messageInfo);
        }
        return list;
    }

    //获取车间审核人员未未审核所调拨的单据数量 ，未审核所报废调拨的单据数量 和车间未审核返修调拨的单据数量
    private List<MessageInfo> getWorkShopMessageRole(Long depotId) {
        List<MessageInfo> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        //车间未审核所调拨的单据数量
        params.put("depotId", Long.valueOf(depotId));
        params.put("sheetType", (short) 10);
        int noVerifyCount = sheetInfoMapper.getNotVerifySheetInfo(params);
        if (noVerifyCount > 0) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage_info("车间未审核所调拨的单据为" + noVerifyCount + "条");
            messageInfo.setMessage_state(noVerifyCount);
            messageInfo.setMessage_id(0);
            list.add(messageInfo);
        }
        params.put("depotId", Long.valueOf(depotId));
        params.put("sheetType", (short) 5);
        noVerifyCount = sheetInfoMapper.getNotVerifySheetInfo(params);
        if (noVerifyCount > 0) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage_info("车间未审核所报废调拨的单据为" + noVerifyCount + "条");
            messageInfo.setMessage_state(noVerifyCount);
            messageInfo.setMessage_id(1);
            list.add(messageInfo);
        }
        params.put("depotId", Long.valueOf(depotId));
        params.put("sheetType", (short) 9);
        noVerifyCount = sheetInfoMapper.getNotVerifySendSheetInfo(params);
        if (noVerifyCount > 0) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage_info("车间未审核返修调拨的单据为" + noVerifyCount + "条");
            messageInfo.setMessage_state(noVerifyCount);
            messageInfo.setMessage_id(2);
            list.add(messageInfo);
        }
        return list;
    }

    //班组录入人员 获得探测站拆除配件未安装数量
    private List<MessageInfo> getDepotMessage(Long depotId){
        List<MessageInfo> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        //车间未接收所调拨的单据数量
        params.clear();
        params.put("depotId", Long.valueOf(depotId));
        List<String> detectDeviceList = detectPartsMapper.getPartsUnstall(params);
        if (detectDeviceList.size() > 0) {
            int i=0;
            for (String detectDevice : detectDeviceList) {
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setMessage_info("探测站" + detectDevice + "有配件未安装");
                messageInfo.setMessage_state(1);
                messageInfo.setMessage_id(i);
                i++;
                list.add(messageInfo);
            }
        }
        return list;
    }

    /**
     * MIS系统各登录用户根据部门及角色获取消息
     * @param depotId
     * @return
     */

    //集团调度员  获得故障预报未确认信息条数  获得故障开始处理未确认信息条数  获得故障结束未确认信息条数
    private List<MessageInfo> getDispatchMessage(Long depotId,Long dispatcher) {
        List<MessageInfo> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        // 获得故障预报未确认信息条数
        params.clear();
        params.put("flag", Long.valueOf(2));
        params.put("dispatcher", dispatcher);
        int noVerifyCount = faultHandleMapper.getFaultCheckByMap(params);
        if (noVerifyCount > 0) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage_info("故障预报未确认信息为" + noVerifyCount + "条");
            messageInfo.setMessage_state(noVerifyCount);
            messageInfo.setMessage_id(0);
            list.add(messageInfo);
        }
        //获得故障开始处理未确认信息条数
        params.put("flag", Long.valueOf(4));
        params.put("dispatcher", dispatcher);
        noVerifyCount = faultHandleMapper.getFaultCheckByMap(params);
        if (noVerifyCount > 0) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage_info("故障处理开始未确认信息为" + noVerifyCount + "条");
            messageInfo.setMessage_state(noVerifyCount);
            messageInfo.setMessage_id(1);
            list.add(messageInfo);
        }
        //获得故障结束未确认信息条数
        params.put("flag", Long.valueOf(6));
        params.put("dispatcher", dispatcher);
        noVerifyCount = faultHandleMapper.getFaultCheckByMap(params);
        if (noVerifyCount > 0) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage_info("故障处理结束未确认信息为" + noVerifyCount + "条");
            messageInfo.setMessage_state(noVerifyCount);
            messageInfo.setMessage_id(2);
            list.add(messageInfo);
        }

        //获得检修开始未确认信息条数
        params.put("status", Long.valueOf(3));
        params.put("dispatcher", dispatcher);
        noVerifyCount = planCheckMapper.getPlanCheckByMap(params);
        if (noVerifyCount > 0) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage_info("计划检修开始未确认信息为" + noVerifyCount + "条");
            messageInfo.setMessage_state(noVerifyCount);
            messageInfo.setMessage_id(3);
            list.add(messageInfo);
        }
        //获得检修结束未确认信息条数
        params.put("status", Long.valueOf(5));
        params.put("dispatcher", dispatcher);
        noVerifyCount = planCheckMapper.getPlanCheckByMap(params);
        if (noVerifyCount > 0) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage_info("计划检修结束未确认信息为" + noVerifyCount + "条");
            messageInfo.setMessage_state(noVerifyCount);
            messageInfo.setMessage_id(4);
            list.add(messageInfo);
        }

        return list;
    }

    //段值班员 获得故障预报未确认信息条数  获得故障开始处理未确认信息条数  获得故障结束未确认信息条数
    private List<MessageInfo> getDispatchEchoMessage(Long depotId) {
        List<MessageInfo> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        // 获得故障预报未确认信息条数
        params.clear();
        params.put("flag", Long.valueOf(3));
        params.put("depotId", Long.valueOf(depotId));
        int noVerifyCount = faultHandleMapper.getFaultCheckByMapDepot(params);
        if (noVerifyCount > 0) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage_info("故障待开始处理未确认信息为" + noVerifyCount + "条");
            messageInfo.setMessage_state(noVerifyCount);
            messageInfo.setMessage_id(0);
            list.add(messageInfo);
        }
        //获得故障开始处理未确认信息条数
        params.put("flag", Long.valueOf(5));
        params.put("depotId", Long.valueOf(depotId));
        noVerifyCount = faultHandleMapper.getFaultCheckByMapDepot(params);
        if (noVerifyCount > 0) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage_info("故障待处理结束未确认信息为" + noVerifyCount + "条");
            messageInfo.setMessage_state(noVerifyCount);
            messageInfo.setMessage_id(1);
            list.add(messageInfo);
        }

        //获得计划检修处理待结束未确认信息条数
        params.put("status", Long.valueOf(4));
        params.put("depotId", Long.valueOf(depotId));
        noVerifyCount = planCheckMapper.getPlanCheckByMapDept(params);
        if (noVerifyCount > 0) {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMessage_info("计划检修待处理结束未确认信息为" + noVerifyCount + "条");
            messageInfo.setMessage_state(noVerifyCount);
            messageInfo.setMessage_id(2);
            list.add(messageInfo);
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
            String roleId = request.getParameter("roleId");      //    ZF:增加了用户权限参数
            String roleCode = request.getParameter("roleCode");   //ZF:增加了用户登录的系统编号
            String dispatcher=request.getParameter("dispatcher"); //ZF:增加了调度员所选调度台

            switch (roleCode){
                case "PMIS":      //用户登录的为配件系统
                    if (StringUtils.isNotBlank(depotId)) {
                        Depot depot = depotMapper.selectByPrimaryKey(Long.valueOf(depotId));
                        switch (depot.getDepotLevel()) {                    //    ZF:按用户权限进行了二次分类获取信息
                            case 2://检测所单据未接收信息
                                if(roleId.equals("2")){ // 机辆所库管员
                                    list.addAll(getTestMessage(Long.valueOf(depotId)));
                                    break;
                                }
                                if(roleId.equals("3")){  //机辆所审核员
                                    list.addAll(getTestMessageRole(Long.valueOf(depotId)));
                                    break;
                                }
                                if(roleId.equals("4")){  //机辆所检修员
                                    list.addAll(getFixMessage(Long.valueOf(depotId)));
                                    break;
                                }
                                break;
                            case 4://车间单据未接收信息
                                if(roleId.equals("6")){   //车间录入员
                                    list.addAll(getWorkShopMessage(Long.valueOf(depotId)));
                                    break;
                                }
                                if(roleId.equals("7")){   //车间审核员
                                    list.addAll(getWorkShopMessageRole(Long.valueOf(depotId)));
                                    break;
                                }
                                break;
                            case 5://班组探测站配件未安装信息
                                list.addAll(getDepotMessage(Long.valueOf(depotId)));
                                break;

                        }
                    }
                    break;
                case "TMIS":   //用户登录的为故障预报系统
                    if (StringUtils.isNotBlank(depotId)) {
                        Depot depot = depotMapper.selectByPrimaryKey(Long.valueOf(depotId));
                        switch (depot.getDepotLevel()) {                    //    ZF:按用户权限进行了二次分类获取信息
                            case 2://辆安站单据未接收信息
                                if(roleId.equals("13")){ // 辆安站调度员
                                    list.addAll(getDispatchMessage(Long.valueOf(depotId),Long.valueOf(dispatcher)));
                                    break;
                                }
                                break;
                            case 3://车间单据未接收信息
                            case 4://车间单据未接收信息
                                if(roleId.equals("12")){   //车间值班员
                                    list.addAll(getDispatchEchoMessage(Long.valueOf(depotId)));
                                    break;
                                }
                                break;
                        }
                    }
                    break;
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