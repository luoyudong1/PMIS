package com.kthw.pmis.entiy;

import java.math.BigDecimal;

/**
 *
 * @mbg.generated do_not_delete_during_merge 2019-11-22 09:12:02
 */
public class PartsDict {
    /**
     * Database Column Remarks:
     *   tbl_parts_dict.parts_id: 配件ID
     */
    private Integer partsId;

    /**
     * Database Column Remarks:
     *   tbl_parts_dict.parts_code: 配件编码前缀
     */
    private String partsCode;

    /**
     * Database Column Remarks:
     *   tbl_parts_dict.device_parts_id: 配件名称ID
     */
    private Short devicePartsId;

    /**
     * Database Column Remarks:
     *   tbl_parts_dict.device_type_id: 配件所属设备类型ID
     */
    private Short deviceTypeId;

    /**
     * Database Column Remarks:
     *   tbl_parts_dict.device_model_id: 配件所属设备型号ID
     */
    private Short deviceModelId;

    /**
     * Database Column Remarks:
     *   tbl_parts_dict.unit_price: 单价
     */
    private BigDecimal unitPrice;

    /**
     * Database Column Remarks:
     *   tbl_parts_dict.unit: 单位
     */
    private String unit;

    /**
     * Database Column Remarks:
     *   tbl_parts_dict.pic: 图片
     */
    private String pic;

    /**
     * Database Column Remarks:
     *   tbl_parts_dict.detect_price: 检测单价
     */
    private BigDecimal detectPrice;

    /**
     * Database Column Remarks:
     *   tbl_parts_dict.fix_max_pric: 检修最高价
     */
    private BigDecimal fixMaxPric;

    /**
     * Database Column Remarks:
     *   tbl_parts_dict.normal_quantity: 应配备数量
     */
    private BigDecimal normalQuantity;

    /**
     * Database Column Remarks:
     *   tbl_parts_dict.low_alarm_quantity: 低储备报警数量
     */
    private BigDecimal lowAlarmQuantity;

    /**
     * Database Column Remarks:
     *   tbl_parts_dict.high_alarm_quantity: 高储备报警数量
     */
    private BigDecimal highAlarmQuantity;

    /**
     * Database Column Remarks:
     *   tbl_parts_dict.enabled: 是否可用，1：可用，0：不可用
     */
    private Short enabled;

    /**
     * Database Column Remarks:
     *   tbl_parts_dict.supplier_id: 所属厂家ID
     */
    private Short supplierId;

    /**
     * Database Column Remarks:
     *   tbl_parts_dict.comment: 备注内容
     */
    private String comment;

    /**
     * @return the value of tbl_parts_dict.parts_id
     */
    public Integer getPartsId() {
        return partsId;
    }

    /**
     * @param partsId the value for tbl_parts_dict.parts_id
     */
    public void setPartsId(Integer partsId) {
        this.partsId = partsId;
    }

    /**
     * @return the value of tbl_parts_dict.parts_code
     */
    public String getPartsCode() {
        return partsCode;
    }

    /**
     * @param partsCode the value for tbl_parts_dict.parts_code
     */
    public void setPartsCode(String partsCode) {
        this.partsCode = partsCode == null ? null : partsCode.trim();
    }

    /**
     * @return the value of tbl_parts_dict.device_parts_id
     */
    public Short getDevicePartsId() {
        return devicePartsId;
    }

    /**
     * @param devicePartsId the value for tbl_parts_dict.device_parts_id
     */
    public void setDevicePartsId(Short devicePartsId) {
        this.devicePartsId = devicePartsId;
    }

    /**
     * @return the value of tbl_parts_dict.device_type_id
     */
    public Short getDeviceTypeId() {
        return deviceTypeId;
    }

    /**
     * @param deviceTypeId the value for tbl_parts_dict.device_type_id
     */
    public void setDeviceTypeId(Short deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    /**
     * @return the value of tbl_parts_dict.device_model_id
     */
    public Short getDeviceModelId() {
        return deviceModelId;
    }

    /**
     * @param deviceModelId the value for tbl_parts_dict.device_model_id
     */
    public void setDeviceModelId(Short deviceModelId) {
        this.deviceModelId = deviceModelId;
    }

    /**
     * @return the value of tbl_parts_dict.unit_price
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * @param unitPrice the value for tbl_parts_dict.unit_price
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * @return the value of tbl_parts_dict.unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @param unit the value for tbl_parts_dict.unit
     */
    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    /**
     * @return the value of tbl_parts_dict.pic
     */
    public String getPic() {
        return pic;
    }

    /**
     * @param pic the value for tbl_parts_dict.pic
     */
    public void setPic(String pic) {
        this.pic = pic == null ? null : pic.trim();
    }

    /**
     * @return the value of tbl_parts_dict.detect_price
     */
    public BigDecimal getDetectPrice() {
        return detectPrice;
    }

    /**
     * @param detectPrice the value for tbl_parts_dict.detect_price
     */
    public void setDetectPrice(BigDecimal detectPrice) {
        this.detectPrice = detectPrice;
    }

    /**
     * @return the value of tbl_parts_dict.fix_max_pric
     */
    public BigDecimal getFixMaxPric() {
        return fixMaxPric;
    }

    /**
     * @param fixMaxPric the value for tbl_parts_dict.fix_max_pric
     */
    public void setFixMaxPric(BigDecimal fixMaxPric) {
        this.fixMaxPric = fixMaxPric;
    }

    /**
     * @return the value of tbl_parts_dict.normal_quantity
     */
    public BigDecimal getNormalQuantity() {
        return normalQuantity;
    }

    /**
     * @param normalQuantity the value for tbl_parts_dict.normal_quantity
     */
    public void setNormalQuantity(BigDecimal normalQuantity) {
        this.normalQuantity = normalQuantity;
    }

    /**
     * @return the value of tbl_parts_dict.low_alarm_quantity
     */
    public BigDecimal getLowAlarmQuantity() {
        return lowAlarmQuantity;
    }

    /**
     * @param lowAlarmQuantity the value for tbl_parts_dict.low_alarm_quantity
     */
    public void setLowAlarmQuantity(BigDecimal lowAlarmQuantity) {
        this.lowAlarmQuantity = lowAlarmQuantity;
    }

    /**
     * @return the value of tbl_parts_dict.high_alarm_quantity
     */
    public BigDecimal getHighAlarmQuantity() {
        return highAlarmQuantity;
    }

    /**
     * @param highAlarmQuantity the value for tbl_parts_dict.high_alarm_quantity
     */
    public void setHighAlarmQuantity(BigDecimal highAlarmQuantity) {
        this.highAlarmQuantity = highAlarmQuantity;
    }

    /**
     * @return the value of tbl_parts_dict.enabled
     */
    public Short getEnabled() {
        return enabled;
    }

    /**
     * @param enabled the value for tbl_parts_dict.enabled
     */
    public void setEnabled(Short enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the value of tbl_parts_dict.supplier_id
     */
    public Short getSupplierId() {
        return supplierId;
    }

    /**
     * @param supplierId the value for tbl_parts_dict.supplier_id
     */
    public void setSupplierId(Short supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * @return the value of tbl_parts_dict.comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the value for tbl_parts_dict.comment
     */
    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", partsId=").append(partsId);
        sb.append(", partsCode=").append(partsCode);
        sb.append(", devicePartsId=").append(devicePartsId);
        sb.append(", deviceTypeId=").append(deviceTypeId);
        sb.append(", deviceModelId=").append(deviceModelId);
        sb.append(", unitPrice=").append(unitPrice);
        sb.append(", unit=").append(unit);
        sb.append(", pic=").append(pic);
        sb.append(", detectPrice=").append(detectPrice);
        sb.append(", fixMaxPric=").append(fixMaxPric);
        sb.append(", normalQuantity=").append(normalQuantity);
        sb.append(", lowAlarmQuantity=").append(lowAlarmQuantity);
        sb.append(", highAlarmQuantity=").append(highAlarmQuantity);
        sb.append(", enabled=").append(enabled);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", comment=").append(comment);
        sb.append("]");
        return sb.toString();
    }
}