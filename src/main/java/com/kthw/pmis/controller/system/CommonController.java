package com.kthw.pmis.controller.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kthw.pmis.model.TableView;
import com.kthw.pmis.model.system.Car;
import com.kthw.pmis.model.system.Driver;
import com.kthw.pmis.model.system.Team;
import com.kthw.pmis.service.system.CommonService;
@Transactional
@Controller
@RequestMapping(value = "/system/common")
public class CommonController {
	private static Logger logger = LoggerFactory.getLogger(CommonController.class);

	@Autowired
	private CommonService commonService;

	@RequestMapping(value = "getStationData", method = { RequestMethod.GET })
	@ResponseBody
	public TableView<Map<String, Object>> getStationData() {
		logger.info("查询车站信息表");
		TableView<Map<String, Object>> view = new TableView<>();
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			list = commonService.getPublicStation();
		} catch (Exception e) {
			logger.info(e.toString());
		}
		view.setData(list);
		return view;
	}
	
	@RequestMapping(value = "listTeamCarDriver", method = { RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> listTeamCarDriver(@RequestParam Map<String, Object> params) {
		logger.info("查询班组、车牌号、司机");
		Map<String, Object> ret = new HashMap<String, Object>();
		List<Car> listCar = new ArrayList<>();
		List<Driver> listDriver = new ArrayList<>();
		List<Team> listTeam = new ArrayList<>();
		try {
			listCar = commonService.listCarInfo(params);
			listDriver = commonService.listDriverInfo(params);
			listTeam = commonService.listTeamInfo(params);
		} catch (Exception e) {
			logger.info(e.toString());
		}
		ret.put("listCar", listCar);
		ret.put("listDriver", listDriver);
		ret.put("listTeam", listTeam);
		return ret;
	}
}
