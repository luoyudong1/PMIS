package com.kthw.pmis.service.system;

import java.util.List;
import java.util.Map;

import com.kthw.pmis.model.system.Car;
import com.kthw.pmis.model.system.Driver;
import com.kthw.pmis.model.system.Team;

public interface CommonService {
	
	List<Map<String, Object>> getPublicStation();
	
    List<Car> listCarInfo(Map<String, Object> map);
	
	List<Driver> listDriverInfo(Map<String, Object> map);
	
	List<Team> listTeamInfo(Map<String, Object> map);
	
}
