package com.kthw.pmis.entiy;

/**
 *
 * @mbg.generated do_not_delete_during_merge 2019-08-28 09:31:39
 */
public class DeviceType {
    /**
     * Database Column Remarks:
     *   tbl_5t_device_type_dict.device_type_id:
     */
    private Short deviceTypeId;

    /**
     * Database Column Remarks:
     *   tbl_5t_device_type_dict.device_type_name: 
     */
    private String deviceTypeName;

    /**
     * Database Column Remarks:
     *   tbl_5t_device_type_dict.enabled: 
     */
    private Short enabled;

    /**
     * Database Column Remarks:
     *   tbl_5t_device_type_dict.device_type_code: 
     */
    private String deviceTypeCode;

    /**
     * @return the value of tbl_5t_device_type_dict.device_type_id
     */
    public Short getDeviceTypeId() {
        return deviceTypeId;
    }

    /**
     * @param deviceTypeId the value for tbl_5t_device_type_dict.device_type_id
     */
    public void setDeviceTypeId(Short deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    /**
     * @return the value of tbl_5t_device_type_dict.device_type_name
     */
    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    /**
     * @param deviceTypeName the value for tbl_5t_device_type_dict.device_type_name
     */
    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName == null ? null : deviceTypeName.trim();
    }

    /**
     * @return the value of tbl_5t_device_type_dict.enabled
     */
    public Short getEnabled() {
        return enabled;
    }

    /**
     * @param enabled the value for tbl_5t_device_type_dict.enabled
     */
    public void setEnabled(Short enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the value of tbl_5t_device_type_dict.device_type_code
     */
    public String getDeviceTypeCode() {
        return deviceTypeCode;
    }

    /**
     * @param deviceTypeCode the value for tbl_5t_device_type_dict.device_type_code
     */
    public void setDeviceTypeCode(String deviceTypeCode) {
        this.deviceTypeCode = deviceTypeCode == null ? null : deviceTypeCode.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", deviceTypeId=").append(deviceTypeId);
        sb.append(", deviceTypeName=").append(deviceTypeName);
        sb.append(", enabled=").append(enabled);
        sb.append(", deviceTypeCode=").append(deviceTypeCode);
        sb.append("]");
        return sb.toString();
    }
}