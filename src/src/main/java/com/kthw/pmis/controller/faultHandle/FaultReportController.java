package com.kthw.pmis.controller.faultHandle;

import com.kthw.common.DataTable;
import com.kthw.common.base.ErrCode;
import com.kthw.pmis.entiy.*;
import com.kthw.pmis.helper.DepotHelper;
import com.kthw.pmis.mapper.common.*;
import com.kthw.pmis.service.faultHandle.FaultHandleService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private FaultHandleService faultHandleService;
    @Autowired
    private DepotHelper depotHelper;
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
        try {
            Map<String, Object> params = new HashMap<>();
            List<FaultHandle> list = new ArrayList<>();
            //获取faultHandle表
            if (StringUtils.isNotBlank(depotId)) {
                List<Depot> childrens = depotHelper.getChildrens(Long.valueOf(depotId));
                params.put("depotIdList", childrens);
            }
            params.put("eqCompleteFlag", Short.valueOf(completeFlag));
            if (StringUtils.isNotBlank(type)) {
                params.put("eqType", type);
            }
            params.put("queryTime", queryTime);
            params.put("queryTime2", queryTime2);
            params.put("eqFinished", 1);
            params.put("orderByClause", "update_time desc,complete_flag asc");
            list = faultHandleMapper.selectByMap(params);
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
        try {
            Map<String, Object> params = new HashMap<>();
            List<FaultHandle> list = new ArrayList<>();
            //获取faultHandle表
            if (StringUtils.isNotBlank(depotId)) {
                List<Depot> childrens = depotHelper.getChildrens(Long.valueOf(depotId));
                params.put("depotIdList", childrens);
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
        String queryTime = request.getParameter("queryTime");
        String queryTime2 = request.getParameter("queryTime2");
        String type = request.getParameter("type");
        String finished = request.getParameter("finished");
        String dispatcher = request.getParameter("dispatcher");
        try {
            Map<String, Object> params = new HashMap<>();
            List<FaultHandle> list = new ArrayList<>();
            //获取faultHandle表
            if (StringUtils.isNotBlank(depotId)) {
                params.put("eqDepotId", Long.valueOf(depotId));

            } else if (!StringUtils.isNotBlank(depotId)) {
                params.put("gtCompleteFlag", 1);
            }
            if(StringUtils.isNotBlank(finished)) {
                params.put("eqFinished", Short.valueOf(finished));
            }
            if (StringUtils.isNotBlank(type)) {
                params.put("eqType", type);
            }

            if (StringUtils.isNotBlank(dispatcher)) {
                params.put("eqDispatcher", Integer.valueOf(dispatcher));
            }
            params.put("orderByClause", "update_time desc,complete_flag asc");
            list.addAll(faultHandleMapper.selectByMap(params));
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
                    float hours = (float) (diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
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
     * 修改故障预报
     *
     * @param faultHandle
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

}
