package com.kthw.pmis.controller.system;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kthw.pmis.model.system.StoreHouseInfo;
import com.kthw.pmis.model.system.User;
import com.kthw.pmis.service.system.StoreHouseManageService;

/**
 * 描述:仓库信息管理Controller.
 */
@Transactional
@Controller
@RequestMapping(value = "/system/storehouse")
public class StoreHouseManageController {

	private static Logger logger = LoggerFactory.getLogger(StoreHouseManageController.class);
	private User user=null;
	
	@Autowired
	private StoreHouseManageService  storeHouseManageService;
	
	@ResponseBody
	@RequestMapping(value = "/getUserStorehouseByUserId", method = { RequestMethod.GET })
	public List<StoreHouseInfo> getUserStorehouseList(@RequestParam Map<String, Object> params,HttpServletRequest request,HttpSession httpSession ) {
		user = (User) httpSession.getAttribute("AUTH_USER");
		logger.info("用户"+user.getUser_id()+" 查询用户管理的仓库信息;");
		params.put("user_id", user.getUser_id());
	    List<StoreHouseInfo> storehouses= storeHouseManageService.getUserStorehouseByUserId(params);
        return storehouses;
    }
}
