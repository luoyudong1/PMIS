package com.kthw.pmis.controller;

import com.kthw.common.base.BaseController;
import com.kthw.common.base.ErrCode;
import com.kthw.pmis.entiy.Depot;
import com.kthw.pmis.helper.SystemConfig;
import com.kthw.pmis.mapper.common.DepotMapper;
import com.kthw.pmis.model.system.Role;
import com.kthw.pmis.model.system.User;
import com.kthw.pmis.model.system.UserLog;
import com.kthw.pmis.model.system.UserRole;
import com.kthw.pmis.service.system.UserService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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
    @Autowired
    private SystemConfig systemConfig;

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String getLoginPage(HttpSession session) {
        User user=(User)session.getAttribute("AUTH_USER");//ZF.add
        if (user != null) {
            Role role =userService.getRoleByUserRole(user.getUser_role());
            switch (role.getRole_code()){
                case "PMIS":
                    return "redirect:/pages/index.html? userRole="+user.getUser_role();
                case "TMIS":
                    return "redirect:/pages/index-dpch.html? userRole="+user.getUser_role();
                case "SET":
                    return "redirect:/pages/index-set.html? userRole="+user.getUser_role();
                default:
                    return "login";
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
        Map<String, Object> ret = new HashMap<String, Object>(); //ZFF.ADD
        String read=params.get("read");
        if(read.equals("true")){
            String version=systemConfig.jsVersion;
            String update=systemConfig.jsDate;
            String updateTime=systemConfig.jsTime;
            ret.put("version", version);
            ret.put("update", update);
            ret.put("updateTime",updateTime);
        }else{
            String userId = params.get("userid");
            String password = params.get("password");
            String remember = params.get("remember");
            Map<String, Object> rtmap = new HashMap<String, Object>();
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
            ret.put("errCode", errCode);
            ret.put("message", ErrCode.getMessage(errCode));
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

    @ResponseBody
    @RequestMapping(value = "returnlog", method = {RequestMethod.GET})
    public void returnlog(HttpServletResponse response, HttpSession httpSession) throws IOException {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            try {
                User user = (User) httpSession.getAttribute("AUTH_USER");
                if (user != null) {
                    logger.info(" 返回登录页:" + user.getUser_id());
                    user.setLogin_status(1);
                    userService.updateUser_Index(user);
                    response.sendRedirect("./pages/logined.html?logined="+user.getLogin_status());
                }

            } catch (Exception e) {
                logger.error("logout error: ", e);
            }
        }else{

            response.sendRedirect("login");
        }

    }


    @ResponseBody
    @RequestMapping(value = "logined", method = { RequestMethod.POST })
    public Map<String, Object> logined(HttpServletResponse response, HttpSession httpSession) throws IOException {
        Map<String, Object> ret = new HashMap<String, Object>();        //ZFF.ADD
        User user = (User) httpSession.getAttribute("AUTH_USER");
        if(user!=null){
            try{
                logger.info("login user is: ============" + user.getUser_id());
                Depot depot = depotMapper.selectByPrimaryKey(Long.valueOf(user.getDepot_id()));
                List<UserRole> roles = userService.getUserRole(user.getUser_id());
                ret.put("depotId",user.getDepot_id());
                ret.put("user_name",user.getUser_name());
                ret.put("depotName",depot.getDepotName());
                ret.put("roles",roles);
            }catch (Exception e) {
                logger.error("logout error: ", e);
            }
        }else{
            response.sendRedirect("login");
        }

        return ret;
    }

    @ResponseBody
    @RequestMapping(value = "loginAgain", method = { RequestMethod.GET })
    public boolean loginAgain(HttpSession httpSession) throws IOException {
        boolean ret;
        User user = (User) httpSession.getAttribute("AUTH_USER");
        if(user!=null){
            ret=true;
        }else{
            ret=false;
        }
        return ret;
    }

    @ResponseBody
    @RequestMapping(value = "getJsVersion", method = { RequestMethod.GET })
    public String getJsVersion(HttpServletResponse response){

        return systemConfig.jsVersion;
    }
}
