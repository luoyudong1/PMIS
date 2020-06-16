package com.kthw.pmis.entiy;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 *
 * @mbg.generated do_not_delete_during_merge 2019-08-29 15:30:01
 */
public class SheetDetail extends SheetDetailKey {
    private Short quantity;

    /**
     * Database Column Remarks:
     *   tbl_sheet_detail.unit_price: 
     */
    private BigDecimal unitPrice;

    /**
     * Database Column Remarks:
     *   tbl_sheet_detail.remark: ע
     */
    private String remark;

    /**
     * Database Column Remarks:
     *   tbl_sheet_detail.location: 
     */
    private String location;

    /**
     * Database Column Remarks:
     *   tbl_sheet_detail.part_code:
     */
    private String partId;

    /**
     * Database Column Remarks:
     *   tbl_sheet_detail.asset_attributes_id:
     */
    private Short assetAttributesId;

    /**
     * Database Column Remarks:
     *   tbl_sheet_detail.fault_info: 
     */
    private String faultInfo;

    /**
     * Database Column Remarks:
     *   tbl_sheet_detail.fault_date: 
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date faultDate;

    /**
     * Database Column Remarks:
     *   tbl_sheet_detail.fault_remark:ע
     */
    private String faultRemark;

    /**
     * Database Column Remarks:
     *   tbl_sheet_detail.prepare_check:
     */
    private String prepareCheck;

    /**
     * Database Column Remarks:
     *   tbl_sheet_detail.machine_check: 
     */
    private String machineCheck;

    /**
     * Database Column Remarks:
     *   tbl_sheet_detail.replace_component_check: 
     */
    private String replaceComponentCheck;

    /**
     * Database Column Remarks:
     *   tbl_sheet_detail.copy_machine_start_time: 
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date copyMachineStartTime;

    /**
     * Database Column Remarks:
     *   tbl_sheet_detail.copy_machine_end_time: 
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date copyMachineEndTime;

    private String copyMachineCheck;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date checkedDate;

    private String checkedUserId;

    private String checkedUserName;

    private String reviewUserId;

    private String reviewUserName;

    private String checkedRemark;

    private BigDecimal checkedPrice;

 
    private Short repaireState;


    private String scrapReason;


    private String factoryReplaceComponent;


    private Short factoryReplaceCount;


    private Integer factoryRepairedPrice;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date factoryRepairedDate;


    private String usedStationName;


    private Short usedStationId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date purchaseOrRepairedDate;


    private Integer purchaseOrRepairedPrice;


    private Short partState;

    private Short warranty;
    
    private Integer detectPartsId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;//新增时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;//修改时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date purchaseDate;//新购时间
    private String newPartCode;//新出厂编码
    private String newRepairPrice;//修改返厂修复价格
    private String assetAttributesName;//资产属性名称

    public String getAssetAttributesName() {
        return assetAttributesName;
    }

    public void setAssetAttributesName(String assetAttributesName) {
        this.assetAttributesName = assetAttributesName;
    }

    public String getNewRepairPrice() {
        return newRepairPrice;
    }

    public void setNewRepairPrice(String newRepairPrice) {
        this.newRepairPrice = newRepairPrice;
    }

    public String getNewPartCode() {
        return newPartCode;
    }

    public void setNewPartCode(String newPartCode) {
        this.newPartCode = newPartCode;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDetectPartsId() {
		return detectPartsId;
	}

	public void setDetectPartsId(Integer detectPartsId) {
		this.detectPartsId = detectPartsId;
	}

	public Short getWarranty() {
		return warranty;
	}

	public void setWarranty(Short warranty) {
		this.warranty = warranty;
	}

	/**
     * @return the value of tbl_sheet_detail.quantity
     */
    public Short getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the value for tbl_sheet_detail.quantity
     */
    public void setQuantity(Short quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the value of tbl_sheet_detail.unit_price
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * @param unitPrice the value for tbl_sheet_detail.unit_price
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * @return the value of tbl_sheet_detail.remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the value for tbl_sheet_detail.remark
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * @return the value of tbl_sheet_detail.location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the value for tbl_sheet_detail.location
     */
    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    /**
     * @return the value of tbl_sheet_detail.part_code
     */
    public String getPartId() {
        return partId;
    }

    /**
     * @param
     */
    public void setPartId(String partId) {
        this.partId = partId == null ? null : partId.trim();
    }

    /**
     * @return the value of tbl_sheet_detail.asset_attributes_id
     */
    public Short getAssetAttributesId() {
        return assetAttributesId;
    }


    /**
     * @param assetAttributesId the value for tbl_sheet_detail.asset_attributes_id
     */
    public void setAssetAttributesId(Short assetAttributesId) {
        this.assetAttributesId = assetAttributesId;
    }

    /**
     * @return the value of tbl_sheet_detail.fault_info
     */
    public String getFaultInfo() {
        return faultInfo;
    }

    /**
     * @param faultInfo the value for tbl_sheet_detail.fault_info
     */
    public void setFaultInfo(String faultInfo) {
        this.faultInfo = faultInfo == null ? null : faultInfo.trim();
    }

    /**
     * @return the value of tbl_sheet_detail.fault_date
     */
    public Date getFaultDate() {
        return faultDate;
    }

    /**
     * @param faultDate the value for tbl_sheet_detail.fault_date
     */
    public void setFaultDate(Date faultDate) {
        this.faultDate = faultDate;
    }

    /**
     * @return the value of tbl_sheet_detail.fault_remark
     */
    public String getFaultRemark() {
        return faultRemark;
    }

    /**
     * @param faultRemark the value for tbl_sheet_detail.fault_remark
     */
    public void setFaultRemark(String faultRemark) {
        this.faultRemark = faultRemark == null ? null : faultRemark.trim();
    }

    /**
     * @return the value of tbl_sheet_detail.prepare_check
     */
    public String getPrepareCheck() {
        return prepareCheck;
    }

    /**
     * @param prepareCheck the value for tbl_sheet_detail.prepare_check
     */
    public void setPrepareCheck(String prepareCheck) {
        this.prepareCheck = prepareCheck == null ? null : prepareCheck.trim();
    }

    /**
     * @return the value of tbl_sheet_detail.machine_check
     */
    public String getMachineCheck() {
        return machineCheck;
    }

    /**
     * @param machineCheck the value for tbl_sheet_detail.machine_check
     */
    public void setMachineCheck(String machineCheck) {
        this.machineCheck = machineCheck == null ? null : machineCheck.trim();
    }

    /**
     * @return the value of tbl_sheet_detail.replace_component_check
     */
    public String getReplaceComponentCheck() {
        return replaceComponentCheck;
    }

    /**
     * @param replaceComponentCheck the value for tbl_sheet_detail.replace_component_check
     */
    public void setReplaceComponentCheck(String replaceComponentCheck) {
        this.replaceComponentCheck = replaceComponentCheck == null ? null : replaceComponentCheck.trim();
    }

    /**
     * @return the value of tbl_sheet_detail.copy_machine_start_time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date getCopyMachineStartTime() {
        return copyMachineStartTime;
    }

    /**
     * @param copyMachineStartTime the value for tbl_sheet_detail.copy_machine_start_time
     */

    public void setCopyMachineStartTime(Date copyMachineStartTime) {
        this.copyMachineStartTime = copyMachineStartTime;
    }

    /**
     * @return the value of tbl_sheet_detail.copy_machine_end_time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date getCopyMachineEndTime() {
        return copyMachineEndTime;
    }

    /**
     * @param copyMachineEndTime the value for tbl_sheet_detail.copy_machine_end_time
     */
    public void setCopyMachineEndTime(Date copyMachineEndTime) {
        this.copyMachineEndTime = copyMachineEndTime;
    }

    /**
     * @return the value of tbl_sheet_detail.copy_machine_check
     */
    
    public String getCopyMachineCheck() {
        return copyMachineCheck;
    }

    /**
     * @param copyMachineCheck the value for tbl_sheet_detail.copy_machine_check
     */
    public void setCopyMachineCheck(String copyMachineCheck) {
        this.copyMachineCheck = copyMachineCheck == null ? null : copyMachineCheck.trim();
    }

    /**
     * @return the value of tbl_sheet_detail.checked_date
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date getCheckedDate() {
        return checkedDate;
    }

    /**
     * @param checkedDate the value for tbl_sheet_detail.checked_date
     */

    public void setCheckedDate(Date checkedDate) {
        this.checkedDate = checkedDate;
    }

    /**
     * @return the value of tbl_sheet_detail.checked_user_id
     */
    public String getCheckedUserId() {
        return checkedUserId;
    }

    /**
     * @param checkedUserId the value for tbl_sheet_detail.checked_user_id
     */
    public void setCheckedUserId(String checkedUserId) {
        this.checkedUserId = checkedUserId == null ? null : checkedUserId.trim();
    }

    /**
     * @return the value of tbl_sheet_detail.checked_user_name
     */
    public String getCheckedUserName() {
        return checkedUserName;
    }

    /**
     * @param checkedUserName the value for tbl_sheet_detail.checked_user_name
     */
    public void setCheckedUserName(String checkedUserName) {
        this.checkedUserName = checkedUserName == null ? null : checkedUserName.trim();
    }

    /**
     * @return the value of tbl_sheet_detail.review_user_id
     */
    public String getReviewUserId() {
        return reviewUserId;
    }

    /**
     * @param reviewUserId the value for tbl_sheet_detail.review_user_id
     */
    public void setReviewUserId(String reviewUserId) {
        this.reviewUserId = reviewUserId == null ? null : reviewUserId.trim();
    }

    /**
     * @return the value of tbl_sheet_detail.review_user_name
     */
    public String getReviewUserName() {
        return reviewUserName;
    }

    /**
     * @param reviewUserName the value for tbl_sheet_detail.review_user_name
     */
    public void setReviewUserName(String reviewUserName) {
        this.reviewUserName = reviewUserName == null ? null : reviewUserName.trim();
    }

    /**
     * @return the value of tbl_sheet_detail.checked_remark
     */
    public String getCheckedRemark() {
        return checkedRemark;
    }

    /**
     * @param checkedRemark the value for tbl_sheet_detail.checked_remark
     */
    public void setCheckedRemark(String checkedRemark) {
        this.checkedRemark = checkedRemark == null ? null : checkedRemark.trim();
    }

    /**
     * @return the value of tbl_sheet_detail.checked_price
     */
    public BigDecimal getCheckedPrice() {
        return checkedPrice;
    }

    /**
     * @param checkedPrice the value for tbl_sheet_detail.checked_price
     */
    public void setCheckedPrice(BigDecimal checkedPrice) {
        this.checkedPrice = checkedPrice;
    }

    /**
     * @return the value of tbl_sheet_detail.repaire_state
     */
    public Short getRepaireState() {
        return repaireState;
    }

    /**
     * @param repaireState the value for tbl_sheet_detail.repaire_state
     */
    public void setRepaireState(Short repaireState) {
        this.repaireState = repaireState;
    }

    /**
     * @return the value of tbl_sheet_detail.scrap_reason
     */
    public String getScrapReason() {
        return scrapReason;
    }

    /**
     * @param scrapReason the value for tbl_sheet_detail.scrap_reason
     */
    public void setScrapReason(String scrapReason) {
        this.scrapReason = scrapReason == null ? null : scrapReason.trim();
    }

    /**
     * @return the value of tbl_sheet_detail.factory_replace_component
     */
    public String getFactoryReplaceComponent() {
        return factoryReplaceComponent;
    }

    /**
     * @param factoryReplaceComponent the value for tbl_sheet_detail.factory_replace_component
     */
    public void setFactoryReplaceComponent(String factoryReplaceComponent) {
        this.factoryReplaceComponent = factoryReplaceComponent == null ? null : factoryReplaceComponent.trim();
    }

    /**
     * @return the value of tbl_sheet_detail.factory_replace_count
     */
    public Short getFactoryReplaceCount() {
        return factoryReplaceCount;
    }

    /**
     * @param factoryReplaceCount the value for tbl_sheet_detail.factory_replace_count
     */
    public void setFactoryReplaceCount(Short factoryReplaceCount) {
        this.factoryReplaceCount = factoryReplaceCount;
    }

    /**
     * @return the value of tbl_sheet_detail.factory_repaired_price
     */
    public Integer getFactoryRepairedPrice() {
        return factoryRepairedPrice;
    }

    /**
     * @param factoryRepairedPrice the value for tbl_sheet_detail.factory_repaired_price
     */
    public void setFactoryRepairedPrice(Integer factoryRepairedPrice) {
        this.factoryRepairedPrice = factoryRepairedPrice;
    }

    /**
     * @return the value of tbl_sheet_detail.factory_repaired_date
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    public Date getFactoryRepairedDate() {
        return factoryRepairedDate;
    }

    /**
     * @param factoryRepairedDate the value for tbl_sheet_detail.factory_repaired_date
     */
    public void setFactoryRepairedDate(Date factoryRepairedDate) {
        this.factoryRepairedDate = factoryRepairedDate;
    }

    /**
     * @return the value of tbl_sheet_detail.used_station_name
     */
    public String getUsedStationName() {
        return usedStationName;
    }

    /**
     * @param usedStationName the value for tbl_sheet_detail.used_station_name
     */
    public void setUsedStationName(String usedStationName) {
        this.usedStationName = usedStationName == null ? null : usedStationName.trim();
    }

    /**
     * @return the value of tbl_sheet_detail.used_station_id
     */
    public Short getUsedStationId() {
        return usedStationId;
    }

    /**
     * @param usedStationId the value for tbl_sheet_detail.used_station_id
     */
    public void setUsedStationId(Short usedStationId) {
        this.usedStationId = usedStationId;
    }

    /**
     * @return the value of tbl_sheet_detail.purchase_or_repaired_date
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    public Date getPurchaseOrRepairedDate() {
        return purchaseOrRepairedDate;
    }

    /**
     * @param purchaseOrRepairedDate the value for tbl_sheet_detail.purchase_or_repaired_date
     */

    public void setPurchaseOrRepairedDate(Date purchaseOrRepairedDate) {
        this.purchaseOrRepairedDate = purchaseOrRepairedDate;
    }

    /**
     * @return the value of tbl_sheet_detail.purchase_or_repaired_price
     */
    public Integer getPurchaseOrRepairedPrice() {
        return purchaseOrRepairedPrice;
    }

    /**
     * @param purchaseOrRepairedPrice the value for tbl_sheet_detail.purchase_or_repaired_price
     */
    public void setPurchaseOrRepairedPrice(Integer purchaseOrRepairedPrice) {
        this.purchaseOrRepairedPrice = purchaseOrRepairedPrice;
    }

    /**
     * @return the value of tbl_sheet_detail.part_state
     */
    public Short getPartState() {
        return partState;
    }

    /**
     * @param partState the value for tbl_sheet_detail.part_state
     */
    public void setPartState(Short partState) {
        this.partState = partState;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", quantity=").append(quantity);
        sb.append(", unitPrice=").append(unitPrice);
        sb.append(", remark=").append(remark);
        sb.append(", location=").append(location);
        sb.append(", partId=").append(partId);
        sb.append(", assetAttributesId=").append(assetAttributesId);
        sb.append(", faultInfo=").append(faultInfo);
        sb.append(", faultDate=").append(faultDate);
        sb.append(", faultRemark=").append(faultRemark);
        sb.append(", prepareCheck=").append(prepareCheck);
        sb.append(", machineCheck=").append(machineCheck);
        sb.append(", replaceComponentCheck=").append(replaceComponentCheck);
        sb.append(", copyMachineStartTime=").append(copyMachineStartTime);
        sb.append(", copyMachineEndTime=").append(copyMachineEndTime);
        sb.append(", copyMachineCheck=").append(copyMachineCheck);
        sb.append(", checkedDate=").append(checkedDate);
        sb.append(", checkedUserId=").append(checkedUserId);
        sb.append(", checkedUserName=").append(checkedUserName);
        sb.append(", reviewUserId=").append(reviewUserId);
        sb.append(", reviewUserName=").append(reviewUserName);
        sb.append(", checkedRemark=").append(checkedRemark);
        sb.append(", checkedPrice=").append(checkedPrice);
        sb.append(", repaireState=").append(repaireState);
        sb.append(", scrapReason=").append(scrapReason);
        sb.append(", factoryReplaceComponent=").append(factoryReplaceComponent);
        sb.append(", factoryReplaceCount=").append(factoryReplaceCount);
        sb.append(", factoryRepairedPrice=").append(factoryRepairedPrice);
        sb.append(", factoryRepairedDate=").append(factoryRepairedDate);
        sb.append(", usedStationName=").append(usedStationName);
        sb.append(", usedStationId=").append(usedStationId);
        sb.append(", purchaseOrRepairedDate=").append(purchaseOrRepairedDate);
        sb.append(", purchaseOrRepairedPrice=").append(purchaseOrRepairedPrice);
        sb.append(", partState=").append(partState);
        sb.append("]");
        sb.append(", from super class ");
        sb.append(super.toString());
        return sb.toString();
    }
}