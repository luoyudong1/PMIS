package com.kthw.pmis.entiy;

/**
 *
 * @mbg.generated do_not_delete_during_merge 2020-03-27 10:15:43
 */
public class LineDepot {
    /**
     * Database Column Remarks:
     *   tbl_line_depot.line_id: 线别id
     */
    private Integer lineId;

    /**
     * Database Column Remarks:
     *   tbl_line_depot.line_name: 线别名称
     */
    private String lineName;

    /**
     * Database Column Remarks:
     *   tbl_line_depot.depot_id: 部门id
     */
    private Integer depotId;

    /**
     * Database Column Remarks:
     *   tbl_line_depot.depot_name: 部门名称
     */
    private String depotName;

    /**
     * @return the value of tbl_line_depot.line_id
     */
    public Integer getLineId() {
        return lineId;
    }

    /**
     * @param lineId the value for tbl_line_depot.line_id
     */
    public void setLineId(Integer lineId) {
        this.lineId = lineId;
    }

    /**
     * @return the value of tbl_line_depot.line_name
     */
    public String getLineName() {
        return lineName;
    }

    /**
     * @param lineName the value for tbl_line_depot.line_name
     */
    public void setLineName(String lineName) {
        this.lineName = lineName == null ? null : lineName.trim();
    }

    /**
     * @return the value of tbl_line_depot.depot_id
     */
    public Integer getDepotId() {
        return depotId;
    }

    /**
     * @param depotId the value for tbl_line_depot.depot_id
     */
    public void setDepotId(Integer depotId) {
        this.depotId = depotId;
    }

    /**
     * @return the value of tbl_line_depot.depot_name
     */
    public String getDepotName() {
        return depotName;
    }

    /**
     * @param depotName the value for tbl_line_depot.depot_name
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
        sb.append(", lineId=").append(lineId);
        sb.append(", lineName=").append(lineName);
        sb.append(", depotId=").append(depotId);
        sb.append(", depotName=").append(depotName);
        sb.append("]");
        return sb.toString();
    }
}