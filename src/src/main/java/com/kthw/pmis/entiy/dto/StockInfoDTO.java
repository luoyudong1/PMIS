package com.kthw.pmis.entiy.dto;

import com.kthw.pmis.entiy.StockInfo1;

public class StockInfoDTO extends StockInfo1{
	private String devicePartsName;
	private String deviceModelName;
	private String deviceTypeName;
	private String supplierName;
	private String assetAttributesName;
	private String storehouseName;
	private Short sendVerifyFlag;
	private Short receiptVerifyFlag;
	private Integer unitPrice;
	private String detectDeviceName;
	private Long depotId;

	public Long getDepotId() {
		return depotId;
	}

	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}

	public String getDetectDeviceName() {
		return detectDeviceName;
	}
	public void setDetectDeviceName(String detectDeviceName) {
		this.detectDeviceName = detectDeviceName;
	}
	public String getStorehouseName() {
		return storehouseName;
	}
	public void setStorehouseName(String storehouseName) {
		this.storehouseName = storehouseName;
	}
	public Integer getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Integer unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Short getSendVerifyFlag() {
		return sendVerifyFlag;
	}
	public void setSendVerifyFlag(Short sendVerifyFlag) {
		this.sendVerifyFlag = sendVerifyFlag;
	}
	public Short getReceiptVerifyFlag() {
		return receiptVerifyFlag;
	}
	public void setReceiptVerifyFlag(Short receiptVerifyFlag) {
		this.receiptVerifyFlag = receiptVerifyFlag;
	}
	public String getAssetAttributesName() {
		return assetAttributesName;
	}
	public void setAssetAttributesName(String assetAttributesName) {
		this.assetAttributesName = assetAttributesName;
	}
	public String getDevicePartsName() {
		return devicePartsName;
	}
	public void setDevicePartsName(String devicePartsName) {
		this.devicePartsName = devicePartsName;
	}
	public String getDeviceModelName() {
		return deviceModelName;
	}
	public void setDeviceModelName(String deviceModelName) {
		this.deviceModelName = deviceModelName;
	}
	public String getDeviceTypeName() {
		return deviceTypeName;
	}
	public void setDeviceTypeName(String deviceTypeName) {
		this.deviceTypeName = deviceTypeName;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
}
