package com.kthw.pmis.controller.report;

/**
 * 维修累计统计
 */

import com.kthw.common.DataTable;
import com.kthw.pmis.entiy.AssetAttributes;
import com.kthw.pmis.entiy.Depot;
import com.kthw.pmis.entiy.SheetDetail;
import com.kthw.pmis.entiy.dto.ComprehensiveStatisticsDTO;
import com.kthw.pmis.entiy.dto.SheetDetailDTO;
import com.kthw.pmis.entiy.dto.StockInfoDTO;
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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:维修累计统计Controller.
 */
@Transactional
@Controller
@RequestMapping(value = "/report/accumulativeStatistics")
public class AccumulativeStatisticsController {

    private static Logger logger = LoggerFactory.getLogger(AccumulativeStatisticsController.class);
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
        logger.info("维修累计统计查询");
        String depotId = request.getParameter("depotId");
        String queryTime2 = request.getParameter("queryTime2");
        List<Depot> depots = new ArrayList<>();
        DataTable<ComprehensiveStatisticsDTO> dt = new DataTable<>();
        List<ComprehensiveStatisticsDTO> result = new ArrayList<>();
        ComprehensiveStatisticsDTO sum = new ComprehensiveStatisticsDTO();//合计
        StatiscsReturnDataHelper.initData(sum);//初始化
        depots = depotHelper.getManageWorkShopDepots(Long.valueOf(depotId));//获取管理的车间
        for (Depot depot : depots) {
            ComprehensiveStatisticsDTO cs = new ComprehensiveStatisticsDTO();
            Map<String, Object> params = new HashMap<>();
            params.put("eqDepotId", depot.getDepotId());
            List<AssetAttributes> assetAttributes = new ArrayList<>();


            assetAttributes = assetAttributesMapper.selectByMap(params);//获取资产属性
            if (assetAttributes.size() > 0) {
                Short assetAttributeId = assetAttributes.get(0).getAssetAttributesId();
                //获取累计送修的配件
                List<SheetDetailDTO> repairToTestParts = partsStaticsHelper.getRepairToTestParts(assetAttributeId, queryTime2);
                //获取探头数量
                Integer detectorCount = partsStaticsHelper.getDetectorCount(repairToTestParts);
                //获取累计修返的配件
                List<SheetDetailDTO> repairedParts = partsStaticsHelper.getRepairedWellParts(assetAttributeId, queryTime2);
                //所内修复合格
                List<SheetDetailDTO> testRepairWellParts = partsStaticsHelper.getTestRepairWellParts(repairedParts);
                //报废数量
                List<SheetDetailDTO> scrapOutParts = partsStaticsHelper.getScrapOutParts(assetAttributeId, queryTime2);
                // 添加返回参数
                cs.setDepotId(depot.getDepotId());
                cs.setDepotName(depot.getDepotName());
                //送修总数
                cs.setDepotToTestCount(repairToTestParts.size());
                //探头总数
                cs.setDetectorCount(detectorCount);
                //其他
                cs.setOtherCount(repairToTestParts.size() - detectorCount);

                //所内修复总数
                cs.setTestRepairedCount(testRepairWellParts.size());
                //所内修复探头数量
                Integer testRepairedWellDetectorCount = partsStaticsHelper.getDetectorCount(testRepairWellParts);
                cs.setTestRepairWellDetectorCount(testRepairedWellDetectorCount);
                //所内修复其他数量
                cs.setTestRepairWellOtherCount(testRepairWellParts.size() - testRepairedWellDetectorCount);
                //所内修复金额
                Integer testRepairPrice=partsStaticsHelper.getPurchaseOrRepairPrice(testRepairWellParts);
                if(testRepairPrice>0) {
                    cs.setTestRepairPrice(testRepairPrice.doubleValue() / 10000);//万元
                }

                //所内修复比例
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(2);
                if (repairToTestParts.size() > 0) {
                    String testRepairRatio = numberFormat.format((float) testRepairWellParts.size() / (float) repairToTestParts.size() * 100);
                    cs.setTestRepairRatio(testRepairRatio + '%');
                }
                //在所内的配件及数量
                List<StockInfoDTO> stockInfoDTOS=partsStaticsHelper.getInTestParts(assetAttributeId);
                cs.setInTestCount(stockInfoDTOS.size());
                // 报废数量
                cs.setScrapOutCount(scrapOutParts.size());

                //送厂检修数量
               List<SheetDetailDTO> repairToFactoryParts = partsStaticsHelper.getRepairToFactoryParts(assetAttributeId, queryTime2);
                cs.setRepairToFactoryCount(repairToFactoryParts.size());
                //送厂检修探头数量
                Integer repairToFactoryDetectorCount = partsStaticsHelper.getDetectorCount(repairToFactoryParts);
                cs.setRepairToFactoryDetectorCount(repairToFactoryDetectorCount);
                //送厂检修其他数量
                cs.setRepairToFactoryOtherCount(repairToFactoryParts.size() - repairToFactoryDetectorCount);
                //厂家修复数量
                List<SheetDetailDTO> factoryRepairWellParts = partsStaticsHelper.getFactoryRepairWellParts(repairedParts);
                cs.setRepairOutFactoryCount(factoryRepairWellParts.size());
                //厂家修复探头数量
                Integer factoryRepairWellDetectorCount = partsStaticsHelper.getDetectorCount(factoryRepairWellParts);
                cs.setFactoryRepairWellDetectorCount(factoryRepairWellDetectorCount);
                //厂家修复其他数量
                cs.setFactoryRepairWellOtherCount(factoryRepairWellParts.size() - factoryRepairWellDetectorCount);
                //厂家修复比例
                if (repairToTestParts.size() > 0) {
                    String factoryRepairRatio = numberFormat.format((float) factoryRepairWellParts.size() / (float) repairToTestParts.size() * 100);
                    cs.setFactoryRepairRatio(factoryRepairRatio + '%');
                }
                //获取厂家修复金额
                Integer factoryRepairPrice=partsStaticsHelper.getPurchaseOrRepairPrice(factoryRepairWellParts);
                if(factoryRepairPrice>0){
                    cs.setFactoryRepairPrice(factoryRepairPrice.doubleValue()/10000);//万元
                }
                //厂家修复探头金额
                Integer factoryRepairDetectorPrice=partsStaticsHelper.getDetectorPrice(factoryRepairWellParts);
                if(factoryRepairDetectorPrice>0){
                    cs.setFactoryRepairDetectorPrice(factoryRepairDetectorPrice.doubleValue()/10000);//万元
                }
                //厂家修复其他金额
                if(factoryRepairPrice>0) {
                    if(factoryRepairDetectorPrice==null){
                        factoryRepairDetectorPrice=0;
                    }
                    cs.setFactoryRepairOtherPrice((factoryRepairPrice.doubleValue() - factoryRepairDetectorPrice.doubleValue()) / 10000);//万元
                }//在厂数量
                List<StockInfoDTO> inFactoryParts = partsStaticsHelper.getInFactoryParts(assetAttributeId);
                cs.setRepairInFactoryCount(inFactoryParts.size());
                //在厂探头数量
                Integer inFactoryDetectorCount = partsStaticsHelper.getDetectorCountByStockInfo(inFactoryParts);
                cs.setInFactoryDetectorCount(inFactoryDetectorCount);
                //在厂其他数量
                cs.setInFactoryOtherCount(inFactoryParts.size() - inFactoryDetectorCount);
                StatiscsReturnDataHelper.addData(sum, cs);
                result.add(cs);
            }
        }
        if (result.size() > 1) {//多于一行才合计
            //所内修复比例
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 设置精确到小数点后2位
            numberFormat.setMaximumFractionDigits(2);
            if (sum.getDepotToTestCount()>0) {
                String testRepairRatio = numberFormat.format((float) sum.getTestRepairedCount()/ sum.getDepotToTestCount() * 100);
                sum.setTestRepairRatio(testRepairRatio + '%');
            //厂家修复比例
                String factoryRepairRatio = numberFormat.format((float) sum.getRepairOutFactoryCount() / (float) sum.getDepotToTestCount() * 100);
                sum.setFactoryRepairRatio(factoryRepairRatio + '%');
            }
            result.add(sum);
        }
        dt.setRecordsTotal(result.size());
        dt.setRecordsFiltered(result.size());
        dt.setData(result);
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }

}
