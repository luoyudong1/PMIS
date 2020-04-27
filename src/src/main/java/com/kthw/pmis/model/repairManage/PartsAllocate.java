package com.kthw.pmis.model.repairManage;

import java.sql.Timestamp;

/**
 * 描述:检修表.
 */
public class PartsAllocate {

	private String part_id_seq;//配件编号
	private int maintenance_price;//维修价格
	private Timestamp send_factory_date;//送厂日期
	private int source_storehouse_id;//源仓库ID
	private String storehouse_name;//源仓库名称
	private String operator_id;//操作人ID
	private String operator_name;//操作人名称
	private String remark;//备注
	private String verify_id;//审核人ID
	private String verify_name;//审核人名称
	private Timestamp verify_date;//审核时间
	private String maintenance_info;//维修情况
	private int verify_flag;//审核标志，0：待审核、1：审核中、2:已审核
	private String fault_info;//故障信息
	private Timestamp return_date;//修返时间
	private int object_storehouse_id;//目标仓库ID
	private String location;//仓库详细位置，探测站
	private Timestamp add_date;//添加时间
	
	private String part_code;//配件出厂编号
	private String part_name;//配件名称
	private String device_model;//配件型号
	
	public Timestamp getAdd_date() {
		return add_date;
	}
	public void setAdd_date(Timestamp add_date) {
		this.add_date = add_date;
	}
	public String getPart_id_seq() {
		return part_id_seq;
	}
	public void setPart_id_seq(String part_id_seq) {
		this.part_id_seq = part_id_seq;
	}
	public int getMaintenance_price() {
		return maintenance_price;
	}
	public void setMaintenance_price(int maintenance_price) {
		this.maintenance_price = maintenance_price;
	}
	public Timestamp getSend_factory_date() {
		return send_factory_date;
	}
	public void setSend_factory_date(Timestamp send_factory_date) {
		this.send_factory_date = send_factory_date;
	}
	public int getSource_storehouse_id() {
		return source_storehouse_id;
	}
	public void setSource_storehouse_id(int source_storehouse_id) {
		this.source_storehouse_id = source_storehouse_id;
	}
	public String getStorehouse_name() {
		return storehouse_name;
	}
	public void setStorehouse_name(String storehouse_name) {
		this.storehouse_name = storehouse_name;
	}
	public String getOperator_id() {
		return operator_id;
	}
	public void setOperator_id(String operator_id) {
		this.operator_id = operator_id;
	}
	public String getOperator_name() {
		return operator_name;
	}
	public void setOperator_name(String operator_name) {
		this.operator_name = operator_name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getVerify_id() {
		return verify_id;
	}
	public void setVerify_id(String verify_id) {
		this.verify_id = verify_id;
	}
	public String getVerify_name() {
		return verify_name;
	}
	public void setVerify_name(String verify_name) {
		this.verify_name = verify_name;
	}
	public Timestamp getVerify_date() {
		return verify_date;
	}
	public void setVerify_date(Timestamp verify_date) {
		this.verify_date = verify_date;
	}
	public String getMaintenance_info() {
		return maintenance_info;
	}
	public void setMaintenance_info(String maintenance_info) {
		this.maintenance_info = maintenance_info;
	}
	public int getVerify_flag() {
		return verify_flag;
	}
	public void setVerify_flag(int verify_flag) {
		this.verify_flag = verify_flag;
	}
	public String getFault_info() {
		return fault_info;
	}
	public void setFault_info(String fault_info) {
		this.fault_info = fault_info;
	}
	public Timestamp getReturn_date() {
		return return_date;
	}
	public void setReturn_date(Timestamp return_date) {
		this.return_date = return_date;
	}
	public int getObject_storehouse_id() {
		return object_storehouse_id;
	}
	public void setObject_storehouse_id(int object_storehouse_id) {
		this.object_storehouse_id = object_storehouse_id;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getPart_code() {
		return part_code;
	}
	public void setPart_code(String part_code) {
		this.part_code = part_code;
	}
	public String getPart_name() {
		return part_name;
	}
	public void setPart_name(String part_name) {
		this.part_name = part_name;
	}
	public String getDevice_model() {
		return device_model;
	}
	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}
	
}
