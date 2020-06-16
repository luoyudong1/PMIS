package com.kthw.pmis.entiy;

/**
 *
 * @mbg.generated do_not_delete_during_merge 2020-06-11 16:12:44
 */
public class DetectScore {
    /**
     * Database Column Remarks:
     *   tbl_detect_score.detect_device_id: 探测站id
     */
    private Integer detectDeviceId;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.detect_device_name: 探测站名称
     */
    private String detectDeviceName;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.detect_device_type: 探测站类型
     */
    private String detectDeviceType;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.year: 年份
     */
    private Integer year;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.month: 月份
     */
    private Integer month;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.stop_time: 超时
     */
    private Float stopTime;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.day_fault_count: 晚上故障次数
     */
    private Integer dayFaultCount;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.night_fault_count: 白天故障次数
     */
    private Integer nightFaultCount;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.total_fault_count: 故障总件数
     */
    private Integer totalFaultCount;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.stop_time_deduction: 停时扣分
     */
    private Float stopTimeDeduction;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.deduction_table_url: 扣分表url
     */
    private String deductionTableUrl;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.table_deduction: 未上传扣分表扣分
     */
    private Float tableDeduction;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.fault_unreport_dedution: 故障漏报扣分
     */
    private Float faultUnreportDedution;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.not_enough_time_dedution1: 设备检修-巡检时间不足扣分
     */
    private Float notEnoughTimeDedution1;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.unreport_dedution1: 设备检修-巡检漏报扣分
     */
    private Float unreportDedution1;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.uncheck_dedution1: 设备检修-巡检漏检扣分
     */
    private Float uncheckDedution1;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.not_enough_time_dedution2: 设备检修-春秋季整修时间不足扣分
     */
    private Float notEnoughTimeDedution2;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.unreport_dedution2: 设备检修-春秋季整修漏报扣分
     */
    private Float unreportDedution2;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.uncheck_dedution2: 设备检修-春秋季整修漏检扣分
     */
    private Float uncheckDedution2;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.not_enough_time_dedution3: 设备检修-项修计划时间不足扣分
     */
    private Float notEnoughTimeDedution3;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.unreport_dedution3: 设备检修-项修计划漏报扣分
     */
    private Float unreportDedution3;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.uncheck_dedution3: 设备检修-项修计划漏检扣分
     */
    private Float uncheckDedution3;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.interrupt_delay_time: 中断延时-累计时间
     */
    private Float interruptDelayTime;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.unreport_dedution4: 中断延时-未提报扣分
     */
    private Float unreportDedution4;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.should_score: 应得分
     */
    private Float shouldScore;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.real_score: 实得分
     */
    private Float realScore;

    /**
     * Database Column Remarks:
     *   tbl_detect_score.rank: 排名
     */
    private Integer rank;

    /**
     * @return the value of tbl_detect_score.detect_device_id
     */
    public Integer getDetectDeviceId() {
        return detectDeviceId;
    }

    /**
     * @param detectDeviceId the value for tbl_detect_score.detect_device_id
     */
    public void setDetectDeviceId(Integer detectDeviceId) {
        this.detectDeviceId = detectDeviceId;
    }

    /**
     * @return the value of tbl_detect_score.detect_device_name
     */
    public String getDetectDeviceName() {
        return detectDeviceName;
    }

    /**
     * @param detectDeviceName the value for tbl_detect_score.detect_device_name
     */
    public void setDetectDeviceName(String detectDeviceName) {
        this.detectDeviceName = detectDeviceName == null ? null : detectDeviceName.trim();
    }

    /**
     * @return the value of tbl_detect_score.detect_device_type
     */
    public String getDetectDeviceType() {
        return detectDeviceType;
    }

    /**
     * @param detectDeviceType the value for tbl_detect_score.detect_device_type
     */
    public void setDetectDeviceType(String detectDeviceType) {
        this.detectDeviceType = detectDeviceType == null ? null : detectDeviceType.trim();
    }

    /**
     * @return the value of tbl_detect_score.year
     */
    public Integer getYear() {
        return year;
    }

    /**
     * @param year the value for tbl_detect_score.year
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * @return the value of tbl_detect_score.month
     */
    public Integer getMonth() {
        return month;
    }

    /**
     * @param month the value for tbl_detect_score.month
     */
    public void setMonth(Integer month) {
        this.month = month;
    }

    /**
     * @return the value of tbl_detect_score.stop_time
     */
    public Float getStopTime() {
        return stopTime;
    }

    /**
     * @param stopTime the value for tbl_detect_score.stop_time
     */
    public void setStopTime(Float stopTime) {
        this.stopTime = stopTime;
    }

    /**
     * @return the value of tbl_detect_score.day_fault_count
     */
    public Integer getDayFaultCount() {
        return dayFaultCount;
    }

    /**
     * @param dayFaultCount the value for tbl_detect_score.day_fault_count
     */
    public void setDayFaultCount(Integer dayFaultCount) {
        this.dayFaultCount = dayFaultCount;
    }

    /**
     * @return the value of tbl_detect_score.night_fault_count
     */
    public Integer getNightFaultCount() {
        return nightFaultCount;
    }

    /**
     * @param nightFaultCount the value for tbl_detect_score.night_fault_count
     */
    public void setNightFaultCount(Integer nightFaultCount) {
        this.nightFaultCount = nightFaultCount;
    }

    /**
     * @return the value of tbl_detect_score.total_fault_count
     */
    public Integer getTotalFaultCount() {
        return totalFaultCount;
    }

    /**
     * @param totalFaultCount the value for tbl_detect_score.total_fault_count
     */
    public void setTotalFaultCount(Integer totalFaultCount) {
        this.totalFaultCount = totalFaultCount;
    }

    /**
     * @return the value of tbl_detect_score.stop_time_deduction
     */
    public Float getStopTimeDeduction() {
        return stopTimeDeduction;
    }

    /**
     * @param stopTimeDeduction the value for tbl_detect_score.stop_time_deduction
     */
    public void setStopTimeDeduction(Float stopTimeDeduction) {
        this.stopTimeDeduction = stopTimeDeduction;
    }

    /**
     * @return the value of tbl_detect_score.deduction_table_url
     */
    public String getDeductionTableUrl() {
        return deductionTableUrl;
    }

    /**
     * @param deductionTableUrl the value for tbl_detect_score.deduction_table_url
     */
    public void setDeductionTableUrl(String deductionTableUrl) {
        this.deductionTableUrl = deductionTableUrl == null ? null : deductionTableUrl.trim();
    }

    /**
     * @return the value of tbl_detect_score.table_deduction
     */
    public Float getTableDeduction() {
        return tableDeduction;
    }

    /**
     * @param tableDeduction the value for tbl_detect_score.table_deduction
     */
    public void setTableDeduction(Float tableDeduction) {
        this.tableDeduction = tableDeduction;
    }

    /**
     * @return the value of tbl_detect_score.fault_unreport_dedution
     */
    public Float getFaultUnreportDedution() {
        return faultUnreportDedution;
    }

    /**
     * @param faultUnreportDedution the value for tbl_detect_score.fault_unreport_dedution
     */
    public void setFaultUnreportDedution(Float faultUnreportDedution) {
        this.faultUnreportDedution = faultUnreportDedution;
    }

    /**
     * @return the value of tbl_detect_score.not_enough_time_dedution1
     */
    public Float getNotEnoughTimeDedution1() {
        return notEnoughTimeDedution1;
    }

    /**
     * @param notEnoughTimeDedution1 the value for tbl_detect_score.not_enough_time_dedution1
     */
    public void setNotEnoughTimeDedution1(Float notEnoughTimeDedution1) {
        this.notEnoughTimeDedution1 = notEnoughTimeDedution1;
    }

    /**
     * @return the value of tbl_detect_score.unreport_dedution1
     */
    public Float getUnreportDedution1() {
        return unreportDedution1;
    }

    /**
     * @param unreportDedution1 the value for tbl_detect_score.unreport_dedution1
     */
    public void setUnreportDedution1(Float unreportDedution1) {
        this.unreportDedution1 = unreportDedution1;
    }

    /**
     * @return the value of tbl_detect_score.uncheck_dedution1
     */
    public Float getUncheckDedution1() {
        return uncheckDedution1;
    }

    /**
     * @param uncheckDedution1 the value for tbl_detect_score.uncheck_dedution1
     */
    public void setUncheckDedution1(Float uncheckDedution1) {
        this.uncheckDedution1 = uncheckDedution1;
    }

    /**
     * @return the value of tbl_detect_score.not_enough_time_dedution2
     */
    public Float getNotEnoughTimeDedution2() {
        return notEnoughTimeDedution2;
    }

    /**
     * @param notEnoughTimeDedution2 the value for tbl_detect_score.not_enough_time_dedution2
     */
    public void setNotEnoughTimeDedution2(Float notEnoughTimeDedution2) {
        this.notEnoughTimeDedution2 = notEnoughTimeDedution2;
    }

    /**
     * @return the value of tbl_detect_score.unreport_dedution2
     */
    public Float getUnreportDedution2() {
        return unreportDedution2;
    }

    /**
     * @param unreportDedution2 the value for tbl_detect_score.unreport_dedution2
     */
    public void setUnreportDedution2(Float unreportDedution2) {
        this.unreportDedution2 = unreportDedution2;
    }

    /**
     * @return the value of tbl_detect_score.uncheck_dedution2
     */
    public Float getUncheckDedution2() {
        return uncheckDedution2;
    }

    /**
     * @param uncheckDedution2 the value for tbl_detect_score.uncheck_dedution2
     */
    public void setUncheckDedution2(Float uncheckDedution2) {
        this.uncheckDedution2 = uncheckDedution2;
    }

    /**
     * @return the value of tbl_detect_score.not_enough_time_dedution3
     */
    public Float getNotEnoughTimeDedution3() {
        return notEnoughTimeDedution3;
    }

    /**
     * @param notEnoughTimeDedution3 the value for tbl_detect_score.not_enough_time_dedution3
     */
    public void setNotEnoughTimeDedution3(Float notEnoughTimeDedution3) {
        this.notEnoughTimeDedution3 = notEnoughTimeDedution3;
    }

    /**
     * @return the value of tbl_detect_score.unreport_dedution3
     */
    public Float getUnreportDedution3() {
        return unreportDedution3;
    }

    /**
     * @param unreportDedution3 the value for tbl_detect_score.unreport_dedution3
     */
    public void setUnreportDedution3(Float unreportDedution3) {
        this.unreportDedution3 = unreportDedution3;
    }

    /**
     * @return the value of tbl_detect_score.uncheck_dedution3
     */
    public Float getUncheckDedution3() {
        return uncheckDedution3;
    }

    /**
     * @param uncheckDedution3 the value for tbl_detect_score.uncheck_dedution3
     */
    public void setUncheckDedution3(Float uncheckDedution3) {
        this.uncheckDedution3 = uncheckDedution3;
    }

    /**
     * @return the value of tbl_detect_score.interrupt_delay_time
     */
    public Float getInterruptDelayTime() {
        return interruptDelayTime;
    }

    /**
     * @param interruptDelayTime the value for tbl_detect_score.interrupt_delay_time
     */
    public void setInterruptDelayTime(Float interruptDelayTime) {
        this.interruptDelayTime = interruptDelayTime;
    }

    /**
     * @return the value of tbl_detect_score.unreport_dedution4
     */
    public Float getUnreportDedution4() {
        return unreportDedution4;
    }

    /**
     * @param unreportDedution4 the value for tbl_detect_score.unreport_dedution4
     */
    public void setUnreportDedution4(Float unreportDedution4) {
        this.unreportDedution4 = unreportDedution4;
    }

    /**
     * @return the value of tbl_detect_score.should_score
     */
    public Float getShouldScore() {
        return shouldScore;
    }

    /**
     * @param shouldScore the value for tbl_detect_score.should_score
     */
    public void setShouldScore(Float shouldScore) {
        this.shouldScore = shouldScore;
    }

    /**
     * @return the value of tbl_detect_score.real_score
     */
    public Float getRealScore() {
        return realScore;
    }

    /**
     * @param realScore the value for tbl_detect_score.real_score
     */
    public void setRealScore(Float realScore) {
        this.realScore = realScore;
    }

    /**
     * @return the value of tbl_detect_score.rank
     */
    public Integer getRank() {
        return rank;
    }

    /**
     * @param rank the value for tbl_detect_score.rank
     */
    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", detectDeviceId=").append(detectDeviceId);
        sb.append(", detectDeviceName=").append(detectDeviceName);
        sb.append(", detectDeviceType=").append(detectDeviceType);
        sb.append(", year=").append(year);
        sb.append(", month=").append(month);
        sb.append(", stopTime=").append(stopTime);
        sb.append(", dayFaultCount=").append(dayFaultCount);
        sb.append(", nightFaultCount=").append(nightFaultCount);
        sb.append(", totalFaultCount=").append(totalFaultCount);
        sb.append(", stopTimeDeduction=").append(stopTimeDeduction);
        sb.append(", deductionTableUrl=").append(deductionTableUrl);
        sb.append(", tableDeduction=").append(tableDeduction);
        sb.append(", faultUnreportDedution=").append(faultUnreportDedution);
        sb.append(", notEnoughTimeDedution1=").append(notEnoughTimeDedution1);
        sb.append(", unreportDedution1=").append(unreportDedution1);
        sb.append(", uncheckDedution1=").append(uncheckDedution1);
        sb.append(", notEnoughTimeDedution2=").append(notEnoughTimeDedution2);
        sb.append(", unreportDedution2=").append(unreportDedution2);
        sb.append(", uncheckDedution2=").append(uncheckDedution2);
        sb.append(", notEnoughTimeDedution3=").append(notEnoughTimeDedution3);
        sb.append(", unreportDedution3=").append(unreportDedution3);
        sb.append(", uncheckDedution3=").append(uncheckDedution3);
        sb.append(", interruptDelayTime=").append(interruptDelayTime);
        sb.append(", unreportDedution4=").append(unreportDedution4);
        sb.append(", shouldScore=").append(shouldScore);
        sb.append(", realScore=").append(realScore);
        sb.append(", rank=").append(rank);
        sb.append("]");
        return sb.toString();
    }
}