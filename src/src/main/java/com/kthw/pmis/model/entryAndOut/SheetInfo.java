package com.kthw.pmis.model.entryAndOut;

import java.sql.Timestamp;

/**
 * 描述:出入库单据信息.
 */
public class SheetInfo {

	private String sheet_id;//单据唯一编码
	private int sheet_type;//单据类型(1：采购入库、2：生产入库、3：返修入库、4：返修出库、5：报废出库)
	private String supplier_name;//供货商名称
	private int send_verify_flag;//发送方审核标志，0：待审核、1：审核中、2:已审核
	private Timestamp  sheet_date;//出入库日期
	private int source_store_house_id;//源的仓库ID
	private String send_operator_id;//发送方经办人ID
	private String send_operator_name;//发送方经办人名称
	private String send_remark;//发送方备注
	private String send_verify_id;//发送方审核人ID
	private String send_verify_name;//发送方审核人名称
	private Timestamp send_verify_date;//发送方审核时间
	private Timestamp add_date;//添加日期
	private int source;//1：大修、2：更新、3：项修:、4：新线建设
	private int object_store_house_id;//目的仓库ID
	private String source_storehouse_name;//目的仓库名称
	private String receipt_operator_id;//接收方经办人ID
	private String receipt_operator_name;//接收方经办人名称
	private String receipt_remark;//接收方备注
	private String receipt_verify_id;//接收方审核人ID
	private String receipt_verify_name;//接收方审核人名称
	private Timestamp receipt_verify_date;//接收方审核日期
	private int receipt_verify_flag;//接收方方审核标志，0：待审核、1：审核中、2:已审核
	private int state;//单据状态：0：发送方待审核；1.发送方审核中；2：发送方已审核；3：配送中；4：接收方待审核；5.接收方审核中；6：接收方已审核；7：待返厂；8：已返厂；
	
	private String storehouse_name; //仓库名称
	
	private String tracking_number; //物流单号
	
	public String getTracking_number() {
		return tracking_number;
	}
	public void setTracking_number(String tracking_number) {
		this.tracking_number = tracking_number;
	}
	public String getSource_storehouse_name() {
		return source_storehouse_name;
	}
	public void setObject_storehouse_name(String source_storehouse_name) {
		this.source_storehouse_name = source_storehouse_name;
	}
	public String getSheet_id() {
		return sheet_id;
	}
	public void setSheet_id(String sheet_id) {
		this.sheet_id = sheet_id;
	}
	public int getSheet_type() {
		return sheet_type;
	}
	public void setSheet_type(int sheet_type) {
		this.sheet_type = sheet_type;
	}
	public String getSupplier_name() {
		return supplier_name;
	}
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}
	public int getSend_verify_flag() {
		return send_verify_flag;
	}
	public void setSend_verify_flag(int send_verify_flag) {
		this.send_verify_flag = send_verify_flag;
	}
	public Timestamp getSheet_date() {
		return sheet_date;
	}
	public void setSheet_date(Timestamp sheet_date) {
		this.sheet_date = sheet_date;
	}
	public int getSource_store_house_id() {
		return source_store_house_id;
	}
	public void setSource_store_house_id(int source_store_house_id) {
		this.source_store_house_id = source_store_house_id;
	}
	public String getSend_operator_id() {
		return send_operator_id;
	}
	public void setSend_operator_id(String send_operator_id) {
		this.send_operator_id = send_operator_id;
	}
	public String getSend_operator_name() {
		return send_operator_name;
	}
	public void setSend_operator_name(String send_operator_name) {
		this.send_operator_name = send_operator_name;
	}
	public String getSend_remark() {
		return send_remark;
	}
	public void setSend_remark(String send_remark) {
		this.send_remark = send_remark;
	}
	public String getSend_verify_id() {
		return send_verify_id;
	}
	public void setSend_verify_id(String send_verify_id) {
		this.send_verify_id = send_verify_id;
	}
	public String getSend_verify_name() {
		return send_verify_name;
	}
	public void setSend_verify_name(String send_verify_name) {
		this.send_verify_name = send_verify_name;
	}
	public Timestamp getSend_verify_date() {
		return send_verify_date;
	}
	public void setSend_verify_date(Timestamp send_verify_date) {
		this.send_verify_date = send_verify_date;
	}
	public Timestamp getAdd_date() {
		return add_date;
	}
	public void setAdd_date(Timestamp add_date) {
		this.add_date = add_date;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public int getObject_store_house_id() {
		return object_store_house_id;
	}
	public void setObject_store_house_id(int object_store_house_id) {
		this.object_store_house_id = object_store_house_id;
	}
	public String getReceipt_operator_id() {
		return receipt_operator_id;
	}
	public void setReceipt_operator_id(String receipt_operator_id) {
		this.receipt_operator_id = receipt_operator_id;
	}
	public String getReceipt_operator_name() {
		return receipt_operator_name;
	}
	public void setReceipt_operator_name(String receipt_operator_name) {
		this.receipt_operator_name = receipt_operator_name;
	}
	public String getReceipt_remark() {
		return receipt_remark;
	}
	public void setReceipt_remark(String receipt_remark) {
		this.receipt_remark = receipt_remark;
	}
	public String getReceipt_verify_id() {
		return receipt_verify_id;
	}
	public void setReceipt_verify_id(String receipt_verify_id) {
		this.receipt_verify_id = receipt_verify_id;
	}
	public String getReceipt_verify_name() {
		return receipt_verify_name;
	}
	public void setReceipt_verify_name(String receipt_verify_name) {
		this.receipt_verify_name = receipt_verify_name;
	}
	public Timestamp getReceipt_verify_date() {
		return receipt_verify_date;
	}
	public void setReceipt_verify_date(Timestamp receipt_verify_date) {
		this.receipt_verify_date = receipt_verify_date;
	}
	public int getReceipt_verify_flag() {
		return receipt_verify_flag;
	}
	public void setReceipt_verify_flag(int receipt_verify_flag) {
		this.receipt_verify_flag = receipt_verify_flag;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getStorehouse_name() {
		return storehouse_name;
	}
	public void setStorehouse_name(String storehouse_name) {
		this.storehouse_name = storehouse_name;
	}
	
	
}
