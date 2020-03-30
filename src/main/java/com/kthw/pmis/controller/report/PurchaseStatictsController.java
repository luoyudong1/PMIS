package com.kthw.pmis.controller.report;

import com.kthw.common.DataTable;
import com.kthw.pmis._enum.SheetInfoType;
import com.kthw.pmis._enum.StoreHouseEnum;
import com.kthw.pmis.entiy.AssetAttributes;
import com.kthw.pmis.entiy.Depot;
import com.kthw.pmis.entiy.SheetDetail;
import com.kthw.pmis.entiy.SheetInfo;
import com.kthw.pmis.entiy.dto.ComprehensiveStatisticsDTO;
import com.kthw.pmis.entiy.dto.PartsStatictsDTO;
import com.kthw.pmis.entiy.dto.SheetDetailDTO;
import com.kthw.pmis.entiy.ext.SheetInfoExt;
import com.kthw.pmis.helper.DepotHelper;
import com.kthw.pmis.helper.PartsStaticsHelper;
import com.kthw.pmis.helper.SheetDetailHelper;
import com.kthw.pmis.mapper.common.*;
import com.kthw.pmis.service.entryAndOut.PurchasePartsService;
import com.kthw.pmis.util.excel.ExportExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;


/**
 * 描述:新购统计Controller.
 */
@Transactional
@Controller
@RequestMapping(value = "/report/purchaseStaticts")
public class PurchaseStatictsController {

    private static Logger logger = LoggerFactory.getLogger(PurchaseStatictsController.class);
    @Autowired
    private DepotHelper depotHelper;
    @Autowired
    private AssetAttributesMapper assetAttributesMapper;
    @Autowired
    private SheetDetailMapper sheetDetailMapper;
    @Autowired
    private PartsStaticsHelper partsStaticsHelper;

    private void initData(ComprehensiveStatisticsDTO cs) {
        // 添加返回参数
        cs.setDepotId(0L);
        cs.setDepotName("合计");
        //新购总数
        cs.setPurchaseCount(0);
        //新购金额
        cs.setPurchasePrice(0);
        cs.setDetectorCount(0);
        cs.setDetectorPrice(0);
        cs.setOtherCount(0);
        cs.setOtherPrice(0);
    }
    private void addData(ComprehensiveStatisticsDTO cs,ComprehensiveStatisticsDTO row) {
        // 添加返回参数
        //新购总数
        cs.setPurchaseCount(cs.getPurchaseCount()+row.getPurchaseCount());
        //新购金额
        cs.setPurchasePrice(cs.getPurchasePrice()+row.getPurchasePrice());
        cs.setDetectorCount(cs.getDetectorCount()+row.getDetectorCount());
        cs.setDetectorPrice(cs.getDetectorPrice()+row.getDetectorPrice());
        cs.setOtherCount(cs.getOtherCount()+row.getOtherCount());
        cs.setOtherPrice(cs.getOtherPrice()+row.getOtherPrice());
    }
    @ResponseBody
    @RequestMapping(value = "/getPurchaseDataByDepotId", method = {RequestMethod.GET})
    public DataTable<ComprehensiveStatisticsDTO> getDataByDepotId(HttpServletRequest request) {
        logger.info("采购统计查询");
        String depotId = request.getParameter("depotId");
        String queryTime = request.getParameter("queryTime");
        String queryTime2 = request.getParameter("queryTime2");
        List<Depot> depots = new ArrayList<>();
        DataTable<ComprehensiveStatisticsDTO> dt = new DataTable<>();
        List<ComprehensiveStatisticsDTO> result = new ArrayList<>();
        ComprehensiveStatisticsDTO sum = new ComprehensiveStatisticsDTO();//合计
        initData(sum);//初始化
        depots = depotHelper.getManageWorkShopDepots(Long.valueOf(depotId));//获取管理的车间
        for (Depot depot : depots) {
            ComprehensiveStatisticsDTO cs = new ComprehensiveStatisticsDTO();
            Map<String, Object> params = new HashMap<>();
            params.put("eqDepotId", depot.getDepotId());
            List<AssetAttributes> assetAttributes = new ArrayList<>();


            assetAttributes = assetAttributesMapper.selectByMap(params);//获取资产属性
            if (assetAttributes.size() > 0) {
                Short assetAttributeId = assetAttributes.get(0).getAssetAttributesId();
                //获取新购的配件
                List<SheetDetailDTO> purchaseParts = partsStaticsHelper.getPurchaseParts(assetAttributeId, queryTime, queryTime2);
                //获取新购金额
                Integer purchasePrice = partsStaticsHelper.getPurchaseOrRepairPrice(purchaseParts);
                //获取探头数量
                Integer detectorCount = partsStaticsHelper.getDetectorCount(purchaseParts);
                //获取探头总费用
                Integer detectorPrice = partsStaticsHelper.getDetectorPrice(purchaseParts);

                // 添加返回参数
                cs.setDepotId(depot.getDepotId());
                cs.setDepotName(depot.getDepotName());
                //新购总数

                cs.setPurchaseCount(purchaseParts.size());


                //新购金额
                cs.setPurchasePrice(purchasePrice);
                cs.setDetectorCount(detectorCount);
                cs.setDetectorPrice(detectorPrice);
                cs.setOtherCount(purchaseParts.size() - detectorCount);
                cs.setOtherPrice(purchasePrice - detectorPrice);
                addData(sum,cs);//合计

                result.add(cs);
            }
        }
        if (result.size()>1) {//多于一行才合计
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
        logger.info("采购统计---ExcelUtil导出报表");
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
            List<SheetDetailDTO> purchaseParts = partsStaticsHelper.getPurchaseParts(assetAttributeId, queryTime, queryTime2);
            String[] title = {"编号", "关键配件名称", "设备型号", "设备类型", "生产厂家", "配件出厂编码", "配件二维码", "资产配属", "数量", "新购单价", "备注"};
            Depot source = new Depot();
            source.setDepotName("机辆检测所");
            Depot object = new Depot();
            object.setDepotName(depotName);
            try {
                HSSFWorkbook wb = ExportExcelUtil.exportStatisticsExcel(sheetName, title, purchaseParts, source, object, null);
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
