package com.kthw.common.shiro;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kthw.common.Utils;
import com.kthw.pmis.model.system.User;
import com.kthw.pmis.service.system.UserService;

public class ShiroDbRealm extends AuthorizingRealm {

	private static final Logger log = LoggerFactory.getLogger(ShiroDbRealm.class);

	private static final String authorizationCacheName = "shiro-authorizationCacheName";

	@Autowired
	protected UserService userService;

	/**
	 * ShiroDbRealm提供编码信息，用于密码密码比对 描述
	*/
	public ShiroDbRealm() {
		// 认证不缓存
		super.setAuthenticationCachingEnabled(false);
		// 授权
		super.setAuthorizationCacheName(authorizationCacheName);
	}

	/**
	 * 认证回调函数, 登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken)
			throws AuthenticationException {
		// UsernamePasswordToken对象用来存放提交的登录信息
		UsernamePasswordToken token = (UsernamePasswordToken) authToken;
		User user = userService.getUserById(token.getUsername());
		if (user != null) {
		    String password = Utils.dePassword(user.getUser_pass());
		    if (password.equals(String.valueOf(token.getPassword()))) {
                // 若存在，将此用户存放到登录认证info中
                AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.getUser_id(), password, getName());
                doGetAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());//因为在验证后不自动调用doGetAuthorizationInfo,所以采用此方法
                setSession("AUTH_USER", user);
                if(user.getUser_role()==12||user.getUser_role()==13)
				{
					setSessionTimeOut(72000000);}
                else {
					setSessionTimeOut(3600000);
				}
                return authenticationInfo;
            } else {
		        throw new IncorrectCredentialsException();
            }
		} else {
			return null;
		}
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 * 第一次需要授权的时候调用此方法将授权存入缓存，后续验证授权的时候不再访问本方法，只读取缓存中的权限即可
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		if (!SecurityUtils.getSubject().isAuthenticated()) {
			doClearCache(principals);
			SecurityUtils.getSubject().logout();
			return null;
		}
		// 获取登录时输入的用户名
		String userId = (String) principals.getPrimaryPrincipal();
		if (StringUtils.isNotBlank(userId)) {

			// 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			Set<String> roleset = userService.getUserRolesAsString(userId);
			// 用户的角色集合
			info.addRoles(roleset);
			Set<String> permisset = userService.getPermissionAsString(userId);
			// 用户的权限集合
			info.addStringPermissions(permisset);
			return info;
		} else {
			return null;
		}
	}

	/**
	 * 清除所有用户授权信息缓存.
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}

    private void setSession(Object key, Object value) {
        Subject currentUser = SecurityUtils.getSubject();
        if (null != currentUser) {
            Session session = currentUser.getSession();
            if (null != session) {
                session.setAttribute(key, value);
            }
        }
    }
	private void setSessionTimeOut(Integer time) {
		Subject currentUser = SecurityUtils.getSubject();
		if (null != currentUser) {
			Session session = currentUser.getSession();
			if (null != session) {
				session.setTimeout(time);
			}
		}
	}
	private Session getSession(Object key) {
		Subject currentUser = SecurityUtils.getSubject();
		if (null != currentUser) {
			Session session = currentUser.getSession();
			if (null != session) {
				return session;
			}
		}
				return null;

		}
	}


