package com.kthw.pmis.controller.report;

import com.kthw.common.DataTable;
import com.kthw.pmis._enum.SheetInfoType;
import com.kthw.pmis._enum.StoreHouseEnum;
import com.kthw.pmis._enum.VerifyFlagType;
import com.kthw.pmis.controller.detectProduce.PartsConsumeController;
import com.kthw.pmis.entiy.AssetAttributes;
import com.kthw.pmis.entiy.Depot;
import com.kthw.pmis.entiy.SheetDetail;
import com.kthw.pmis.entiy.dto.ComprehensiveStatisticsDTO;
import com.kthw.pmis.entiy.dto.PartsStatictsDTO;
import com.kthw.pmis.entiy.dto.SheetDetailDTO;
import com.kthw.pmis.helper.DepotHelper;
import com.kthw.pmis.helper.PartsStaticsHelper;
import com.kthw.pmis.helper.StatiscsReturnDataHelper;
import com.kthw.pmis.mapper.common.AssetAttributesMapper;
import com.kthw.pmis.mapper.common.SheetDetailMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：综合统计Controller.
 * 根据车间，或车间上级来进行
 */
@Transactional
@Controller
@RequestMapping(value = "/report/comprehensiveStatics")
public class ComprehensiveStatisticsController {
    private static Logger logger = LoggerFactory.getLogger(ComprehensiveStatisticsController.class);
    @Autowired
    private DepotHelper depotHelper;
    @Autowired
    private AssetAttributesMapper assetAttributesMapper;
    @Autowired
    private SheetDetailMapper sheetDetailMapper;
    @Autowired
    private PartsStaticsHelper partsStaticsHelper;

    @ResponseBody
    @RequestMapping(value = "/getDataByDepotId", method = {RequestMethod.GET})
    public DataTable<ComprehensiveStatisticsDTO> getDataByDepotId(HttpServletRequest request) {
        logger.info("统计查询");
        String depotId = request.getParameter("depotId");
        String queryTime = request.getParameter("queryTime");
        String queryTime2 = request.getParameter("queryTime2");
        List<Depot> depots = new ArrayList<>();
        DataTable<ComprehensiveStatisticsDTO> dt = new DataTable<>();
        List<ComprehensiveStatisticsDTO> result = new ArrayList<>();
        depots = depotHelper.getManageWorkShopDepots(Long.valueOf(depotId));//获取管理的车间
        for (Depot depot : depots) {
            ComprehensiveStatisticsDTO cs = new ComprehensiveStatisticsDTO();
            Map<String, Object> params = new HashMap<>();
            params.put("eqDepotId", depot.getDepotId());
            List<AssetAttributes> assetAttributes = new ArrayList<>();


            assetAttributes = assetAttributesMapper.selectByMap(params);//获取资产属性
            if (assetAttributes.size() > 0) {
                Short assetAttributeId = assetAttributes.get(0).getAssetAttributesId();
                //获取新购总数
                List<PartsStatictsDTO> purchaseCount = partsStaticsHelper.getPurchaseCount(assetAttributeId, queryTime, queryTime2);
                //获取新购金额
                Integer purchasePrice = partsStaticsHelper.getPurchasePrice(assetAttributeId, queryTime, queryTime2);
                //获取送修数量
                List<PartsStatictsDTO> depotToTestCount = partsStaticsHelper.getDepotToTestCount(assetAttributeId, queryTime, queryTime2);
                //获取修复金额
                Integer repairPrice = partsStaticsHelper.getRepairToDepotPartsPrice(assetAttributeId, queryTime, queryTime2);//获取送修到所的配件
                List<SheetDetailDTO> depotToTestParts = partsStaticsHelper.getDepotToTestParts(assetAttributeId, queryTime, queryTime2);
                //获取返厂的配件
                List<SheetDetailDTO> repairToFactoryParts = partsStaticsHelper.getRepairToFactoryParts(assetAttributeId, queryTime, queryTime2);
                //获取送厂修返的配件
                List<SheetDetailDTO> repairOutFactoryParts = partsStaticsHelper.getRepairOutFactoryParts(assetAttributeId, queryTime, queryTime2);
                //获取送厂的配件数量（一段时间内先送修到所的，然后送到厂家的）
                Integer repairToFactoryCount = partsStaticsHelper.getRealCount(depotToTestParts, repairToFactoryParts);
                //获取送厂修返的配件数量（一段时间内先送修到所的，然后送到厂家的）
                Integer repairOutFactoryCount = partsStaticsHelper.getRealCount(depotToTestParts, repairOutFactoryParts);
                //获取所内修复合格的配件
                List<SheetDetailDTO> checkInPartsRepaired = partsStaticsHelper.getCheckInParts((short) StoreHouseEnum.TEST_SEND_REPAIR.getId(),
                        (short) 1, assetAttributeId, queryTime, queryTime2);
                //获取所内修复合格的配件数量
                Integer checkInPartsRepairedCount = partsStaticsHelper.getRealCount(depotToTestParts, checkInPartsRepaired);
                //获取所内报废的配件
                List<SheetDetailDTO> scrapOutParts = partsStaticsHelper.getCheckInParts((short) StoreHouseEnum.TEST_SEND_REPAIR.getId(),
                        (short) 3, assetAttributeId, queryTime, queryTime2);
                //获取所内报废的配件的数量
                Integer scrapOutCount = partsStaticsHelper.getRealCount(depotToTestParts, scrapOutParts);

                //修返回部门的数量
                List<SheetDetailDTO> repairToDepotParts = partsStaticsHelper.getRepairToDepotParts(assetAttributeId, queryTime, queryTime2);
                Integer repairToDepotCount = partsStaticsHelper.getRealCount(depotToTestParts, repairToDepotParts);
                // 添加返回参数
                cs.setDepotId(depot.getDepotId());
                cs.setDepotName(depot.getDepotName());
                if (purchaseCount.size() > 0) {
                    cs.setPurchaseCount(purchaseCount.get(0).getCount());
                }
                if (depotToTestCount.size() > 0) {
                    cs.setDepotToTestCount(depotToTestCount.get(0).getCount());

                    //送厂的数量
                    cs.setRepairToFactoryCount(repairToFactoryCount);
                    //新购金额
                    cs.setPurchasePrice(purchasePrice);
                    //修返的数量
                    cs.setRepairOutFactoryCount(repairOutFactoryCount);
                    //修复金额
                    cs.setRepairPrice(repairPrice);
                    //在厂的数量
                    cs.setRepairInFactoryCount(repairToFactoryCount - repairOutFactoryCount);
                    //所内修复合格的数量
                    cs.setTestRepairedCount(checkInPartsRepairedCount);
                    //报废的数量
                    cs.setScrapOutCount(scrapOutCount);
                    //在所内的数量
                    Integer inTestCount = depotToTestCount.get(0).getCount() - repairToFactoryCount + repairOutFactoryCount -repairToDepotCount- scrapOutCount;
                    cs.setInTestCount(inTestCount);
                    //修返的数量
                    cs.setRepairToDepotCount(repairToDepotCount);
                }
                result.add(cs);
            }
        }
        dt.setRecordsTotal(result.size());
        dt.setRecordsFiltered(result.size());
        dt.setData(result);
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }
}
