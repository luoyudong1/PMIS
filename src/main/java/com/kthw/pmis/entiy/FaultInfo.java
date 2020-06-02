package com.kthw.pmis.entiy;

/**
 *
 * @mbg.generated do_not_delete_during_merge 2020-05-18 17:27:22
 */
public class FaultInfo {
    /**
     * Database Column Remarks:
     *   tbl_fault_info.id: 故障id
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   tbl_fault_info.fault_info: 故障名称
     */
    private String faultInfo;

    /**
     * Database Column Remarks:
     *   tbl_fault_info.device_type: 设备类型
     */
    private String deviceType;

    /**
     * @return the value of tbl_fault_info.id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the value for tbl_fault_info.id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the value of tbl_fault_info.fault_info
     */
    public String getFaultInfo() {
        return faultInfo;
    }

    /**
     * @param faultInfo the value for tbl_fault_info.fault_info
     */
    public void setFaultInfo(String faultInfo) {
        this.faultInfo = faultInfo == null ? null : faultInfo.trim();
    }

    /**
     * @return the value of tbl_fault_info.device_type
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * @param deviceType the value for tbl_fault_info.device_type
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType == null ? null : deviceType.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", faultInfo=").append(faultInfo);
        sb.append(", deviceType=").append(deviceType);
        sb.append("]");
        return sb.toString();
    }
}