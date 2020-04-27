package com.kthw.pmis.model.system;

/**
 * 班组
 */
public class Team {
    private int workshop_id;
    private int team_id;
    private String team_name;
    private String flag;
	
	public int getWorkshop_id() {
		return workshop_id;
	}
	public void setWorkshop_id(int workshop_id) {
		this.workshop_id = workshop_id;
	}
	public int getTeam_id() {
		return team_id;
	}
	public void setTeam_id(int team_id) {
		this.team_id = team_id;
	}
	public String getTeam_name() {
		return team_name;
	}
	public void setTeam_name(String team_name) {
		this.team_name = team_name;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
    
    
}
