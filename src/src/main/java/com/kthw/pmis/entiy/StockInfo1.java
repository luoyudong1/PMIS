package com.kthw.pmis.entiy;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @mbg.generated do_not_delete_during_merge 2019-09-04 22:27:48
 */
public class StockInfo1 {
    
    private String factoryPartsCode;


    private String partIdSeq;


    private Short storehouseId;


    private Short enabled;


    private Short assetAttributesId;
    
    private String sheetId;
    private Short warranty;
    private Short deviceId;//探测站id
    private Date createTime;//新增时间
    private Date updateTime;//修改时间
    private Date purchaseDate;//新购时间
    private BigDecimal purchasePrice;//新购单价
    private String remark;
    private String newPartCode;

    public String getNewPartCode() {
        return newPartCode;
    }

    public void setNewPartCode(String newPartCode) {
        this.newPartCode = newPartCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Short getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Short deviceId) {
		this.deviceId = deviceId;
	}

	private String sourcePartCode;
    public String getSourcePartCode() {
		return sourcePartCode;
	}

	public void setSourcePartCode(String sourcePartCode) {
		this.sourcePartCode = sourcePartCode;
	}

	public Short getWarranty() {
		return warranty;
	}

	public void setWarranty(Short warranty) {
		this.warranty = warranty;
	}

	private Short partsState;
    public Short getPartsState() {
		return partsState;
	}

	public void setPartsState(Short partsState) {
		this.partsState = partsState;
	}

	public String getSheetId() {
		return sheetId;
	}

	public void setSheetId(String sheetId) {
		this.sheetId = sheetId;
	}

	public String getFactoryPartsCode() {
        return factoryPartsCode;
    }

    /**
     * @param factoryPartsCode the value for tbl_stock_info.factory_parts_code
     */
    public void setFactoryPartsCode(String factoryPartsCode) {
        this.factoryPartsCode = factoryPartsCode == null ? null : factoryPartsCode.trim();
    }

    /**
     * @return the value of tbl_stock_info.part_id_seq
     */
    public String getPartIdSeq() {
        return partIdSeq;
    }

    /**
     * @param partIdSeq the value for tbl_stock_info.part_id_seq
     */
    public void setPartIdSeq(String partIdSeq) {
        this.partIdSeq = partIdSeq == null ? null : partIdSeq.trim();
    }

    /**
     * @return the value of tbl_stock_info.storehouse_id
     */
    public Short getStorehouseId() {
        return storehouseId;
    }

    /**
     * @param storehouseId the value for tbl_stock_info.storehouse_id
     */
    public void setStorehouseId(Short storehouseId) {
        this.storehouseId = storehouseId;
    }

    /**
     * @return the value of tbl_stock_info.enabled
     */
    public Short getEnabled() {
        return enabled;
    }

    /**
     * @param enabled the value for tbl_stock_info.enabled
     */
    public void setEnabled(Short enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the value of tbl_stock_info.asset_attributes_id
     */
    public Short getAssetAttributesId() {
        return assetAttributesId;
    }

    /**
     * @param assetAttributesId the value for tbl_stock_info.asset_attributes_id
     */
    public void setAssetAttributesId(Short assetAttributesId) {
        this.assetAttributesId = assetAttributesId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", factoryPartsCode=").append(factoryPartsCode);
        sb.append(", partIdSeq=").append(partIdSeq);
        sb.append(", storehouseId=").append(storehouseId);
        sb.append(", enabled=").append(enabled);
        sb.append(", assetAttributesId=").append(assetAttributesId);
        sb.append("]");
        return sb.toString();
    }
}