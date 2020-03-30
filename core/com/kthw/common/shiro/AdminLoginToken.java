package com.kthw.common.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class AdminLoginToken extends UsernamePasswordToken {
	/** 描述 */
	private static final long serialVersionUID = -3178260335127476542L;

	private String captcha;
	
	private String channel;
	
	
	public AdminLoginToken(String username, String password,
			boolean rememberMe, String host, String captcha, String channel) {
		super(username, password, rememberMe, host);
		this.captcha = captcha;
		this.channel = channel;
	}
	
	public AdminLoginToken(String username, String password, boolean rememberMe, String captcha, String channel) {
		super(username, password, rememberMe);
		this.captcha = captcha;
		this.channel = channel;
	}
	
	public AdminLoginToken(String username, String password, String captcha, String channel) {
		super(username, password);
		this.captcha = captcha;
		this.channel = channel;
	}
	
	public AdminLoginToken(String username, String password) {
		super(username, password);
		this.captcha = captcha;
		this.channel = channel;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public AdminLoginToken() {
		super();
	}



	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

}
