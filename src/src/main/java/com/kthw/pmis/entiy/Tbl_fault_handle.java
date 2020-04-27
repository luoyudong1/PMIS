package com.kthw.pmis.entiy;

import java.util.Date;

/**
 *
 * @mbg.generated do_not_delete_during_merge 2020-04-09 14:28:03
 */
public class Tbl_fault_handle {
    /**
     * Database Column Remarks:
     *   tbl_fault_handle.id: 故障预报id
     */
    private String id;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.detect_device_id: 探测站id
     */
    private Integer detectDeviceId;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.detect_device_name: 探测站名称
     */
    private String detectDeviceName;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.detect_device_type: 探测站类别
     */
    private String detectDeviceType;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.hault_start_time: 停机开始时间
     */
    private Date haultStartTime;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.hault_end_time: 停机结束时间
     */
    private Date haultEndTime;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.fault_stop_time: 故障停时
     */
    private String faultStopTime;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.type: 故障日表类别:设备故障，电力故障，通信故障，网络报文故障,其他故障
     */
    private String type;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.fault_level_type: 故障级别：A,B,C
     */
    private String faultLevelType;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.fault_info: 故障现象
     */
    private String faultInfo;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.handle_info: 处理情况
     */
    private String handleInfo;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.forecast_fault_time: 预报时间
     */
    private Date forecastFaultTime;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.segment_duty_user: 段值班员
     */
    private String segmentDutyUser;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.handle_start_time: 故障处理开始时间
     */
    private Date handleStartTime;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.handle_end_time: 故障处理结束时间
     */
    private Date handleEndTime;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.repair_person: 维修人员
     */
    private String repairPerson;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.fault_type: 故障分类
     */
    private String faultType;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.confirm_fault_person: 检测所确认设备故障及天窗申请
     */
    private String confirmFaultPerson;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.complete_flag: 完成标志1:新建，2：提交预报：3预报确认，4故障处理提交，5故障处理确认
     */
    private Short completeFlag;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.telegraph_number: 计划停电时间电报号
     */
    private String telegraphNumber;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.remark: 备注
     */
    private String remark;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.plan_outage_start_time: 计划检修停电开始时间
     */
    private Date planOutageStartTime;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.plan_outage_end_time: 计划检修停电结束时间
     */
    private Date planOutageEndTime;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.notice_time: 通知时间
     */
    private Date noticeTime;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.responsible_unit: 责任单位
     */
    private String responsibleUnit;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.depot_id: 部门id
     */
    private Long depotId;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.depot_name: 部门名称
     */
    private String depotName;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.update_time: 更新时间
     */
    private Date updateTime;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.responsible_user: 责任值班员
     */
    private String responsibleUser;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.notice_user: 通知责任部门责任人
     */
    private String noticeUser;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.responsible_depot: 班组责任部门
     */
    private String responsibleDepot;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.submit_user: 提交人员
     */
    private String submitUser;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.maintenance_time: 维修天窗时间
     */
    private Date maintenanceTime;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.check_info: 现场设备检测情况
     */
    private String checkInfo;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.fault_info_detail: 故障详细描述
     */
    private String faultInfoDetail;

    /**
     * Database Column Remarks:
     *   tbl_fault_handle.segment_depot: 所属段
     */
    private String segmentDepot;

    /**
     * @return the value of tbl_fault_handle.id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the value for tbl_fault_handle.id
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * @return the value of tbl_fault_handle.detect_device_id
     */
    public Integer getDetectDeviceId() {
        return detectDeviceId;
    }

    /**
     * @param detectDeviceId the value for tbl_fault_handle.detect_device_id
     */
    public void setDetectDeviceId(Integer detectDeviceId) {
        this.detectDeviceId = detectDeviceId;
    }

    /**
     * @return the value of tbl_fault_handle.detect_device_name
     */
    public String getDetectDeviceName() {
        return detectDeviceName;
    }

    /**
     * @param detectDeviceName the value for tbl_fault_handle.detect_device_name
     */
    public void setDetectDeviceName(String detectDeviceName) {
        this.detectDeviceName = detectDeviceName == null ? null : detectDeviceName.trim();
    }

    /**
     * @return the value of tbl_fault_handle.detect_device_type
     */
    public String getDetectDeviceType() {
        return detectDeviceType;
    }

    /**
     * @param detectDeviceType the value for tbl_fault_handle.detect_device_type
     */
    public void setDetectDeviceType(String detectDeviceType) {
        this.detectDeviceType = detectDeviceType == null ? null : detectDeviceType.trim();
    }

    /**
     * @return the value of tbl_fault_handle.hault_start_time
     */
    public Date getHaultStartTime() {
        return haultStartTime;
    }

    /**
     * @param haultStartTime the value for tbl_fault_handle.hault_start_time
     */
    public void setHaultStartTime(Date haultStartTime) {
        this.haultStartTime = haultStartTime;
    }

    /**
     * @return the value of tbl_fault_handle.hault_end_time
     */
    public Date getHaultEndTime() {
        return haultEndTime;
    }

    /**
     * @param haultEndTime the value for tbl_fault_handle.hault_end_time
     */
    public void setHaultEndTime(Date haultEndTime) {
        this.haultEndTime = haultEndTime;
    }

    /**
     * @return the value of tbl_fault_handle.fault_stop_time
     */
    public String getFaultStopTime() {
        return faultStopTime;
    }

    /**
     * @param faultStopTime the value for tbl_fault_handle.fault_stop_time
     */
    public void setFaultStopTime(String faultStopTime) {
        this.faultStopTime = faultStopTime == null ? null : faultStopTime.trim();
    }

    /**
     * @return the value of tbl_fault_handle.type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the value for tbl_fault_handle.type
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * @return the value of tbl_fault_handle.fault_level_type
     */
    public String getFaultLevelType() {
        return faultLevelType;
    }

    /**
     * @param faultLevelType the value for tbl_fault_handle.fault_level_type
     */
    public void setFaultLevelType(String faultLevelType) {
        this.faultLevelType = faultLevelType == null ? null : faultLevelType.trim();
    }

    /**
     * @return the value of tbl_fault_handle.fault_info
     */
    public String getFaultInfo() {
        return faultInfo;
    }

    /**
     * @param faultInfo the value for tbl_fault_handle.fault_info
     */
    public void setFaultInfo(String faultInfo) {
        this.faultInfo = faultInfo == null ? null : faultInfo.trim();
    }

    /**
     * @return the value of tbl_fault_handle.handle_info
     */
    public String getHandleInfo() {
        return handleInfo;
    }

    /**
     * @param handleInfo the value for tbl_fault_handle.handle_info
     */
    public void setHandleInfo(String handleInfo) {
        this.handleInfo = handleInfo == null ? null : handleInfo.trim();
    }

    /**
     * @return the value of tbl_fault_handle.forecast_fault_time
     */
    public Date getForecastFaultTime() {
        return forecastFaultTime;
    }

    /**
     * @param forecastFaultTime the value for tbl_fault_handle.forecast_fault_time
     */
    public void setForecastFaultTime(Date forecastFaultTime) {
        this.forecastFaultTime = forecastFaultTime;
    }

    /**
     * @return the value of tbl_fault_handle.segment_duty_user
     */
    public String getSegmentDutyUser() {
        return segmentDutyUser;
    }

    /**
     * @param segmentDutyUser the value for tbl_fault_handle.segment_duty_user
     */
    public void setSegmentDutyUser(String segmentDutyUser) {
        this.segmentDutyUser = segmentDutyUser == null ? null : segmentDutyUser.trim();
    }

    /**
     * @return the value of tbl_fault_handle.handle_start_time
     */
    public Date getHandleStartTime() {
        return handleStartTime;
    }

    /**
     * @param handleStartTime the value for tbl_fault_handle.handle_start_time
     */
    public void setHandleStartTime(Date handleStartTime) {
        this.handleStartTime = handleStartTime;
    }

    /**
     * @return the value of tbl_fault_handle.handle_end_time
     */
    public Date getHandleEndTime() {
        return handleEndTime;
    }

    /**
     * @param handleEndTime the value for tbl_fault_handle.handle_end_time
     */
    public void setHandleEndTime(Date handleEndTime) {
        this.handleEndTime = handleEndTime;
    }

    /**
     * @return the value of tbl_fault_handle.repair_person
     */
    public String getRepairPerson() {
        return repairPerson;
    }

    /**
     * @param repairPerson the value for tbl_fault_handle.repair_person
     */
    public void setRepairPerson(String repairPerson) {
        this.repairPerson = repairPerson == null ? null : repairPerson.trim();
    }

    /**
     * @return the value of tbl_fault_handle.fault_type
     */
    public String getFaultType() {
        return faultType;
    }

    /**
     * @param faultType the value for tbl_fault_handle.fault_type
     */
    public void setFaultType(String faultType) {
        this.faultType = faultType == null ? null : faultType.trim();
    }

    /**
     * @return the value of tbl_fault_handle.confirm_fault_person
     */
    public String getConfirmFaultPerson() {
        return confirmFaultPerson;
    }

    /**
     * @param confirmFaultPerson the value for tbl_fault_handle.confirm_fault_person
     */
    public void setConfirmFaultPerson(String confirmFaultPerson) {
        this.confirmFaultPerson = confirmFaultPerson == null ? null : confirmFaultPerson.trim();
    }

    /**
     * @return the value of tbl_fault_handle.complete_flag
     */
    public Short getCompleteFlag() {
        return completeFlag;
    }

    /**
     * @param completeFlag the value for tbl_fault_handle.complete_flag
     */
    public void setCompleteFlag(Short completeFlag) {
        this.completeFlag = completeFlag;
    }

    /**
     * @return the value of tbl_fault_handle.telegraph_number
     */
    public String getTelegraphNumber() {
        return telegraphNumber;
    }

    /**
     * @param telegraphNumber the value for tbl_fault_handle.telegraph_number
     */
    public void setTelegraphNumber(String telegraphNumber) {
        this.telegraphNumber = telegraphNumber == null ? null : telegraphNumber.trim();
    }

    /**
     * @return the value of tbl_fault_handle.remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the value for tbl_fault_handle.remark
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * @return the value of tbl_fault_handle.plan_outage_start_time
     */
    public Date getPlanOutageStartTime() {
        return planOutageStartTime;
    }

    /**
     * @param planOutageStartTime the value for tbl_fault_handle.plan_outage_start_time
     */
    public void setPlanOutageStartTime(Date planOutageStartTime) {
        this.planOutageStartTime = planOutageStartTime;
    }

    /**
     * @return the value of tbl_fault_handle.plan_outage_end_time
     */
    public Date getPlanOutageEndTime() {
        return planOutageEndTime;
    }

    /**
     * @param planOutageEndTime the value for tbl_fault_handle.plan_outage_end_time
     */
    public void setPlanOutageEndTime(Date planOutageEndTime) {
        this.planOutageEndTime = planOutageEndTime;
    }

    /**
     * @return the value of tbl_fault_handle.notice_time
     */
    public Date getNoticeTime() {
        return noticeTime;
    }

    /**
     * @param noticeTime the value for tbl_fault_handle.notice_time
     */
    public void setNoticeTime(Date noticeTime) {
        this.noticeTime = noticeTime;
    }

    /**
     * @return the value of tbl_fault_handle.responsible_unit
     */
    public String getResponsibleUnit() {
        return responsibleUnit;
    }

    /**
     * @param responsibleUnit the value for tbl_fault_handle.responsible_unit
     */
    public void setResponsibleUnit(String responsibleUnit) {
        this.responsibleUnit = responsibleUnit == null ? null : responsibleUnit.trim();
    }

    /**
     * @return the value of tbl_fault_handle.depot_id
     */
    public Long getDepotId() {
        return depotId;
    }

    /**
     * @param depotId the value for tbl_fault_handle.depot_id
     */
    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    /**
     * @return the value of tbl_fault_handle.depot_name
     */
    public String getDepotName() {
        return depotName;
    }

    /**
     * @param depotName the value for tbl_fault_handle.depot_name
     */
    public void setDepotName(String depotName) {
        this.depotName = depotName == null ? null : depotName.trim();
    }

    /**
     * @return the value of tbl_fault_handle.update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime the value for tbl_fault_handle.update_time
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return the value of tbl_fault_handle.responsible_user
     */
    public String getResponsibleUser() {
        return responsibleUser;
    }

    /**
     * @param responsibleUser the value for tbl_fault_handle.responsible_user
     */
    public void setResponsibleUser(String responsibleUser) {
        this.responsibleUser = responsibleUser == null ? null : responsibleUser.trim();
    }

    /**
     * @return the value of tbl_fault_handle.notice_user
     */
    public String getNoticeUser() {
        return noticeUser;
    }

    /**
     * @param noticeUser the value for tbl_fault_handle.notice_user
     */
    public void setNoticeUser(String noticeUser) {
        this.noticeUser = noticeUser == null ? null : noticeUser.trim();
    }

    /**
     * @return the value of tbl_fault_handle.responsible_depot
     */
    public String getResponsibleDepot() {
        return responsibleDepot;
    }

    /**
     * @param responsibleDepot the value for tbl_fault_handle.responsible_depot
     */
    public void setResponsibleDepot(String responsibleDepot) {
        this.responsibleDepot = responsibleDepot == null ? null : responsibleDepot.trim();
    }

    /**
     * @return the value of tbl_fault_handle.submit_user
     */
    public String getSubmitUser() {
        return submitUser;
    }

    /**
     * @param submitUser the value for tbl_fault_handle.submit_user
     */
    public void setSubmitUser(String submitUser) {
        this.submitUser = submitUser == null ? null : submitUser.trim();
    }

    /**
     * @return the value of tbl_fault_handle.maintenance_time
     */
    public Date getMaintenanceTime() {
        return maintenanceTime;
    }

    /**
     * @param maintenanceTime the value for tbl_fault_handle.maintenance_time
     */
    public void setMaintenanceTime(Date maintenanceTime) {
        this.maintenanceTime = maintenanceTime;
    }

    /**
     * @return the value of tbl_fault_handle.check_info
     */
    public String getCheckInfo() {
        return checkInfo;
    }

    /**
     * @param checkInfo the value for tbl_fault_handle.check_info
     */
    public void setCheckInfo(String checkInfo) {
        this.checkInfo = checkInfo == null ? null : checkInfo.trim();
    }

    /**
     * @return the value of tbl_fault_handle.fault_info_detail
     */
    public String getFaultInfoDetail() {
        return faultInfoDetail;
    }

    /**
     * @param faultInfoDetail the value for tbl_fault_handle.fault_info_detail
     */
    public void setFaultInfoDetail(String faultInfoDetail) {
        this.faultInfoDetail = faultInfoDetail == null ? null : faultInfoDetail.trim();
    }

    /**
     * @return the value of tbl_fault_handle.segment_depot
     */
    public String getSegmentDepot() {
        return segmentDepot;
    }

    /**
     * @param segmentDepot the value for tbl_fault_handle.segment_depot
     */
    public void setSegmentDepot(String segmentDepot) {
        this.segmentDepot = segmentDepot == null ? null : segmentDepot.trim();
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
        sb.append(", haultStartTime=").append(haultStartTime);
        sb.append(", haultEndTime=").append(haultEndTime);
        sb.append(", faultStopTime=").append(faultStopTime);
        sb.append(", type=").append(type);
        sb.append(", faultLevelType=").append(faultLevelType);
        sb.append(", faultInfo=").append(faultInfo);
        sb.append(", handleInfo=").append(handleInfo);
        sb.append(", forecastFaultTime=").append(forecastFaultTime);
        sb.append(", segmentDutyUser=").append(segmentDutyUser);
        sb.append(", handleStartTime=").append(handleStartTime);
        sb.append(", handleEndTime=").append(handleEndTime);
        sb.append(", repairPerson=").append(repairPerson);
        sb.append(", faultType=").append(faultType);
        sb.append(", confirmFaultPerson=").append(confirmFaultPerson);
        sb.append(", completeFlag=").append(completeFlag);
        sb.append(", telegraphNumber=").append(telegraphNumber);
        sb.append(", remark=").append(remark);
        sb.append(", planOutageStartTime=").append(planOutageStartTime);
        sb.append(", planOutageEndTime=").append(planOutageEndTime);
        sb.append(", noticeTime=").append(noticeTime);
        sb.append(", responsibleUnit=").append(responsibleUnit);
        sb.append(", depotId=").append(depotId);
        sb.append(", depotName=").append(depotName);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", responsibleUser=").append(responsibleUser);
        sb.append(", noticeUser=").append(noticeUser);
        sb.append(", responsibleDepot=").append(responsibleDepot);
        sb.append(", submitUser=").append(submitUser);
        sb.append(", maintenanceTime=").append(maintenanceTime);
        sb.append(", checkInfo=").append(checkInfo);
        sb.append(", faultInfoDetail=").append(faultInfoDetail);
        sb.append(", segmentDepot=").append(segmentDepot);
        sb.append("]");
        return sb.toString();
    }
}