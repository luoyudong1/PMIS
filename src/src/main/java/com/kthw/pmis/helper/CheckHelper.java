package com.kthw.pmis.helper;

import com.kthw.pmis.entiy.SheetDetail;
import com.kthw.pmis.entiy.SheetInfo;
import org.springframework.stereotype.Component;

@Component("checkHelper")
public class CheckHelper {

    public SheetDetail setCheckedRemark(SheetInfo sheetInfo, SheetDetail sheetDetail) {
        if (sheetDetail.getRepaireState() == 1) {//合格
            if (sheetInfo.getSourceStoreHouseId() == 1) {//新购
                sheetDetail.setCheckedRemark("新购合格");
                if(sheetDetail.getPurchaseDate()!=null)
                sheetDetail.setPurchaseOrRepairedDate(sheetDetail.getPurchaseDate());
                if(sheetDetail.getUnitPrice()!=null)
                sheetDetail.setPurchaseOrRepairedPrice(sheetDetail.getUnitPrice().intValue());

            } else if (sheetInfo.getSourceStoreHouseId() == 4) {//送修
                sheetDetail.setCheckedRemark("送修检测所修复合格");
                if(sheetDetail.getCheckedPrice()!=null)
                sheetDetail.setPurchaseOrRepairedPrice(sheetDetail.getCheckedPrice().intValue());
                sheetDetail.setPurchaseOrRepairedDate(sheetDetail.getCheckedDate());

            } else if (sheetInfo.getSourceStoreHouseId() == 6) {//修返
                sheetDetail.setCheckedRemark("返厂修复合格");
                sheetDetail.setPurchaseOrRepairedPrice(sheetDetail.getFactoryRepairedPrice());
                sheetDetail.setPurchaseOrRepairedDate(sheetDetail.getFactoryRepairedDate());
            }
        } else if (sheetDetail.getRepaireState() == 2) {//不合格
            if (sheetInfo.getSourceStoreHouseId() == 1) {//新购
                sheetDetail.setCheckedRemark("新购不合格");

            } else if (sheetInfo.getSourceStoreHouseId() == 4) {//送修
                sheetDetail.setCheckedRemark("送修所内检测不合格");

            } else if (sheetInfo.getSourceStoreHouseId() == 6) {//修返
                sheetDetail.setCheckedRemark("返厂修复不合格");
            }
        } else if (sheetDetail.getRepaireState() == 3) {//报废
            if (sheetInfo.getSourceStoreHouseId() == 1) {//新购
                sheetDetail.setCheckedRemark("新购报废");

            } else if (sheetInfo.getSourceStoreHouseId() == 4) {//送修
                sheetDetail.setCheckedRemark("送修所内检测报废");

            } else if (sheetInfo.getSourceStoreHouseId() == 6) {//修返
                sheetDetail.setCheckedRemark("返厂修复报废");
            }
        }

        return sheetDetail;
    }
}
