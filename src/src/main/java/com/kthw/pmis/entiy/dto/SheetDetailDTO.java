package com.kthw.pmis.entiy.dto;

import com.kthw.pmis.entiy.SheetDetail;

import java.math.BigDecimal;
import java.util.Date;

public class SheetDetailDTO extends SheetDetail{
	private String devicePartsName;
	private String deviceModelName;
	private String deviceTypeName;
	private String supplierName;
	private String sourcePartCode;
	private Short sendVerifyFlag;
	private Short receiptVerifyFlag;
	private Short assetAttrId;
	private Short partsState;
	private String sourceStoreHouseName;
	private String objectStoreHouseName;
	private Integer detectDeviceId;
	private String detectDeviceName;
	private String  assetAttrName;
	private String  sendOperatorName;//操作人
	private String  sendVerifyName;//操作审核人
	private String  receiptOperatorName;//接收人
	private String  receiptVerifyName;//接受审核人
	private BigDecimal purchasePrice;//新购价格
	private Date addDate;//添加时间
	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}


	public String getSendOperatorName() {
		return sendOperatorName;
	}

	public void setSendOperatorName(String sendOperatorName) {
		this.sendOperatorName = sendOperatorName;
	}

	public String getSendVerifyName() {
		return sendVerifyName;
	}

	public void setSendVerifyName(String sendVerifyName) {
		this.sendVerifyName = sendVerifyName;
	}

	public String getReceiptOperatorName() {
		return receiptOperatorName;
	}

	public void setReceiptOperatorName(String receiptOperatorName) {
		this.receiptOperatorName = receiptOperatorName;
	}

	public String getReceiptVerifyName() {
		return receiptVerifyName;
	}

	public void setReceiptVerifyName(String receiptVerifyName) {
		this.receiptVerifyName = receiptVerifyName;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public String getAssetAttrName() {
		return assetAttrName;
	}

	public void setAssetAttrName(String assetAttrName) {
		this.assetAttrName = assetAttrName;
	}

	public Integer getDetectDeviceId() {
		return detectDeviceId;
	}

	public void setDetectDeviceId(Integer detectDeviceId) {
		this.detectDeviceId = detectDeviceId;
	}

	public String getDetectDeviceName() {
		return detectDeviceName;
	}

	public void setDetectDeviceName(String detectDeviceName) {
		this.detectDeviceName = detectDeviceName;
	}

	public String getSourceStoreHouseName() {
		return sourceStoreHouseName;
	}
	public void setSourceStoreHouseName(String sourceStoreHouseName) {
		this.sourceStoreHouseName = sourceStoreHouseName;
	}
	public String getObjectStoreHouseName() {
		return objectStoreHouseName;
	}
	public void setObjectStoreHouseName(String objectStoreHouseName) {
		this.objectStoreHouseName = objectStoreHouseName;
	}
	public String getSourcePartCode() {
		return sourcePartCode;
	}
	public void setSourcePartCode(String sourcePartCode) {
		this.sourcePartCode = sourcePartCode;
	}
	public Short getAssetAttrId() {
		return assetAttrId;
	}
	public void setAssetAttrId(Short assetAttrId) {
		this.assetAttrId = assetAttrId;
	}
	public Short getPartsState() {
		return partsState;
	}
	public void setPartsState(Short partsState) {
		this.partsState = partsState;
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
