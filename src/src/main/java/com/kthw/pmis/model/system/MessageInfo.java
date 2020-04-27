package com.kthw.pmis.model.system;


import java.io.Serializable;
import java.util.Date;

public class MessageInfo implements Serializable {
    
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8344448674793622309L;
	
	//消息主键
	private int message_id;
	
	//消息内容
	private String message_info;
    
	//添加日期
	private Date add_time;
	
	//消息类型
	private int message_type;
	
	//部门ID
	private int depart_id;
	
	//用户ID	
	private String user_id;
	
	//优先级
	private int message_priority;
	
	//处理状态
	private int message_state;

	
	public int getMessage_id() {
		return message_id;
	}

	public void setMessage_id(int message_id) {
		this.message_id = message_id;
	}

	public String getMessage_info() {
		return message_info;
	}

	public void setMessage_info(String message_info) {
		this.message_info = message_info;
	}

	public Date getAdd_time() {
		return add_time;
	}

	public void setAdd_time(Date add_time) {
		this.add_time = add_time;
	}

	public int getMessage_type() {
		return message_type;
	}

	public void setMessage_type(int message_type) {
		this.message_type = message_type;
	}

	public int getDepart_id() {
		return depart_id;
	}

	public void setDepart_id(int depart_id) {
		this.depart_id = depart_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getMessage_priority() {
		return message_priority;
	}

	public void setMessage_priority(int message_priority) {
		this.message_priority = message_priority;
	}

	public int getMessage_state() {
		return message_state;
	}

	public void setMessage_state(int message_state) {
		this.message_state = message_state;
	}

	
}
