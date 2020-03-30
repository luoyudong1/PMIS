package com.kthw.common.shiro;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by YFZX-WB on 2017/5/3.
 */
public class FramePermissionsAuthorizationFilter
        extends PermissionsAuthorizationFilter {

    private static final Logger logger = LoggerFactory.getLogger(FramePermissionsAuthorizationFilter.class);

    @Override
    public boolean isAccessAllowed(ServletRequest request,
            ServletResponse response, Object mappedValue) throws IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        if (uri.endsWith(".html")) {
            uri = uri.substring(0, uri.length() - 5)
                    .replace("/pages", "");
        }
        int i = uri.indexOf(contextPath);
        if (i > -1) {
            uri = uri.substring(i + contextPath.length());
        }
        logger.info("request uri is: " + uri);
        if (StringUtils.isBlank(uri) || "/".equals(uri) || "/index".equals(uri)) {
            return true;
        } else {
            Subject subject = getSubject(request, response);
            return subject.isPermitted(uri);
        }
    }

}
