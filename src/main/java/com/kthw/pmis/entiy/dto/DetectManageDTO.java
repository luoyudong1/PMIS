package com.kthw.pmis.entiy.dto;

import java.util.List;
import java.util.Set;

public class DetectManageDTO {
    private Set<String> lines;
    private Set<String> detectDeviceType;
    private List<DetectDeviceDTO> list;

    public Set<String> getLines() {
        return lines;
    }

    public void setLines(Set<String> lines) {
        this.lines = lines;
    }

    public Set<String> getDetectDeviceType() {
        return detectDeviceType;
    }

    public void setDetectDeviceType(Set<String> detectDeviceType) {
        this.detectDeviceType = detectDeviceType;
    }

    public List<DetectDeviceDTO> getList() {
        return list;
    }

    public void setList(List<DetectDeviceDTO> list) {
        this.list = list;
    }
}
