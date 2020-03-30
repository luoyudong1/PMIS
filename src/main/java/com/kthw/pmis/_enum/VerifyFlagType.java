package com.kthw.pmis._enum;

public enum VerifyFlagType {
	NOVERIFY(0, "未审核"),
	VERIFYING(1, "审核中"),
	VERIFIED(2, "已审核"),
    ;
	private int id;
    private String name;
    
    private VerifyFlagType(int id, String name) {
		this.id = id;
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
