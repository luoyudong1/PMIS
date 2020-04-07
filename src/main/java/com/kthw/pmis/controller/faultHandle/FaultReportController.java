package com.kthw.pmis.controller.faultHandle;

import com.kthw.common.DataTable;
import com.kthw.common.base.ErrCode;
import com.kthw.pmis.entiy.*;
import com.kthw.pmis.mapper.common.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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
        String completeFlag = request.getParameter("status");
        String queryTime = request.getParameter("queryTime");
        String queryTime2 = request.getParameter("queryTime2");
        String type = request.getParameter("type");
        try {
            Map<String, Object> params = new HashMap<>();
            List<FaultHandle> list = new ArrayList<>();
            //获取faultHandle表
            if (StringUtils.isNotBlank(depotId) && !StringUtils.isNotBlank(completeFlag)) {
                params.put("eqDepotId", Long.valueOf(depotId));
                params.put("ltCompleteFlag", 5);
            } else if (!StringUtils.isNotBlank(depotId) && !StringUtils.isNotBlank(completeFlag)) {
                params.put("gtCompleteFlag", 1);
                params.put("ltCompleteFlag", 5);
            } else if (StringUtils.isNotBlank(depotId) && StringUtils.isNotBlank(completeFlag)) {
                params.put("eqDepotId", Long.valueOf(depotId));
                params.put("eqCompleteFlag", Short.valueOf(completeFlag));
            } else if (!StringUtils.isNotBlank(depotId) && StringUtils.isNotBlank(completeFlag)) {
                params.put("eqCompleteFlag", Short.valueOf(completeFlag));
            }
            if (StringUtils.isNotBlank(type)) {
                params.put("eqType", type);
            }
            params.put("queryTime",queryTime);
            params.put("queryTime2",queryTime2);
            params.put("orderByClause", "complete_flag asc,update_time desc");
            list = faultHandleMapper.selectByMap(params);
            dt.setRecordsTotal(list.size());
            dt.setRecordsFiltered(list.size());
            dt.setData(list);
            dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        } catch (Exception e) {
            logger.error("故障预报出错");
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
            List<FaultHandle> list = new ArrayList<>();
            DataTable<FaultHandle> dt = new DataTable<FaultHandle>();
            //新增faultHandle表
            Integer id = faultHandleMapper.getMaxId();
            id = (id == null ? 1 : id + 1);
            faultHandle.setId(id);
            faultHandle.setForecastFaultTime(new Timestamp(new Date().getTime()));
            faultHandle.setUpdateTime(new Timestamp(new Date().getTime()));
            faultHandle.setCompleteFlag((short) 1);
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
            List<FaultHandle> list = new ArrayList<>();
            DataTable<FaultHandle> dt = new DataTable<FaultHandle>();
            //修改faultHandle表
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
        String ret=new String();
        try {
            detectDevice = detectDeviceMapper.selectByPrimaryKey(detectDeviceId);
            Depot depot=depotMapper.selectByPrimaryKey(detectDevice.getDepotId());
            ret=depot.getDepotName();
            params.put("eqDepotId",depot.getParentId());
            List<Depot> list=depotMapper.selectByMap(params);
            ret=list.get(0).getDepotName()+ret;

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
    @RequestMapping(value = "/getResponsibleUnit", method = RequestMethod.GET)
    public DetectDevice getResponsibleUnit(Integer detectDeviceId,String type) {
        logger.info("获取责任单位");
        List<ResponsibleUnit> list = new ArrayList<>();
        DetectDevice detectDevice = new DetectDevice();
        Map<String, Object> params = new HashMap<>();
        try {
            detectDevice = detectDeviceMapper.selectByPrimaryKey(detectDeviceId);

        } catch (Exception e) {
            logger.error("获取责任单位");
        }
        return  detectDevice;

    }
}
