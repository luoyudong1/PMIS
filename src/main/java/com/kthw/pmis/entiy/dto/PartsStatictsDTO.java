package com.kthw.pmis.entiy.dto;

import java.math.BigDecimal;

public class PartsStatictsDTO {
    private String partId;
    private Short assetAttributesId;
    private String devicePartsName;
    private String deviceModelName;
    private String deviceTypeName;
    private String supplierName;
    private BigDecimal unitPrice;
    private Integer count;

    public Short getAssetAttributesId() {
        return assetAttributesId;
    }

    public void setAssetAttributesId(Short assetAttributesId) {
        this.assetAttributesId = assetAttributesId;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
