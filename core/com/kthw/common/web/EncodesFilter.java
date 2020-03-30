package com.kthw.common.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @deprecated：针对前台页面提交的中文字符进行转码，统一采用UTF-8的编码方式；不需要在controller中再单独处理
 * 前台页面如果要兼容所有浏览器，须采用encodeURI将中文参数进行转码，最保险的方式是进行2次转码:encodeURI(encodeURI())
 * @author WZH
 * @Date: 2018/3/21
 */
public class EncodesFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(EncodesFilter.class);

	public String filter(HttpServletRequest request, String input) {
		String ret = input;
		// ios客户端请求参数值可能为(null)服务端过滤掉当null处理即可
		if (input == null || input.trim().equals("(null)")) {
			ret = null;
			return ret;
		}
		// 判断是不是AJAX请求，如果是进行过滤
		if (request.getHeader("x-requested-with") != null
				&& "XMLHttpRequest".equalsIgnoreCase(request.getHeader("x-requested-with"))) {
			try {
				ret = URLDecoder.decode(ret, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.info("URLDecoder.decode error!");
				e.printStackTrace();
			}
		}
		// 该处可以实现各种业务的自定义的过滤机制 判断是GET或者POST 或者是andriod判断等等
		/***
		 * final String userAgent = request.getHeader("User-Agent"); final
		 * String method = request.getMethod(); if
		 * (method.equalsIgnoreCase("get") ||
		 * userAgent.toLowerCase().indexOf("android") != -1) { try { ret = new
		 * String(input.getBytes("ISO8859-1"), "utf-8"); } catch
		 * (UnsupportedEncodingException e) { e.printStackTrace(); } }
		 */
		return ret;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		filterChain.doFilter(new HttpServletRequestWrapper(request) {
			@Override
			public String getParameter(String name) {
				String value = super.getParameter(name);
				return filter(this, value);
			}

			@Override
			public String[] getParameterValues(String name) {
				String[] values = super.getParameterValues(name);
				if (values == null) {
					return null;
				}
				for (int i = 0; i < values.length; i++) {
					values[i] = filter(this, values[i]);
				}
				return values;
			}

		}, response);
	}
}
