package com.kthw.pmis.model.system;

//Table: login_info, Column: 4
public class LoginInfo {
  String user_id; // user_id VARCHAR
  java.sql.Timestamp login_time; // login_time TIMESTAMP
  String user_roles; // user_roles VARCHAR
  String login_ip; // login_ip VARCHAR
  int login_status; // login_status TINYINT

  public void setUser_id(String user_id) {
    this.user_id = user_id;
  }

  public String getUser_id() {
    return this.user_id;
  }

  public void setLogin_time(java.sql.Timestamp login_time) {
    this.login_time = login_time;
  }

  public java.sql.Timestamp getLogin_time() {
    return this.login_time;
  }

  public void setUser_roles(String user_roles) {
    this.user_roles = user_roles;
  }

  public String getUser_roles() {
    return this.user_roles;
  }

  public void setLogin_ip(String login_ip) {
    this.login_ip = login_ip;
  }

  public String getLogin_ip() {
    return this.login_ip;
  }

  public int getLogin_status() {
    return login_status;
  }

  public void setLogin_status(int login_status) {
    this.login_status = login_status;
  }

}