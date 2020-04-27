package com.kthw.pmis.model.system;

import java.io.Serializable;

public class AuthFunction implements Serializable {

	private int func_id;
	
	private int role_id;	
	
	private String opt_type;

	private String operation;

	public int getFunc_id() {
		return func_id;
	}

	public void setFunc_id(int func_id) {
		this.func_id = func_id;
	}

	public int getRole_id() {
		return role_id;
	}

	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}

	public String getOpt_type() {
		return opt_type;
	}

	public void setOpt_type(String opt_type) {
		this.opt_type = opt_type;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

  	
}
