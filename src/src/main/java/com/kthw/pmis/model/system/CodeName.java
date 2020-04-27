package com.kthw.pmis.model.system;

import java.io.Serializable;

public class CodeName implements Serializable {

	private static final long serialVersionUID = 123123232423L;
	int code;
	String name;
	
	double param1;//
	int param2;
	int param3;
	double param4;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getParam1() {
		return param1;
	}
	public void setParam1(double param1) {
		this.param1 = param1;
	}
	public int getParam2() {
		return param2;
	}
	public void setParam2(int param2) {
		this.param2 = param2;
	}
	public int getParam3() {
		return param3;
	}
	public void setParam3(int param3) {
		this.param3 = param3;
	}
	public double getParam4() {
		return param4;
	}
	public void setParam4(double param4) {
		this.param4 = param4;
	}


}
