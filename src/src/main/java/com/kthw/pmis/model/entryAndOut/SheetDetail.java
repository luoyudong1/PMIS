package com.kthw.pmis.model.entryAndOut;

import java.sql.Timestamp;

/**
 * 描述:出入库配件详情.
 */
public class SheetDetail {

	private String sheet_id;//单据唯一编码
	private String part_id;//配件编号
	private int quantity;//配件数量，只能是“１”，每个配件只有唯一一件
	private double unit_price;//单价
	private String remark;//备注
	private String location;//仓库详细位置
	private String part_code;//配件出厂编号
	private int asset_attributes_id;//资产属性ID
	private String fault_info;//故障信息
	private Timestamp fault_date;//故障发生日期
	private String fault_remark;//故障备注
	private String prepare_check;//检修项目1：上机预检
	private String machine_check;//检修项目2：检测计算机检测
	private String replace_component_check;//检修项目3：更换元器件
	private Timestamp copy_machine_start_time;//检修项目4：拷机检测开始时间
	private Timestamp copy_machine_end_time;//检修项目4：拷机检测结束时间
	private String copy_machine_check;//检修项目4：拷机检测
	private Timestamp checked_date;//所内检修日期
	private String checked_user_id;//所内检修人ID
	private String checked_user_name;//所内检修人名称
	private String review_user_id;//检修复核人ID
	private String review_user_name;//检修复核人名称
	private String checked_remark;//所内检修备注
	private int checked_price;//所内检修单价
	private int repaire_state;//修复状态：（1.所内检修合格；2.所内检修不合格；3.所内检修报废；4.返厂检修合格；5.返厂检修不合格；6.返厂检修报废）
	private String scrap_reason;//报废原因
	private String factory_replace_component;//返厂检修更换元件
	private int factory_replace_count;//返厂检修更换元件数量
	private int factory_repaired_price;//返厂检修费用
	private Timestamp factory_repaired_date;//返厂检修日期
	private String used_station_name;//使用探测站名称
	private int used_station_id;//使用探测站ID
	private Timestamp purchase_or_repaired_date;//新购或修复日期
	private int purchase_or_repaired_price;//新购或修复单价
	private int part_state;//配件属性：（1.新购；2.修复；3.检修；4.报废）
	private Timestamp arrive_date;//到返厂时间
	
	
	private String device_parts_name; //配件名称
	private String device_model_name;//设备型号
	private String device_type_name;//设备类型名称
	private String supplier_name;//厂家名称
	private Timestamp  verify_date;//审核日期
	private int source;//来源方式
	private String asset_attributes_name;//资产属性名称
	private Timestamp arrival_date;//到返厂库时间
	private String fault_information;//故障信息
	private Timestamp return_factory_date;//返厂时间
	private int maintenance_price;//维修价格
	private Timestamp send_factory_date;//送厂时间
	private String maintenance_info;//维修情况
	
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
	public String getMaintenance_info() {
		return maintenance_info;
	}
	public void setMaintenance_info(String maintenance_info) {
		this.maintenance_info = maintenance_info;
	}
	public Timestamp getVerify_date() {
		return verify_date;
	}
	public void setVerify_date(Timestamp verify_date) {
		this.verify_date = verify_date;
	}
	public String getSheet_id() {
		return sheet_id;
	}
	public void setSheet_id(String sheet_id) {
		this.sheet_id = sheet_id;
	}
	public String getPart_id() {
		return part_id;
	}
	public void setPart_id(String part_id) {
		this.part_id = part_id;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(double unit_price) {
		this.unit_price = unit_price;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getDevice_parts_name() {
		return device_parts_name;
	}
	public void setDevice_parts_name(String device_parts_name) {
		this.device_parts_name = device_parts_name;
	}
	public String getDevice_model_name() {
		return device_model_name;
	}
	public void setDevice_model_name(String device_model_name) {
		this.device_model_name = device_model_name;
	}
	public String getDevice_type_name() {
		return device_type_name;
	}
	public void setDevice_type_name(String device_type_name) {
		this.device_type_name = device_type_name;
	}
	public String getSupplier_name() {
		return supplier_name;
	}
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public int getAsset_attributes_id() {
		return asset_attributes_id;
	}
	public void setAsset_attributes_id(int asset_attributes_id) {
		this.asset_attributes_id = asset_attributes_id;
	}
	public String getAsset_attributes_name() {
		return asset_attributes_name;
	}
	public void setAsset_attributes_name(String asset_attributes_name) {
		this.asset_attributes_name = asset_attributes_name;
	}
	public Timestamp getArrival_date() {
		return arrival_date;
	}
	public void setArrival_date(Timestamp arrival_date) {
		this.arrival_date = arrival_date;
	}
	public String getFault_information() {
		return fault_information;
	}
	public void setFault_information(String fault_information) {
		this.fault_information = fault_information;
	}
	public Timestamp getReturn_factory_date() {
		return return_factory_date;
	}
	public void setReturn_factory_date(Timestamp return_factory_date) {
		this.return_factory_date = return_factory_date;
	}
	public String getFault_info() {
		return fault_info;
	}
	public void setFault_info(String fault_info) {
		this.fault_info = fault_info;
	}
	public Timestamp getFault_date() {
		return fault_date;
	}
	public void setFault_date(Timestamp fault_date) {
		this.fault_date = fault_date;
	}
	public String getFault_remark() {
		return fault_remark;
	}
	public void setFault_remark(String fault_remark) {
		this.fault_remark = fault_remark;
	}
	public String getPrepare_check() {
		return prepare_check;
	}
	public void setPrepare_check(String prepare_check) {
		this.prepare_check = prepare_check;
	}
	public String getMachine_check() {
		return machine_check;
	}
	public void setMachine_check(String machine_check) {
		this.machine_check = machine_check;
	}
	public String getReplace_component_check() {
		return replace_component_check;
	}
	public void setReplace_component_check(String replace_component_check) {
		this.replace_component_check = replace_component_check;
	}
	public Timestamp getCopy_machine_start_time() {
		return copy_machine_start_time;
	}
	public void setCopy_machine_start_time(Timestamp copy_machine_start_time) {
		this.copy_machine_start_time = copy_machine_start_time;
	}
	public Timestamp getCopy_machine_end_time() {
		return copy_machine_end_time;
	}
	public void setCopy_machine_end_time(Timestamp copy_machine_end_time) {
		this.copy_machine_end_time = copy_machine_end_time;
	}
	public String getCopy_machine_check() {
		return copy_machine_check;
	}
	public void setCopy_machine_check(String copy_machine_check) {
		this.copy_machine_check = copy_machine_check;
	}
	public Timestamp getChecked_date() {
		return checked_date;
	}
	public void setChecked_date(Timestamp checked_date) {
		this.checked_date = checked_date;
	}
	public String getChecked_user_id() {
		return checked_user_id;
	}
	public void setChecked_user_id(String checked_user_id) {
		this.checked_user_id = checked_user_id;
	}
	public String getChecked_user_name() {
		return checked_user_name;
	}
	public void setChecked_user_name(String checked_user_name) {
		this.checked_user_name = checked_user_name;
	}
	public String getReview_user_id() {
		return review_user_id;
	}
	public void setReview_user_id(String review_user_id) {
		this.review_user_id = review_user_id;
	}
	public String getReview_user_name() {
		return review_user_name;
	}
	public void setReview_user_name(String review_user_name) {
		this.review_user_name = review_user_name;
	}
	public String getChecked_remark() {
		return checked_remark;
	}
	public void setChecked_remark(String checked_remark) {
		this.checked_remark = checked_remark;
	}
	public int getChecked_price() {
		return checked_price;
	}
	public void setChecked_price(int checked_price) {
		this.checked_price = checked_price;
	}
	public int getRepaire_state() {
		return repaire_state;
	}
	public void setRepaire_state(int repaire_state) {
		this.repaire_state = repaire_state;
	}
	public String getScrap_reason() {
		return scrap_reason;
	}
	public void setScrap_reason(String scrap_reason) {
		this.scrap_reason = scrap_reason;
	}
	public String getFactory_replace_component() {
		return factory_replace_component;
	}
	public void setFactory_replace_component(String factory_replace_component) {
		this.factory_replace_component = factory_replace_component;
	}
	public int getFactory_replace_count() {
		return factory_replace_count;
	}
	public void setFactory_replace_count(int factory_replace_count) {
		this.factory_replace_count = factory_replace_count;
	}
	public int getFactory_repaired_price() {
		return factory_repaired_price;
	}
	public void setFactory_repaired_price(int factory_repaired_price) {
		this.factory_repaired_price = factory_repaired_price;
	}
	public Timestamp getFactory_repaired_date() {
		return factory_repaired_date;
	}
	public void setFactory_repaired_date(Timestamp factory_repaired_date) {
		this.factory_repaired_date = factory_repaired_date;
	}
	public String getUsed_station_name() {
		return used_station_name;
	}
	public void setUsed_station_name(String used_station_name) {
		this.used_station_name = used_station_name;
	}
	public int getUsed_station_id() {
		return used_station_id;
	}
	public void setUsed_station_id(int used_station_id) {
		this.used_station_id = used_station_id;
	}
	public Timestamp getPurchase_or_repaired_date() {
		return purchase_or_repaired_date;
	}
	public void setPurchase_or_repaired_date(Timestamp purchase_or_repaired_date) {
		this.purchase_or_repaired_date = purchase_or_repaired_date;
	}
	public int getPurchase_or_repaired_price() {
		return purchase_or_repaired_price;
	}
	public void setPurchase_or_repaired_price(int purchase_or_repaired_price) {
		this.purchase_or_repaired_price = purchase_or_repaired_price;
	}
	public int getPart_state() {
		return part_state;
	}
	public void setPart_state(int part_state) {
		this.part_state = part_state;
	}
	public Timestamp getArrive_date() {
		return arrive_date;
	}
	public void setArrive_date(Timestamp arrive_date) {
		this.arrive_date = arrive_date;
	}
	
}
