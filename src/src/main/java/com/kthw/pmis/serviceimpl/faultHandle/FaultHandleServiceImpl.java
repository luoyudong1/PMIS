package com.kthw.pmis.serviceimpl.faultHandle;

import com.kthw.pmis.entiy.FaultHandle;
import com.kthw.pmis.mapper.common.FaultHandleMapper;
import com.kthw.pmis.service.faultHandle.FaultHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FaultHandleServiceImpl implements FaultHandleService {
@Autowired
private FaultHandleMapper faultHandleMapper;
    @Override
    public String getMaxId(String id) {
        try {
            String maxId = faultHandleMapper.getMaxId(id);
            if (null == maxId) {
                maxId = id + "00000001";
                return maxId;

            } else {
                Integer serial = Integer.valueOf(maxId.substring(7)) + 1;
                StringBuffer sb = new StringBuffer();
                String str = String.valueOf(serial);
                for (int i = 0; i < 8 - str.length(); ++i) {
                    sb.append("0");
                }
                sb.append(serial);
                String sheet_id2 = id + sb;
                return sheet_id2;
            }
        } catch (Exception e) {

        }
        return null;

    }

    @Override
    public void finished() {
        Date date=new Date();
        List<FaultHandle> list=new ArrayList<>();
        Map<String,Object> params=new HashMap<>();
        params.put("eqCompleteFlag",7);
        params.put("eqFinished",0);
        params.put("queryTime2",date);
        list=faultHandleMapper.selectByMap(params);
        for(FaultHandle handle:list){
            handle.setFinished(1);
        }
        faultHandleMapper.batchUpdate(list);
    }

}
