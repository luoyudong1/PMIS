package com.kthw.pmis.model.system;

/**
 *车间
 */
public class Workshop {

	private int workshop_id;
	private String workshop_name;
	private int enabled;
	private int depot_id;
	
	public int getWorkshop_id() {
		return workshop_id;
	}
	public void setWorkshop_id(int workshop_id) {
		this.workshop_id = workshop_id;
	}
	public String getWorkshop_name() {
		return workshop_name;
	}
	public void setWorkshop_name(String workshop_name) {
		this.workshop_name = workshop_name;
	}
	public int getEnabled() {
		return enabled;
	}
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	public int getDepot_id() {
		return depot_id;
	}
	public void setDepot_id(int depot_id) {
		this.depot_id = depot_id;
	}
	
}
