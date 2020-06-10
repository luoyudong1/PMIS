package com.kthw.pmis.controller.faultHandle;

import com.kthw.common.DataTable;
import com.kthw.common.base.ErrCode;
import com.kthw.pmis.entiy.*;
import com.kthw.pmis.helper.DepotHelper;
import com.kthw.pmis.helper.FaultHandleHelper;
import com.kthw.pmis.mapper.common.*;
import com.kthw.pmis.service.faultHandle.FaultHandleService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;

@Controller
@Transactional
@RequestMapping("/faultHandle/faultReport")
public class FaultReportController {
    @Autowired
    private FaultHandleMapper faultHandleMapper;
    @Autowired
    private DetectDeviceMapper detectDeviceMapper;
    @Autowired
    private RepairUserMapper repairUserMapper;
    @Autowired
    private FaultTypeMapper faultTypeMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private FaultHandleService faultHandleService;
    @Autowired
    private DepotHelper depotHelper;
    @Autowired
    private FaultHandleHelper faultHandleHelper;
    @Autowired
    private FaultInfoMapper faultInfoMapper;
    private static final Logger logger = LoggerFactory.getLogger(FaultReportController.class);

    /**
     * 显示故障预报表
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public DataTable<FaultHandle> list(HttpServletRequest request) {
        logger.info("显示故障预报");
        DataTable<FaultHandle> dt = new DataTable<FaultHandle>();
        String depotId = request.getParameter("depotId");
        String completeFlag = request.getParameter("completeFlag");
        String queryTime = request.getParameter("queryTime");
        String queryTime2 = request.getParameter("queryTime2");
        String type = request.getParameter("type");
        String detectDeviceName = request.getParameter("detectDeviceName");
        try {
            Map<String, Object> params = new HashMap<>();
            List<FaultHandle> list = new ArrayList<>();
            //获取faultHandle表
            Depot depot = null;
            if (StringUtils.isNotBlank(depotId)) {
                depot = depotHelper.getDepot(Long.valueOf(depotId));
                List<Depot> childrens = depotHelper.getChildrens(Long.valueOf(depotId));
                params.put("depotIdList", childrens);
            }
            params.put("eqCompleteFlag", Short.valueOf(completeFlag));
            if (StringUtils.isNotBlank(type)) {
                params.put("eqType", type);
            }
            if (StringUtils.isNotBlank(detectDeviceName)) {
                params.put("likeDetectDeviceName", '%' + detectDeviceName + '%');
            }
            params.put("queryTime", queryTime);
            params.put("queryTime2", queryTime2);
            params.put("eqFinished", 1);
            params.put("orderByClause", "update_time desc,complete_flag asc");
            list = faultHandleMapper.selectByMap(params);
            list = faultHandleHelper.sortNewList(list, depot);
            dt.setRecordsTotal(list.size());
            dt.setRecordsFiltered(list.size());
            dt.setData(list);
            dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        } catch (Exception e) {
            logger.error("显示故障预报出错");
        }
        return dt;

    }

    /**
     * 显示申诉故障预报表
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listAppeal", method = RequestMethod.GET)
    public DataTable<FaultHandle> listAppeal(HttpServletRequest request) {
        logger.info("显示申诉故障预报表");
        DataTable<FaultHandle> dt = new DataTable<FaultHandle>();
        String depotId = request.getParameter("depotId");
        String queryTime = request.getParameter("queryTime");
        String queryTime2 = request.getParameter("queryTime2");
        String type = request.getParameter("type");
        String detectDeviceName = request.getParameter("detectDeviceName");
        try {
            Map<String, Object> params = new HashMap<>();
            List<FaultHandle> list = new ArrayList<>();
            //获取faultHandle表
            Depot depot = null;
            if (StringUtils.isNotBlank(depotId)) {
                depot = depotHelper.getDepot(Long.valueOf(depotId));
                List<Depot> childrens = depotHelper.getChildrens(Long.valueOf(depotId));
                params.put("depotIdList", childrens);
            }
            params.put("gtAppealFlag", 0);
            if (StringUtils.isNotBlank(type)) {
                params.put("eqType", type);
            }
            if (StringUtils.isNotBlank(detectDeviceName)) {
                params.put("likeDetectDeviceName", '%' + detectDeviceName + '%');
            }
            params.put("queryTime", queryTime);
            params.put("queryTime2", queryTime2);
            params.put("orderByClause", "update_time desc,complete_flag asc");
            list = faultHandleMapper.selectByMap(params);
            list = faultHandleHelper.sortNewList(list, depot);
            dt.setRecordsTotal(list.size());
            dt.setRecordsFiltered(list.size());
            dt.setData(list);
            dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        } catch (Exception e) {
            logger.error("显示申诉故障预报出错");
        }
        return dt;

    }

    /**
     * 查询故障预报表
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public DataTable<FaultHandle> search(HttpServletRequest request) {
        logger.info("查询故障预报");
        DataTable<FaultHandle> dt = new DataTable<FaultHandle>();
        String depotId = request.getParameter("depotId");
        String queryTime = request.getParameter("queryTime");
        String queryTime2 = request.getParameter("queryTime2");
        String detectDeviceName = request.getParameter("detectDeviceName");
        try {
            Map<String, Object> params = new HashMap<>();
            List<FaultHandle> list = new ArrayList<>();
            //获取faultHandle表
            if (StringUtils.isNotBlank(depotId)) {
                List<Depot> childrens = depotHelper.getChildrens(Long.valueOf(depotId));
                params.put("depotIdList", childrens);
            }
            if (StringUtils.isNotBlank(detectDeviceName)) {
                params.put("likeDetectDeviceName", '%' + detectDeviceName + '%');
            }
            params.put("queryTime", queryTime);
            params.put("queryTime2", queryTime2);
            params.put("orderByClause", "update_time desc,complete_flag asc");
            list = faultHandleMapper.selectByMap(params);
            dt.setRecordsTotal(list.size());
            dt.setRecordsFiltered(list.size());
            dt.setData(list);
            dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        } catch (Exception e) {
            logger.error("查询故障预报出错");
        }
        return dt;

    }

    /**
     * 显示故障预报表
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/byEightNextDay", method = RequestMethod.GET)
    public DataTable<FaultHandle> byEightNextDay(HttpServletRequest request) {
        logger.info("显示第二天八点前故障预报");
        DataTable<FaultHandle> dt = new DataTable<FaultHandle>();
        String depotId = request.getParameter("depotId");
        String completeFlag = request.getParameter("completeFlag");
        String type = request.getParameter("type");
        String finished = request.getParameter("finished");
        String dispatcher = request.getParameter("dispatcher");
        String detectDeviceName = request.getParameter("detectDeviceName");
        try {
            Depot depot = null;
            Map<String, Object> params = new HashMap<>();
            List<FaultHandle> list = new ArrayList<>();
            //获取faultHandle表
            if (StringUtils.isNotBlank(depotId)) {
                depot = depotHelper.getDepot(Long.valueOf(depotId));
                List<Depot> childrens = depotHelper.getChildrens(Long.valueOf(depotId));
                params.put("depotIdList", childrens);
            } else if (!StringUtils.isNotBlank(depotId)) {
                params.put("gtCompleteFlag", 1);
            }
            if (StringUtils.isNotBlank(finished)) {
                params.put("eqFinished", Short.valueOf(finished));
            }
            if (StringUtils.isNotBlank(type)) {
                params.put("eqType", type);
            }
            if (StringUtils.isNotBlank(detectDeviceName)) {
                params.put("likeDetectDeviceName", "%" + detectDeviceName + "%");
            }
            if (StringUtils.isNotBlank(completeFlag)) {
                params.put("eqCompleteFlag", Short.valueOf(completeFlag));
            }
            if (StringUtils.isNotBlank(dispatcher)) {
                params.put("eqDispatcher", Integer.valueOf(dispatcher));
            }
            params.put("orderByClause", "update_time desc,complete_flag asc");
            list.addAll(faultHandleMapper.selectByMap(params));
            list = faultHandleHelper.sortNewList(list, depot);
            dt.setRecordsTotal(list.size());
            dt.setRecordsFiltered(list.size());
            dt.setData(list);
            dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        } catch (Exception e) {
            logger.error("显示第二天八点前故障预报出错");
        }
        return dt;

    }

    /**
     * 新增故障预报
     *
     * @param faultHandle
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public synchronized Map<String, Object> add(@RequestBody FaultHandle faultHandle) {
        logger.info("新增故障预报");
        int code = 0;
        Map<String, Object> ret = new HashMap<>();
        try {
            //新增faultHandle表
            faultHandle.setUpdateTime(new Timestamp(new Date().getTime()));
            faultHandle.setCompleteFlag((short) 1);
            faultHandle.setFinished(0);
            int flag = faultHandleMapper.insert(faultHandle);
            if (flag == 1) {
                ret.put("code", code);
                ret.put("msg", ErrCode.getMessage(code));
            }
            return ret;
        } catch (Exception e) {
            logger.error("新增故障预报出错");
        }
        code = ErrCode.ADD_ERROR;
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;

    }

    /**
     * 拆分故障
     *
     * @param faultHandle
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updatePartOfFault", method = RequestMethod.POST)
    public Map<String, Object> updatePartOfFault(@RequestBody FaultHandle faultHandle) {
        logger.info("拆分故障");
        int code = 0;
        Map<String, Object> ret = new HashMap<>();
        try {
            Date oldStartTime = new Date();
            Date oldEndTime = new Date();

            //获取旧的faultHandle表
            FaultHandle old = faultHandleMapper.selectByPrimaryKey(faultHandle.getId());
            oldStartTime = old.getHaultStartTime();
            oldEndTime = old.getHandleEndTime();
            String type = old.getType();
            //新增部分故障
            FaultHandle newFault = new FaultHandle();
            newFault = old;
            String id = faultHandleService.getMaxId(old.getId().substring(0, 6));
            newFault.setId(id);
            long diff = faultHandle.getHandleEndTime().getTime() - oldStartTime.getTime();
            Float hours = (float) diff / (1000 * 60 * 60);
            //保留两位小数
            hours = new BigDecimal(hours.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            //新增部分故障
            newFault.setFaultStopTime(hours);
            newFault.setHaultStartTime(oldStartTime);
            newFault.setHandleEndTime(faultHandle.getHandleEndTime());
            newFault.setType(faultHandle.getType());
            faultHandleMapper.insert(newFault);

            //修改旧的故障信息
            diff = oldEndTime.getTime() - faultHandle.getHandleEndTime().getTime();
            //计算小时
            hours = (float) diff / (1000 * 60 * 60);
            //保留两位小数
            hours = new BigDecimal(hours.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            old.setFaultStopTime(hours);
            old.setType(type);
            old.setId(faultHandle.getId());
            old.setHaultStartTime(faultHandle.getHandleEndTime());
            old.setHandleEndTime(oldEndTime);
            old.setAdjustReason(faultHandle.getAdjustReason());
            old.setAdjustSubmitUser(faultHandle.getAdjustSubmitUser());
            old.setAdjustSubmitDate(new Date());
            faultHandleMapper.updateByPrimaryKeySelective(old);
            ret.put("code", code);
            ret.put("msg", ErrCode.getMessage(code));
            return ret;
        } catch (Exception e) {
            logger.error("拆分故障出错");
        }
        code = ErrCode.ADD_ERROR;
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;

    }

    /**
     * 修改故障预报
     *
     * @param faultHandle
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, Object> update(@RequestBody FaultHandle faultHandle) {
        logger.info("修改故障预报");
        int code = 0;
        Map<String, Object> ret = new HashMap<>();
        try {
            //修改faultHandle表
            if (faultHandle.getCompleteFlag() != null) {
                if (faultHandle.getCompleteFlag() == (short) 2) {
                    faultHandle.setForecastFaultTime(new Timestamp(new Date().getTime()));
                }
                if (faultHandle.getCompleteFlag() == (short) 7) {
                    FaultHandle info = faultHandleMapper.selectByPrimaryKey(faultHandle.getId());
                    Date startTime = info.getHaultStartTime();
                    Date endTime = info.getHandleEndTime();
                    long diff = endTime.getTime() - startTime.getTime();
                    //计算小时
                    Float hours = (float) diff / (1000 * 60 * 60);
                    //保留两位小数
                    hours = new BigDecimal(hours.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                    faultHandle.setFaultStopTime(hours);
                }
            }
            faultHandle.setUpdateTime(new Timestamp(new Date().getTime()));
            int flag = faultHandleMapper.updateByPrimaryKeySelective(faultHandle);
            if (flag == 1) {
                ret.put("code", code);
                ret.put("msg", ErrCode.getMessage(code));
            }
            return ret;
        } catch (Exception e) {
            logger.error("修改故障预报出错");
        }
        code = ErrCode.MODIFY_ERROR;
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;

    }

    /**
     * 申诉故障
     *
     * @param faultHandle
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/appeal", method = RequestMethod.POST)
    public Map<String, Object> appeal(FaultHandle faultHandle, @RequestParam("file") MultipartFile file) {
        logger.info("申诉故障");
        int code = 0;
        Map<String, Object> ret = new HashMap<>();
        Date date=new Date();
        try {
            /**
             * 上传文件
             */
            if (file != null) {
                //图片上传成功后，将图片的地址写到数据库
                String filePath = "D:\\upload";//保存图片的路径
                //获取原始图片的拓展名
                String originalFilename = file.getOriginalFilename();
                //新的文件名字
                String newFileName = UUID.randomUUID() + originalFilename;
                //封装上传文件位置的全路径
                File targetFile = new File(filePath, newFileName);
                //把本地文件上传到封装上传文件位置的全路径
                file.transferTo(targetFile);
                faultHandle.setAppealDocUrl(newFileName);
            }
            faultHandle.setAppealSubmitDate(date);
            faultHandle.setUpdateTime(date);
            int flag = faultHandleMapper.updateByPrimaryKeySelective(faultHandle);
            if (flag == 1) {
                ret.put("code", code);
                ret.put("msg", ErrCode.getMessage(code));
            }
            return ret;
        } catch (Exception e) {
            logger.error("申诉故障出错");
        }
        code = ErrCode.MODIFY_ERROR;
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;

    }

    /**
     * 修改故障预报
     *
     * @param faultHandle
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/agree", method = RequestMethod.POST)
    public Map<String, Object> agree(@RequestBody FaultHandle faultHandle) {
        logger.info("同意故障申诉");
        int code = 0;
        Map<String, Object> ret = new HashMap<>();
        Date date=new Date();
        try {
            faultHandle.setFaultType("非责任故障");
            faultHandle.setAppealVerifyDate(new Date());
            faultHandle.setUpdateTime(date);
            int flag = faultHandleMapper.updateByPrimaryKeySelective(faultHandle);
            if (flag == 1) {
                ret.put("code", code);
                ret.put("msg", ErrCode.getMessage(code));
            }
            return ret;
        } catch (Exception e) {
            logger.error("同意故障申诉出错");
        }
        code = ErrCode.MODIFY_ERROR;
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;

    }
    /**
     * 文件下载,只需要传入对应文件名字
     */
    @RequestMapping(value = "/download/{fileName:.+}",method = RequestMethod.GET)
    public void fileDownload(
            @PathVariable("fileName") String fileName,
            HttpServletResponse resp) {
        logger.info("申诉附件下载");
/*******************4.告诉浏览器以附件的形式下载*************************/
// 告诉浏览器是以附件形式来下载 不要解析
        try {
            resp.setContentType("multipart/form-data;");
            resp.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            resp.addHeader("Pargam", "no-cache");
            resp.addHeader("Cache-Control", "no-cache");
            String path = "D:\\upload/"+fileName;
//写入流中
            FileInputStream is = new FileInputStream(path);
//获取相应的输出流
            ServletOutputStream os = resp.getOutputStream();
            byte[] b = new byte[1024];
            int len;
//写入浏览器中
            while ((len = is.read(b)) != -1) {
                os.write(b, 0, len);
            }
//关闭对应的流
            os.close();
            is.close();

        } catch (Exception e) {
            logger.error("申诉附件下载出错");
        }
    }
    /**
     * 删除故障预报
     *
     * @param faultHandle
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Map<String, Object> delete(@RequestBody FaultHandle faultHandle) {
        logger.info("删除故障预报");
        int code = 0;
        Map<String, Object> ret = new HashMap<>();
        try {
            List<FaultHandle> list = new ArrayList<>();
            DataTable<FaultHandle> dt = new DataTable<FaultHandle>();
            //新增faultHandle表
            int flag = faultHandleMapper.deleteByPrimaryKey(faultHandle.getId());
            if (flag == 0) {
                code = ErrCode.DELETE_ERROR;
            }
        } catch (Exception e) {
            logger.error("删除故障预报出错");
        }
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;

    }

    /**
     * 获取故障类别
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getFaultType", method = RequestMethod.GET)
    public List<FaultType> getFaultType() {
        logger.info("获取故障类别");
        List<FaultType> list = new ArrayList<>();
        try {
            //新增faultHandle表
            list = faultTypeMapper.selectByMap(null);
        } catch (Exception e) {
            logger.error("获取故障类别出错");
        }
        return list;
    }

    /**
     * 获取探测站维修人员
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getRepairPerson", method = RequestMethod.GET)
    public List<RepairUser> getRepairPerson(Integer detectDeviceId) {
        logger.info("获取探测站维修人员");
        List<RepairUser> list = new ArrayList<>();
        DetectDevice detectDevice = new DetectDevice();
        Map<String, Object> params = new HashMap<>();
        try {
            detectDevice = detectDeviceMapper.selectByPrimaryKey(detectDeviceId);
            if (detectDevice != null && detectDevice.getDepotId() != null) {
                params.put("eqDepotId", detectDevice.getDepotId());
                list = repairUserMapper.selectByMap(params);
            }
        } catch (Exception e) {
            logger.error("获取探测站维修人员");
        }
        return list;
    }

    /**
     * 获取探测站责任部门
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getResponsibleDepot", method = RequestMethod.GET)
    public String getResponsibleDepot(Integer detectDeviceId) {
        logger.info("获取探测站责任部门");
        DetectDevice detectDevice = new DetectDevice();
        Map<String, Object> params = new HashMap<>();
        String ret = new String();
        try {
            detectDevice = detectDeviceMapper.selectByPrimaryKey(detectDeviceId);
            Depot depot = depotMapper.selectByPrimaryKey(detectDevice.getDepotId());
            ret = depot.getDepotName();
            params.put("eqDepotId", depot.getParentId());
            List<Depot> list = depotMapper.selectByMap(params);
            ret = list.get(0).getDepotName() + ret;

        } catch (Exception e) {
            logger.error("获取探测站责任部门");
        }
        return ret;
    }

    /**
     * 获取探测站责任单位
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getResponsibleSegment", method = RequestMethod.GET)
    public String getResponsibleSegment(Integer detectDeviceId) {
        logger.info("获取探测站责任单位（段）");
        DetectDevice detectDevice = new DetectDevice();
        Map<String, Object> params = new HashMap<>();
        String ret = new String();
        try {
            detectDevice = detectDeviceMapper.selectByPrimaryKey(detectDeviceId);
            Depot depot = depotMapper.selectByPrimaryKey(detectDevice.getDepotId());
            ret = depot.getDepotName();
            params.put("eqDepotId", depot.getParentId());
            List<Depot> list = depotMapper.selectByMap(params);
            if (list.size() > 0) {
                params.put("eqDepotId", list.get(0).getParentId());
                List<Depot> list2 = depotMapper.selectByMap(params);
                if (list2.size() > 0) {
                    ret = list2.get(0).getDepotName();
                }
            }
        } catch (Exception e) {
            logger.error("获取探测站责任单位（段）");
        }
        return ret;
    }

    /**
     * 获取探测站责任单位
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getResponsibleUnit", method = RequestMethod.GET)
    public DetectDevice getResponsibleUnit(Integer detectDeviceId, String type) {
        logger.info("获取责任单位");
        List<ResponsibleUnit> list = new ArrayList<>();
        DetectDevice detectDevice = new DetectDevice();
        Map<String, Object> params = new HashMap<>();
        try {
            detectDevice = detectDeviceMapper.selectByPrimaryKey(detectDeviceId);

        } catch (Exception e) {
            logger.error("获取责任单位");
        }
        return detectDevice;

    }

    /**
     * 获取探测站最近发生的故障
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getFaultInfo", method = RequestMethod.GET)
    public List<FaultHandle> getFaultInfo(Integer detectDeviceId, String type) {
        logger.info("获取探测站最近发生的故障");
        Map<String, Object> params = new HashMap<>();
        List<FaultHandle> list = new ArrayList<>();
        try {
            params.put("eqDetectDeviceId", detectDeviceId);
            params.put("orderByClause", "update_time desc limit 7");
            list = faultHandleMapper.selectByMap(params);

        } catch (Exception e) {
            logger.error("获取探测站最近发生的故障");
        }
        return list;

    }

    @ResponseBody
    @RequestMapping(value = "/getMaxSheetId", method = {RequestMethod.GET})
    public synchronized String getMaxSheetId(@RequestParam String sheet_id, HttpServletRequest request) {
        logger.info("获取故障预报最大id");

        try {
            String maxSheetId = faultHandleService.getMaxId(sheet_id);
            return maxSheetId;
        } catch (Exception e) {
            logger.error("获取故障预报最大id" + request);
        }
        return null;
    }

    /**
     * 归档故障预报
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/finished", method = RequestMethod.GET)
    public Map<String, Object> finished() {
        logger.info("归档故障预报");
        int code = 0;
        Map<String, Object> ret = new HashMap<>();
        try {

            faultHandleService.finished();
            ret.put("code", code);
            ret.put("msg", ErrCode.getMessage(code));
            return ret;
        } catch (Exception e) {
            logger.error("归档故障预报出错");
        }
        code = ErrCode.MODIFY_ERROR;
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;

    }

    /**
     * 获取故障现象
     *
     * @param deviceType 设备类型 AEI,THDS
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getFaultInfo", method = {RequestMethod.POST})
    public List<FaultInfo> getFaultInfo(@RequestParam("deviceType") String deviceType) {
        logger.info("获取故障现象");
        List<FaultInfo> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        System.out.println(deviceType);
        try {
            if (StringUtils.isNotBlank(deviceType)) {
                params.put("eqDeviceType", deviceType);
            }
            list = faultInfoMapper.selectByMap(params);
        } catch (Exception e) {
            logger.error("获取故障现象error");
        }
        return list;
    }
}
