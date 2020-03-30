package com.kthw.common.base;

import java.util.List;

import com.kthw.pmis.model.system.LuJu;

/**
 * 用于应用级数据缓存
 */
public class AppCache {

	private List<LuJu> bureauList;
	
	public List<LuJu> getBureauList() {
		return bureauList;
	}

	public void setBureauList(List<LuJu> bureauList) {
		this.bureauList = bureauList;
	}
}
