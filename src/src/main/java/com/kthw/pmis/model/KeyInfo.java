package com.kthw.pmis.model;

public class KeyInfo {
    private String name;
    private int value;
    private String vehClass;
    private String vehStatus;
    
    private int vehSize;
    private int groupSize;
    
    private int id;
    private String info;
    
    
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getVehSize() {
		return vehSize;
	}
	public void setVehSize(int vehSize) {
		this.vehSize = vehSize;
	}
	public int getGroupSize() {
		return groupSize;
	}
	public void setGroupSize(int groupSize) {
		this.groupSize = groupSize;
	}
	public String getVehStatus() {
		return vehStatus;
	}
	public void setVehStatus(String vehStatus) {
		this.vehStatus = vehStatus;
	}
	public String getVehClass() {
		return vehClass;
	}
	public void setVehClass(String vehClass) {
		this.vehClass = vehClass;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
    ;
}
