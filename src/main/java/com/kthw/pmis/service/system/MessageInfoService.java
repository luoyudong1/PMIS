package com.kthw.pmis.service.system;

import java.util.List;
import java.util.Map;

import com.kthw.pmis.model.system.MessageInfo;

import javax.servlet.http.HttpServletRequest;

public interface MessageInfoService {

    //获取消息
    List<MessageInfo> getMessageInfoByUser(String userId);

    //更新消息
    int updateMessageInfo(int message_id);

    //插入消息
    int insertMessageInfo(List<MessageInfo> list);

    //获取消息接收人员
    List<MessageInfo> getMessageReceiveUser(Map<String, String> params);

    //获取消息
    List<MessageInfo> getMessageBydepotId(HttpServletRequest request);

    //获取提示信息
    List<MessageInfo> getHintBydepotId(HttpServletRequest request);

}
