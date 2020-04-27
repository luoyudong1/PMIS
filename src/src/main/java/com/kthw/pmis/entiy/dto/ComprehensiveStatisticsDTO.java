package com.kthw.pmis.entiy.dto;

public class ComprehensiveStatisticsDTO {
    private Long depotId;//部门id
    private String depotName;//部门名称
    private Integer purchaseCount;//新购数量
    private Integer depotToTestCount;//送修数量
    private Integer repairToFactoryCount;//返厂数量
    private Integer repairInFactoryCount;//在厂数量
    private Integer repairOutFactoryCount;//修返数量
    private Integer testRepairedCount;//所内修复的数量
    private Integer InTestCount;//在所内的数量
    private Integer ScrapOutCount;//报废数量
    private Integer repairToDepotCount;//检修返回数量
    private Integer purchasePrice;//新购送到车间总金额
    private Integer repairPrice;//检修返回车间配件总金额
    private Integer detectorCount;//探头数量
    private Integer detectorPrice;//探头数量
    private Integer otherCount;//其他配件数量
    private Integer otherPrice;//其他配件总费用
    private Integer repairToTestdDetectorCount;//送修探头数量
    private Integer repairToTestOtherCount;//送修其他配件数量
    private Integer testRepairWellDetectorCount;//所内探头数量
    private Integer testRepairWellOtherCount;//送修其他配件数量
    private Integer repairToFactoryDetectorCount;//送厂检修探头数量
    private Integer repairToFactoryOtherCount;//送厂检修其他配件数量
    private Integer factoryRepairWellDetectorCount;//厂家修复探头数量
    private Integer factoryRepairWellOtherCount;//厂家修复其他配件数量
    private Integer InFactoryDetectorCount;//在厂探头数量
    private Integer InFactoryOtherCount;//在厂其他配件数量
    private String testRepairRatio;//所内修复比例
    private String factoryRepairRatio;//厂家修复比例
    private Double testRepairPrice;//所内修复金额
    private Double factoryRepairPrice;//厂家修复金额
    private Double factoryRepairDetectorPrice;//厂家修复探头金额
    private Double factoryRepairOtherPrice;//厂家修复其他金额

    public Double getTestRepairPrice() {
        return testRepairPrice;
    }

    public void setTestRepairPrice(Double testRepairPrice) {
        this.testRepairPrice = testRepairPrice;
    }

    public Double getFactoryRepairPrice() {
        return factoryRepairPrice;
    }

    public void setFactoryRepairPrice(Double factoryRepairPrice) {
        this.factoryRepairPrice = factoryRepairPrice;
    }

    public Double getFactoryRepairDetectorPrice() {
        return factoryRepairDetectorPrice;
    }

    public void setFactoryRepairDetectorPrice(Double factoryRepairDetectorPrice) {
        this.factoryRepairDetectorPrice = factoryRepairDetectorPrice;
    }

    public Double getFactoryRepairOtherPrice() {
        return factoryRepairOtherPrice;
    }

    public void setFactoryRepairOtherPrice(Double factoryRepairOtherPrice) {
        this.factoryRepairOtherPrice = factoryRepairOtherPrice;
    }

    public String getTestRepairRatio() {
        return testRepairRatio;
    }

    public void setTestRepairRatio(String testRepairRatio) {
        this.testRepairRatio = testRepairRatio;
    }

    public String getFactoryRepairRatio() {
        return factoryRepairRatio;
    }

    public void setFactoryRepairRatio(String factoryRepairRatio) {
        this.factoryRepairRatio = factoryRepairRatio;
    }

    public Integer getInFactoryDetectorCount() {
        return InFactoryDetectorCount;
    }

    public void setInFactoryDetectorCount(Integer inFactoryDetectorCount) {
        InFactoryDetectorCount = inFactoryDetectorCount;
    }

    public Integer getInFactoryOtherCount() {
        return InFactoryOtherCount;
    }

    public void setInFactoryOtherCount(Integer inFactoryOtherCount) {
        InFactoryOtherCount = inFactoryOtherCount;
    }

    public Integer getRepairToTestdDetectorCount() {
        return repairToTestdDetectorCount;
    }

    public void setRepairToTestdDetectorCount(Integer repairToTestdDetectorCount) {
        this.repairToTestdDetectorCount = repairToTestdDetectorCount;
    }

    public Integer getRepairToTestOtherCount() {
        return repairToTestOtherCount;
    }

    public void setRepairToTestOtherCount(Integer repairToTestOtherCount) {
        this.repairToTestOtherCount = repairToTestOtherCount;
    }

    public Integer getTestRepairWellDetectorCount() {
        return testRepairWellDetectorCount;
    }

    public void setTestRepairWellDetectorCount(Integer testRepairWellDetectorCount) {
        this.testRepairWellDetectorCount = testRepairWellDetectorCount;
    }

    public Integer getTestRepairWellOtherCount() {
        return testRepairWellOtherCount;
    }

    public void setTestRepairWellOtherCount(Integer testRepairWellOtherCount) {
        this.testRepairWellOtherCount = testRepairWellOtherCount;
    }

    public Integer getRepairToFactoryDetectorCount() {
        return repairToFactoryDetectorCount;
    }

    public void setRepairToFactoryDetectorCount(Integer repairToFactoryDetectorCount) {
        this.repairToFactoryDetectorCount = repairToFactoryDetectorCount;
    }

    public Integer getRepairToFactoryOtherCount() {
        return repairToFactoryOtherCount;
    }

    public void setRepairToFactoryOtherCount(Integer repairToFactoryOtherCount) {
        this.repairToFactoryOtherCount = repairToFactoryOtherCount;
    }

    public Integer getFactoryRepairWellDetectorCount() {
        return factoryRepairWellDetectorCount;
    }

    public void setFactoryRepairWellDetectorCount(Integer factoryRepairWellDetectorCount) {
        this.factoryRepairWellDetectorCount = factoryRepairWellDetectorCount;
    }

    public Integer getFactoryRepairWellOtherCount() {
        return factoryRepairWellOtherCount;
    }

    public void setFactoryRepairWellOtherCount(Integer factoryRepairWellOtherCount) {
        this.factoryRepairWellOtherCount = factoryRepairWellOtherCount;
    }

    public Integer getDetectorCount() {
        return detectorCount;
    }

    public void setDetectorCount(Integer detectorCount) {
        this.detectorCount = detectorCount;
    }

    public Integer getDetectorPrice() {
        return detectorPrice;
    }

    public void setDetectorPrice(Integer detectorPrice) {
        this.detectorPrice = detectorPrice;
    }

    public Integer getOtherCount() {
        return otherCount;
    }

    public void setOtherCount(Integer otherCount) {
        this.otherCount = otherCount;
    }

    public Integer getOtherPrice() {
        return otherPrice;
    }

    public void setOtherPrice(Integer otherPrice) {
        this.otherPrice = otherPrice;
    }

    public Integer getRepairPrice() {
        return repairPrice;
    }

    public void setRepairPrice(Integer repairPrice) {
        this.repairPrice = repairPrice;
    }

    public Integer getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Integer purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Integer getInTestCount() {
        return InTestCount;
    }

    public void setInTestCount(Integer inTestCount) {
        InTestCount = inTestCount;
    }

    public Integer getTestRepairedCount() {
        return testRepairedCount;
    }

    public void setTestRepairedCount(Integer testRepairedCount) {
        this.testRepairedCount = testRepairedCount;
    }

    public Long getDepotId() {
        return depotId;
    }

    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public Integer getPurchaseCount() {
        return purchaseCount;
    }

    public void setPurchaseCount(Integer purchaseCount) {
        this.purchaseCount = purchaseCount;
    }

    public Integer getDepotToTestCount() {
        return depotToTestCount;
    }

    public void setDepotToTestCount(Integer depotToTestCount) {
        this.depotToTestCount = depotToTestCount;
    }

    public Integer getRepairToFactoryCount() {
        return repairToFactoryCount;
    }

    public void setRepairToFactoryCount(Integer repairToFactoryCount) {
        this.repairToFactoryCount = repairToFactoryCount;
    }

    public Integer getRepairInFactoryCount() {
        return repairInFactoryCount;
    }

    public void setRepairInFactoryCount(Integer repairInFactoryCount) {
        this.repairInFactoryCount = repairInFactoryCount;
    }

    public Integer getRepairOutFactoryCount() {
        return repairOutFactoryCount;
    }

    public void setRepairOutFactoryCount(Integer repairOutFactoryCount) {
        this.repairOutFactoryCount = repairOutFactoryCount;
    }

    public Integer getScrapOutCount() {
        return ScrapOutCount;
    }

    public void setScrapOutCount(Integer scrapOutCount) {
        ScrapOutCount = scrapOutCount;
    }

    public Integer getRepairToDepotCount() {
        return repairToDepotCount;
    }

    public void setRepairToDepotCount(Integer repairToDepotCount) {
        this.repairToDepotCount = repairToDepotCount;
    }
}
