package com.kthw.pmis.serviceimpl.detectManage;

import com.kthw.pmis.entiy.Depot;
import com.kthw.pmis.entiy.DetectDevice;
import com.kthw.pmis.entiy.dto.DetectDeviceDTO;
import com.kthw.pmis.helper.DepotHelper;
import com.kthw.pmis.mapper.common.DetectDeviceMapper;
import com.kthw.pmis.service.detectManage.DetectManageService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DetectManageServiceImpl implements DetectManageService {
@Autowired
private DetectDeviceMapper detectDeviceMapper;
@Autowired
private DepotHelper depotHelper;
    @Override
    public List<DetectDeviceDTO> listDetect(Long depotId,Integer faultEnable) {
        //获取子部门
        Map<String, Object> params=new HashMap<>();
        List<DetectDeviceDTO> list = new ArrayList<>();
        if (depotId!=null) {
            List<Depot> childrens = depotHelper.getChildrens(Long.valueOf(depotId));
            params.put("depotIdList", childrens);
        } if (faultEnable!=null) {
            params.put("eqFaultEnable", Integer.valueOf(faultEnable));
        }
        params.put("orderByClause","ddd.detect_device_id asc");
        list=detectDeviceMapper.listDetectDevice(params);
        return list;
    }

    @Override
    public List<DetectDevice> selectByMap(Map<String, Object> params) {
        List<DetectDevice> list=detectDeviceMapper.selectByMap(params);
        return list;
    }
}
