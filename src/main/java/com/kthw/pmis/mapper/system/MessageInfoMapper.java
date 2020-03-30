package com.kthw.pmis.mapper.system;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kthw.pmis.model.system.MessageInfo;


@Repository
public interface MessageInfoMapper {

	//获取消息
	List<MessageInfo> getMessageInfoByUser(String userId);
	
	//更新消息
	void updateMessageInfo(int message_id);
	
	//插入消息
	void insertMessageInfo(List<MessageInfo> list);
	
	//做完消息人员配置，获取处理人员
	List<MessageInfo> getMessageConfig(Map<String, String> params);
	
	//未做消息人员配置时，获取处理人员
	List<MessageInfo> getUserByDepart(Map<String, String> params);
	
}
