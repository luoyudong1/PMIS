package com.kthw.pmis.service;

import com.kthw.common.base.AppCache;

public interface InitDictionaryService {

	/**
	 * 说明：缓存相关字典放入Application级的内存中。方便调用
	 * 放入方法：
	 * 1.新建缓存类：AppCache appcache = new AppCache();
	 * 2.通过appcache的set方法放入字典
	 * 
	 * 在controller中调用的方法是：
	 * 1.让controller类继承 BaseController
	 * 2.在方法中 调用基类方法获得对象：AppCache appcache = getAppCache();
	 * 3.通过appcache中的get方法获取字典信息
	 * */
	
	public AppCache createAppChche();
	

}
