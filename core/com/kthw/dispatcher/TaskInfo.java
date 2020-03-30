package com.kthw.dispatcher;

public class TaskInfo {
	PlatformInfo platform;
	Route route;
	public PlatformInfo getPlatform() {
		return platform;
	}
	public void setPlatform(PlatformInfo platform) {
		this.platform = platform;
	}
	public Route getRoute() {
		return route;
	}
	public void setRoute(Route route) {
		this.route = route;
	}
}
