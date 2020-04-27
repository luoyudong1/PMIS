package com.kthw.pmis.model.system;

import java.io.Serializable;

public class Function implements Serializable {

	private int id;
	
	private int role_id;	

	private String perm_name;

  	private String func_name;

    private String func_url;

    private Integer parent_id;

    private int func_level;

    private String func_desc;

    private int show_order;

    private int enable;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    
    public String getFunc_name() {
        return func_name;
    }

    public void setFunc_name(String func_name) {
        this.func_name = func_name;
    }

    public String getFunc_url() {
        return func_url;
    }

    public void setFunc_url(String func_url) {
        this.func_url = func_url;
    }

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public int getFunc_level() {
        return func_level;
    }

    public void setFunc_level(int func_level) {
        this.func_level = func_level;
    }

    public String getFunc_desc() {
        return func_desc;
    }

    public void setFunc_desc(String func_desc) {
        this.func_desc = func_desc;
    }

    public int getShow_order() {
        return show_order;
    }

    public void setShow_order(int show_order) {
        this.show_order = show_order;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }
}
