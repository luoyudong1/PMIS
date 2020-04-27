package com.kthw.pmis.entiy;

/**
 *
 * @mbg.generated do_not_delete_during_merge 2019-08-28 09:31:39
 */
public class DeviceModel {
    /**
     * Database Column Remarks:
     *   tbl_5t_device_model_dict.device_model_id: 
     */
    private Short deviceModelId;

    /**
     * Database Column Remarks:
     *   tbl_5t_device_model_dict.device_model_name: 
     */
    private String deviceModelName;

    /**

     */
    private Short enabled;

    /**
     * Database Column Remarks:
     *   tbl_5t_device_model_dict.device_model_code
     */
    private String deviceModelCode;

    /**
     * Database Column Remarks:
     *   tbl_5t_device_model_dict.device_type_id
     */
    private Short deviceTypeId;

    /**
     * @return the value of tbl_5t_device_model_dict.device_model_id
     */
    public Short getDeviceModelId() {
        return deviceModelId;
    }

    /**
     * @param deviceModelId the value for tbl_5t_device_model_dict.device_model_id
     */
    public void setDeviceModelId(Short deviceModelId) {
        this.deviceModelId = deviceModelId;
    }

    /**
     * @return the value of tbl_5t_device_model_dict.device_model_name
     */
    public String getDeviceModelName() {
        return deviceModelName;
    }

    /**
     * @param deviceModelName the value for tbl_5t_device_model_dict.device_model_name
     */
    public void setDeviceModelName(String deviceModelName) {
        this.deviceModelName = deviceModelName == null ? null : deviceModelName.trim();
    }

    /**
     * @return the value of tbl_5t_device_model_dict.enabled
     */
    public Short getEnabled() {
        return enabled;
    }

    /**
     * @param enabled the value for tbl_5t_device_model_dict.enabled
     */
    public void setEnabled(Short enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the value of tbl_5t_device_model_dict.device_model_code
     */
    public String getDeviceModelCode() {
        return deviceModelCode;
    }

    /**
     * @param deviceModelCode the value for tbl_5t_device_model_dict.device_model_code
     */
    public void setDeviceModelCode(String deviceModelCode) {
        this.deviceModelCode = deviceModelCode == null ? null : deviceModelCode.trim();
    }

    /**
     * @return the value of tbl_5t_device_model_dict.device_type_id
     */
    public Short getDeviceTypeId() {
        return deviceTypeId;
    }

    /**
     * @param deviceTypeId the value for tbl_5t_device_model_dict.device_type_id
     */
    public void setDeviceTypeId(Short deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", deviceModelId=").append(deviceModelId);
        sb.append(", deviceModelName=").append(deviceModelName);
        sb.append(", enabled=").append(enabled);
        sb.append(", deviceModelCode=").append(deviceModelCode);
        sb.append(", deviceTypeId=").append(deviceTypeId);
        sb.append("]");
        return sb.toString();
    }
}