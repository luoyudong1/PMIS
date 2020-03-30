package com.kthw.pmis.entiy;

/**
 *
 * @mbg.generated do_not_delete_during_merge 2020-03-13 15:35:19
 */
public class ResponsibleUnit {
    /**
     * Database Column Remarks:
     *   tbl_responsible_unit.depot_id: 责任单位id
     */
    private Integer depotId;

    /**
     * Database Column Remarks:
     *   tbl_responsible_unit.depot_name: 责任单位名称
     */
    private String depotName;

    /**
     * Database Column Remarks:
     *   tbl_responsible_unit.depot_type: 责任单位类型
     */
    private String depotType;

    /**
     * @return the value of tbl_responsible_unit.depot_id
     */
    public Integer getDepotId() {
        return depotId;
    }

    /**
     * @param depotId the value for tbl_responsible_unit.depot_id
     */
    public void setDepotId(Integer depotId) {
        this.depotId = depotId;
    }

    /**
     * @return the value of tbl_responsible_unit.depot_name
     */
    public String getDepotName() {
        return depotName;
    }

    /**
     * @param depotName the value for tbl_responsible_unit.depot_name
     */
    public void setDepotName(String depotName) {
        this.depotName = depotName == null ? null : depotName.trim();
    }

    /**
     * @return the value of tbl_responsible_unit.depot_type
     */
    public String getDepotType() {
        return depotType;
    }

    /**
     * @param depotType the value for tbl_responsible_unit.depot_type
     */
    public void setDepotType(String depotType) {
        this.depotType = depotType == null ? null : depotType.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", depotId=").append(depotId);
        sb.append(", depotName=").append(depotName);
        sb.append(", depotType=").append(depotType);
        sb.append("]");
        return sb.toString();
    }
}