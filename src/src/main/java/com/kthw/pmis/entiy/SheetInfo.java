package com.kthw.pmis.entiy;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @mbg.generated do_not_delete_during_merge 2019-08-29 15:30:01
 */
public class SheetInfo {

    private String sheetId;


    private Short sheetType;


    private String supplierName;


    private Short sendVerifyFlag;


    private Date sheetDate;


    private Short sourceStoreHouseId;


    private String sendOperatorId;


    private String sendOperatorName;


    private String sendRemark;

    /**
     * Database Column Remarks:
     *   tbl_sheet_info.send_verify_id: 
     */
    private String sendVerifyId;

    /**
     * Database Column Remarks:
     *   tbl_sheet_info.send_verify_name: 
     */
    private String sendVerifyName;

    /**
     * Database Column Remarks:
     *   tbl_sheet_info.send_verify_date: 
     */
    private Date sendVerifyDate;

    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    private Date addDate;


    private Short source;


    private Short objectStoreHouseId;


    private String receiptOperatorId;


    private String receiptOperatorName;


    private String receiptRemark;

 
    private String receiptVerifyId;

 
    private String receiptVerifyName;

    private Date receiptVerifyDate;

 
    private Short receiptVerifyFlag;

 
    private Short state;
 
    private String trackingNumber;
    
    private Short deviceId;
    
    private String deviceName;
    
    private Short assetAttributesId;
    
    private Long depotId;
    
    public Long getDepotId() {
		return depotId;
	}


	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}


	public Short getAssetAttributesId() {
		return assetAttributesId;
	}


	public void setAssetAttributesId(Short assetAttributesId) {
		this.assetAttributesId = assetAttributesId;
	}


	public String getDeviceName() {
		return deviceName;
	}


	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}


	public Short getDeviceId() {
		return deviceId;
	}


	public void setDeviceId(Short deviceId) {
		this.deviceId = deviceId;
	}


	public String getTrackingNumber() {
		return trackingNumber;
	}


	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}


	public String getSheetId() {
        return sheetId;
    }


    public void setSheetId(String sheetId) {
        this.sheetId = sheetId == null ? null : sheetId.trim();
    }

    public Short getSheetType() {
        return sheetType;
    }


    public void setSheetType(Short sheetType) {
        this.sheetType = sheetType;
    }

 
    public String getSupplierName() {
        return supplierName;
    }


    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName == null ? null : supplierName.trim();
    }


    public Short getSendVerifyFlag() {
        return sendVerifyFlag;
    }

  
    public void setSendVerifyFlag(Short sendVerifyFlag) {
        this.sendVerifyFlag = sendVerifyFlag;
    }


    public Date getSheetDate() {
        return sheetDate;
    }

    /**
     * @param sheetDate the value for tbl_sheet_info.sheet_date
     */
    public void setSheetDate(Date sheetDate) {
        this.sheetDate = sheetDate;
    }

    /**
     * @return the value of tbl_sheet_info.source_store_house_id
     */
    public Short getSourceStoreHouseId() {
        return sourceStoreHouseId;
    }

    /**
     * @param sourceStoreHouseId the value for tbl_sheet_info.source_store_house_id
     */
    public void setSourceStoreHouseId(Short sourceStoreHouseId) {
        this.sourceStoreHouseId = sourceStoreHouseId;
    }

    /**
     * @return the value of tbl_sheet_info.send_operator_id
     */
    public String getSendOperatorId() {
        return sendOperatorId;
    }

    /**
     * @param sendOperatorId the value for tbl_sheet_info.send_operator_id
     */
    public void setSendOperatorId(String sendOperatorId) {
        this.sendOperatorId = sendOperatorId == null ? null : sendOperatorId.trim();
    }

    /**
     * @return the value of tbl_sheet_info.send_operator_name
     */
    public String getSendOperatorName() {
        return sendOperatorName;
    }

    /**
     * @param sendOperatorName the value for tbl_sheet_info.send_operator_name
     */
    public void setSendOperatorName(String sendOperatorName) {
        this.sendOperatorName = sendOperatorName == null ? null : sendOperatorName.trim();
    }

    /**
     * @return the value of tbl_sheet_info.send_remark
     */
    public String getSendRemark() {
        return sendRemark;
    }

    /**
     * @param sendRemark the value for tbl_sheet_info.send_remark
     */
    public void setSendRemark(String sendRemark) {
        this.sendRemark = sendRemark == null ? null : sendRemark.trim();
    }

    /**
     * @return the value of tbl_sheet_info.send_verify_id
     */
    public String getSendVerifyId() {
        return sendVerifyId;
    }

    /**
     * @param sendVerifyId the value for tbl_sheet_info.send_verify_id
     */
    public void setSendVerifyId(String sendVerifyId) {
        this.sendVerifyId = sendVerifyId == null ? null : sendVerifyId.trim();
    }

    /**
     * @return the value of tbl_sheet_info.send_verify_name
     */
    public String getSendVerifyName() {
        return sendVerifyName;
    }

    /**
     * @param sendVerifyName the value for tbl_sheet_info.send_verify_name
     */
    public void setSendVerifyName(String sendVerifyName) {
        this.sendVerifyName = sendVerifyName == null ? null : sendVerifyName.trim();
    }

    /**
     * @return the value of tbl_sheet_info.send_verify_date
     */
    public Date getSendVerifyDate() {
        return sendVerifyDate;
    }

    /**
     * @param sendVerifyDate the value for tbl_sheet_info.send_verify_date
     */
    public void setSendVerifyDate(Date sendVerifyDate) {
        this.sendVerifyDate = sendVerifyDate;
    }

    /**
     * @return the value of tbl_sheet_info.add_date
     */
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    public Date getAddDate() {
        return addDate;
    }
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    /**
     * @param addDate the value for tbl_sheet_info.add_date
     */
    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    /**
     * @return the value of tbl_sheet_info.source
     */
    public Short getSource() {
        return source;
    }

    /**
     * @param source the value for tbl_sheet_info.source
     */
    public void setSource(Short source) {
        this.source = source;
    }

    /**
     * @return the value of tbl_sheet_info.object_store_house_id
     */
    public Short getObjectStoreHouseId() {
        return objectStoreHouseId;
    }

    /**
     * @param objectStoreHouseId the value for tbl_sheet_info.object_store_house_id
     */
    public void setObjectStoreHouseId(Short objectStoreHouseId) {
        this.objectStoreHouseId = objectStoreHouseId;
    }

    /**
     * @return the value of tbl_sheet_info.receipt_operator_id
     */
    public String getReceiptOperatorId() {
        return receiptOperatorId;
    }

    /**
     * @param receiptOperatorId the value for tbl_sheet_info.receipt_operator_id
     */
    public void setReceiptOperatorId(String receiptOperatorId) {
        this.receiptOperatorId = receiptOperatorId == null ? null : receiptOperatorId.trim();
    }

    /**
     * @return the value of tbl_sheet_info.receipt_operator_name
     */
    public String getReceiptOperatorName() {
        return receiptOperatorName;
    }

    /**
     * @param receiptOperatorName the value for tbl_sheet_info.receipt_operator_name
     */
    public void setReceiptOperatorName(String receiptOperatorName) {
        this.receiptOperatorName = receiptOperatorName == null ? null : receiptOperatorName.trim();
    }

    /**
     * @return the value of tbl_sheet_info.receipt_remark
     */
    public String getReceiptRemark() {
        return receiptRemark;
    }

    /**
     * @param receiptRemark the value for tbl_sheet_info.receipt_remark
     */
    public void setReceiptRemark(String receiptRemark) {
        this.receiptRemark = receiptRemark == null ? null : receiptRemark.trim();
    }

    /**
     * @return the value of tbl_sheet_info.receipt_verify_id
     */
    public String getReceiptVerifyId() {
        return receiptVerifyId;
    }

    /**
     * @param receiptVerifyId the value for tbl_sheet_info.receipt_verify_id
     */
    public void setReceiptVerifyId(String receiptVerifyId) {
        this.receiptVerifyId = receiptVerifyId == null ? null : receiptVerifyId.trim();
    }

    /**
     * @return the value of tbl_sheet_info.receipt_verify_name
     */
    public String getReceiptVerifyName() {
        return receiptVerifyName;
    }

    /**
     * @param receiptVerifyName the value for tbl_sheet_info.receipt_verify_name
     */
    public void setReceiptVerifyName(String receiptVerifyName) {
        this.receiptVerifyName = receiptVerifyName == null ? null : receiptVerifyName.trim();
    }

    /**
     * @return the value of tbl_sheet_info.receipt_verify_date
     */
    public Date getReceiptVerifyDate() {
        return receiptVerifyDate;
    }

    /**
     * @param receiptVerifyDate the value for tbl_sheet_info.receipt_verify_date
     */
    public void setReceiptVerifyDate(Date receiptVerifyDate) {
        this.receiptVerifyDate = receiptVerifyDate;
    }

    /**
     * @return the value of tbl_sheet_info.receipt_verify_flag
     */
    public Short getReceiptVerifyFlag() {
        return receiptVerifyFlag;
    }

    /**
     * @param receiptVerifyFlag the value for tbl_sheet_info.receipt_verify_flag
     */
    public void setReceiptVerifyFlag(Short receiptVerifyFlag) {
        this.receiptVerifyFlag = receiptVerifyFlag;
    }

    /**
     * @return the value of tbl_sheet_info.state
     */
    public Short getState() {
        return state;
    }

    /**
     * @param state the value for tbl_sheet_info.state
     */
    public void setState(Short state) {
        this.state = state;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", sheetId=").append(sheetId);
        sb.append(", sheetType=").append(sheetType);
        sb.append(", supplierName=").append(supplierName);
        sb.append(", sendVerifyFlag=").append(sendVerifyFlag);
        sb.append(", sheetDate=").append(sheetDate);
        sb.append(", sourceStoreHouseId=").append(sourceStoreHouseId);
        sb.append(", sendOperatorId=").append(sendOperatorId);
        sb.append(", sendOperatorName=").append(sendOperatorName);
        sb.append(", sendRemark=").append(sendRemark);
        sb.append(", sendVerifyId=").append(sendVerifyId);
        sb.append(", sendVerifyName=").append(sendVerifyName);
        sb.append(", sendVerifyDate=").append(sendVerifyDate);
        sb.append(", addDate=").append(addDate);
        sb.append(", source=").append(source);
        sb.append(", objectStoreHouseId=").append(objectStoreHouseId);
        sb.append(", receiptOperatorId=").append(receiptOperatorId);
        sb.append(", receiptOperatorName=").append(receiptOperatorName);
        sb.append(", receiptRemark=").append(receiptRemark);
        sb.append(", receiptVerifyId=").append(receiptVerifyId);
        sb.append(", receiptVerifyName=").append(receiptVerifyName);
        sb.append(", receiptVerifyDate=").append(receiptVerifyDate);
        sb.append(", receiptVerifyFlag=").append(receiptVerifyFlag);
        sb.append(", state=").append(state);
        sb.append("]");
        return sb.toString();
    }
}