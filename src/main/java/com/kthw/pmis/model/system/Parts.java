package com.kthw.pmis.model.system;

import java.io.Serializable;

/**
 * 描述:配件
 */
public class Parts implements Serializable{
	private String parts_id;//配件ID
	private String parts_code;//配件编码前缀
	private int device_parts_id;//配件名称ID
	private int device_type_id;//配件所属设备类型ID
	private int device_model_id;//配件所属设备型号ID
	private int unit_price;//单价
	private String unit;//单位
	private String pic;//图片
	private int low_alarm_quantity;//低储备报警数量
	private int high_alarm_quantity;//高储备报警数量
	private int enabled;//是否可用，1：可用，0：不可用
	private int supplier_id;//所属厂家ID
	
	private String supplier_name;//所属厂家名称
	private String device_parts_name;//配件名称
	private String device_type_name;//配件所属设备类型名称
	private String device_model_name;//配件所属设备型号名称
	
	public String getParts_id() {
		return parts_id;
	}
	public void setParts_id(String parts_id) {
		this.parts_id = parts_id;
	}
	public String getParts_code() {
		return parts_code;
	}
	public void setParts_code(String parts_code) {
		this.parts_code = parts_code;
	}
	public int getDevice_parts_id() {
		return device_parts_id;
	}
	public void setDevice_parts_id(int device_parts_id) {
		this.device_parts_id = device_parts_id;
	}
	public int getDevice_type_id() {
		return device_type_id;
	}
	public void setDevice_type_id(int device_type_id) {
		this.device_type_id = device_type_id;
	}
	public int getDevice_model_id() {
		return device_model_id;
	}
	public void setDevice_model_id(int device_model_id) {
		this.device_model_id = device_model_id;
	}
	public int getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(int unit_price) {
		this.unit_price = unit_price;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public int getLow_alarm_quantity() {
		return low_alarm_quantity;
	}
	public void setLow_alarm_quantity(int low_alarm_quantity) {
		this.low_alarm_quantity = low_alarm_quantity;
	}
	public int getHigh_alarm_quantity() {
		return high_alarm_quantity;
	}
	public void setHigh_alarm_quantity(int high_alarm_quantity) {
		this.high_alarm_quantity = high_alarm_quantity;
	}
	public int getEnabled() {
		return enabled;
	}
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	public int getSupplier_id() {
		return supplier_id;
	}
	public void setSupplier_id(int supplier_id) {
		this.supplier_id = supplier_id;
	}
	public String getSupplier_name() {
		return supplier_name;
	}
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}
	public String getDevice_parts_name() {
		return device_parts_name;
	}
	public void setDevice_parts_name(String device_parts_name) {
		this.device_parts_name = device_parts_name;
	}
	public String getDevice_type_name() {
		return device_type_name;
	}
	public void setDevice_type_name(String device_type_name) {
		this.device_type_name = device_type_name;
	}
	public String getDevice_model_name() {
		return device_model_name;
	}
	public void setDevice_model_name(String device_model_name) {
		this.device_model_name = device_model_name;
	}
	
}
