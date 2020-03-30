package com.kthw.aiplan;

public class GroupType {
	
	private static final long serialVersionUID = 9107229461760770L;
	
	private int type_id;
	private String type_name;
	private String type_desc;
	private String veh_type;
	private String veh_class;
	private String main_line_volt;
	private String brake_form;
	private String wind_supply_form;
	private String mutual_power;
	
	public int getType_id() {
		return type_id;
	}
	public void setType_id(int type_id) {
		this.type_id = type_id;
	}
	public String getVeh_type() {
		return veh_type;
	}
	public void setVeh_type(String veh_type) {
		this.veh_type = veh_type;
	}
	public String getVeh_class() {
		return veh_class;
	}
	public void setVeh_class(String veh_class) {
		this.veh_class = veh_class;
	}
	public String getMain_line_volt() {
		return main_line_volt;
	}
	public void setMain_line_volt(String main_line_volt) {
		this.main_line_volt = main_line_volt;
	}
	public String getBrake_form() {
		return brake_form;
	}
	public void setBrake_form(String brake_form) {
		this.brake_form = brake_form;
	}
	public String getWind_supply_form() {
		return wind_supply_form;
	}
	public void setWind_supply_form(String wind_supply_form) {
		this.wind_supply_form = wind_supply_form;
	}
	public String getMutual_power() {
		return mutual_power;
	}
	public void setMutual_power(String mutual_power) {
		this.mutual_power = mutual_power;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public String getType_desc() {
		return type_desc;
	}
	public void setType_desc(String type_desc) {
		this.type_desc = type_desc;
	}
	
	

}
