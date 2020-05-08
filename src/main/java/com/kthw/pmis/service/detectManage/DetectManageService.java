package com.kthw.pmis.service.detectManage;

import com.kthw.pmis.entiy.dto.DetectDeviceDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface DetectManageService {
    List<DetectDeviceDTO> listDetect(Long depotId,Integer faultEnable);
}
