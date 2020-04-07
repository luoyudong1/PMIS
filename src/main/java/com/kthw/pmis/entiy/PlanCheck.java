package com.kthw.pmis.entiy;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 *
 * @mbg.generated do_not_delete_during_merge 2020-04-01 16:52:07
 */
public class PlanCheck {
    /**
     * Database Column Remarks:
     *   tbl_plan_check_detect.id: 检修计划id
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_detect.detect_device_id: 探测站id
     */
    private Integer detectDeviceId;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_detect.detect_device_name: 探测站名称
     */
    private String detectDeviceName;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_detect.detect_device_type: 探测站类别
     */
    private String detectDeviceType;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_detect.plan_time: 计划检修时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planTime;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_detect.start_time: 检修开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_detect.end_time: 检修结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_detect.check_record: 检修记录
     */
    private String checkRecord;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_detect.plan_type: 检修类型：日检、半月检、月检、春秋检
     */
    private String planType;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_detect.remark: 备注
     */
    private String remark;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_detect.create_time: 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_detect.update_time: 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_detect.create_user: 创建人
     */
    private String createUser;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_detect.verify_user: 审核人
     */
    private String verifyUser;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_detect.delay: 是否延期
     */
    private String delay;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_detect.status: 状态：新建、审核、待检修、检修审核、检修完成，过期，未完成审核
     */
    private Short status;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_detect.delay_count: 延期次数
     */
    private Short delayCount;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_detect.next_plan_enable: 是否开启下次计划检修
     */
    private String nextPlanEnable;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_detect.check_user: 检修人
     */
    private String checkUser;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_detect.overdue: 是否逾期
     */
    private String overdue;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_detect.overdue_reason: 逾期原因
     */
    private String overdueReason;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_detect.depot_id: 部门id
     */
    private Long depotId;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_detect.depot_name: 部门名称
     */
    private String depotName;

    /**
     * Database Column Remarks:
     *   tbl_plan_check_detect.sheet_id: 单据id
     */
    private String sheetId;

    /**
     * @return the value of tbl_plan_check_detect.id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the value for tbl_plan_check_detect.id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the value of tbl_plan_check_detect.detect_device_id
     */
    public Integer getDetectDeviceId() {
        return detectDeviceId;
    }

    /**
     * @param detectDeviceId the value for tbl_plan_check_detect.detect_device_id
     */
    public void setDetectDeviceId(Integer detectDeviceId) {
        this.detectDeviceId = detectDeviceId;
    }

    /**
     * @return the value of tbl_plan_check_detect.detect_device_name
     */
    public String getDetectDeviceName() {
        return detectDeviceName;
    }

    /**
     * @param detectDeviceName the value for tbl_plan_check_detect.detect_device_name
     */
    public void setDetectDeviceName(String detectDeviceName) {
        this.detectDeviceName = detectDeviceName == null ? null : detectDeviceName.trim();
    }

    /**
     * @return the value of tbl_plan_check_detect.detect_device_type
     */
    public String getDetectDeviceType() {
        return detectDeviceType;
    }

    /**
     * @param detectDeviceType the value for tbl_plan_check_detect.detect_device_type
     */
    public void setDetectDeviceType(String detectDeviceType) {
        this.detectDeviceType = detectDeviceType == null ? null : detectDeviceType.trim();
    }

    /**
     * @return the value of tbl_plan_check_detect.plan_time
     */
    public Date getPlanTime() {
        return planTime;
    }

    /**
     * @param planTime the value for tbl_plan_check_detect.plan_time
     */
    public void setPlanTime(Date planTime) {
        this.planTime = planTime;
    }

    /**
     * @return the value of tbl_plan_check_detect.start_time
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the value for tbl_plan_check_detect.start_time
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the value of tbl_plan_check_detect.end_time
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the value for tbl_plan_check_detect.end_time
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the value of tbl_plan_check_detect.check_record
     */
    public String getCheckRecord() {
        return checkRecord;
    }

    /**
     * @param checkRecord the value for tbl_plan_check_detect.check_record
     */
    public void setCheckRecord(String checkRecord) {
        this.checkRecord = checkRecord == null ? null : checkRecord.trim();
    }

    /**
     * @return the value of tbl_plan_check_detect.plan_type
     */
    public String getPlanType() {
        return planType;
    }

    /**
     * @param planType the value for tbl_plan_check_detect.plan_type
     */
    public void setPlanType(String planType) {
        this.planType = planType == null ? null : planType.trim();
    }

    /**
     * @return the value of tbl_plan_check_detect.remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the value for tbl_plan_check_detect.remark
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * @return the value of tbl_plan_check_detect.create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the value for tbl_plan_check_detect.create_time
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the value of tbl_plan_check_detect.update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime the value for tbl_plan_check_detect.update_time
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return the value of tbl_plan_check_detect.create_user
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * @param createUser the value for tbl_plan_check_detect.create_user
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    /**
     * @return the value of tbl_plan_check_detect.verify_user
     */
    public String getVerifyUser() {
        return verifyUser;
    }

    /**
     * @param verifyUser the value for tbl_plan_check_detect.verify_user
     */
    public void setVerifyUser(String verifyUser) {
        this.verifyUser = verifyUser == null ? null : verifyUser.trim();
    }

    /**
     * @return the value of tbl_plan_check_detect.delay
     */
    public String getDelay() {
        return delay;
    }

    /**
     * @param delay the value for tbl_plan_check_detect.delay
     */
    public void setDelay(String delay) {
        this.delay = delay == null ? null : delay.trim();
    }

    /**
     * @return the value of tbl_plan_check_detect.status
     */
    public Short getStatus() {
        return status;
    }

    /**
     * @param status the value for tbl_plan_check_detect.status
     */
    public void setStatus(Short status) {
        this.status = status;
    }

    /**
     * @return the value of tbl_plan_check_detect.delay_count
     */
    public Short getDelayCount() {
        return delayCount;
    }

    /**
     * @param delayCount the value for tbl_plan_check_detect.delay_count
     */
    public void setDelayCount(Short delayCount) {
        this.delayCount = delayCount;
    }

    /**
     * @return the value of tbl_plan_check_detect.next_plan_enable
     */
    public String getNextPlanEnable() {
        return nextPlanEnable;
    }

    /**
     * @param nextPlanEnable the value for tbl_plan_check_detect.next_plan_enable
     */
    public void setNextPlanEnable(String nextPlanEnable) {
        this.nextPlanEnable = nextPlanEnable == null ? null : nextPlanEnable.trim();
    }

    /**
     * @return the value of tbl_plan_check_detect.check_user
     */
    public String getCheckUser() {
        return checkUser;
    }

    /**
     * @param checkUser the value for tbl_plan_check_detect.check_user
     */
    public void setCheckUser(String checkUser) {
        this.checkUser = checkUser == null ? null : checkUser.trim();
    }

    /**
     * @return the value of tbl_plan_check_detect.overdue
     */
    public String getOverdue() {
        return overdue;
    }

    /**
     * @param overdue the value for tbl_plan_check_detect.overdue
     */
    public void setOverdue(String overdue) {
        this.overdue = overdue == null ? null : overdue.trim();
    }

    /**
     * @return the value of tbl_plan_check_detect.overdue_reason
     */
    public String getOverdueReason() {
        return overdueReason;
    }

    /**
     * @param overdueReason the value for tbl_plan_check_detect.overdue_reason
     */
    public void setOverdueReason(String overdueReason) {
        this.overdueReason = overdueReason == null ? null : overdueReason.trim();
    }

    /**
     * @return the value of tbl_plan_check_detect.depot_id
     */
    public Long getDepotId() {
        return depotId;
    }

    /**
     * @param depotId the value for tbl_plan_check_detect.depot_id
     */
    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    /**
     * @return the value of tbl_plan_check_detect.depot_name
     */
    public String getDepotName() {
        return depotName;
    }

    /**
     * @param depotName the value for tbl_plan_check_detect.depot_name
     */
    public void setDepotName(String depotName) {
        this.depotName = depotName == null ? null : depotName.trim();
    }

    /**
     * @return the value of tbl_plan_check_detect.sheet_id
     */
    public String getSheetId() {
        return sheetId;
    }

    /**
     * @param sheetId the value for tbl_plan_check_detect.sheet_id
     */
    public void setSheetId(String sheetId) {
        this.sheetId = sheetId == null ? null : sheetId.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", detectDeviceId=").append(detectDeviceId);
        sb.append(", detectDeviceName=").append(detectDeviceName);
        sb.append(", detectDeviceType=").append(detectDeviceType);
        sb.append(", planTime=").append(planTime);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", checkRecord=").append(checkRecord);
        sb.append(", planType=").append(planType);
        sb.append(", remark=").append(remark);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createUser=").append(createUser);
        sb.append(", verifyUser=").append(verifyUser);
        sb.append(", delay=").append(delay);
        sb.append(", status=").append(status);
        sb.append(", delayCount=").append(delayCount);
        sb.append(", nextPlanEnable=").append(nextPlanEnable);
        sb.append(", checkUser=").append(checkUser);
        sb.append(", overdue=").append(overdue);
        sb.append(", overdueReason=").append(overdueReason);
        sb.append(", depotId=").append(depotId);
        sb.append(", depotName=").append(depotName);
        sb.append(", sheetId=").append(sheetId);
        sb.append("]");
        return sb.toString();
    }
}