package com.kthw.pmis.controller.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kthw.common.base.BaseController;
import com.kthw.common.base.ErrCode;
import com.kthw.pmis.model.TableView;
import com.kthw.pmis.model.system.Function;
import com.kthw.pmis.model.system.Role;
import com.kthw.pmis.service.system.UserService;

/**
 * 描述:用户权限管理Controller.
 */
@Transactional
@Controller
@RequestMapping(value = "/system/roleManage")
public class AuthManageController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(AuthManageController.class);

	@Autowired
	private UserService userService;

	@RequestMapping(value = "getRoleList", method = { RequestMethod.GET })
	@ResponseBody
	public TableView<Role> getRoleList(HttpServletRequest request, HttpSession httpSession) {
		logger.info("查询所有角色url");
		TableView<Role> view = new TableView<Role>();
        view.setData(userService.getRoleList());
        return view;
	}
	
	@RequestMapping(value = "getFuncList", method = { RequestMethod.GET })
	@ResponseBody
	public List<Function> getFunctionList(HttpServletRequest request, HttpSession httpSession) {
		logger.info("查询功能项");
        return userService.getFuncList();
	}

	@RequestMapping(value = "createFunc", method = { RequestMethod.POST })
	@ResponseBody
	public Function createFunction(@RequestBody Function function) {
		logger.info("新建功能项");
		if (function != null) {
            return userService.createFunc(function);
        }
        return null;
	}

    @RequestMapping(value = "deleteFunc", method = { RequestMethod.GET })
    @ResponseBody
    public Map<String, Object> deleteFunction(@RequestParam("id") String funcId) {
        logger.info("删除功能项");
        int code = 0;
        if (StringUtils.isNotBlank(funcId)) {
            try {
                userService.deleteFunc(Integer.parseInt(funcId));
            } catch (Exception e) {
                code = ErrCode.DB_ERROR;
            }
        } else {
        	code = ErrCode.INCOMPLETE_INFO;
		}
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    @RequestMapping(value = "modifyFunc", method = {RequestMethod.POST})
	@ResponseBody
	public Map<String, Object> modifyFunction(@RequestBody Function function) {
	    logger.info("修改功能项");
		int code;
		if (function != null && function.getId() > 0) {
			code = userService.modifyFunc(function);
		} else {
			code = ErrCode.INCOMPLETE_INFO;
		}
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}

	@RequestMapping(value = "getPermission", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getPermission(@RequestParam("roleId") String roleId) {
	    logger.info("获取角色权限");
        List<Integer> keys = null;
        if (StringUtils.isNotBlank(roleId)) {
	        keys = userService.getFuncIdsByRole(Integer.parseInt(roleId));
        }
	    Map<String, Object> ret = new HashMap<String, Object>();
	    ret.put("funcIds", keys);
	    return ret;
    }

    @RequestMapping(value = "updatePermission", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> permission(@RequestBody Map<String, String> params) {
        logger.info("更新角色权限");
        int code = 0;
		if (params != null && StringUtils.isNotBlank(params.get("roleId"))) {
            try {
                userService.updatePermission(params);
            } catch (Exception e) {
                code = ErrCode.DB_ERROR;
            }
        } else {
		    code = ErrCode.INCOMPLETE_INFO;
        }
        Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    @RequestMapping(value = "createRole", method = { RequestMethod.POST })
    @ResponseBody
    public Map<String, Object> createRole(@RequestBody Role role) {
        logger.info("新建角色");
        Map<String, Object> ret = new HashMap<String, Object>();
        int code;
        if (role != null && StringUtils.isNotBlank(role.getRole_name())) {
            code = userService.createRole(role);
            if (code == ErrCode.SUCCESS) {
                ret.put("data", role);
            }
        } else {
            code = ErrCode.INCOMPLETE_INFO;
        }
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    @RequestMapping(value = "modifyRole", method = { RequestMethod.POST })
    @ResponseBody
    public Map<String, Object> modifyRole(@RequestBody Role role) {
        logger.info("修改角色");
        int code;
        if (role != null && role.getRole_id() != 0 && StringUtils.isNotBlank(role.getRole_name())) {
            code = userService.modifyRole(role);
        } else {
            code = ErrCode.INCOMPLETE_INFO;
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    @RequestMapping(value = "deleteRole", method = { RequestMethod.GET })
    @ResponseBody
    public Map<String, Object> deleteRole(@RequestParam("roleId") String roleId) {
        logger.info("删除角色");
        int code = ErrCode.SUCCESS;
        if (StringUtils.isNotBlank(roleId)) {
            try {
                userService.deleteRole(Integer.parseInt(roleId));
            } catch (Exception e) {
                code = ErrCode.DB_ERROR;
            }
        } else {
            code = ErrCode.INCOMPLETE_INFO;
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }
    
	/**
	 * 查询角色对应的功能
	 */
    @RequestMapping(value = "queryFuncPermByRole", method = { RequestMethod.GET })
	@ResponseBody
	public TableView<Map<String, Object>> queryFuncPermByRole(@RequestParam Map<String, Object> params,HttpServletRequest request) {
		logger.info("查询角色对应的功能!");
		TableView<Map<String, Object>> tb = new TableView<>();
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			list = userService.queryFuncPermByRole(params);
			tb.setData(list);
		} catch (Exception e) {
			tb.setError("服务器处理数据异常");
			tb.setData(new ArrayList<Map<String, Object>>());
			logger.info("查询角色功能异常"+e);
		}
        return tb;
	}
    
	/**
	 * 修改用户操作权限
	 */
    @RequestMapping(value = "modifyPermission", method = { RequestMethod.POST })
    @ResponseBody
    public Map<String, Object> modifyPermission(@RequestParam Map<String, Object> params) {
        logger.info("修改操作权限");
        int code = userService.modifyPermission(params);
        Map<String, Object> ret = new HashMap<String, Object>();
        if(code == 1){
        	ret.put("code", code);
        	ret.put("msg", "修改用户操作权限成功!");
        }else{
            ret.put("code", code);
            ret.put("msg", ErrCode.getMessage(code));
        }
        return ret;
    }
    

}
