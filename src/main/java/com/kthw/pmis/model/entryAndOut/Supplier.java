package com.kthw.pmis.model.entryAndOut;

/**
 * 描述:供货商信息.
 */
public class Supplier {

	private int supplier_id;//厂家ID
	private String supplier_name;//厂家名称
	private String supplier_code;//厂家编码
	public String getSupplier_code() {
		return supplier_code;
	}
	public void setSupplier_code(String supplier_code) {
		this.supplier_code = supplier_code;
	}
	private int enabled;//是否可用
	public int getSupplier_id() {
		return supplier_id;
	}
	public void setSupplier_id(int supplier_id) {
		this.supplier_id = supplier_id;
	}
	public String getSupplier_name() {
		return supplier_name;
	}
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}
	public int getEnabled() {
		return enabled;
	}
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
}
