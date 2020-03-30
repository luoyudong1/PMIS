package com.kthw.pmis.service.system;

import java.util.List;
import java.util.Map;

import com.kthw.pmis.model.system.DeviceModel;
import com.kthw.pmis.model.system.DeviceName;
import com.kthw.pmis.model.system.DeviceType;
import com.kthw.pmis.model.system.Parts;

public interface PartsService {

	//获取所有配件
	List<Parts> getAllParts(Map<String, Object> params);
	
	//添加配件
	int addParts(Parts parts);
	
	//修改配件
	int updatePars(Parts parts);
	
	//删除配件
	int removePartsById(String part_id);
	
	//根据id查询配件
	List<Parts> getPartsById(String part_id);
	
	//有条件总记录数
	int getPartsParamCount(Map<String, Object> params);
	
	//无条件总记录数
	int getAllPartsCount();

	//查询所有配件型号
	List<DeviceModel> getAllDeviceModel(Map<String, Object> params);

	//查询所有配件类型
	List<DeviceType> getAllDeviceType(Map<String, Object> params);

	// 查询所有配件名称
	List<DeviceName> getAllDeviceName(Map<String, Object> params);
	
	//上传图片
	void updatePicById(Map<String, Object> params);

	//修改图片
	void modifyUploadPicById(Map<String, Object> params);
}
