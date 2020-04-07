package com.kthw.pmis.serviceimpl.planCheckSheet;

import com.kthw.pmis.entiy.PlanCheckSheet;
import com.kthw.pmis.mapper.common.PlanCheckSheetMapper;
import com.kthw.pmis.service.planCheckSheet.PlanCheckSheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanCheckServiceImpl implements PlanCheckSheetService {
@Autowired
    private PlanCheckSheetMapper planCheckSheetMapper;
    @Override
    public String getMaxSheetId(String sheet_id) {
        String maxSheetId = planCheckSheetMapper.getMaxSheetId(sheet_id);
        try {
            if (null == maxSheetId) {
                maxSheetId = sheet_id + "00000001";
                return maxSheetId;

            } else {
                Integer serial = Integer.valueOf(maxSheetId.substring(7)) + 1;
                StringBuffer sb = new StringBuffer();
                String str = String.valueOf(serial);
                for (int i = 0; i < 8 - str.length(); ++i) {
                    sb.append("0");
                }
                sb.append(serial);
                String sheet_id2 = sheet_id + sb;
                return sheet_id2;
            }
        } catch (Exception e) {

        }
        return null;

    }
}
