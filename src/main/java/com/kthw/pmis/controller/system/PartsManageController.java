package com.kthw.pmis.controller.system;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.kthw.common.DataTable;
import com.kthw.common.FileParam;
import com.kthw.common.base.ErrCode;
import com.kthw.pmis.model.system.DeviceModel;
import com.kthw.pmis.model.system.DeviceName;
import com.kthw.pmis.model.system.DeviceType;
import com.kthw.pmis.model.system.Parts;
import com.kthw.pmis.service.SequenceService;
import com.kthw.pmis.service.system.PartsService;

/**
 * 描述:配件管理Controller.
 */
@Transactional
@Controller
@RequestMapping(value = "/system/partsManage")
public class PartsManageController {

	private static Logger logger = LoggerFactory.getLogger(PartsManageController.class);
	
	@Autowired
	private PartsService partsService;
	
	@ResponseBody
	@RequestMapping(value = "/getAllParts", method = { RequestMethod.GET })
	public DataTable<Parts> partsManageList(HttpServletRequest request) {
		logger.info("显示配件");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", request.getParameter("start"));
	    params.put("length", request.getParameter("length"));
		String part_name = request.getParameter("part_name");
		String part_code = request.getParameter("part_id");
		String deviceTypeName = request.getParameter("deviceTypeName");
		String deviceModelName = request.getParameter("deviceModelName");
		String devicePartsName = request.getParameter("devicePartsName");
		String supplierName = request.getParameter("supplierName");
		String supplier_name = request.getParameter("supplier_name");
		params.put("part_name", part_name);
		params.put("part_code", part_code);
		params.put("supplier_name", supplier_name);
		params.put("deviceTypeName", deviceTypeName);
		params.put("deviceModelName",deviceModelName);
		params.put("devicePartsName", devicePartsName);
		params.put("supplierName", supplierName);
		DataTable<Parts> dt = new DataTable<Parts>();
		int total = 0;
		if(part_name != null && part_name != ""){
	    	total = partsService.getPartsParamCount(params);
	    }else{
	    	total = partsService.getAllPartsCount();
	    }
		dt.setRecordsTotal(total);
        dt.setRecordsFiltered(total);
		dt.setData(partsService.getAllParts(params));
		dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
		return dt;
    }
	
	@ResponseBody
	@RequestMapping(value = "/create", method = { RequestMethod.POST })
	public Map<String, Object> partsManageNew(@RequestBody Parts parts) {
		logger.info("增加配件");
		int code = 0;
		if (parts != null && StringUtils.isNotBlank(parts.getDevice_parts_name()) && StringUtils.isNotBlank(parts.getDevice_model_name())) {
			if (partsService.getPartsById(parts.getParts_code()).size() > 0) {
				code = ErrCode.PARTS_ALREADY_EXISTS;
			} else {
				code = partsService.addParts(parts);
			}
			
        } else {
		    code = ErrCode.INCOMPLETE_INFO;
        }
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}
	
	@ResponseBody
	@RequestMapping(value = "/modify", method = { RequestMethod.POST })
	public Map<String, Object> partsManageEdit(@RequestBody Parts parts) {
		logger.info("修改配件");
		int code = 0;
		if (parts != null ) {
            code = partsService.updatePars(parts);
        } else {
		    code = ErrCode.INCOMPLETE_INFO;
        }
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = { RequestMethod.POST })
	public Map<String, Object> partsManageDelete(@RequestBody Parts parts) {
		logger.info("删除配件");
		int code = 0;
		if (parts != null ) {
            code = partsService.removePartsById(parts.getParts_code());
        } else {
		    code = ErrCode.INCOMPLETE_INFO;
        }
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}
	
	@ResponseBody
	@RequestMapping(value = "/find", method = { RequestMethod.GET })
	public DataTable<Parts> getPartsById(HttpServletRequest request, HttpSession httpSession) {
		logger.info("按id查询配件");
		Map<String, Object> params = new HashMap<String, Object>();
		String part_id = request.getParameter("part_id");
		params.put("part_id", part_id);
		DataTable<Parts> dt = new DataTable<Parts>();
		dt.setData(partsService.getPartsById(part_id));
		return dt;
    }
	
	@ResponseBody
	@RequestMapping(value = "/getAllDeviceModel", method = { RequestMethod.GET })
	public DataTable<DeviceModel> getAllDeviceModel(@RequestParam Map<String, Object> params,HttpServletRequest request) {
		logger.info("查询配件型号");
		List<DeviceModel> list = new ArrayList<>();
		DataTable<DeviceModel> dt = new DataTable<DeviceModel>();
		list = partsService.getAllDeviceModel(params);
		dt.setData(list);
		dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
		return dt;
    }
	
	@ResponseBody
	@RequestMapping(value = "/getAllDeviceName", method = { RequestMethod.GET })
	public DataTable<DeviceName> getAllDeviceName(@RequestParam Map<String, Object> params,HttpServletRequest request) {
		logger.info("查询配件名称");
		List<DeviceName> list = new ArrayList<>();
		DataTable<DeviceName> dt = new DataTable<DeviceName>();
		list = partsService.getAllDeviceName(params);
		dt.setData(list);
		dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
		return dt;
    }
	
	@ResponseBody
	@RequestMapping(value = "/getAllDeviceType", method = { RequestMethod.GET })
	public DataTable<DeviceType> getAllDeviceType(@RequestParam Map<String, Object> params,HttpServletRequest request) {
		logger.info("查询配件类型");
		List<DeviceType> list = new ArrayList<>();
		DataTable<DeviceType> dt = new DataTable<DeviceType>();
		list = partsService.getAllDeviceType(params);
		dt.setData(list);
		dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
		return dt;
    }
	
	@RequestMapping(value = "download/{part_id}/{fileName:.+}")
    public ResponseEntity<byte[]> download(@PathVariable String part_id, @PathVariable String fileName, HttpServletRequest request) throws IOException {
    	logger.info("物资图片查看");
    	String filePath=FileParam.readSetting(request,"partsInfoFilePath");
        File file =new File(filePath + File.separator + part_id + File.separator + fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    	return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }
	
    @RequestMapping(value = "upload", method = {RequestMethod.POST})
    @ResponseBody
    public String uploadFile(@RequestParam("parts_id") String part_id, @RequestParam("partImageName") String partImageName, 
    		@RequestParam(value = "partPicAdd") MultipartFile[] file, HttpServletRequest request) {
    	logger.info("上传图片");
    	if (StringUtils.isNotBlank(part_id)) {
    		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
    		String path = FileParam.readSetting(request, "partsPicPath") + File.separator + part_id;
    		if (multipartResolver.isMultipart(request)) {
    			try {
    				StringBuilder imgPath = new StringBuilder();
                    if(partImageName!=null&&!"".equals(partImageName)){
                    	imgPath.append(partImageName);
                    }
                    for (MultipartFile f : file) {
                    	if (f != null) {
                            if (StringUtils.isNotBlank(f.getOriginalFilename())) {
                                logger.info("upload file {}, content type {}", f.getOriginalFilename(), f.getContentType());
                                String typeFile = f.getContentType().toLowerCase();
//                                int count = sequenceService.selectPictureNum();
                                String name = part_id + ".jpg";
                                File localFile = new File(path + File.separator + name);
                                if (!localFile.getParentFile().exists()) {
                                    localFile.getParentFile().mkdirs();
                                    localFile.createNewFile();
                                }
                                if (typeFile.contains("image")) {
                                	typeFile = "image";
                                	if (imgPath.length() > 0) {
                                        imgPath.append(";");
                                    }
                                    imgPath.append(localFile.getName());
                                }
                                f.transferTo(localFile);
                            }
                        }
					}
                    Map<String, Object> params = new HashMap<>();
                    params.put("part_id", part_id);
                    params.put("partPicAdd", imgPath.toString());
                    partsService.updatePicById(params);
                    return "success";
				} catch (Exception e) {
					e.printStackTrace();
					return "error";
				}
    		}
    	}
    	return "error";
    }
    
    @RequestMapping(value = "modifyUpload", method = {RequestMethod.POST})
    @ResponseBody
    public String modifyUploadFile(@RequestParam("part_id") String part_id, @RequestParam("partImageName") String partImageName, 
    		@RequestParam(value = "partPic") MultipartFile[] file, HttpServletRequest request) {
    	logger.info("修改图片");
    	if (StringUtils.isNotBlank(part_id)) {
    		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
    		String path = FileParam.readSetting(request, "partsPicPath") + File.separator + part_id;
    		if (multipartResolver.isMultipart(request)) {
    			try {
    				StringBuilder imgPath = new StringBuilder();
                    if(partImageName!=null&&!"".equals(partImageName)){
                    	imgPath.append(partImageName);
                    }
                    for (MultipartFile f : file) {
                    	if (f != null) {
                            if (StringUtils.isNotBlank(f.getOriginalFilename())) {
                                logger.info("upload file {}, content type {}", f.getOriginalFilename(), f.getContentType());
                                String typeFile = f.getContentType().toLowerCase();
//                                int count = sequenceService.selectPictureNum();
                                String name = part_id + ".jpg";
                                File localFile = new File(path + File.separator + name);
                                if (!localFile.getParentFile().exists()) {
                                    localFile.getParentFile().mkdirs();
                                    localFile.createNewFile();
                                }
                                if (typeFile.contains("image")) {
                                	typeFile = "image";
                                	if (imgPath.length() > 0) {
                                        imgPath.append(";");
                                    }
                                    imgPath.append(localFile.getName());
                                }
                                f.transferTo(localFile);
                            }
                        }
					}
                    Map<String, Object> params = new HashMap<>();
                    params.put("part_id", part_id);
                    params.put("partPic", imgPath.toString());
                    partsService.modifyUploadPicById(params);
                    return "success";
				} catch (Exception e) {
					e.printStackTrace();
					return "error";
				}
    		}
    	}
    	return "error";
    }
	
}
