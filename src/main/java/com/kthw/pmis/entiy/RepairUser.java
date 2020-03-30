package com.kthw.pmis.entiy;

/**
 *
 * @mbg.generated do_not_delete_during_merge 2020-03-13 15:35:19
 */
public class RepairUser {
    /**
     * Database Column Remarks:
     *   tbl_repair_user.id: 维修工id
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   tbl_repair_user.name: 维修工名称
     */
    private String name;

    /**
     * Database Column Remarks:
     *   tbl_repair_user.depot_id: 所属部门tbl_depot_dict id
     */
    private Integer depotId;

    /**
     * Database Column Remarks:
     *   tbl_repair_user.depot_name: 所属部门名称
     */
    private String depotName;

    /**
     * @return the value of tbl_repair_user.id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the value for tbl_repair_user.id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the value of tbl_repair_user.name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the value for tbl_repair_user.name
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * @return the value of tbl_repair_user.depot_id
     */
    public Integer getDepotId() {
        return depotId;
    }

    /**
     * @param depotId the value for tbl_repair_user.depot_id
     */
    public void setDepotId(Integer depotId) {
        this.depotId = depotId;
    }

    /**
     * @return the value of tbl_repair_user.depot_name
     */
    public String getDepotName() {
        return depotName;
    }

    /**
     * @param depotName the value for tbl_repair_user.depot_name
     */
    public void setDepotName(String depotName) {
        this.depotName = depotName == null ? null : depotName.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", depotId=").append(depotId);
        sb.append(", depotName=").append(depotName);
        sb.append("]");
        return sb.toString();
    }
}