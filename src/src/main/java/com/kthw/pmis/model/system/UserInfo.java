package com.kthw.pmis.model.system;

//Table: user_info, Column: 6
public class UserInfo {
	String user_id; // user_id VARCHAR
	String user_name; // user_name VARCHAR
	String user_pass; // user_pass VARCHAR
	String user_roles; // user_roles VARCHAR
	java.sql.Timestamp create_time; // create_time TIMESTAMP
	int enabled; // enabled SMALLINT

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_id() {
		return this.user_id;
	}

	public void setUser_pass(String user_pass) {
		this.user_pass = user_pass;
	}

	public String getUser_pass() {
		return this.user_pass;
	}

	public void setUser_roles(String user_roles) {
		this.user_roles = user_roles;
	}

	public String getUser_roles() {
		return this.user_roles;
	}

	public void setCreate_time(java.sql.Timestamp create_time) {
		this.create_time = create_time;
	}

	public java.sql.Timestamp getCreate_time() {
		return this.create_time;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public int getEnabled() {
		return this.enabled;
	}

  public String getUser_name() {
    return user_name;
  }

  public void setUser_name(String user_name) {
    this.user_name = user_name;
  }

}