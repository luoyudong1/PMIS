package com.kthw.pmis.serviceimpl.system;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kthw.pmis.mapper.system.CommonMapper;
import com.kthw.pmis.model.system.Car;
import com.kthw.pmis.model.system.Driver;
import com.kthw.pmis.model.system.Team;
import com.kthw.pmis.service.system.CommonService;

@Service
public class CommonServiceImpl implements CommonService{
	@Autowired
    private CommonMapper commonMapper;

	@Override
	public List<Map<String, Object>> getPublicStation() {
		return commonMapper.selectPublicStation();
	}
	
	@Override
	public List<Car> listCarInfo(Map<String, Object> map) {
		return commonMapper.listCarInfo(map);
	}
	
	@Override
	public List<Driver> listDriverInfo(Map<String, Object> map) {
		return commonMapper.listDriverInfo(map);
	}
	
	@Override
	public List<Team> listTeamInfo(Map<String, Object> map) {
		return commonMapper.listTeamInfo(map);
	}

}
