package com.kthw.pmis.service.detectManage;

import com.kthw.pmis.entiy.DetectDevice;
import com.kthw.pmis.entiy.dto.DetectDeviceDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface DetectManageService {
    List<DetectDeviceDTO> listDetect(Long depotId,Integer faultEnable);
    List<DetectDevice> selectByMap(Map<String,Object> params);
}
