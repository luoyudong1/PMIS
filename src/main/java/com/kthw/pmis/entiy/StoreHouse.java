package com.kthw.pmis.entiy;

/**
 *
 * @mbg.generated do_not_delete_during_merge 2019-09-24 14:34:23
 */
public class StoreHouse {
    /**
     * Database Column Remarks:
     *   tbl_storehouse_dict.storehouse_id:
     */
    private Short storehouseId;

    /**
     * Database Column Remarks:
     *   tbl_storehouse_dict.storehouse_name:
     */
    private String storehouseName;

    /**
     * Database Column Remarks:
     *   tbl_storehouse_dict.depot_id: 
     */
    private Short depotId;

    /**
     * Database Column Remarks:
     *   tbl_storehouse_dict.type: 
     */
    private Short type;

    /**
     * Database Column Remarks:
     *   tbl_storehouse_dict.enabled:
     */
    private Short enabled;

    /**
     * Database Column Remarks:
     *   tbl_storehouse_dict.extr_storehouse_id: 
     */
    private Short extrStorehouseId;

    /**
     * @return the value of tbl_storehouse_dict.storehouse_id
     */
    public Short getStorehouseId() {
        return storehouseId;
    }

    /**
     * @param storehouseId the value for tbl_storehouse_dict.storehouse_id
     */
    public void setStorehouseId(Short storehouseId) {
        this.storehouseId = storehouseId;
    }

    /**
     * @return the value of tbl_storehouse_dict.storehouse_name
     */
    public String getStorehouseName() {
        return storehouseName;
    }

    /**
     * @param storehouseName the value for tbl_storehouse_dict.storehouse_name
     */
    public void setStorehouseName(String storehouseName) {
        this.storehouseName = storehouseName == null ? null : storehouseName.trim();
    }

    /**
     * @return the value of tbl_storehouse_dict.depot_id
     */
    public short getDepotId() {
        return depotId;
    }

    /**
     * @param depotId the value for tbl_storehouse_dict.depot_id
     */
    public void setDepotId(short depotId) {
        this.depotId = depotId ;
    }

    /**
     * @return the value of tbl_storehouse_dict.type
     */
    public Short getType() {
        return type;
    }

    /**
     * @param type the value for tbl_storehouse_dict.type
     */
    public void setType(Short type) {
        this.type = type;
    }

    /**
     * @return the value of tbl_storehouse_dict.enabled
     */
    public Short getEnabled() {
        return enabled;
    }

    /**
     * @param enabled the value for tbl_storehouse_dict.enabled
     */
    public void setEnabled(Short enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the value of tbl_storehouse_dict.extr_storehouse_id
     */
    public Short getExtrStorehouseId() {
        return extrStorehouseId;
    }

    /**
     * @param extrStorehouseId the value for tbl_storehouse_dict.extr_storehouse_id
     */
    public void setExtrStorehouseId(Short extrStorehouseId) {
        this.extrStorehouseId = extrStorehouseId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", storehouseId=").append(storehouseId);
        sb.append(", storehouseName=").append(storehouseName);
        sb.append(", depotId=").append(depotId);
        sb.append(", type=").append(type);
        sb.append(", enabled=").append(enabled);
        sb.append(", extrStorehouseId=").append(extrStorehouseId);
        sb.append("]");
        return sb.toString();
    }
}