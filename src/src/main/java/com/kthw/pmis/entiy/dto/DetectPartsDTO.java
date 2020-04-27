package com.kthw.pmis.entiy.dto;

import java.math.BigDecimal;

import com.kthw.pmis.entiy.DetectParts;

public class DetectPartsDTO extends DetectParts{
	private String detectDeviceName;
	private String devicePartsName;
	private String deviceModelName;
	private String deviceTypeName;
	private String supplierName;
	private String factoryPartsCode;
	private BigDecimal unitPrice;
	private BigDecimal purchasePrice;

	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getDetectDeviceName() {
		return detectDeviceName;
	}
	public void setDetectDeviceName(String detectDeviceName) {
		this.detectDeviceName = detectDeviceName;
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
	public String getFactoryPartsCode() {
		return factoryPartsCode;
	}
	public void setFactoryPartsCode(String factoryPartsCode) {
		this.factoryPartsCode = factoryPartsCode;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

}
