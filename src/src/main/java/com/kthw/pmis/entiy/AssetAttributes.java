package com.kthw.pmis.entiy;

/**
 *
 * @mbg.generated do_not_delete_during_merge 2019-10-28 10:53:31
 */
public class AssetAttributes {
    /**
     * Database Column Remarks:
     *   tbl_asset_attributes.asset_attributes_id: 
     */
    private Short assetAttributesId;

    /**
     * Database Column Remarks:
     *   tbl_asset_attributes.asset_attributes_name:
     */
    private String assetAttributesName;

    /**
     * Database Column Remarks:
     *   tbl_asset_attributes.depot_id: 
     */
    private Long depotId;

    /**
     * @return the value of tbl_asset_attributes.asset_attributes_id
     */
    public Short getAssetAttributesId() {
        return assetAttributesId;
    }

    /**
     * @param assetAttributesId the value for tbl_asset_attributes.asset_attributes_id
     */
    public void setAssetAttributesId(Short assetAttributesId) {
        this.assetAttributesId = assetAttributesId;
    }

    /**
     * @return the value of tbl_asset_attributes.asset_attributes_name
     */
    public String getAssetAttributesName() {
        return assetAttributesName;
    }

    /**
     * @param assetAttributesName the value for tbl_asset_attributes.asset_attributes_name
     */
    public void setAssetAttributesName(String assetAttributesName) {
        this.assetAttributesName = assetAttributesName == null ? null : assetAttributesName.trim();
    }

    /**
     * @return the value of tbl_asset_attributes.depot_id
     */
    public Long getDepotId() {
        return depotId;
    }

    /**
     * @param depotId the value for tbl_asset_attributes.depot_id
     */
    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", assetAttributesId=").append(assetAttributesId);
        sb.append(", assetAttributesName=").append(assetAttributesName);
        sb.append(", depotId=").append(depotId);
        sb.append("]");
        return sb.toString();
    }
}