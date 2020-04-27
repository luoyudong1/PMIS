package com.kthw.pmis.model.system;

import java.io.Serializable;

public class DeviceName implements Serializable{
	private int device_parts_id;
	private String device_parts_name;
	private String device_parts_code;
	private short enabled;
	private String comment;
	public int getDevice_parts_id() {
		return device_parts_id;
	}
	public void setDevice_parts_id(int device_parts_id) {
		this.device_parts_id = device_parts_id;
	}
	public String getDevice_parts_name() {
		return device_parts_name;
	}
	public void setDevice_parts_name(String device_parts_name) {
		this.device_parts_name = device_parts_name;
	}
	public String getDevice_parts_code() {
		return device_parts_code;
	}
	public void setDevice_parts_code(String device_parts_code) {
		this.device_parts_code = device_parts_code;
	}
	public short getEnabled() {
		return enabled;
	}
	public void setEnabled(short enabled) {
		this.enabled = enabled;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
