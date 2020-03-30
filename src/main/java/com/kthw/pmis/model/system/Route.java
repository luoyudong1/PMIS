package com.kthw.pmis.model.system;

import java.io.Serializable;

public class Route implements Serializable {


	/**
	 * 线路
	 */
	private static final long serialVersionUID = 8010899730645721768L;
	String pk_id;	
	String xl_bh;
	String xl_mc;	
	String xl_qzzm;
	String xl_yylc;
	String lj_bh;
	String i_line;
	String xl_py;

	public String getXl_py() {
		return xl_py;
	}
	public void setXl_py(String xl_py) {
		this.xl_py = xl_py;
	}
	public String getI_line() {
		return i_line;
	}
	public void setI_line(String i_line) {
		this.i_line = i_line;
	}
	public String getPk_id() {
		return pk_id;
	}
	public void setPk_id(String pk_id) {
		this.pk_id = pk_id;
	}
	public String getXl_bh() {
		return xl_bh;
	}
	public void setXl_bh(String xl_bh) {
		this.xl_bh = xl_bh;
	}
	public String getXl_mc() {
		return xl_mc;
	}
	public void setXl_mc(String xl_mc) {
		this.xl_mc = xl_mc;
	}
	public String getXl_qzzm() {
		return xl_qzzm;
	}
	public void setXl_qzzm(String xl_qzzm) {
		this.xl_qzzm = xl_qzzm;
	}
	public String getXl_yylc() {
		return xl_yylc;
	}
	public void setXl_yylc(String xl_yylc) {
		this.xl_yylc = xl_yylc;
	}
	public String getLj_bh() {
		return lj_bh;
	}
	public void setLj_bh(String lj_bh) {
		this.lj_bh = lj_bh;
	}
	

}
