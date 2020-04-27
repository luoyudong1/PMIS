package com.kthw.pmis.entiy;

/**
 *
 * @mbg.generated do_not_delete_during_merge 2019-10-28 09:05:08
 */
public class PartIdConfig {
    /**
     * Database Column Remarks:
     *   tbl_part_id_config.depot_id: 部门id
     */
    private Long depotId;

    /**
     * Database Column Remarks:
     *   tbl_part_id_config.supplier_name: 厂家名称
     */
    private String supplierName;

    /**
     * Database Column Remarks:
     *   tbl_part_id_config.device_model_name: 型号
     */
    private String deviceModelName;

    /**
     * Database Column Remarks:
     *   tbl_part_id_config.min_part_id: 最小part_id序列号
     */
    private String minPartId;

    /**
     * Database Column Remarks:
     *   tbl_part_id_config.max_part_id: 最大part_id序列号
     */
    private String maxPartId;

    /**
     * @return the value of tbl_part_id_config.depot_id
     */
    public Long getDepotId() {
        return depotId;
    }

    /**
     * @param depotId the value for tbl_part_id_config.depot_id
     */
    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    /**
     * @return the value of tbl_part_id_config.supplier_name
     */
    public String getSupplierName() {
        return supplierName;
    }

    /**
     * @param supplierName the value for tbl_part_id_config.supplier_name
     */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName == null ? null : supplierName.trim();
    }

    /**
     * @return the value of tbl_part_id_config.device_model_name
     */
    public String getDeviceModelName() {
        return deviceModelName;
    }

    /**
     * @param deviceModelName the value for tbl_part_id_config.device_model_name
     */
    public void setDeviceModelName(String deviceModelName) {
        this.deviceModelName = deviceModelName == null ? null : deviceModelName.trim();
    }

    /**
     * @return the value of tbl_part_id_config.min_part_id
     */
    public String getMinPartId() {
        return minPartId;
    }

    /**
     * @param minPartId the value for tbl_part_id_config.min_part_id
     */
    public void setMinPartId(String minPartId) {
        this.minPartId = minPartId == null ? null : minPartId.trim();
    }

    /**
     * @return the value of tbl_part_id_config.max_part_id
     */
    public String getMaxPartId() {
        return maxPartId;
    }

    /**
     * @param maxPartId the value for tbl_part_id_config.max_part_id
     */
    public void setMaxPartId(String maxPartId) {
        this.maxPartId = maxPartId == null ? null : maxPartId.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", depotId=").append(depotId);
        sb.append(", supplierName=").append(supplierName);
        sb.append(", deviceModelName=").append(deviceModelName);
        sb.append(", minPartId=").append(minPartId);
        sb.append(", maxPartId=").append(maxPartId);
        sb.append("]");
        return sb.toString();
    }
}