package com.kthw.pmis.entiy.ext;

import com.kthw.pmis.entiy.PartsDict;

public class ViewParts extends PartsDict {
    private String devicePartsName;
    private String deviceModelName;
    private String deviceTypeName;
    private String supplierName;

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
