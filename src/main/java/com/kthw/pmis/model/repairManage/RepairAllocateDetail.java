package com.kthw.pmis.model.repairManage;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @mbg.generated do_not_delete_during_merge 2019-08-22 11:28:40
 */
public class RepairAllocateDetail {
    /**
     * Database Column Remarks:
     *   tbl_repair_allocate_detail.sheet_id: ����Ψһ����
     */
    private String sheetId;

    /**
     * Database Column Remarks:
     *   tbl_repair_allocate_detail.part_id: ������
     */
    private String partId;

    /**
     * Database Column Remarks:
     *   tbl_repair_allocate_detail.quantity: ���������ֻ���ǡ�������ÿ�����ֻ��Ψһһ��
     */
    private Short quantity;

    /**
     * Database Column Remarks:
     *   tbl_repair_allocate_detail.unit_price: ����
     */
    private BigDecimal unitPrice;

    /**
     * Database Column Remarks:
     *   tbl_repair_allocate_detail.remark: ��ע
     */
    private String remark;

    /**
     * Database Column Remarks:
     *   tbl_repair_allocate_detail.location: �ֿ���ϸλ��
     */
    private String location;

    /**
     * Database Column Remarks:
     *   tbl_repair_allocate_detail.part_code: ����������
     */
    private String partCode;

    /**
     * Database Column Remarks:
     *   tbl_repair_allocate_detail.used_dict: ʹ��̽��վ
     */
    private String usedDict;

    /**
     * Database Column Remarks:
     *   tbl_repair_allocate_detail.happened_time: ���Ϸ���ʱ��
     */
    private Date happenedTime;

    /**
     * Database Column Remarks:
     *   tbl_repair_allocate_detail.fault : ��������
     */
    private String fault;

    /**
     * Database Column Remarks:
     *   tbl_repair_allocate_detail.maintenance_info: �������
     */
    private String maintenanceInfo;

    /**
     * Database Column Remarks:
     *   tbl_repair_allocate_detail.return_date: �޷�ʱ��
     */
    private Date returnDate;

    /**
     * Database Column Remarks:
     *   tbl_repair_allocate_detail.repaired_price: ���޵���
     */
    private BigDecimal repairedPrice;

    /**
     * @return the value of tbl_repair_allocate_detail.sheet_id
     */
    public String getSheetId() {
        return sheetId;
    }

    /**
     * @param sheetId the value for tbl_repair_allocate_detail.sheet_id
     */
    public void setSheetId(String sheetId) {
        this.sheetId = sheetId == null ? null : sheetId.trim();
    }

    /**
     * @return the value of tbl_repair_allocate_detail.part_id
     */
    public String getPartId() {
        return partId;
    }

    /**
     * @param partId the value for tbl_repair_allocate_detail.part_id
     */
    public void setPartId(String partId) {
        this.partId = partId == null ? null : partId.trim();
    }

    /**
     * @return the value of tbl_repair_allocate_detail.quantity
     */
    public Short getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the value for tbl_repair_allocate_detail.quantity
     */
    public void setQuantity(Short quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the value of tbl_repair_allocate_detail.unit_price
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * @param unitPrice the value for tbl_repair_allocate_detail.unit_price
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * @return the value of tbl_repair_allocate_detail.remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the value for tbl_repair_allocate_detail.remark
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * @return the value of tbl_repair_allocate_detail.location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the value for tbl_repair_allocate_detail.location
     */
    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    /**
     * @return the value of tbl_repair_allocate_detail.part_code
     */
    public String getPartCode() {
        return partCode;
    }

    /**
     * @param partCode the value for tbl_repair_allocate_detail.part_code
     */
    public void setPartCode(String partCode) {
        this.partCode = partCode == null ? null : partCode.trim();
    }

    /**
     * @return the value of tbl_repair_allocate_detail.used_dict
     */
    public String getUsedDict() {
        return usedDict;
    }

    /**
     * @param usedDict the value for tbl_repair_allocate_detail.used_dict
     */
    public void setUsedDict(String usedDict) {
        this.usedDict = usedDict == null ? null : usedDict.trim();
    }

    /**
     * @return the value of tbl_repair_allocate_detail.happened_time
     */
    public Date getHappenedTime() {
        return happenedTime;
    }

    /**
     * @param happenedTime the value for tbl_repair_allocate_detail.happened_time
     */
    public void setHappenedTime(Date happenedTime) {
        this.happenedTime = happenedTime;
    }

    /**
     * @return the value of tbl_repair_allocate_detail.fault 
     */
    public String getFault() {
        return fault;
    }

    /**
     * @param fault the value for tbl_repair_allocate_detail.fault 
     */
    public void setFault(String fault) {
        this.fault = fault == null ? null : fault.trim();
    }

    /**
     * @return the value of tbl_repair_allocate_detail.maintenance_info
     */
    public String getMaintenanceInfo() {
        return maintenanceInfo;
    }

    /**
     * @param maintenanceInfo the value for tbl_repair_allocate_detail.maintenance_info
     */
    public void setMaintenanceInfo(String maintenanceInfo) {
        this.maintenanceInfo = maintenanceInfo == null ? null : maintenanceInfo.trim();
    }

    /**
     * @return the value of tbl_repair_allocate_detail.return_date
     */
    public Date getReturnDate() {
        return returnDate;
    }

    /**
     * @param returnDate the value for tbl_repair_allocate_detail.return_date
     */
    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * @return the value of tbl_repair_allocate_detail.repaired_price
     */
    public BigDecimal getRepairedPrice() {
        return repairedPrice;
    }

    /**
     * @param repairedPrice the value for tbl_repair_allocate_detail.repaired_price
     */
    public void setRepairedPrice(BigDecimal repairedPrice) {
        this.repairedPrice = repairedPrice;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", sheetId=").append(sheetId);
        sb.append(", partId=").append(partId);
        sb.append(", quantity=").append(quantity);
        sb.append(", unitPrice=").append(unitPrice);
        sb.append(", remark=").append(remark);
        sb.append(", location=").append(location);
        sb.append(", partCode=").append(partCode);
        sb.append(", usedDict=").append(usedDict);
        sb.append(", happenedTime=").append(happenedTime);
        sb.append(", fault=").append(fault);
        sb.append(", maintenanceInfo=").append(maintenanceInfo);
        sb.append(", returnDate=").append(returnDate);
        sb.append(", repairedPrice=").append(repairedPrice);
        sb.append("]");
        return sb.toString();
    }
}