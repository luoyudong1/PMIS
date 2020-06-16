package com.kthw.pmis.entiy;

public class DetectDeviceWithDepot {
    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.detect_device_id: 探测站id
     */
    private Integer detectDeviceId;

    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.detect_device_name: 探测站设备名称
     */
    private String detectDeviceName;
    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.device_type_id: 设备类型ID
     */
    private String deviceTypeName;
    private String deviceModelName;
    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.depot_id: 所属部门ID
     */
    private String depotName;
    private String workShopName;
    private String segmentName;
    private Float managePerCapita;

    public String getDeviceModelName() {
        return deviceModelName;
    }

    public void setDeviceModelName(String deviceModelName) {
        this.deviceModelName = deviceModelName;
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

    public String getWorkShopName() {
        return workShopName;
    }

    public void setWorkShopName(String workShopName) {
        this.workShopName = workShopName;
    }

    public String getSegmentName() {
        return segmentName;
    }

    public void setSegmentName(String segmentName) {
        this.segmentName = segmentName;
    }

    public Float getManagePerCapita() {
        return managePerCapita;
    }

    public void setManagePerCapita(Float managePerCapita) {
        this.managePerCapita = managePerCapita;
    }
}
