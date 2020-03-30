package com.kthw.pmis.model.system;

import java.io.Serializable;

public class CheLiangDuan implements Serializable {


	/**
	 *车辆段
	 */
	private static final long serialVersionUID = 4696612296592393055L;
	String cld_bh;	
	String fj_bh;
	String cld_qc;	
	String cld_jc;
	String enabled;
	public String getCld_bh() {
		return cld_bh;
	}
	public void setCld_bh(String cld_bh) {
		this.cld_bh = cld_bh;
	}
	public String getFj_bh() {
		return fj_bh;
	}
	public void setFj_bh(String fj_bh) {
		this.fj_bh = fj_bh;
	}
	public String getCld_qc() {
		return cld_qc;
	}
	public void setCld_qc(String cld_qc) {
		this.cld_qc = cld_qc;
	}
	public String getCld_jc() {
		return cld_jc;
	}
	public void setCld_jc(String cld_jc) {
		this.cld_jc = cld_jc;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}



}
