package com.kthw.pmis.entiy;

/**
 *
 * @mbg.generated do_not_delete_during_merge 2019-09-06 09:24:30
 */
public class Depot {
  
    private Long depotId;


    private String depotName;


    private Long parentId;

   
    private Short depotLevel;

   
    private Short depotOrder;


    private String depotDesc;


    private Short enabled;


    public Long getDepotId() {
        return depotId;
    }


    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

 
    public String getDepotName() {
        return depotName;
    }

   
    public void setDepotName(String depotName) {
        this.depotName = depotName == null ? null : depotName.trim();
    }

 
    public Long getParentId() {
        return parentId;
    }


    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

 
    public Short getDepotLevel() {
        return depotLevel;
    }


    public void setDepotLevel(Short depotLevel) {
        this.depotLevel = depotLevel;
    }


    public Short getDepotOrder() {
        return depotOrder;
    }


    public void setDepotOrder(Short depotOrder) {
        this.depotOrder = depotOrder;
    }


    public String getDepotDesc() {
        return depotDesc;
    }

  
    public void setDepotDesc(String depotDesc) {
        this.depotDesc = depotDesc == null ? null : depotDesc.trim();
    }

    public Short getEnabled() {
        return enabled;
    }


    public void setEnabled(Short enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", depotId=").append(depotId);
        sb.append(", depotName=").append(depotName);
        sb.append(", parentId=").append(parentId);
        sb.append(", depotLevel=").append(depotLevel);
        sb.append(", depotOrder=").append(depotOrder);
        sb.append(", depotDesc=").append(depotDesc);
        sb.append(", enabled=").append(enabled);
        sb.append("]");
        return sb.toString();
    }
}