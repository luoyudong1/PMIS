package com.kthw.pmis.controller.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.kthw.common.DataTable;
import com.kthw.common.Utils;
import com.kthw.common.base.ErrCode;
import com.kthw.pmis.model.system.Depot;
import com.kthw.pmis.model.system.Role;
import com.kthw.pmis.model.system.Team;
import com.kthw.pmis.model.system.User;
import com.kthw.pmis.model.system.Workshop;
import com.kthw.pmis.service.system.UserService;

/**
 * 描述:用户管理Controller.
 */
@Transactional
@Controller
@RequestMapping(value = "/system/userManage")
public class UserManageController {

	private static Logger logger = LoggerFactory.getLogger(UserManageController.class);

	@Autowired
	UserService userService;

	@ResponseBody
	@RequestMapping(value = "/list", method = { RequestMethod.GET })
	public DataTable<User> userManageList(HttpServletRequest request) {
	    Map<String, String> params = new HashMap<String, String>();
	    params.put("start", request.getParameter("start"));
	    params.put("length", request.getParameter("length"));
	    String username = request.getParameter("user_name");
	    params.put("user_name", username);
	    DataTable<User> dt = new DataTable<User>();
	    int total = 0;
	    if(username != null && username != ""){
	    	total = userService.getUsersParamCount(params);
	    }else{
	    	total = userService.getAllUsersCount();
	    }
        dt.setRecordsTotal(total);
        dt.setRecordsFiltered(total);
        dt.setData(userService.getAllUsers(params));
        dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
        return dt;
    }
	@ResponseBody
	@RequestMapping(value = "/getUser", method = { RequestMethod.POST })
	public User getUser(@RequestParam(value = "userId") String userId) {
		logger.info("查询用户信息");
		User user = userService.getUserById(userId);
		return user;
	}
	@ResponseBody
	@RequestMapping(value = "/create", method = { RequestMethod.POST })
	public Map<String, Object> userManageNew(@RequestBody User user) {
		logger.info("增加用户信息");
		int code = 0;
		User u = userService.getUserById(user.getUser_id());
		  if (u != null) {
          	code = ErrCode.USER_EXIST_ALREADY;
			} else {
				if (user != null && StringUtils.isNotBlank(user.getUser_id())) {
				    Date date = new Date();
		            user.setUser_pass(Utils.enPassword(user.getUser_pass()));
		            user.setLast_login(date);
		            user.setReg_date(date);
		            String roleIds = user.getUser_role_name();
		            user.setUser_role(0);
		            code = userService.addUser(user, roleIds);
		        } else {
				    code = ErrCode.INCOMPLETE_INFO;
		        }
			}
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}

	@ResponseBody
	@RequestMapping(value = "/modify", method = { RequestMethod.POST })
	public Map<String, Object> userManageEdit(@RequestBody User user) {
		logger.info("编辑用户信息");
		int code = 0;
		User u = userService.getUserById(user.getUser_id());
		if(!u.getUser_pass().equals(user.getUser_pass())) {
			user.setUser_pass(Utils.enPassword(user.getUser_pass()));
		}
		if (user != null && StringUtils.isNotBlank(user.getUser_id())) {
            code = userService.updateUser(user, user.getUser_role_name());
        } else {
		    code = ErrCode.INCOMPLETE_INFO;
        }
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}
	@ResponseBody
	@RequestMapping(value = "/updateDispatcher", method = { RequestMethod.POST })
	public Map<String, Object> update(@RequestBody User user) {
		logger.info("修改用户调度台信息");
		int code = 0;
		if (user != null && StringUtils.isNotBlank(user.getUser_id())) {
			code = userService.updateUser(user);
		} else {
			code = ErrCode.INCOMPLETE_INFO;
		}
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}
	@ResponseBody
	@RequestMapping(value = "/modifyPassWord", method = { RequestMethod.POST })
	public Map<String, Object> modifyPassWord(@RequestBody User user) {
		logger.info("修改用户密码");
		int code = 0;
		User u = userService.getUserById(user.getUser_id());
		if(!u.getUser_pass().equals(user.getUser_pass())) {
			user.setUser_pass(Utils.enPassword(user.getUser_pass()));
		}
		if (user != null && StringUtils.isNotBlank(user.getUser_id())) {
			code = userService.updateUser(user, user.getUser_role_name());
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
	public Map<String, Object> userManageDelete(@RequestParam(value = "userId") String userId) {
		logger.info("删除用户信息");
		int code = userService.removeUser(userId);
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}

	@ResponseBody
	@RequestMapping(value = "/find", method = { RequestMethod.POST })
	public Map<String, Object> userManageFind(@RequestParam(value = "userId") String userId) {
		logger.info("查询单独用户信息");
		User user = userService.getUserById(userId);
		int code = 0;
		if (user != null) {
			code = ErrCode.USER_EXIST_ALREADY;
		}
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", code);
		ret.put("msg", ErrCode.getMessage(code));
		return ret;
	}

	@ResponseBody
	@RequestMapping(value = "/getRolesList", method = { RequestMethod.GET })
	public List<Role> getRolesList() {
		return userService.getRoleList();
	}
	
	@ResponseBody
	@RequestMapping(value = "/getAllDepot", method = { RequestMethod.GET })
	public DataTable<Depot> getAllDepot(@RequestParam Map<String, Object> params,HttpServletRequest request) {
		logger.info("查询用户部门");
		List<Depot> list = new ArrayList<>();
		DataTable<Depot> dt = new DataTable<Depot>();
		list = userService.getAllDepot(params);
		dt.setData(list);
		dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
		return dt;
    }
	
	@ResponseBody
	@RequestMapping(value = "/getAllWorkshop", method = { RequestMethod.GET })
	public DataTable<Workshop> getAllWorkshop(@RequestParam Map<String, Object> params,HttpServletRequest request) {
		logger.info("查询用户车间");
		List<Workshop> list = new ArrayList<>();
		DataTable<Workshop> dt = new DataTable<Workshop>();
		list = userService.getAllWorkshop(params);
		dt.setData(list);
		dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
		return dt;
    }
	
	@ResponseBody
	@RequestMapping(value = "/getAllTeam", method = { RequestMethod.GET })
	public DataTable<Team> getAllTeam(@RequestParam Map<String, Object> params,HttpServletRequest request) {
		logger.info("查询用户班组");
		List<Team> list = new ArrayList<>();
		DataTable<Team> dt = new DataTable<Team>();
		list = userService.getAllTeam(params);
		dt.setData(list);
		dt.setDraw(Integer.parseInt(request.getParameter("draw") == null ? "0" : request.getParameter("draw")) + 1);
		return dt;
    }

}
