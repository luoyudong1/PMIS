package com.kthw.pmis.model.stock;

import java.io.Serializable;
import java.sql.Timestamp;

import com.kthw.pmis.model.system.Parts;

/**
 * 描述:配件库存信息.
 */
public class StockInfo extends Parts implements Serializable{
	
	private static final long serialVersionUID = -6701279636363055010L;
	
	private String factory_parts_code;  //配件出厂编号
	private String part_id_seq;  //配件编号,流水号
	private int quantity; // 配件数量，只能是“１”，每个配件只有唯一一件
	//private float unit_price; //单价
	private int entry_type; //入库方式（1：采购入库，2：生产入库，0:手动添加）
	private String sheet_id; //单据编号(采购入库、生产入库生成的单据编号)
	private int storehouse_id; //仓库ID
	private String operator_id; //操作人ID
	private String operator_name; //操作人名称
	private String remark;  //备注
	private String location; //仓库详细位置
	private int state; //配件状态(1：库存，0：已报废）
	private Timestamp add_date; //添加日期
	
	private String storehouse_name; //仓库名称

	public String getFactory_parts_code() {
		return factory_parts_code;
	}


	public void setFactory_parts_code(String factory_parts_code) {
		this.factory_parts_code = factory_parts_code;
	}

	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getEntry_type() {
		return entry_type;
	}


	public void setEntry_type(int entry_type) {
		this.entry_type = entry_type;
	}


	public String getSheet_id() {
		return sheet_id;
	}


	public void setSheet_id(String sheet_id) {
		this.sheet_id = sheet_id;
	}


	public int getStorehouse_id() {
		return storehouse_id;
	}


	public void setStorehouse_id(int storehouse_id) {
		this.storehouse_id = storehouse_id;
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


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public int getState() {
		return state;
	}


	public void setState(int state) {
		this.state = state;
	}

	public Timestamp getAdd_date() {
		return add_date;
	}


	public void setAdd_date(Timestamp add_date) {
		this.add_date = add_date;
	}


	public String getStorehouse_name() {
		return storehouse_name;
	}


	public void setStorehouse_name(String storehouse_name) {
		this.storehouse_name = storehouse_name;
	}


	public String getPart_id_seq() {
		return part_id_seq;
	}


	public void setPart_id_seq(String part_id_seq) {
		this.part_id_seq = part_id_seq;
	}


	/*public float getUnit_price() {
		return unit_price;
	}


	public void setUnit_price(float unit_price) {
		this.unit_price = unit_price;
	}*/
	
	
	
	
	
	
	

}
