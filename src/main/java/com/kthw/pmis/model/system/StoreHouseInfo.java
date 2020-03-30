package com.kthw.pmis.model.system;

public class StoreHouseInfo {

	private int storehouse_id;  //仓库ID
	private String storehouse_name; //仓库名称
	private String depot_id; //仓库所属地(机辆所、车辆段)
	private int type; //仓库类别，1：入库类库、2：出库类库:、3：配送类库
	private int enable; //是否可用，1：可用，0：不可用
	
	
	public int getStorehouse_id() {
		return storehouse_id;
	}
	public void setStorehouse_id(int storehouse_id) {
		this.storehouse_id = storehouse_id;
	}
	public String getStorehouse_name() {
		return storehouse_name;
	}
	public void setStorehouse_name(String storehouse_name) {
		this.storehouse_name = storehouse_name;
	}
	public String getDepot_id() {
		return depot_id;
	}
	public void setDepot_id(String depot_id) {
		this.depot_id = depot_id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}
	
	 
}
