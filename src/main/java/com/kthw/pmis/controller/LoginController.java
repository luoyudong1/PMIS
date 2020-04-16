package com.kthw.pmis.controller;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kthw.pmis.entiy.Depot;
import com.kthw.pmis.mapper.common.DepotMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kthw.common.GsonUtils;
import com.kthw.common.base.BaseController;
import com.kthw.common.base.ErrCode;
import com.kthw.pmis.model.system.User;
import com.kthw.pmis.model.system.UserLog;
import com.kthw.pmis.service.system.UserService;

/**
 * 描述:登录注销Controller.
 */
@Controller
@RequestMapping(value = "/")
public class LoginController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private DepotMapper depotMapper;

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String getLoginPage(HttpSession session) {
        User user=(User)session.getAttribute("AUTH_USER");      //ZF.add
        if (user != null) {
            if(user.getUser_role()<12) {
                return "redirect:/pages/index.html";
            }else{
                if(user.getUser_role()>11){
                    return "redirect:/pages/index-dpch.html";
                }else{
                    return "login";
                }
            }

        } else {
            return "login";
        }
    }

    private List<Session> getLoginedSession() {
        //获得当前登录用户的全部session
        Collection<Session> list = ((DefaultSessionManager) ((DefaultSecurityManager) SecurityUtils
                .getSecurityManager()).getSessionManager()).getSessionDAO()
                .getActiveSessions();
        List<Session> loginedList = new ArrayList<Session>();
        //User loginUser = (User) currentUser.getSession().;//获得当前用户信息
        for (Session session : list) {

            Subject s = new Subject.Builder().session(session).buildSubject();

            if (s.isAuthenticated()) {

                loginedList.add(session);//把除当前登录用户的其他的同名用户session信息加入集合
            }


        }
        return loginedList;
    }
private void clear(List<Session> list){

}
    @ResponseBody
    @RequestMapping(value = "login", method = {RequestMethod.POST})
    public Map<String, Object> login(@RequestBody Map<String, String> params, HttpServletRequest request) {
        String userId = params.get("userid");
        String password = params.get("password");
        String remember = params.get("remember");
        Map<String, Object> rtmap = new HashMap<String, Object>();
        Map<String, Object> ret = new HashMap<String, Object>();        //ZFF.ADD
        int errCode = 0;
        logger.info("login user is: " + userId + " ip:" + request.getRemoteAddr());
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(password)) {
            Subject currentUser = SecurityUtils.getSubject();
            List<Session> list = getLoginedSession();
            for (Session session : list) {
                User user = (User) session.getAttribute("AUTH_USER");
                if (user != null) {
                    String loginUserId = user.getUser_id();
                    if(loginUserId.equals(userId)){
                        session.stop();
                        break;
                    }
                }
            }
            UsernamePasswordToken token = new UsernamePasswordToken(userId, password);
            if ("checked".equals(remember)) {
                token.setRememberMe(true);
            }
            try {
                currentUser.login(token);
                UserLog record = new UserLog(userId, request.getRemoteAddr(), new Date());
                userService.login(record);
            } catch (UnknownAccountException e) {
                errCode = ErrCode.USER_NOT_EXIST;
            } catch (IncorrectCredentialsException e) {
                errCode = ErrCode.PASSWORD_ERROR;
            } catch (Exception e) {
                logger.error("login error: ", e);
                errCode = ErrCode.DB_ERROR;
            }

        } else {
            errCode = ErrCode.USER_PASSWORD_FORMAT_ERROR;
        }
        //rtmap.put("errCode", errCode);
        //rtmap.put("message", ErrCode.getMessage(errCode));
        //String result = GsonUtils.object2Json(rtmap);
        //return result;
        ret.put("errCode", errCode);
        ret.put("message", ErrCode.getMessage(errCode));
        if(errCode==0){
            User user = userService.getUserById(userId);
            Depot depot = depotMapper.selectByPrimaryKey(Long.valueOf(user.getDepot_id()));
            ret.put("user_name",user.getUser_name());
            ret.put("depotName",depot.getDepotName());
            ret.put("user_role",user.getUser_role());
        }

        return ret;
    }

    @ResponseBody
    @RequestMapping(value = "logout", method = {RequestMethod.GET})
    public void logout(HttpServletResponse response, HttpSession httpSession) throws IOException {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            try {
                User user = (User) httpSession.getAttribute("AUTH_USER");
                if (user != null) {
                    logger.info("登出用户:" + user.getUser_id());
                    userService.logout(user.getUser_id());
                }
                subject.logout();
            } catch (Exception e) {
                logger.error("logout error: ", e);
            }
        }
        response.sendRedirect("login");
    }

}
