package com.kthw.pmis.model.system;

import com.kthw.common.DateConvertor;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private static final long serialVersionUID = 8180572524645255849L;
    public static final int DOWN = 0;// 停用
    public static final int JUNIOR = 1;// 一般用户
    public static final int WORKER = 3;// 值班员
    public static final int SENIOR = 6;// 高级用户
    public static final int ADMIN = 9;// 管理员
    String user_id;
    String user_pass;
    String user_name;
    int user_role;
    String user_role_name;
    int enabled;
    int team_id;
    String team_name;
    int depot_id;
    String depot_name;//部门名称
    int department_id;
    String department_name;
    int workshop_id;
    String workshop_name;
    int work_mode;
    int part_group;

    String last_ip;
    String login_ip;
    Date last_login;
    Date reg_date;
    int login_status;
    String idx_url;//用户设定的首页
    int dispatcher;//调度台

    public int getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(int dispatcher) {
        this.dispatcher = dispatcher;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_pass() {
        return user_pass;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getUser_role() {
        return user_role;
    }

    public void setUser_role(int user_role) {
        this.user_role = user_role;
    }

	public String getTeam_name() {
		return team_name;
	}

	public void setTeam_name(String team_name) {
		this.team_name = team_name;
	}

	public int getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(int department_id) {
		this.department_id = department_id;
	}

	public String getDepartment_name() {
		return department_name;
	}

	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}

	public int getDepot_id() {
		return depot_id;
	}

	public void setDepot_id(int depot_id) {
		this.depot_id = depot_id;
	}

	public String getDepot_name() {
		return depot_name;
	}

	public void setDepot_name(String depot_name) {
		this.depot_name = depot_name;
	}

	public String getWorkshop_name() {
		return workshop_name;
	}

	public void setWorkshop_name(String workshop_name) {
		this.workshop_name = workshop_name;
	}

	public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public int getTeam_id() {
        return team_id;
    }

    public void setTeam_id(int team_id) {
        this.team_id = team_id;
    }

    public int getWorkshop_id() {
		return workshop_id;
	}

	public void setWorkshop_id(int workshop_id) {
		this.workshop_id = workshop_id;
	}

	public int getWork_mode() {
        return work_mode;
    }

    public void setWork_mode(int work_mode) {
        this.work_mode = work_mode;
    }

    public int getPart_group() {
        return part_group;
    }

    public void setPart_group(int part_group) {
        this.part_group = part_group;
    }

    public String getLast_ip() {
        return last_ip;
    }

    public void setLast_ip(String last_ip) {
        this.last_ip = last_ip;
    }

    public String getLogin_ip() {
        return login_ip;
    }

    public void setLogin_ip(String login_ip) {
        this.login_ip = login_ip;
    }

    public Date getLast_login() {
        return last_login;
    }

    public void setLast_login(Date last_login) {
        this.last_login = last_login;
    }

    public String getLast_login_display() {
        return DateConvertor.getTimeString(this.last_login);
    }

    public int getLogin_status() {
        return login_status;
    }

    public void setLogin_status(int login_status) {
        this.login_status = login_status;
    }

    public String getUser_role_name() {
        return user_role_name;
    }

    public void setUser_role_name(String user_role_name) {
        this.user_role_name = user_role_name;
    }

    public Date getReg_date() {
        return reg_date;
    }

    public void setReg_date(Date reg_date) {
        this.reg_date = reg_date;
    }

    public String getReg_date_display() {
        return DateConvertor.getTimeString(this.reg_date);
    }

    public String getIdx_url() {
        return idx_url;
    }

    public void setIdx_url(String idx_url) {
        this.idx_url = idx_url;
    }


}
