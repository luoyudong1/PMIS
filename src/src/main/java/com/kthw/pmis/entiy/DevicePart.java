package com.kthw.pmis.entiy;

/**
 *
 * @mbg.generated do_not_delete_during_merge 2019-08-28 09:31:39
 */
public class DevicePart {
    /**
     * Database Column Remarks:
     *   tbl_5t_device_parts_dict.device_parts_id:
     */
    private Short devicePartsId;

    /**
     * Database Column Remarks:
     *   tbl_5t_device_parts_dict.device_parts_name: 
     */
    private String devicePartsName;

    /**
     * Database Column Remarks:
     *   tbl_5t_device_parts_dict.enabled:
     */
    private Short enabled;

    /**
     * Database Column Remarks:
     *   tbl_5t_device_parts_dict.device_parts_code: 
     */
    private String devicePartsCode;

    /**
     * Database Column Remarks:
     *   tbl_5t_device_parts_dict.comment:
     */
    private String comment;

    /**
     * @return the value of tbl_5t_device_parts_dict.device_parts_id
     */
    public Short getDevicePartsId() {
        return devicePartsId;
    }

    /**
     * @param devicePartsId the value for tbl_5t_device_parts_dict.device_parts_id
     */
    public void setDevicePartsId(Short devicePartsId) {
        this.devicePartsId = devicePartsId;
    }

    /**
     * @return the value of tbl_5t_device_parts_dict.device_parts_name
     */
    public String getDevicePartsName() {
        return devicePartsName;
    }

    /**
     * @param devicePartsName the value for tbl_5t_device_parts_dict.device_parts_name
     */
    public void setDevicePartsName(String devicePartsName) {
        this.devicePartsName = devicePartsName == null ? null : devicePartsName.trim();
    }

    /**
     * @return the value of tbl_5t_device_parts_dict.enabled
     */
    public Short getEnabled() {
        return enabled;
    }

    /**
     * @param enabled the value for tbl_5t_device_parts_dict.enabled
     */
    public void setEnabled(Short enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the value of tbl_5t_device_parts_dict.device_parts_code
     */
    public String getDevicePartsCode() {
        return devicePartsCode;
    }

    /**
     * @param devicePartsCode the value for tbl_5t_device_parts_dict.device_parts_code
     */
    public void setDevicePartsCode(String devicePartsCode) {
        this.devicePartsCode = devicePartsCode == null ? null : devicePartsCode.trim();
    }

    /**
     * @return the value of tbl_5t_device_parts_dict.comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the value for tbl_5t_device_parts_dict.comment
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
        sb.append(", devicePartsId=").append(devicePartsId);
        sb.append(", devicePartsName=").append(devicePartsName);
        sb.append(", enabled=").append(enabled);
        sb.append(", devicePartsCode=").append(devicePartsCode);
        sb.append(", comment=").append(comment);
        sb.append("]");
        return sb.toString();
    }
}