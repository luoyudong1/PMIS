package com.kthw.pmis.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kthw.common.Utils;
import com.kthw.common.base.ErrCode;
import com.kthw.pmis.model.system.AuthFunction;
import com.kthw.pmis.model.system.MenuNode;
import com.kthw.pmis.model.system.User;
import com.kthw.pmis.model.system.UserRole;
import com.kthw.pmis.service.system.UserService;


@Controller
@RequestMapping(value = "/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;


    @ResponseBody
    @RequestMapping(value = "/modifyPassword", method = { RequestMethod.POST })
    public Map<String, Object> ModifyPassword(@RequestBody Map<String, String> params, HttpSession httpSession) {
        int code;
        if (Utils.checkPassword(params.get("newPas"))) {
            User user = (User) httpSession.getAttribute("AUTH_USER");
            if (user != null) {
                if (Utils.enPassword(params.get("password")).equals(user.getUser_pass())) { // 口令验证
                	user.setUser_pass(Utils.enPassword(params.get("newPas")));
                	code = userService.updateUser(user);
                    if (code == 0) {
                    }
                } else {
                    code = ErrCode.PASSWORD_ERROR;
                }
            } else {
                code = ErrCode.NOT_LOGIN;
            }
        } else {
            code = ErrCode.PASSWORD_ILLEGAL;
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

    @ResponseBody
    @RequestMapping(value = "/getRoleIDbyUser", method = { RequestMethod.GET })
    public Map<String, Object> getRoleIDbyUser(HttpSession httpSession) {
        logger.info("获取用户基本信息");
        User user = (User) httpSession.getAttribute("AUTH_USER");
        List<UserRole> roles = userService.getUserRole(user.getUser_id());
        List<MenuNode> menus = userService.buildUserMenu(user.getUser_id());
        List<AuthFunction> auths = userService.buildUserOperation(user.getUser_id());
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("userId", user.getUser_id());//用户id
        ret.put("userName", user.getUser_name());//用户名
        ret.put("departmentId", user.getDepartment_id());//部门
        ret.put("roles", roles);//角色信息
        ret.put("menus", menus);//菜单
        ret.put("auths", auths);//页面内元素的操作权限
        ret.put("idxUrl", user.getIdx_url());//用户首页
        return ret;
    }

    @ResponseBody
    @RequestMapping(value = "/modifyIdxUrl", method = { RequestMethod.POST })
    public Map<String, Object> modifyIdxUrl(@RequestParam(value = "userId") String userId,
                                            @RequestParam(value = "idxUrl") String userUrl, HttpSession session) {
        logger.info("提交首页显示");
        int code = 0;
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userUrl)) {
            code = userService.updateUserIdxUrl(userId, userUrl);
            if (code == 0) {
                User user = (User) session.getAttribute("AUTH_USER");
                user.setIdx_url(userUrl);
            }
        } else {
            code = ErrCode.INCOMPLETE_INFO;
        }
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("code", code);
        ret.put("msg", ErrCode.getMessage(code));
        return ret;
    }

  
}
