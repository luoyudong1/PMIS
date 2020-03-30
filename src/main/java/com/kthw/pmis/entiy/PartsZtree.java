package com.kthw.pmis.entiy;

/**
 *
 * @mbg.generated do_not_delete_during_merge 2019-09-30 14:42:02
 */
public class PartsZtree {
    /**
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   tbl_parts_ztree.func_name: 
     */
    private String funcName;

    /**
     * Database Column Remarks:
     *   tbl_parts_ztree.func_url: href
     */
    private String funcUrl;

    /**
     * Database Column Remarks:
     *   tbl_parts_ztree.parent_id:
     */
    private Short parentId;

    /**
     * Database Column Remarks:
     *   tbl_parts_ztree.func_level: 
     */
    private Short funcLevel;

    /**
     * Database Column Remarks:
     *   tbl_parts_ztree.func_desc: 
     */
    private String funcDesc;

    /**
     * Database Column Remarks:
     *   tbl_parts_ztree.show_order:
     */
    private Short showOrder;

    /**
     * Database Column Remarks:
     *   tbl_parts_ztree.enable: 
     */
    private Short enable;

    private String partId;

    private  Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    /**
     * @return the value of tbl_parts_ztree.id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the value for tbl_parts_ztree.id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the value of tbl_parts_ztree.func_name
     */
    public String getFuncName() {
        return funcName;
    }

    /**
     * @param funcName the value for tbl_parts_ztree.func_name
     */
    public void setFuncName(String funcName) {
        this.funcName = funcName == null ? null : funcName.trim();
    }

    /**
     * @return the value of tbl_parts_ztree.func_url
     */
    public String getFuncUrl() {
        return funcUrl;
    }

    /**
     * @param funcUrl the value for tbl_parts_ztree.func_url
     */
    public void setFuncUrl(String funcUrl) {
        this.funcUrl = funcUrl == null ? null : funcUrl.trim();
    }

    /**
     * @return the value of tbl_parts_ztree.parent_id
     */
    public Short getParentId() {
        return parentId;
    }

    /**
     * @param parentId the value for tbl_parts_ztree.parent_id
     */
    public void setParentId(Short parentId) {
        this.parentId = parentId;
    }

    /**
     * @return the value of tbl_parts_ztree.func_level
     */
    public Short getFuncLevel() {
        return funcLevel;
    }

    /**
     * @param funcLevel the value for tbl_parts_ztree.func_level
     */
    public void setFuncLevel(Short funcLevel) {
        this.funcLevel = funcLevel;
    }

    /**
     * @return the value of tbl_parts_ztree.func_desc
     */
    public String getFuncDesc() {
        return funcDesc;
    }

    /**
     * @param funcDesc the value for tbl_parts_ztree.func_desc
     */
    public void setFuncDesc(String funcDesc) {
        this.funcDesc = funcDesc == null ? null : funcDesc.trim();
    }

    /**
     * @return the value of tbl_parts_ztree.show_order
     */
    public Short getShowOrder() {
        return showOrder;
    }

    /**
     * @param showOrder the value for tbl_parts_ztree.show_order
     */
    public void setShowOrder(Short showOrder) {
        this.showOrder = showOrder;
    }

    /**
     * @return the value of tbl_parts_ztree.enable
     */
    public Short getEnable() {
        return enable;
    }

    /**
     * @param enable the value for tbl_parts_ztree.enable
     */
    public void setEnable(Short enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", funcName=").append(funcName);
        sb.append(", funcUrl=").append(funcUrl);
        sb.append(", parentId=").append(parentId);
        sb.append(", funcLevel=").append(funcLevel);
        sb.append(", funcDesc=").append(funcDesc);
        sb.append(", showOrder=").append(showOrder);
        sb.append(", enable=").append(enable);
        sb.append("]");
        return sb.toString();
    }
}