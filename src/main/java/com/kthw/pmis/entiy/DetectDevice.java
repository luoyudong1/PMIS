package com.kthw.pmis.entiy;

import java.util.Date;

/**
 *
 * @mbg.generated do_not_delete_during_merge 2019-09-30 13:48:12
 */
public class DetectDevice {

    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.detect_device_id: 探测站id
     */
    private Integer detectDeviceId;

    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.detect_device_name: 探测站设备名称
     */
    private String detectDeviceName;

    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.depot_id: 所属部门ID
     */
    private Long depotId;

    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.device_type_id: 设备类型ID
     */
    private Short deviceTypeId;

    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.supplier_id: 生产厂家ID
     */
    private Short supplierId;

    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.device_model_id: 设备型号ID
     */
    private Short deviceModelId;

    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.enabled: 是否可用，1：可用，0：不可用
     */
    private Short enabled;

    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.storehouse_id: 探测站仓库id（对应storehouse表）
     */
    private Short storehouseId;

    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.create_time: 添加时间
     */
    private Date createTime;

    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.update_time: 修改时间
     */
    private Date updateTime;

    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.user_name: 三包责任人
     */
    private String userName;

    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.remark: 备注
     */
    private String remark;

    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.line_name: 线别名称
     */
    private String lineName;

    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.electric_type: 供电方式
     */
    private String electricType;

    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.electric_name: 供电设施维护单位
     */
    private String electricName;

    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.net_connect_type: 长途联网方式
     */
    private String netConnectType;

    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.net_connect_name: 通信设施维护单位
     */
    private String netConnectName;

    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.approval_doc: 设备投产批复文件电子版（word)
     */
    private String approvalDoc;

    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.location_pic: 设备安装位置图(CAD）
     */
    private String locationPic;

    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.trackside_pic: 轨边照片（jpg）
     */
    private String tracksidePic;

    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.outdoor_pic: 探测站室外照片
     */
    private String outdoorPic;

    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.indoor_pic: 探测站室内照片（JPG)
     */
    private String indoorPic;

    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.interior_pic: 室内布局图（JPG)
     */
    private String interiorPic;

    /**
     * Database Column Remarks:
     *   tbl_detect_device_dict.device_location: 安装位置
     */
    private String deviceLocation;
    private String planCheckType;
    private Short  planCheckEnable;

    public String getPlanCheckType() {
        return planCheckType;
    }

    public void setPlanCheckType(String planCheckType) {
        this.planCheckType = planCheckType;
    }

    public Short getPlanCheckEnable() {
        return planCheckEnable;
    }

    public void setPlanCheckEnable(Short planCheckEnable) {
        this.planCheckEnable = planCheckEnable;
    }

    /**
     * @return the value of tbl_detect_device_dict.detect_device_id
     */
    public Integer getDetectDeviceId() {
        return detectDeviceId;
    }

    /**
     * @param detectDeviceId the value for tbl_detect_device_dict.detect_device_id
     */
    public void setDetectDeviceId(Integer detectDeviceId) {
        this.detectDeviceId = detectDeviceId;
    }

    /**
     * @return the value of tbl_detect_device_dict.detect_device_name
     */
    public String getDetectDeviceName() {
        return detectDeviceName;
    }

    /**
     * @param detectDeviceName the value for tbl_detect_device_dict.detect_device_name
     */
    public void setDetectDeviceName(String detectDeviceName) {
        this.detectDeviceName = detectDeviceName == null ? null : detectDeviceName.trim();
    }

    /**
     * @return the value of tbl_detect_device_dict.depot_id
     */
    public Long getDepotId() {
        return depotId;
    }

    /**
     * @param depotId the value for tbl_detect_device_dict.depot_id
     */
    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    /**
     * @return the value of tbl_detect_device_dict.device_type_id
     */
    public Short getDeviceTypeId() {
        return deviceTypeId;
    }

    /**
     * @param deviceTypeId the value for tbl_detect_device_dict.device_type_id
     */
    public void setDeviceTypeId(Short deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    /**
     * @return the value of tbl_detect_device_dict.supplier_id
     */
    public Short getSupplierId() {
        return supplierId;
    }

    /**
     * @param supplierId the value for tbl_detect_device_dict.supplier_id
     */
    public void setSupplierId(Short supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * @return the value of tbl_detect_device_dict.device_model_id
     */
    public Short getDeviceModelId() {
        return deviceModelId;
    }

    /**
     * @param deviceModelId the value for tbl_detect_device_dict.device_model_id
     */
    public void setDeviceModelId(Short deviceModelId) {
        this.deviceModelId = deviceModelId;
    }

    /**
     * @return the value of tbl_detect_device_dict.enabled
     */
    public Short getEnabled() {
        return enabled;
    }

    /**
     * @param enabled the value for tbl_detect_device_dict.enabled
     */
    public void setEnabled(Short enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the value of tbl_detect_device_dict.storehouse_id
     */
    public Short getStorehouseId() {
        return storehouseId;
    }

    /**
     * @param storehouseId the value for tbl_detect_device_dict.storehouse_id
     */
    public void setStorehouseId(Short storehouseId) {
        this.storehouseId = storehouseId;
    }

    /**
     * @return the value of tbl_detect_device_dict.create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the value for tbl_detect_device_dict.create_time
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the value of tbl_detect_device_dict.update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime the value for tbl_detect_device_dict.update_time
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return the value of tbl_detect_device_dict.user_name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the value for tbl_detect_device_dict.user_name
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * @return the value of tbl_detect_device_dict.remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the value for tbl_detect_device_dict.remark
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * @return the value of tbl_detect_device_dict.line_name
     */
    public String getLineName() {
        return lineName;
    }

    /**
     * @param lineName the value for tbl_detect_device_dict.line_name
     */
    public void setLineName(String lineName) {
        this.lineName = lineName == null ? null : lineName.trim();
    }

    /**
     * @return the value of tbl_detect_device_dict.electric_type
     */
    public String getElectricType() {
        return electricType;
    }

    /**
     * @param electricType the value for tbl_detect_device_dict.electric_type
     */
    public void setElectricType(String electricType) {
        this.electricType = electricType == null ? null : electricType.trim();
    }

    /**
     * @return the value of tbl_detect_device_dict.electric_name
     */
    public String getElectricName() {
        return electricName;
    }

    /**
     * @param electricName the value for tbl_detect_device_dict.electric_name
     */
    public void setElectricName(String electricName) {
        this.electricName = electricName == null ? null : electricName.trim();
    }

    /**
     * @return the value of tbl_detect_device_dict.net_connect_type
     */
    public String getNetConnectType() {
        return netConnectType;
    }

    /**
     * @param netConnectType the value for tbl_detect_device_dict.net_connect_type
     */
    public void setNetConnectType(String netConnectType) {
        this.netConnectType = netConnectType == null ? null : netConnectType.trim();
    }

    /**
     * @return the value of tbl_detect_device_dict.net_connect_name
     */
    public String getNetConnectName() {
        return netConnectName;
    }

    /**
     * @param netConnectName the value for tbl_detect_device_dict.net_connect_name
     */
    public void setNetConnectName(String netConnectName) {
        this.netConnectName = netConnectName == null ? null : netConnectName.trim();
    }

    /**
     * @return the value of tbl_detect_device_dict.approval_doc
     */
    public String getApprovalDoc() {
        return approvalDoc;
    }

    /**
     * @param approvalDoc the value for tbl_detect_device_dict.approval_doc
     */
    public void setApprovalDoc(String approvalDoc) {
        this.approvalDoc = approvalDoc == null ? null : approvalDoc.trim();
    }

    /**
     * @return the value of tbl_detect_device_dict.location_pic
     */
    public String getLocationPic() {
        return locationPic;
    }

    /**
     * @param locationPic the value for tbl_detect_device_dict.location_pic
     */
    public void setLocationPic(String locationPic) {
        this.locationPic = locationPic == null ? null : locationPic.trim();
    }

    /**
     * @return the value of tbl_detect_device_dict.trackside_pic
     */
    public String getTracksidePic() {
        return tracksidePic;
    }

    /**
     * @param tracksidePic the value for tbl_detect_device_dict.trackside_pic
     */
    public void setTracksidePic(String tracksidePic) {
        this.tracksidePic = tracksidePic == null ? null : tracksidePic.trim();
    }

    /**
     * @return the value of tbl_detect_device_dict.outdoor_pic
     */
    public String getOutdoorPic() {
        return outdoorPic;
    }

    /**
     * @param outdoorPic the value for tbl_detect_device_dict.outdoor_pic
     */
    public void setOutdoorPic(String outdoorPic) {
        this.outdoorPic = outdoorPic == null ? null : outdoorPic.trim();
    }

    /**
     * @return the value of tbl_detect_device_dict.indoor_pic
     */
    public String getIndoorPic() {
        return indoorPic;
    }

    /**
     * @param indoorPic the value for tbl_detect_device_dict.indoor_pic
     */
    public void setIndoorPic(String indoorPic) {
        this.indoorPic = indoorPic == null ? null : indoorPic.trim();
    }

    /**
     * @return the value of tbl_detect_device_dict.interior_pic
     */
    public String getInteriorPic() {
        return interiorPic;
    }

    /**
     * @param interiorPic the value for tbl_detect_device_dict.interior_pic
     */
    public void setInteriorPic(String interiorPic) {
        this.interiorPic = interiorPic == null ? null : interiorPic.trim();
    }

    /**
     * @return the value of tbl_detect_device_dict.device_location
     */
    public String getDeviceLocation() {
        return deviceLocation;
    }

    /**
     * @param deviceLocation the value for tbl_detect_device_dict.device_location
     */
    public void setDeviceLocation(String deviceLocation) {
        this.deviceLocation = deviceLocation == null ? null : deviceLocation.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", detectDeviceId=").append(detectDeviceId);
        sb.append(", detectDeviceName=").append(detectDeviceName);
        sb.append(", depotId=").append(depotId);
        sb.append(", deviceTypeId=").append(deviceTypeId);
        sb.append(", supplierId=").append(supplierId);
        sb.append(", deviceModelId=").append(deviceModelId);
        sb.append(", enabled=").append(enabled);
        sb.append(", storehouseId=").append(storehouseId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", userName=").append(userName);
        sb.append(", remark=").append(remark);
        sb.append(", lineName=").append(lineName);
        sb.append(", electricType=").append(electricType);
        sb.append(", electricName=").append(electricName);
        sb.append(", netConnectType=").append(netConnectType);
        sb.append(", netConnectName=").append(netConnectName);
        sb.append(", approvalDoc=").append(approvalDoc);
        sb.append(", locationPic=").append(locationPic);
        sb.append(", tracksidePic=").append(tracksidePic);
        sb.append(", outdoorPic=").append(outdoorPic);
        sb.append(", indoorPic=").append(indoorPic);
        sb.append(", interiorPic=").append(interiorPic);
        sb.append(", deviceLocation=").append(deviceLocation);
        sb.append("]");
        return sb.toString();
    }
}