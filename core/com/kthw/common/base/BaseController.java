package com.kthw.common.base;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.kthw.pmis.service.InitDictionaryService;

import net.sf.json.JSONObject;

public class BaseController implements ApplicationContextAware {

    public static final String SUCCESS = "success";
    public static final String ERROR = "error";
    public static final String PROJECT_EXIST = "project_exist";

    private byte[] lock = new byte[0];

    @Autowired
    InitDictionaryService initDictionaryService;

    public Map<String, Object> parseParams(String paramStr) {
        Map<String, Object> param = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(paramStr)) {
            JSONObject jsonObject = JSONObject.fromObject(paramStr);
            Iterator<String> it = jsonObject.keys();
            while (it.hasNext()) {
                String key = it.next();
                String value = jsonObject.getString(key);
                param.put(key, value);
            }
        }
        return param;
    }

    public Map<String, String> parseParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Enumeration<String> keys = request.getParameterNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (!(key.startsWith("columns") || key.startsWith("search") || key.startsWith("_"))) {
                if (key.startsWith("order")) {
                    if (key.lastIndexOf("column") != -1) {
                        params.put("orderColumn", request.getParameter(String.format("columns[%s][data]", request.getParameter(key))));
                    } else if (key.lastIndexOf("dir") != -1) {
                        params.put("orderDir", request.getParameter(key));
                    }
                } else {
                    params.put(key, request.getParameter(key));
                }
            }
        }
        return params;
    }

   /* public AppCache getAppCache() {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext servletContext = webApplicationContext.getServletContext();
        AppCache appcache = (AppCache) servletContext.getAttribute("APP_CACHE");
        if (appcache == null) {
            synchronized (lock) {
                appcache = (AppCache) servletContext.getAttribute("APP_CACHE");
                if (appcache == null) {
                    appcache = initDictionaryService.createAppChche();
                    servletContext.setAttribute("APP_CACHE", appcache);
                }
            }
        }
        return appcache;
    }*/

    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        // TODO Auto-generated method stub

    }

    /**
     * 取出Shiro中的当前用户Id.
     */
//	public Long getCurrentUserId() {
//		log.debug("debug");
//		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
//		return user.getId();
//	}
//
//	/**
//	 * 更新Shiro中当前用户的用户名.
//	 */
//	public void updateCurrentUserName(String userName) {
//		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
//		user.setLoginName(userName);
//	}
//	
//	/**
//	 * 取出Shiro中的当前用户LoginName.
//	 */
//	public String getCurrentUserName() {
//		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
//		return user.getLoginName();
//	}
//	
//	/**
//	 * 取出Shiro中的当前用户userId.
//	 */
//	public String getLoginUserId() {
//		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
//		if(user == null){
//			return null;
//		}
//		return user.getUserId();
//	}


}
