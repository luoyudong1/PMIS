//package com.kthw.dmis.mapper;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.kthw.pmis.entiy.StockInfo1;
//import com.kthw.pmis.entiy.dto.StockInfoDTO;
//import com.kthw.pmis.mapper.common.StockInfo1Mapper;
//import com.kthw.pmis.model.stock.StockInfo;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import com.kthw.pmis.service.entryAndOut.PurchasePartsService;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
//public class StockInfoMapper {
//	@Autowired
//	private StockInfo1Mapper stockInfoMapper;
//
//	@Test
//	public void test() {
//		List<StockInfoDTO> list=new ArrayList<>();
//		List<StockInfo1> params=new ArrayList<>();
//		list=stockInfoMapper.selectWithParts(null);
//		for(StockInfoDTO stockInfoDTO:list){
//		    StockInfo1 stockInfo1=new StockInfo1();
//		    stockInfo1.setFactoryPartsCode(stockInfoDTO.getFactoryPartsCode());
//		    stockInfo1.setPurchasePrice(BigDecimal.valueOf(stockInfoDTO.getUnitPrice()));
//		    params.add(stockInfo1);
//        }
//		stockInfoMapper.batchUpdate(params);
//
//	}
//
//}
