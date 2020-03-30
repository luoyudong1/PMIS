package com.kthw.pmis.model.system;

/**
 * 
 * 
 * @author Ivan
 * @date 2017年1月24日
 */
public class KtProductDict {
	
	private String productId;
	
	private int baseClassId;
	
	private String productName;
	
	private int modelId;
	
	private int productTypeId;
	
	private int stockAmount;
	
	private String spec;
	
	private String productUnit;
	
	private String flag;
	
	private String addDate;
	
	private int dLevel;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getBaseClassId() {
		return baseClassId;
	}

	public void setBaseClassId(int baseClassId) {
		this.baseClassId = baseClassId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getModelId() {
		return modelId;
	}

	public void setModelId(int modelId) {
		this.modelId = modelId;
	}

	public int getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(int productTypeId) {
		this.productTypeId = productTypeId;
	}

	public int getStockAmount() {
		return stockAmount;
	}

	public void setStockAmount(int stockAmount) {
		this.stockAmount = stockAmount;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getProductUnit() {
		return productUnit;
	}

	public void setProductUnit(String productUnit) {
		this.productUnit = productUnit;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getAddDate() {
		return addDate;
	}

	public void setAddDate(String addDate) {
		this.addDate = addDate;
	}

	public int getdLevel() {
		return dLevel;
	}

	public void setdLevel(int dLevel) {
		this.dLevel = dLevel;
	}

	@Override
	public String toString() {
		return "KtProductDict [productId=" + productId + ", baseClassId=" + baseClassId + ", productName=" + productName
				+ ", modelId=" + modelId + ", productTypeId=" + productTypeId + ", stockAmount=" + stockAmount
				+ ", spec=" + spec + ", productUnit=" + productUnit + ", flag=" + flag + ", addDate=" + addDate
				+ ", dLevel=" + dLevel + "]";
	}
	
	

}
