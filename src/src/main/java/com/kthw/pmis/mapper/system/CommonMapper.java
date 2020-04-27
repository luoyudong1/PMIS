package com.kthw.pmis.mapper.system;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kthw.pmis.model.system.Car;
import com.kthw.pmis.model.system.Driver;
import com.kthw.pmis.model.system.Team;

@Repository
public interface CommonMapper {

	List<Map<String, Object>> selectPublicStation();
	
	List<Car> listCarInfo(Map<String, Object> map);
	
	List<Driver> listDriverInfo(Map<String, Object> map);
	
	List<Team> listTeamInfo(Map<String, Object> map);

}
