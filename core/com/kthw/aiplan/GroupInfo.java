package com.kthw.aiplan;

import java.util.ArrayList;
import java.util.List;

public class GroupInfo {
	
	private static final long serialVersionUID = 198732229461760770L;
	
	private int grouped_id_a2_time;
	private int grouped_id_a3_time;
	private int grouped_id_a2_mile;
	private int grouped_id_a3_mile;
	private int grouped_id_wheel_fix;
	private int grouped_num;
	private String vehicle_ids_str;
	private List<String> vehicle_ids_list;
	
	public GroupInfo() {
		vehicle_ids_list = new ArrayList<String>();
	}
	
	public int getGrouped_id_a2_time() {
		return grouped_id_a2_time;
	}
	public void setGrouped_id_a2_time(int grouped_id_a2_time) {
		this.grouped_id_a2_time = grouped_id_a2_time;
	}
	public int getGrouped_id_a3_time() {
		return grouped_id_a3_time;
	}
	public void setGrouped_id_a3_time(int grouped_id_a3_time) {
		this.grouped_id_a3_time = grouped_id_a3_time;
	}
	public int getGrouped_id_a2_mile() {
		return grouped_id_a2_mile;
	}
	public void setGrouped_id_a2_mile(int grouped_id_a2_mile) {
		this.grouped_id_a2_mile = grouped_id_a2_mile;
	}
	public int getGrouped_id_a3_mile() {
		return grouped_id_a3_mile;
	}
	public void setGrouped_id_a3_mile(int grouped_id_a3_mile) {
		this.grouped_id_a3_mile = grouped_id_a3_mile;
	}
	public int getGrouped_id_wheel_fix() {
		return grouped_id_wheel_fix;
	}
	public void setGrouped_id_wheel_fix(int grouped_id_wheel_fix) {
		this.grouped_id_wheel_fix = grouped_id_wheel_fix;
	}
	public int getGrouped_num() {
		return grouped_num;
	}
	public void setGrouped_num(int grouped_num) {
		this.grouped_num = grouped_num;
	}
	public String getVehicle_ids_str() {
		return vehicle_ids_str;
	}
	public void setVehicle_ids_str(String vehicle_ids_str) {
		this.vehicle_ids_str = vehicle_ids_str;
	}
	public List<String> getVehicle_ids_list() {
		return vehicle_ids_list;
	}
	public void setVehicle_ids_list(List<String> vehicle_ids_list) {
		this.vehicle_ids_list = vehicle_ids_list;
	}
}
