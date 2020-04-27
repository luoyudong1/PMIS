package com.kthw.pmis.helper;

import com.kthw.pmis._enum.SheetInfoType;
import com.kthw.pmis._enum.VerifyFlagType;
import com.kthw.pmis.entiy.SheetDetail;
import com.kthw.pmis.entiy.dto.PartsStatictsDTO;
import com.kthw.pmis.entiy.dto.SheetDetailDTO;
import com.kthw.pmis.entiy.dto.StockInfoDTO;
import com.kthw.pmis.mapper.common.SheetDetailMapper;
import com.kthw.pmis.mapper.common.StockInfo1Mapper;
import com.kthw.pmis.mapper.stock.StockInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("partsStaticsHelper")
public class PartsStaticsHelper {
    @Autowired
    private SheetDetailMapper sheetDetailMapper;
    @Autowired
    private StockInfo1Mapper stockInfoMapper;

    /**
     * 获取新购数量
     *
     * @param assetAttributeId
     * @param queryTime
     * @param queryTime2
     * @return
     */
    public List<PartsStatictsDTO> getPurchaseCount(Short assetAttributeId, String queryTime, String queryTime2) {
        Map<String, Object> params = new HashMap<>();
        params.put("sheetType", (short) SheetInfoType.DISTRIBUTIONTRANSFER.getId());
        params.put("receiptVerifyFlag", (short) VerifyFlagType.VERIFIED.getId());
        params.put("groupByParam", "sd.asset_attributes_id");
        params.put("assetAttributesId", assetAttributeId);
        params.put("queryTime", queryTime);
        params.put("queryTime2", queryTime2);
        params.put("partState", 1);//新购
        List<PartsStatictsDTO> purchaseCount = sheetDetailMapper.getPartsCount(params);
        return purchaseCount;
    }

    /**
     * 获取新购金额
     *
     * @param assetAttributeId
     * @param queryTime
     * @param queryTime2
     * @return
     */
    public Integer getPurchasePrice(Short assetAttributeId, String queryTime, String queryTime2) {
        Map<String, Object> params = new HashMap<>();
        params.put("sheetType", (short) SheetInfoType.DISTRIBUTIONTRANSFER.getId());
        params.put("receiptVerifyFlag", (short) VerifyFlagType.VERIFIED.getId());
        params.put("groupByParam", "sd.asset_attributes_id");
        params.put("assetAttributesId", assetAttributeId);
        params.put("queryTime", queryTime);
        params.put("queryTime2", queryTime2);
        params.put("partState", 1);//新购
        Integer purchaseCount = sheetDetailMapper.getPartsPrice(params);
        return purchaseCount;
    }

    /**
     * 获取送修数量
     *
     * @param assetAttributeId
     * @param queryTime
     * @param queryTime2
     * @return
     */
    public List<PartsStatictsDTO> getDepotToTestCount(Short assetAttributeId, String queryTime, String queryTime2) {
        Map<String, Object> params = new HashMap<>();
        params.put("sheetType", (short) SheetInfoType.REWORKTRANSFER.getId());
        params.put("receiptVerifyFlag", (short) VerifyFlagType.VERIFIED.getId());
        params.put("groupByParam", "sd.asset_attributes_id");
        params.put("assetAttributesId", assetAttributeId);
        params.put("queryTime", queryTime);
        params.put("queryTime2", queryTime2);
        List<PartsStatictsDTO> depotToTestCount = sheetDetailMapper.getPartsCount(params);
        return depotToTestCount;
    }

    /**
     * 获取报废数量
     *
     * @param assetAttributeId
     * @param queryTime
     * @param queryTime2
     * @return
     */
    public List<PartsStatictsDTO> getScrapOutCount(Short assetAttributeId, String queryTime, String queryTime2) {
        Map<String, Object> params = new HashMap<>();
        params.put("sheetType", (short) SheetInfoType.SCRAPOUT.getId());
        params.put("receiptVerifyFlag", (short) VerifyFlagType.VERIFIED.getId());
        params.put("groupByParam", "sd.asset_attributes_id");
        params.put("assetAttributesId", assetAttributeId);
        params.put("queryTime", queryTime);
        params.put("queryTime2", queryTime2);
        List<PartsStatictsDTO> depotToTestCount = sheetDetailMapper.getPartsCount(params);
        return depotToTestCount;
    }

    /**
     * 获取一段时间送修到所的配件
     *
     * @param assetAttributeId
     * @param queryTime
     * @param queryTime2
     * @return
     */
    public List<SheetDetailDTO> getDepotToTestParts(Short assetAttributeId, String queryTime, String queryTime2) {
        Map<String, Object> params = new HashMap<>();
        params.put("sheetType", (short) SheetInfoType.REWORKTRANSFER.getId());
        params.put("receiptVerifyFlag", (short) VerifyFlagType.VERIFIED.getId());
        params.put("assetAttributesId", assetAttributeId);
        params.put("queryTime", queryTime);
        params.put("queryTime2", queryTime2);
        List<SheetDetailDTO> depotToTestParts = sheetDetailMapper.getPartsBySheetType(params);
        return depotToTestParts;
    }

    /**
     * 获取一段时间返厂的配件
     *
     * @param assetAttributeId
     * @param queryTime
     * @param queryTime2
     * @return
     */
    public List<SheetDetailDTO> getRepairToFactoryParts(Short assetAttributeId, String queryTime, String queryTime2) {
        Map<String, Object> params = new HashMap<>();
        params.put("sheetType", (short) SheetInfoType.REPAIROUT.getId());
        params.put("sendVerifyFlag", (short) VerifyFlagType.VERIFIED.getId());
        params.put("assetAttributesId", assetAttributeId);
        params.put("sendVerifyDate", queryTime);
        params.put("sendVerifyDate2", queryTime2);
        List<SheetDetailDTO> repairToFactoryParts = sheetDetailMapper.getPartsBySheetType(params);
        return repairToFactoryParts;
    }

    /**
     * 获取一段时间报废的配件
     *
     * @param assetAttributeId
     * @param queryTime
     * @param queryTime2
     * @return
     */
    public List<SheetDetailDTO> getScrapOutParts(Short assetAttributeId, String queryTime, String queryTime2) {
        Map<String, Object> params = new HashMap<>();
        params.put("sheetType", (short) SheetInfoType.SCRAPOUT.getId());
        params.put("receiptVerifyFlag", (short) VerifyFlagType.VERIFIED.getId());
        params.put("assetAttributesId", assetAttributeId);
        params.put("queryTime", queryTime);
        params.put("queryTime2", queryTime2);
        List<SheetDetailDTO> repairToFactoryParts = sheetDetailMapper.getPartsBySheetType(params);
        return repairToFactoryParts;
    }

    /**
     * 获取一段时间返修入库的配件
     *
     * @param assetAttributeId
     * @param queryTime
     * @param queryTime2
     * @return
     */
    public List<SheetDetailDTO> getRepairOutFactoryParts(Short assetAttributeId, String queryTime, String queryTime2) {
        Map<String, Object> params = new HashMap<>();
        params.put("sheetType", (short) SheetInfoType.REPAIRIN.getId());
        params.put("sendVerifyFlag", (short) VerifyFlagType.VERIFIED.getId());
        params.put("assetAttributesId", assetAttributeId);
        params.put("sendVerifyDate", queryTime);
        params.put("sendVerifyDate2", queryTime2);
        List<SheetDetailDTO> repairOutFactoryParts = sheetDetailMapper.getPartsBySheetType(params);
        return repairOutFactoryParts;
    }

    /**
     * 获取一段时间新购到车间的配件
     *
     * @param assetAttributeId
     * @param queryTime
     * @param queryTime2
     * @return
     */
    public List<SheetDetailDTO> getPurchaseParts(Short assetAttributeId, String queryTime, String queryTime2) {
        Map<String, Object> params = new HashMap<>();
        params.put("sheetType", (short) SheetInfoType.DISTRIBUTIONTRANSFER.getId());//配送到车间
        params.put("receiptVerifyFlag", (short) VerifyFlagType.VERIFIED.getId());
        params.put("assetAttributesId", assetAttributeId);
        params.put("queryTime", queryTime);
        params.put("queryTime2", queryTime2);
        params.put("partState", 1);//新购属性
        List<SheetDetailDTO> purchaseParts = sheetDetailMapper.getPartsBySheetType(params);
        return  purchaseParts;
    }
    /**
     * 获取截止时间内车间送修的配件
     *
     * @param assetAttributeId
     * @param queryTime2
     * @param queryTime2
     * @return
     */
    public List<SheetDetailDTO> getRepairToTestParts(Short assetAttributeId, String queryTime2) {
        Map<String, Object> params = new HashMap<>();
        params.put("sheetType", (short) SheetInfoType.REWORKTRANSFER.getId());//送修
        params.put("receiptVerifyFlag", (short) VerifyFlagType.VERIFIED.getId());
        params.put("assetAttributesId", assetAttributeId);
        params.put("queryTime2", queryTime2);
        List<SheetDetailDTO> repairToTestParts = sheetDetailMapper.getPartsBySheetType(params);
        return repairToTestParts;
    }
    /**
     * 获取目前在厂家的数量
     *
     * @param assetAttributeId
     * @return
     */
    public  List<StockInfoDTO> getInFactoryParts(Short assetAttributeId) {
        Map<String, Object> params = new HashMap<>();
        params.put("assetAttributesId", assetAttributeId);
        params.put("eqType", 11);//厂家仓库类型
        List<StockInfoDTO> list =stockInfoMapper.selectWithParts(params);
        return list;
    }
    /**
     * 获取目前在所内的数量
     *
     * @param assetAttributeId
     * @return
     */
    public  List<StockInfoDTO> getInTestParts(Short assetAttributeId) {
        Map<String, Object> params = new HashMap<>();
        params.put("assetAttributesId", assetAttributeId);
        params.put("eqDepotId", 3);//厂家仓库类型
        List<StockInfoDTO> list =stockInfoMapper.selectWithParts(params);
        return list;
    }
    /**
     * 获取截止时间内送厂检修的配件
     *
     * @param assetAttributeId
     * @param queryTime2
     * @param queryTime2
     * @return
     */
    public List<SheetDetailDTO> getRepairToFactoryParts(Short assetAttributeId, String queryTime2) {
        Map<String, Object> params = new HashMap<>();
        params.put("sheetType", (short) SheetInfoType.REPAIROUT.getId());//送修
        params.put("sendVerifyFlag", (short) VerifyFlagType.VERIFIED.getId());
        params.put("assetAttributesId", assetAttributeId);
        params.put("sendVerifyDate2", queryTime2);
        List<SheetDetailDTO> repairToTestParts = sheetDetailMapper.getPartsBySheetType(params);
        return repairToTestParts;
    }
    /**
     * 获取截止时间内返修的配件
     *
     * @param assetAttributeId
     * @param queryTime2
     * @param queryTime2
     * @return
     */
    public List<SheetDetailDTO> getRepairedParts(Short assetAttributeId, String queryTime2) {
        Map<String, Object> params = new HashMap<>();
        params.put("sheetType", (short) SheetInfoType.REWORKTRANSFER.getId());//送修
        params.put("receiptVerifyFlag", (short) VerifyFlagType.VERIFIED.getId());
        params.put("assetAttributesId", assetAttributeId);
        params.put("queryTime2", queryTime2);
        params.put("eqRepairPartState", 1);//不是新购的
        List<SheetDetailDTO> repairToTestParts = sheetDetailMapper.getPartsBySheetType(params);
        return repairToTestParts;
    }
    /**
     * 获取截止时间内修返的配件
     *
     * @param assetAttributeId
     * @param queryTime2
     * @param queryTime2
     * @return
     */
    public List<SheetDetailDTO> getRepairedWellParts(Short assetAttributeId, String queryTime2) {
        Map<String, Object> params = new HashMap<>();
        params.put("sheetType", (short) SheetInfoType.DISTRIBUTIONTRANSFER.getId());//送修
        params.put("receiptVerifyFlag", (short) VerifyFlagType.VERIFIED.getId());
        params.put("assetAttributesId", assetAttributeId);
        params.put("queryTime2", queryTime2);
        List<SheetDetailDTO> repairToTestParts = sheetDetailMapper.getPartsBySheetType(params);
        return repairToTestParts;
    }
    /**
     * 获取截止时间内报废的配件
     *
     * @param assetAttributeId
     * @param queryTime2
     * @param queryTime2
     * @return
     */
    public List<SheetDetailDTO> getScrapOutParts(Short assetAttributeId, String queryTime2) {
        Map<String, Object> params = new HashMap<>();
        params.put("sheetType", (short) SheetInfoType.SCRAPOUT.getId());//报废出库
        params.put("receiptVerifyFlag", (short) VerifyFlagType.VERIFIED.getId());
        params.put("assetAttributesId", assetAttributeId);
        params.put("queryTime2", queryTime2);
        List<SheetDetailDTO> scrapOutParts = sheetDetailMapper.getPartsBySheetType(params);
        return scrapOutParts;
    }
    /**
     * 获取一段时间所内送修入库的配件
     *
     * @param assetAttributeId
     * @param queryTime
     * @param queryTime2
     * @return
     */
    public List<SheetDetailDTO> getCheckInParts(Short sourceStoreHouseId,
                                                Short repaireState,
                                                Short assetAttributeId, String queryTime, String queryTime2) {
        Map<String, Object> params = new HashMap<>();
        params.put("sheetType", (short) SheetInfoType.CHECKIN.getId());
        params.put("sendVerifyFlag", (short) VerifyFlagType.VERIFIED.getId());
        params.put("assetAttributesId", assetAttributeId);
        params.put("sendVerifyDate", queryTime);
        params.put("sendVerifyDate2", queryTime2);
        params.put("sourceStoreHouseId", sourceStoreHouseId);//源仓库
        params.put("repaireState", repaireState);//1合格：2不合格；3报废
        params.put("eqRepairPartState", 1);//不是新购的
        List<SheetDetailDTO> checkInParts = sheetDetailMapper.getPartsBySheetType(params);
        return checkInParts;
    }

    /**
     * 获取一段时间修返回车间的配件
     *
     * @param assetAttributeId
     * @param queryTime
     * @param queryTime2
     * @return
     */
    public List<SheetDetailDTO> getRepairToDepotParts(Short assetAttributeId, String queryTime, String queryTime2) {
        Map<String, Object> params = new HashMap<>();
        params.put("sheetType", (short) SheetInfoType.DISTRIBUTIONTRANSFER.getId());
        params.put("receiptVerifyFlag", (short) VerifyFlagType.VERIFIED.getId());
        params.put("assetAttributesId", assetAttributeId);
        params.put("queryTime", queryTime);
        params.put("queryTime2", queryTime2);
        params.put("repairPartState", 1);//不是新购的
        List<SheetDetailDTO> repairToDepotCount = sheetDetailMapper.getPartsBySheetType(params);
        return repairToDepotCount;
    }

    /**
     * 获取一段时间修返回车间的配件修复金额
     *
     * @param assetAttributeId
     * @param queryTime
     * @param queryTime2
     * @return
     */
    public Integer getRepairToDepotPartsPrice(Short assetAttributeId, String queryTime, String queryTime2) {
        Map<String, Object> params = new HashMap<>();
        params.put("sheetType", (short) SheetInfoType.DISTRIBUTIONTRANSFER.getId());
        params.put("receiptVerifyFlag", (short) VerifyFlagType.VERIFIED.getId());
        params.put("assetAttributesId", assetAttributeId);
        params.put("queryTime", queryTime);
        params.put("queryTime2", queryTime2);
        params.put("repairPartState", 1);//不是新购的
        Integer repairToDepotPrice = sheetDetailMapper.getPartsPrice(params);
        return repairToDepotPrice;
    }

    public Integer getRealCount(List<SheetDetailDTO> source, List<SheetDetailDTO> object) {
        int count = 0;
        if (source.size() > 0 && object.size() > 0) {
            for (SheetDetailDTO part1 : source) {

                for (SheetDetailDTO part2 : object) {
                    if (part1.getPartId().equals(part2.getPartId())) {
                        count++;
                        break;
                    }
                }
            }
        }
        return count;
    }

    /**
     * 获取探头数量
     *
     * @param source
     * @return
     */
    public Integer getDetectorCount(List<SheetDetailDTO> source) {
        int count = 0;
        for (SheetDetailDTO sheetDetail : source) {
            String partId = sheetDetail.getPartId().substring(3, 5);
            if (partId.equals("64") || partId.equals("65")) {
                count++;
            }


        }
        return count;
    }
    /**
     * 获取探头数量
     *
     * @param source
     * @return
     */
    public Integer getDetectorCountByStockInfo(List<StockInfoDTO> source) {
        int count = 0;
        for (StockInfoDTO stockInfoDTO : source) {
            String partId = stockInfoDTO.getPartIdSeq().substring(3, 5);
            if (partId.equals("64") || partId.equals("65")) {
                count++;
            }


        }
        return count;
    }
    /**
     * 获取探头费用
     *
     * @param source
     * @return
     */
    public Integer getDetectorPrice(List<SheetDetailDTO> source) {
        int price = 0;
        for (SheetDetailDTO sheetDetail : source) {
            String partId = sheetDetail.getPartId().substring(3, 5);
            if (partId.equals("64") || partId.equals("65")) {
                price += sheetDetail.getPurchaseOrRepairedPrice();
            }


        }
        return price;
    }

    /**
     * 获取新购或修复金额
     *
     * @param source
     * @return
     */
    public Integer getPurchaseOrRepairPrice(List<SheetDetailDTO> source) {
        int price = 0;
        for (SheetDetailDTO sheetDetail : source) {
            if (sheetDetail.getPurchaseOrRepairedPrice() != null) {
                price += sheetDetail.getPurchaseOrRepairedPrice();
            }


        }
        return price;
    }
    /**
     * 获取返厂修复合格配件
     *
     * @param source
     * @return
     */
    public List<SheetDetailDTO> getFactoryRepairWellParts(List<SheetDetailDTO> source) {
        List<SheetDetailDTO> list=new ArrayList<>();
        for (int i=0;i<source.size();i++) {
            String remark=source.get(i).getCheckedRemark();
            if (StringUtils.isNotBlank(remark) &&remark.equals("返厂修复合格")) {
                SheetDetailDTO sheetDetail=new SheetDetailDTO();
                sheetDetail=source.get(i);
               list.add(sheetDetail);
            }


        }
        return list;
    }
    /**
     * 获取所内修复合格配件
     *
     * @param source
     * @return
     */
    public List<SheetDetailDTO> getTestRepairWellParts(List<SheetDetailDTO> source) {
        List<SheetDetailDTO> list=new ArrayList<>();

            for (int i=0;i<source.size();i++) {
                String remark=source.get(i).getCheckedRemark();
                if (StringUtils.isNotBlank(remark) &&remark.equals("送修检测所修复合格")) {
                    SheetDetailDTO sheetDetail = new SheetDetailDTO();
                    sheetDetail = source.get(i);
                    list.add(sheetDetail);
                }



        }
        return list;
    }
}
