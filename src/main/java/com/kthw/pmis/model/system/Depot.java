package com.kthw.pmis.model.system;

/**
 * 车辆段
 */
public class Depot {

	private int depot_id;
	private String depot_name;
	private int enabled;
	
	public int getDepot_id() {
		return depot_id;
	}
	public void setDepot_id(int depot_id) {
		this.depot_id = depot_id;
	}
	public String getDepot_name() {
		return depot_name;
	}
	public void setDepot_name(String depot_name) {
		this.depot_name = depot_name;
	}
	public int getEnabled() {
		return enabled;
	}
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
}
