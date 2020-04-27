//package com.kthw.dmis.mapper;
//
//import java.util.Date;
//import java.util.HashMap;
//
//import java.util.List;
//import java.util.Map;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.kthw.pmis.entiy.SheetDetail;
//import com.kthw.pmis.entiy.StockInfo1;
//import com.kthw.pmis.entiy.dto.SheetDetailDTO;
//import com.kthw.pmis.entiy.dto.StockInfoDTO;
//import com.kthw.pmis.mapper.common.SheetDetailMapper;
//import com.kthw.pmis.mapper.common.StockInfo1Mapper;
//import com.kthw.pmis.mapper.entryAndOut.ProduceEntryMapper;
//import com.kthw.pmis.mapper.system.PartsMapper;
//import com.kthw.pmis.model.entryAndOut.SheetInfo;
//import com.kthw.pmis.model.system.Parts;
//
//import net.sf.ehcache.search.aggregator.Max;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations={"classpath:applicationContext.xml"})  
//public class EntryAndOutTest {
//	@Autowired
//	ProduceEntryMapper produceEntryMapper;
//	@Autowired
//	private SheetDetailMapper sheetDetailMapper;
//	@Autowired
//	private PartsMapper partsMapper;
//	@Autowired
//	private StockInfo1Mapper stockInfo1Mapper;
//	@Test
//	public void produceEntry() {
//		SheetInfo sheetInfo=new SheetInfo();
//		sheetInfo.setSend_verify_id("1");
//		sheetInfo.setSheet_id("gggggggg");
//		produceEntryMapper.addSheet(sheetInfo);
//	}
//	@Test
//	public void select() {
//		Map<String, Object> params=new HashMap<String, Object>();
//		params.put("partCode", "fdhdfh");
//		Date maxDate=sheetDetailMapper.selectMaxDateByPartCode(params);
//		System.out.println(maxDate);
//		params.put("sendVerifyDate", maxDate);
//		List<SheetDetailDTO> sheetDetailDTOs=sheetDetailMapper.selectWithStock(params);
//		for(SheetDetailDTO sheetDetailDTO:sheetDetailDTOs) {
//			System.out.println(sheetDetailDTO.getPartCode());
//		}
//	}
//	@Test
//	public void getAllParts() {
//		List<Parts> list=partsMapper.getAllParts(null);
//		for(Parts parts:list) {
//			System.out.println(parts.getDevice_parts_name());
//		}
//		
//	}
//	@Test
//	public void getAllPartsByStock() {
//		Map<String, Object> params=new HashMap<String, Object>();
//		params.put("storeHouseId", 14);
//		List<StockInfoDTO> list=stockInfo1Mapper.selectWithParts(null);
//		for(StockInfoDTO s:list) {
//			System.out.println(s.getFactoryPartsCode());
//		}
//		
//	}
//}
