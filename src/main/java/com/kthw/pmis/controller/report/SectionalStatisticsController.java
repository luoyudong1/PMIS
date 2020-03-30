package com.kthw.pmis.controller.report;

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
import com.kthw.pmis.util.excel.ExportExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 维修分段统计
 */

@Transactional
@Controller
@RequestMapping(value = "/report/sectionalStatistics")
public class SectionalStatisticsController {
    private static Logger logger = LoggerFactory.getLogger(SectionalStatisticsController.class);
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
        logger.info("维修分段统计查询");
        String depotId = request.getParameter("depotId");
        String queryTime = request.getParameter("queryTime");
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
                //获取一段时间送修的配件
                List<SheetDetailDTO> repairToTestParts = partsStaticsHelper.getDepotToTestParts(assetAttributeId, queryTime, queryTime2);
                //获取探头数量
                Integer detectorCount = partsStaticsHelper.getDetectorCount(repairToTestParts);
                //获取累计修返的配件
                List<SheetDetailDTO> repairedParts = partsStaticsHelper.getRepairToDepotParts(assetAttributeId, queryTime, queryTime2);
                //所内修复合格
                List<SheetDetailDTO> testRepairWellParts = partsStaticsHelper.getTestRepairWellParts(repairedParts);
                //报废数量
                List<SheetDetailDTO> scrapOutParts = partsStaticsHelper.getScrapOutParts(assetAttributeId, queryTime, queryTime2);
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
                Integer testRepairPrice = partsStaticsHelper.getPurchaseOrRepairPrice(testRepairWellParts);
                if (testRepairPrice > 0) {
                    cs.setTestRepairPrice(testRepairPrice.doubleValue() / 10000);//万元
                }


                // 报废数量
                cs.setScrapOutCount(scrapOutParts.size());

                //送厂检修数量
                List<SheetDetailDTO> repairToFactoryParts = partsStaticsHelper.getRepairToFactoryParts(assetAttributeId, queryTime, queryTime2);
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
                //获取厂家修复金额
                Integer factoryRepairPrice = partsStaticsHelper.getPurchaseOrRepairPrice(factoryRepairWellParts);
                if (factoryRepairPrice > 0) {
                    cs.setFactoryRepairPrice((double)factoryRepairPrice/ 10000);//万元
                }
                //厂家修复探头金额
                Integer factoryRepairDetectorPrice = partsStaticsHelper.getDetectorPrice(factoryRepairWellParts);
                if (factoryRepairDetectorPrice > 0) {
                    cs.setFactoryRepairDetectorPrice((double)factoryRepairDetectorPrice / 10000);//万元
                }
                //厂家修复其他金额
                if (factoryRepairPrice > 0) {
                    if (factoryRepairDetectorPrice == null) {
                        factoryRepairDetectorPrice = 0;
                    }
                    cs.setFactoryRepairOtherPrice((factoryRepairPrice.doubleValue() - factoryRepairDetectorPrice.doubleValue()) / 10000);//万元
                }
                StatiscsReturnDataHelper.addData(sum, cs);
                result.add(cs);
            }
        }
        if (result.size() > 1) {//多于一行才合计
            result.add(sum);
        }
        dt.setRecordsTotal(result.size());
        dt.setRecordsFiltered(result.size());
        dt.setData(result);
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }

    // ExcelUtil导出报表
    @RequestMapping(value = "/export", method = {RequestMethod.GET})
    @ResponseBody
    public void exportSheetInfo(HttpServletRequest request,
                                HttpServletResponse response) {
        logger.info("维修分段统计---ExcelUtil导出报表");
        String depotId = request.getParameter("depotId");
        String queryTime = request.getParameter("queryTime");
        String queryTime2 = request.getParameter("queryTime2");
        String sheetName = request.getParameter("sheetName");
        String depotName = request.getParameter("depotName");
        Map<String, Object> params = new HashMap<>();
        params.put("eqDepotId", Integer.valueOf(depotId));
        List<AssetAttributes> assetAttributes = new ArrayList<>();
        assetAttributes = assetAttributesMapper.selectByMap(params);//获取资产属性
        if (assetAttributes.size() > 0) {
            Short assetAttributeId = assetAttributes.get(0).getAssetAttributesId();
            //获取新购的配件
            List<SheetDetailDTO> repairToDepotParts = partsStaticsHelper.getRepairToDepotParts(assetAttributeId, queryTime, queryTime2);
            List<SheetDetailDTO> factoryRepairWellParts = partsStaticsHelper.getFactoryRepairWellParts(repairToDepotParts);
            String[] title = {"编号", "关键配件名称", "设备型号", "设备类型", "生产厂家", "配件出厂编码", "配件二维码", "资产配属", "数量", "修复价格", "备注"};
            Depot source = new Depot();
            source.setDepotName(depotName);
            Depot object = new Depot();
            object.setDepotName("厂家");
            try {
                HSSFWorkbook wb = ExportExcelUtil.exportStatisticsExcel(sheetName, title, factoryRepairWellParts, source, object, null);
                response.setContentType("application/vnd.ms-excel;charset=utf-8");
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + new String((sheetName + ".xls").getBytes(), "iso-8859-1"));
                response.addHeader("Pargam", "no-cache");
                response.addHeader("Cache-Control", "no-cache");
                OutputStream os = response.getOutputStream();
                wb.write(os);
                os.flush();
                os.close();
            } catch (Exception e) {
                logger.error("export ERROR :" + e);
            }
        }
    }
}
