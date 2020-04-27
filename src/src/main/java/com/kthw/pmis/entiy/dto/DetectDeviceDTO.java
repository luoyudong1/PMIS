package com.kthw.pmis.entiy.dto;

import com.kthw.pmis.entiy.DetectDevice;

import java.util.Date;

public class DetectDeviceDTO extends DetectDevice {
    private String depotName;
    private String supplierName;
    private String deviceModelName;
    private String deviceTypeName;

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getDeviceModelName() {
        return deviceModelName;
    }

    public void setDeviceModelName(String deviceModelName) {
        this.deviceModelName = deviceModelName;
    }


}
