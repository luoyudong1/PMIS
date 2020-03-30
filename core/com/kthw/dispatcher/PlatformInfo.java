package com.kthw.dispatcher;

import java.util.Date;

public class PlatformInfo {
	int track_id;
	int platform_id;
	String vehicle_id;
	String vehicle_type;
	String remarks;
	int target_garage;
	int target_track;
	int target_platform;
	int platform_type;
	Date update_time;
	int node_id;
	
	public int getTrack_id() {
		return track_id;
	}
	public void setTrack_id(int track_id) {
		this.track_id = track_id;
	}
	public int getPlatform_id() {
		return platform_id;
	}
	public void setPlatform_id(int platform_id) {
		this.platform_id = platform_id;
	}
	public String getVehicle_id() {
		return vehicle_id;
	}
	public void setVehicle_id(String vehicle_id) {
		this.vehicle_id = vehicle_id;
	}
	public String getVehicle_type() {
		return vehicle_type;
	}
	public void setVehicle_type(String vehicle_type) {
		this.vehicle_type = vehicle_type;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public int getTarget_garage() {
		return target_garage;
	}
	public void setTarget_garage(int target_garage) {
		this.target_garage = target_garage;
	}
	public int getTarget_track() {
		return target_track;
	}
	public void setTarget_track(int target_track) {
		this.target_track = target_track;
	}
	public int getTarget_platform() {
		return target_platform;
	}
	public void setTarget_platform(int target_platform) {
		this.target_platform = target_platform;
	}
	public int getPlatform_type() {
		return platform_type;
	}
	public void setPlatform_type(int platform_type) {
		this.platform_type = platform_type;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	public int getNode_id() {
		return node_id;
	}
	public void setNode_id(int node_id) {
		this.node_id = node_id;
	}
	
}
