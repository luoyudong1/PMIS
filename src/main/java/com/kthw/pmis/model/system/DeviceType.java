package com.kthw.pmis.model.system;

/**
 * 配件类型
 */
public class DeviceType {

	private int device_type_id;
	private String device_type_name;
	private String device_type_code;
	public String getDevice_type_code() {
		return device_type_code;
	}
	public void setDevice_type_code(String device_type_code) {
		this.device_type_code = device_type_code;
	}
	public int getDevice_type_id() {
		return device_type_id;
	}
	public void setDevice_type_id(int device_type_id) {
		this.device_type_id = device_type_id;
	}
	public String getDevice_type_name() {
		return device_type_name;
	}
	public void setDevice_type_name(String device_type_name) {
		this.device_type_name = device_type_name;
	}
}
