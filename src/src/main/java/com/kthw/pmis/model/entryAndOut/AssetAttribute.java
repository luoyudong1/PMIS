package com.kthw.pmis.model.entryAndOut;

/**
 * 资产属性
 *
 */
public class AssetAttribute {

	private int asset_attributes_id;//资产属性ID
	private String asset_attributes_name;//资产属性名称
	
	public int getAsset_attributes_id() {
		return asset_attributes_id;
	}
	public void setAsset_attributes_id(int asset_attributes_id) {
		this.asset_attributes_id = asset_attributes_id;
	}
	public String getAsset_attributes_name() {
		return asset_attributes_name;
	}
	public void setAsset_attributes_name(String asset_attributes_name) {
		this.asset_attributes_name = asset_attributes_name;
	}
	
}
