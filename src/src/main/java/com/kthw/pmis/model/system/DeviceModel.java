package com.kthw.pmis.model.system;

import java.io.Serializable;

/**
 * 配件型号
 */
public class DeviceModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int device_type_id;
	private String device_model_name;
	private int sort_id;
	private String device_model_code;
	public String getDevice_model_code() {
		return device_model_code;
	}
	public void setDevice_model_code(String device_model_code) {
		this.device_model_code = device_model_code;
	}
	public int getDevice_type_id() {
		return device_type_id;
	}
	public void setDevice_type_id(int device_type_id) {
		this.device_type_id = device_type_id;
	}
	public String getDevice_model_name() {
		return device_model_name;
	}
	public void setDevice_model_name(String device_model_name) {
		this.device_model_name = device_model_name;
	}
	public int getSort_id() {
		return sort_id;
	}
	public void setSort_id(int sort_id) {
		this.sort_id = sort_id;
	}

}
