package com.kthw.pmis.model.system;

import java.io.Serializable;

public class StationPos implements Serializable {
	/**
	 * 探测站
	 */
	private static final long serialVersionUID = 6411722669823426317L;
	String i_thds_id;	
	String s_thds_name;
	String i_line;	
	String i_kelometer;
	String s_kelometer;
	String i_bureau;
	
	String I_LINE_OF_BUREAU ;
	String F_GPS_LON ;
	String F_GPS_LAT;
	String I_THDS_COME;
	String I_THDS_LEAVE;
	String I_STATION;
	String I_DEPARTMENT;
	String I_PRODUCER;
	String I_DETECTOR_POS;
	String I_DIRECTION;
	String I_SECTION;
	public String getI_LINE_OF_BUREAU() {
		return I_LINE_OF_BUREAU;
	}
	public void setI_LINE_OF_BUREAU(String i_LINE_OF_BUREAU) {
		I_LINE_OF_BUREAU = i_LINE_OF_BUREAU;
	}
	public String getF_GPS_LON() {
		return F_GPS_LON;
	}
	public void setF_GPS_LON(String f_GPS_LON) {
		F_GPS_LON = f_GPS_LON;
	}
	public String getF_GPS_LAT() {
		return F_GPS_LAT;
	}
	public void setF_GPS_LAT(String f_GPS_LAT) {
		F_GPS_LAT = f_GPS_LAT;
	}
	public String getI_THDS_COME() {
		return I_THDS_COME;
	}
	public void setI_THDS_COME(String i_THDS_COME) {
		I_THDS_COME = i_THDS_COME;
	}
	public String getI_THDS_LEAVE() {
		return I_THDS_LEAVE;
	}
	public void setI_THDS_LEAVE(String i_THDS_LEAVE) {
		I_THDS_LEAVE = i_THDS_LEAVE;
	}
	public String getI_STATION() {
		return I_STATION;
	}
	public void setI_STATION(String i_STATION) {
		I_STATION = i_STATION;
	}
	public String getI_DEPARTMENT() {
		return I_DEPARTMENT;
	}
	public void setI_DEPARTMENT(String i_DEPARTMENT) {
		I_DEPARTMENT = i_DEPARTMENT;
	}
	public String getI_PRODUCER() {
		return I_PRODUCER;
	}
	public void setI_PRODUCER(String i_PRODUCER) {
		I_PRODUCER = i_PRODUCER;
	}
	public String getI_DETECTOR_POS() {
		return I_DETECTOR_POS;
	}
	public void setI_DETECTOR_POS(String i_DETECTOR_POS) {
		I_DETECTOR_POS = i_DETECTOR_POS;
	}
	public String getI_DIRECTION() {
		return I_DIRECTION;
	}
	public void setI_DIRECTION(String i_DIRECTION) {
		I_DIRECTION = i_DIRECTION;
	}
	public String getI_SECTION() {
		return I_SECTION;
	}
	public void setI_SECTION(String i_SECTION) {
		I_SECTION = i_SECTION;
	}
	
	public String getI_thds_id() {
		return i_thds_id;
	}
	public void setI_thds_id(String i_thds_id) {
		this.i_thds_id = i_thds_id;
	}
	public String getS_thds_name() {
		return s_thds_name;
	}
	public void setS_thds_name(String s_thds_name) {
		this.s_thds_name = s_thds_name;
	}
	public String getI_line() {
		return i_line;
	}
	public void setI_line(String i_line) {
		this.i_line = i_line;
	}
	public String getI_kelometer() {
		return i_kelometer;
	}
	public void setI_kelometer(String i_kelometer) {
		this.i_kelometer = i_kelometer;
	}
	public String getS_kelometer() {
		return s_kelometer;
	}
	public void setS_kelometer(String s_kelometer) {
		this.s_kelometer = s_kelometer;
	}
	public String getI_bureau() {
		return i_bureau;
	}
	public void setI_bureau(String i_bureau) {
		this.i_bureau = i_bureau;
	}
	

	
}
