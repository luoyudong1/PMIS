package com.kthw.pmis.entiy;

/**
 *
 * @mbg.generated do_not_delete_during_merge 2020-03-17 16:19:20
 */
public class FaultType {
    /**
     * Database Column Remarks:
     *   tbl_fault_type.id: 故障类别id
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   tbl_fault_type.name: 故障类别名称
     */
    private String name;

    /**
     * @return the value of tbl_fault_type.id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the value for tbl_fault_type.id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the value of tbl_fault_type.name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the value for tbl_fault_type.name
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append("]");
        return sb.toString();
    }
}