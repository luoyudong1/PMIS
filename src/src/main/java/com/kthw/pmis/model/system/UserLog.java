package com.kthw.pmis.model.system;

import java.io.Serializable;
import java.util.Date;

public class UserLog implements Serializable {

	private static final long serialVersionUID = 8180572524645255849L;

	private int id;

	private String user_id;

	private String login_ip;

	private Date login_date;

	public UserLog() {}

	public UserLog(String user_id, String login_ip, Date login_date) {
		this.user_id = user_id;
		this.login_ip = login_ip;
		this.login_date = login_date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getLogin_ip() {
		return login_ip;
	}

	public void setLogin_ip(String login_ip) {
		this.login_ip = login_ip;
	}

	public Date getLogin_date() {
		return login_date;
	}

	public void setLogin_date(Date login_date) {
		this.login_date = login_date;
	}
}
