package com.kthw.pmis.entiy.ext;

import com.kthw.pmis.entiy.SheetInfo;

public class SheetInfoExt extends SheetInfo{
	private String sourceStoreHouseName;
	private String objectStoreHouseName;
	public String getSourceStorehouseName() {
		return sourceStoreHouseName;
	}
	public void setSourceStorehouseName(String sourceStorehouseName) {
		this.sourceStoreHouseName = sourceStorehouseName;
	}
	public String getObjectStorehouseName() {
		return objectStoreHouseName;
	}
	public void setObjectStorehouseName(String objectStorehouseName) {
		this.objectStoreHouseName = objectStorehouseName;
	}

}
