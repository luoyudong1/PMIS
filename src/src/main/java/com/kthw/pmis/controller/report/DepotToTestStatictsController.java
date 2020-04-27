package com.kthw.pmis.controller.report;

import com.kthw.pmis._enum.SheetInfoType;
import com.kthw.pmis.entiy.dto.PartsStatictsDTO;
import com.kthw.pmis.helper.SheetDetailHelper;
import com.kthw.pmis.mapper.common.PartsZtreeMapper;
import com.kthw.pmis.mapper.common.SheetDetailMapper;
import com.kthw.pmis.mapper.common.SheetInfoMapper;
import com.kthw.pmis.mapper.common.StockInfo1Mapper;
import com.kthw.pmis.service.entryAndOut.PurchasePartsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * 描述:采购入库Controller.
 */
@Transactional
@Controller
@RequestMapping(value = "/report/depotToTestStaticts")
public class DepotToTestStatictsController {

    private static Logger logger = LoggerFactory.getLogger(DepotToTestStatictsController.class);

    @Autowired
    private PurchasePartsService purchasePartsService;
    @Autowired
    private StockInfo1Mapper stockInfoMapper;
    @Autowired
    private SheetDetailMapper sheetDetailMapper;
    @Autowired
    private SheetInfoMapper sheetInfoMapper;
    @Autowired
    private SheetDetailHelper sheetDetailHelper;
    @Autowired
    private PartsZtreeMapper partsZtreeMapper;
    private final String[] ZEROS = {"000", "00", "0", ""};

    @ResponseBody
    @RequestMapping(value = "/getRepairPartsCount", method = {RequestMethod.GET})
    public List<PartsStatictsDTO> sheetsList(HttpServletRequest request) { logger.info("获取采购数量");
        List<PartsStatictsDTO> list = new ArrayList<>();
        int total = 0;
        String depotId=request.getParameter("depotId");
        try {
            Map<String,Object> params=new HashMap<>();
            params.put("queryTime", request.getParameter("queryTime"));
            params.put("queryTime2", request.getParameter("queryTime2"));
            params.put("receiptVerifyFlag", request.getParameter("receiptVerifyFlag"));
            params.put("sheetType", (short) SheetInfoType.REWORKTRANSFER.getId());
            params.put("groupByParam", "si.source_store_house_id");
            list = sheetDetailMapper.getPartsCount(params);
        }catch (Exception e){
            logger.error("getRepairPartsCount"+request);
        }
        return list;
    }




}
