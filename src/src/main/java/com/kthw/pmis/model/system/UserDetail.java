package com.kthw.pmis.model.system;

//Table: user_detail, Column: 18
public class UserDetail {
	String user_id; // user_id VARCHAR
	int enabled; // enabled BIT
	String real_name; // real_name VARCHAR
	String nick_name; // nick_name VARCHAR
	String user_identity; // user_identity VARCHAR
	int marital_status; // marital_status TINYINT
	java.sql.Timestamp birthday; // birthday DATE
	String title; // title VARCHAR
	String dept_id; // dept_id VARCHAR
	String dept_subid; // dept_subid VARCHAR
	int sex; // sex TINYINT
	String phone; // phone VARCHAR
	String address; // address VARCHAR
	String email1; // email1 VARCHAR
	String email2; // email2 VARCHAR
	String user_desc; // user_desc VARCHAR
	String modify_user; // modify_user VARCHAR
	java.sql.Timestamp modify_time; // modify_time TIMESTAMP

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_id() {
		return this.user_id;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public int getEnabled() {
		return this.enabled;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getReal_name() {
		return this.real_name;
	}

	public void setUser_identity(String user_identity) {
		this.user_identity = user_identity;
	}

	public String getUser_identity() {
		return this.user_identity;
	}

	public void setMarital_status(int marital_status) {
		this.marital_status = marital_status;
	}

	public int getMarital_status() {
		return this.marital_status;
	}

	public void setBirthday(java.sql.Timestamp birthday) {
		this.birthday = birthday;
	}

	public java.sql.Timestamp getBirthday() {
		return this.birthday;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setDept_id(String dept_id) {
		this.dept_id = dept_id;
	}

	public String getDept_id() {
		return this.dept_id;
	}

	public void setDept_subid(String dept_subid) {
		this.dept_subid = dept_subid;
	}

	public String getDept_subid() {
		return this.dept_subid;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getSex() {
		return this.sex;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return this.address;
	}

	public void setEmail1(String email1) {
		this.email1 = email1;
	}

	public String getEmail1() {
		return this.email1;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public String getEmail2() {
		return this.email2;
	}

	public void setUser_desc(String user_desc) {
		this.user_desc = user_desc;
	}

	public String getUser_desc() {
		return this.user_desc;
	}

	public void setModify_user(String modify_user) {
		this.modify_user = modify_user;
	}

	public String getModify_user() {
		return this.modify_user;
	}

	public void setModify_time(java.sql.Timestamp modify_time) {
		this.modify_time = modify_time;
	}

	public java.sql.Timestamp getModify_time() {
		return this.modify_time;
	}

  public String getNick_name() {
    return nick_name;
  }

  public void setNick_name(String nick_name) {
    this.nick_name = nick_name;
  }

}