package com.kthw.pmis.model.system;

import java.io.Serializable;

public class LuJu implements Serializable {


	/**
	 * 路局
	 */
	private static final long serialVersionUID = 4568577029703917786L;
	String lj_bh;
    String lj_qc; 
    String lj_jc;
    String lj_ip; 
    String lj_seq;
  
    public String getLj_bh() {
		return lj_bh;
	}
	public void setLj_bh(String lj_bh) {
		this.lj_bh = lj_bh;
	}
	public String getLj_qc() {
		return lj_qc;
	}
	public void setLj_qc(String lj_qc) {
		this.lj_qc = lj_qc;
	}
	public String getLj_jc() {
		return lj_jc;
	}
	public void setLj_jc(String lj_jc) {
		this.lj_jc = lj_jc;
	}
	public String getLj_ip() {
		return lj_ip;
	}
	public void setLj_ip(String lj_ip) {
		this.lj_ip = lj_ip;
	}
	public String getLj_seq() {
		return lj_seq;
	}
	public void setLj_seq(String lj_seq) {
		this.lj_seq = lj_seq;
	}
	


}
