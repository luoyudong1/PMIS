package com.kthw.pmis.entiy;

/**
 *
 * @mbg.generated do_not_delete_during_merge 2019-09-30 14:22:45
 */
public class DetectParts {
    /**
   
     */
    private Integer detectPartsId;

    /**
     * Database Column Remarks:
     */
    private Integer detectDeviceId;

    /**
     */
    private String partsId;

    /**
     */
    private String partId;

    /**
     */
    private String partsSpecial;

    /**
     */
    private String partsCode;

    /**

     */
    private Short enabled;

    /**

     */
    private Short ifMoved;

    /**
     * @return the value of tbl_detect_parts_dic.detect_parts_id
     */
    public Integer getDetectPartsId() {
        return detectPartsId;
    }

    /**
     * @param detectPartsId the value for tbl_detect_parts_dic.detect_parts_id
     */
    public void setDetectPartsId(Integer detectPartsId) {
        this.detectPartsId = detectPartsId;
    }

    /**
     * @return the value of tbl_detect_parts_dic.detect_device_id
     */
    public Integer getDetectDeviceId() {
        return detectDeviceId;
    }

    /**
     * @param detectDeviceId the value for tbl_detect_parts_dic.detect_device_id
     */
    public void setDetectDeviceId(Integer detectDeviceId) {
        this.detectDeviceId = detectDeviceId;
    }

    /**
     * @return the value of tbl_detect_parts_dic.parts_id
     */
    public String getPartsId() {
        return partsId;
    }

    /**
     * @param partsId the value for tbl_detect_parts_dic.parts_id
     */
    public void setPartsId(String partsId) {
        this.partsId = partsId;
    }

    /**
     * @return the value of tbl_detect_parts_dic.part_id
     */
    public String getPartId() {
        return partId;
    }

    /**
     * @param partId the value for tbl_detect_parts_dic.part_id
     */
    public void setPartId(String partId) {
        this.partId = partId == null ? null : partId.trim();
    }

    /**
     * @return the value of tbl_detect_parts_dic.parts_special
     */
    public String getPartsSpecial() {
        return partsSpecial;
    }

    /**
     * @param partsSpecial the value for tbl_detect_parts_dic.parts_special
     */
    public void setPartsSpecial(String partsSpecial) {
        this.partsSpecial = partsSpecial == null ? null : partsSpecial.trim();
    }

    /**
     * @return the value of tbl_detect_parts_dic.parts_code
     */
    public String getPartsCode() {
        return partsCode;
    }

    /**
     * @param partsCode the value for tbl_detect_parts_dic.parts_code
     */
    public void setPartsCode(String partsCode) {
        this.partsCode = partsCode == null ? null : partsCode.trim();
    }

    /**
     * @return the value of tbl_detect_parts_dic.enabled
     */
    public Short getEnabled() {
        return enabled;
    }

    /**
     * @param enabled the value for tbl_detect_parts_dic.enabled
     */
    public void setEnabled(Short enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the value of tbl_detect_parts_dic.if_moved
     */
    public Short getIfMoved() {
        return ifMoved;
    }

    /**
     * @param ifMoved the value for tbl_detect_parts_dic.if_moved
     */
    public void setIfMoved(Short ifMoved) {
        this.ifMoved = ifMoved;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", detectPartsId=").append(detectPartsId);
        sb.append(", detectDeviceId=").append(detectDeviceId);
        sb.append(", partsId=").append(partsId);
        sb.append(", partId=").append(partId);
        sb.append(", partsSpecial=").append(partsSpecial);
        sb.append(", partsCode=").append(partsCode);
        sb.append(", enabled=").append(enabled);
        sb.append(", ifMoved=").append(ifMoved);
        sb.append("]");
        return sb.toString();
    }
}