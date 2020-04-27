package com.kthw.pmis.serviceimpl.system;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kthw.common.base.ErrCode;
import com.kthw.pmis.mapper.system.PartsMapper;
import com.kthw.pmis.model.entryAndOut.Supplier;
import com.kthw.pmis.model.system.DeviceModel;
import com.kthw.pmis.model.system.DeviceName;
import com.kthw.pmis.model.system.DeviceType;
import com.kthw.pmis.model.system.Parts;
import com.kthw.pmis.service.system.PartsService;

@Service
public class PartsServiceImpl implements PartsService {

	private static Logger logger = LoggerFactory.getLogger(PartsServiceImpl.class);

	@Autowired
	private PartsMapper partsMapper;

	@Override
	public List<Parts> getAllParts(Map<String, Object> params) {
		List<Parts> parts = null;
		try {
			parts = partsMapper.getAllParts(params);
		} catch (Exception e) {
			logger.error("getAllParts error: ", e);
		}
		return parts;
	}

	@Override
	public int addParts(Parts parts) {
		int errCode = 0;
		try {
			partsMapper.addParts(parts);
		} catch (Exception e) {
			logger.error("addParts error: ", e);
			errCode = ErrCode.DB_ERROR;
		}
		return errCode;
	}

	@Override
	public int updatePars(Parts parts) {
		int errCode = 0;
		try {
			partsMapper.updatePars(parts);
		} catch (Exception e) {
			logger.error("updateUser error: ", e);
			errCode = ErrCode.DB_ERROR;
		}
		return errCode;
	}

	@Override
	public int removePartsById(String part_id) {
		int errCode = 0;
		try {
			partsMapper.removePartsById(part_id);
		} catch (Exception e) {
			logger.error("removePartsById error: ", e);
			errCode = ErrCode.DB_ERROR;
		}
		return errCode;
	}

	@Override
	public List<Parts> getPartsById(String part_id) {
		List<Parts> parts = null;
		try {
			parts = partsMapper.getPartsById(part_id);
		} catch (Exception e) {
			logger.error("getAllParts error: ", e);
		}
		return parts;
	}

	@Override
	public int getPartsParamCount(Map<String, Object> params) {
		int count = 0;
        try {
            count = partsMapper.getPartsParamCount(params);
        } catch (Exception e) {
            logger.error("getPartsParamCount error: ", e);
        }
        return count;
	}

	@Override
	public int getAllPartsCount() {
		int count = 0;
        try {
            count = partsMapper.getAllPartsCount();
        } catch (Exception e) {
            logger.error("getAllPartsCount error: ", e);
        }
        return count;
	}

	@Override
	public List<DeviceModel> getAllDeviceModel(Map<String, Object> params) {
		List<DeviceModel> deviceModel = null;
		try {
			deviceModel = partsMapper.getAllDeviceModel(params);
		} catch (Exception e) {
			logger.error("getAllDeviceModel error: ", e);
		}
		return deviceModel;
	}

	@Override
	public List<DeviceType> getAllDeviceType(Map<String, Object> params) {
		List<DeviceType> deviceType = null;
		try {
			deviceType = partsMapper.getAllDeviceType(params);
		} catch (Exception e) {
			logger.error("getAllDeviceType error: ", e);
		}
		return deviceType;
	}

	@Override
	public void updatePicById(Map<String, Object> params) {
		partsMapper.updatePicById(params);
		
	}

	@Override
	public void modifyUploadPicById(Map<String, Object> params) {
		partsMapper.modifyUploadPicById(params);
	}
	
	public List<DeviceName> getAllDeviceName(Map<String, Object> params) {
		List<DeviceName> deviceName = null;
		try {
			deviceName = partsMapper.getAllDeviceName(params);
		} catch (Exception e) {
			logger.error("getAllDeviceName error: ", e);
		}
		return deviceName;
	}

}
