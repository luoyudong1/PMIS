package com.kthw.aiplan;

import java.util.Date;

public class DynamicVehInfo {
	String vehicle_id;
	String application_state;
	String use_unit;
	String current_unit;
	String road_traffic;
	String route;
	int average_daymile;
	int total_mile;
	int vehicle_order;
	Date start_date;
	Date arrival_date;
	Date update_time;
	int group_id;
	String train_number;
	int grouptype_id;
	int status;
	int difference_value;
	String wheel_last_fix_time;
	int wheel_last_fix_miles;

	String a1_unit;
	String a1_expire;
	int a1_distance;
	String a2_time;
	String a2_unit;
	String a2_expire;
	int a2_distance;
	String a3_time;
	String a3_unit;
	String a3_expire;
	int a3_distance;
	String a4_time;
	String a4_unit;
	String a4_expire;
	int a4_distance;
	String a5_time;
	String a5_unit;
	String a5_expire;
	int a5_distance;
	int grouped_status;
	
	Date[] ax_expire_date;
	long[] ax_expire_day;
	int[] ax_mile;
	
	int grouped_id;
	int[] grouped_id_ax_time;
	int[] grouped_id_ax_mile;
	int grouped_id_wheel_fix;
	
	
	boolean visited;
	int group_num;
	final static int MAX = 6;
	
	public DynamicVehInfo() {
		ax_expire_date = new Date[MAX];
		ax_expire_day = new long[MAX];
		grouped_id_ax_time = new int[MAX];
		grouped_id_ax_mile = new int[MAX];
		ax_mile = new int[MAX];
		visited = false;
		group_num = 1;
	}
	
	public String getVehicle_id() {
		return vehicle_id;
	}

	public void setVehicle_id(String vehicle_id) {
		this.vehicle_id = vehicle_id;
	}

	public String getApplication_state() {
		return application_state;
	}

	public void setApplication_state(String application_state) {
		this.application_state = application_state;
	}

	public String getUse_unit() {
		return use_unit;
	}

	public void setUse_unit(String use_unit) {
		this.use_unit = use_unit;
	}

	public String getCurrent_unit() {
		return current_unit;
	}

	public void setCurrent_unit(String current_unit) {
		this.current_unit = current_unit;
	}

	public String getRoad_traffic() {
		return road_traffic;
	}

	public void setRoad_traffic(String road_traffic) {
		this.road_traffic = road_traffic;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public int getGroup_id() {
		return group_id;
	}

	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}

	public String getTrain_number() {
		return train_number;
	}

	public void setTrain_number(String train_number) {
		this.train_number = train_number;
	}

	public int getGrouptype_id() {
		return grouptype_id;
	}

	public void setGrouptype_id(int grouptype_id) {
		this.grouptype_id = grouptype_id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getArrival_date() {
		return arrival_date;
	}

	public void setArrival_date(Date arrival_date) {
		this.arrival_date = arrival_date;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public String getA1_unit() {
		return a1_unit;
	}

	public void setA1_unit(String a1_unit) {
		this.a1_unit = a1_unit;
	}

	public String getA1_expire() {
		return a1_expire;
	}

	public void setA1_expire(String a1_expire) {
		this.a1_expire = a1_expire;
	}

	public int getA1_distance() {
		return a1_distance;
	}

	public void setA1_distance(int a1_distance) {
		this.a1_distance = a1_distance;
	}

	public String getA2_time() {
		return a2_time;
	}

	public void setA2_time(String a2_time) {
		this.a2_time = a2_time;
	}

	public String getA2_unit() {
		return a2_unit;
	}

	public void setA2_unit(String a2_unit) {
		this.a2_unit = a2_unit;
	}

	public String getA2_expire() {
		return a2_expire;
	}

	public void setA2_expire(String a2_expire) {
		this.a2_expire = a2_expire;
	}

	public int getA2_distance() {
		return a2_distance;
	}

	public void setA2_distance(int a2_distance) {
		this.a2_distance = a2_distance;
	}

	public String getA3_time() {
		return a3_time;
	}

	public void setA3_time(String a3_time) {
		this.a3_time = a3_time;
	}

	public String getA3_unit() {
		return a3_unit;
	}

	public void setA3_unit(String a3_unit) {
		this.a3_unit = a3_unit;
	}

	public String getA3_expire() {
		return a3_expire;
	}

	public void setA3_expire(String a3_expire) {
		this.a3_expire = a3_expire;
	}

	public int getA3_distance() {
		return a3_distance;
	}

	public void setA3_distance(int a3_distance) {
		this.a3_distance = a3_distance;
	}

	public String getA4_time() {
		return a4_time;
	}

	public void setA4_time(String a4_time) {
		this.a4_time = a4_time;
	}

	public String getA4_unit() {
		return a4_unit;
	}

	public void setA4_unit(String a4_unit) {
		this.a4_unit = a4_unit;
	}

	public String getA4_expire() {
		return a4_expire;
	}

	public void setA4_expire(String a4_expire) {
		this.a4_expire = a4_expire;
	}

	public int getA4_distance() {
		return a4_distance;
	}

	public void setA4_distance(int a4_distance) {
		this.a4_distance = a4_distance;
	}

	public String getA5_time() {
		return a5_time;
	}

	public void setA5_time(String a5_time) {
		this.a5_time = a5_time;
	}

	public String getA5_unit() {
		return a5_unit;
	}

	public void setA5_unit(String a5_unit) {
		this.a5_unit = a5_unit;
	}

	public String getA5_expire() {
		return a5_expire;
	}

	public void setA5_expire(String a5_expire) {
		this.a5_expire = a5_expire;
	}

	public int getA5_distance() {
		return a5_distance;
	}

	public void setA5_distance(int a5_distance) {
		this.a5_distance = a5_distance;
	}

	public int getAverage_daymile() {
		return average_daymile;
	}

	public void setAverage_daymile(int average_daymile) {
		this.average_daymile = average_daymile;
	}

	public int getTotal_mile() {
		return total_mile;
	}

	public void setTotal_mile(int total_mile) {
		this.total_mile = total_mile;
	}

	public int getVehicle_order() {
		return vehicle_order;
	}

	public void setVehicle_order(int vehicle_order) {
		this.vehicle_order = vehicle_order;
	}

	public Date[] getAx_expire_date() {
		return ax_expire_date;
	}

	public void setAx_expire_date(Date[] ax_expire_date) {
		this.ax_expire_date = ax_expire_date;
	}

	public long[] getAx_expire_day() {
		return ax_expire_day;
	}

	public void setAx_expire_day(long[] ax_expire_day) {
		this.ax_expire_day = ax_expire_day;
	}

	public int[] getGrouped_id_ax_time() {
		return grouped_id_ax_time;
	}

	public void setGrouped_id_ax_time(int[] grouped_id_ax_time) {
		this.grouped_id_ax_time = grouped_id_ax_time;
	}

	public int[] getGrouped_id_ax_mile() {
		return grouped_id_ax_mile;
	}

	public void setGrouped_id_ax_mile(int[] grouped_id_ax_mile) {
		this.grouped_id_ax_mile = grouped_id_ax_mile;
	}
	
	
	
	public Date getAx_expire_date(int i) {
		return ax_expire_date[i];
	}

	public void setAx_expire_date(int i, Date ax_expire_date) {
		this.ax_expire_date[i] = new Date();
		this.ax_expire_date[i] = ax_expire_date;
	}
	
	public long getAx_expire_day(int i) {
		return ax_expire_day[i];
	}

	public void setAx_expire_day(int i, long ax_expire_day) {
		this.ax_expire_day[i] = ax_expire_day;
	}

	public int getGrouped_id_ax_time(int i) {
		return grouped_id_ax_time[i];
	}

	public void setGrouped_id_ax_time(int i, int grouped_id_ax_time) {
		this.grouped_id_ax_time[i] = grouped_id_ax_time;
	}
	
	public int getGrouped_id_ax_mile(int i) {
		return grouped_id_ax_mile[i];
	}

	public void setGrouped_id_ax_mile(int i, int grouped_id_ax_mile) {
		this.grouped_id_ax_mile[i] = grouped_id_ax_mile;
	}

	public int[] getAx_mile() {
		return ax_mile;
	}

	public void setAx_mile(int[] ax_mile) {
		this.ax_mile = ax_mile;
	}
	
	public int getAx_mile(int i) {
		return ax_mile[i];
	}

	public void setAx_mile(int i, int ax_mile) {
		this.ax_mile[i] = ax_mile;
	}

	public int getDifference_value() {
		return difference_value;
	}

	public void setDifference_value(int difference_value) {
		this.difference_value = difference_value;
	}

	public String getWheel_last_fix_time() {
		return wheel_last_fix_time;
	}

	public void setWheel_last_fix_time(String wheel_last_fix_time) {
		this.wheel_last_fix_time = wheel_last_fix_time;
	}

	public int getWheel_last_fix_miles() {
		return wheel_last_fix_miles;
	}

	public void setWheel_last_fix_miles(int wheel_last_fix_miles) {
		this.wheel_last_fix_miles = wheel_last_fix_miles;
	}

	public int getGrouped_id_wheel_fix() {
		return grouped_id_wheel_fix;
	}

	public void setGrouped_id_wheel_fix(int grouped_id_wheel_fix) {
		this.grouped_id_wheel_fix = grouped_id_wheel_fix;
	}

	public int getGrouped_id() {
		return grouped_id;
	}

	public void setGrouped_id(int grouped_id) {
		this.grouped_id = grouped_id;
	}

	public int getGrouped_status() {
		return grouped_status;
	}

	public void setGrouped_status(int grouped_status) {
		this.grouped_status = grouped_status;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public int getGroup_num() {
		return group_num;
	}

	public void setGroup_num(int group_num) {
		this.group_num = group_num;
	}
}
