package com.kthw.pmis.entiy;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 *
 * @mbg.generated do_not_delete_during_merge 2020-04-01 15:10:04
 */
public class PlanCheckSheet {
    /**
     * Database Column Remarks:
     *   tbl_plan_check_sheet.sheet_id: 单据id
     */
    private String sheetId;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_sheet.year: 年份
     */
    private Integer year;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_sheet.month: 月份
     */
    private Integer month;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_sheet.depot_id: 部门id
     */
    private Long depotId;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_sheet.depot_name: 部门名称
     */
    private String depotName;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_sheet.flag: 审核状态
     */
    private Short flag;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_sheet.create_time: 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_sheet.update_time: 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_sheet.remark: 备注
     */
    private String remark;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_sheet.create_user: 创建人
     */
    private String createUser;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_sheet.verify_user: 审核人
     */
    private String verifyUser;
    private Integer total;
    private Integer completeCount;
    private Integer singleMonthCount;
    private Integer biMonthCount;
    private Integer temporaryCount;
    private Integer halfMonthCount;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getHalfMonthCount() {
        return halfMonthCount;
    }

    public void setHalfMonthCount(Integer halfMonthCount) {
        this.halfMonthCount = halfMonthCount;
    }

    public Integer getCompleteCount() {
        return completeCount;
    }

    public void setCompleteCount(Integer completeCount) {
        this.completeCount = completeCount;
    }

    public Integer getSingleMonthCount() {
        return singleMonthCount;
    }

    public void setSingleMonthCount(Integer singleMonthCount) {
        this.singleMonthCount = singleMonthCount;
    }

    public Integer getBiMonthCount() {
        return biMonthCount;
    }

    public void setBiMonthCount(Integer biMonthCount) {
        this.biMonthCount = biMonthCount;
    }

    public Integer getTemporaryCount() {
        return temporaryCount;
    }

    public void setTemporaryCount(Integer temporaryCount) {
        this.temporaryCount = temporaryCount;
    }

    /**
     * @return the value of tbl_plan_check_sheet.sheet_id
     */
    public String getSheetId() {
        return sheetId;
    }

    /**
     * @param sheetId the value for tbl_plan_check_sheet.sheet_id
     */
    public void setSheetId(String sheetId) {
        this.sheetId = sheetId == null ? null : sheetId.trim();
    }

    /**
     * @return the value of tbl_plan_check_sheet.year
     */
    public Integer getYear() {
        return year;
    }

    /**
     * @param year the value for tbl_plan_check_sheet.year
     */
    public void setYear(Integer year) {
        this.year = year ;
    }

    /**
     * @return the value of tbl_plan_check_sheet.month
     */
    public Integer getMonth() {
        return month;
    }

    /**
     * @param month the value for tbl_plan_check_sheet.month
     */
    public void setMonth(Integer month) {
        this.month = month ;
    }

    /**
     * @return the value of tbl_plan_check_sheet.depot_id
     */
    public Long getDepotId() {
        return depotId;
    }

    /**
     * @param depotId the value for tbl_plan_check_sheet.depot_id
     */
    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    /**
     * @return the value of tbl_plan_check_sheet.depot_name
     */
    public String getDepotName() {
        return depotName;
    }

    /**
     * @param depotName the value for tbl_plan_check_sheet.depot_name
     */
    public void setDepotName(String depotName) {
        this.depotName = depotName == null ? null : depotName.trim();
    }

    /**
     * @return the value of tbl_plan_check_sheet.flag
     */
    public Short getFlag() {
        return flag;
    }

    /**
     * @param flag the value for tbl_plan_check_sheet.flag
     */
    public void setFlag(Short flag) {
        this.flag = flag;
    }

    /**
     * @return the value of tbl_plan_check_sheet.create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the value for tbl_plan_check_sheet.create_time
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the value of tbl_plan_check_sheet.update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime the value for tbl_plan_check_sheet.update_time
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return the value of tbl_plan_check_sheet.remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the value for tbl_plan_check_sheet.remark
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * @return the value of tbl_plan_check_sheet.create_user
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * @param createUser the value for tbl_plan_check_sheet.create_user
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    /**
     * @return the value of tbl_plan_check_sheet.verify_user
     */
    public String getVerifyUser() {
        return verifyUser;
    }

    /**
     * @param verifyUser the value for tbl_plan_check_sheet.verify_user
     */
    public void setVerifyUser(String verifyUser) {
        this.verifyUser = verifyUser == null ? null : verifyUser.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", sheetId=").append(sheetId);
        sb.append(", year=").append(year);
        sb.append(", month=").append(month);
        sb.append(", depotId=").append(depotId);
        sb.append(", depotName=").append(depotName);
        sb.append(", flag=").append(flag);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", remark=").append(remark);
        sb.append(", createUser=").append(createUser);
        sb.append(", verifyUser=").append(verifyUser);
        sb.append("]");
        return sb.toString();
    }
}