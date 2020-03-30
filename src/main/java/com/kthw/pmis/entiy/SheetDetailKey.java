package com.kthw.pmis.entiy;

/**
 *
 * @mbg.generated do_not_delete_during_merge 2019-08-29 15:30:01
 */
public class SheetDetailKey {
    /**
     * Database Column Remarks:
     *   tbl_sheet_detail.sheet_id:
     */
    private String sheetId;

    /**
     * Database Column Remarks:
     *   tbl_sheet_detail.part_id: 
     */
    private String partCode;

    /**
     * @return the value of tbl_sheet_detail.sheet_id
     */
    public String getSheetId() {
        return sheetId;
    }

    /**
     * @param sheetId the value for tbl_sheet_detail.sheet_id
     */
    public void setSheetId(String sheetId) {
        this.sheetId = sheetId == null ? null : sheetId.trim();
    }

    /**
     * @return the value of tbl_sheet_detail.part_id
     */
    public String getPartCode() {
        return partCode;
    }

    /**
     * @param partId the value for tbl_sheet_detail.part_id
     */
    public void setPartCode(String partCode) {
        this.partCode = partCode == null ? null : partCode.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", sheetId=").append(sheetId);
        sb.append(", partCode=").append(partCode);
        sb.append("]");
        return sb.toString();
    }
}