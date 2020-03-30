package com.kthw.pmis.model.system;

import java.io.Serializable;

public class Permission implements Serializable {

	private int func_id;

	private int role_id;

	private String perm_name;

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

	public String getPerm_name() {
		return perm_name;
	}

	public void setPerm_name(String perm_name) {
		this.perm_name = perm_name;
	}
}
