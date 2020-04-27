package com.kthw.pmis.model.system;

import java.io.Serializable;

public class ViewLogStat implements Serializable {

  String user_id;
  int log_times;
  String stat_date;
public String getUser_id() {
	return user_id;
}
public void setUser_id(String user_id) {
	this.user_id = user_id;
}
public int getLog_times() {
	return log_times;
}
public void setLog_times(int log_times) {
	this.log_times = log_times;
}
public String getStat_date() {
	return stat_date;
}
public void setStat_date(String stat_date) {
	this.stat_date = stat_date;
}
 

}
