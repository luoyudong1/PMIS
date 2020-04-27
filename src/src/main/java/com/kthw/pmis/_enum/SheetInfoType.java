package com.kthw.pmis._enum;
/**
 * 单据类型枚举
 * @author 86135
 *
 */
public enum SheetInfoType {
	PREPATEPURCHASE(0, "预采购入库"),
	PURCHASE(1, "采购入库"),
	PRODUCTION(2, "生产入库"),
	REPAIRIN(3, "返修入库"),
	REPAIROUT(4, "返修出库"),
	SCRAPOUT(5, "报废出库"),
	CHECKIN(6, "检修入库"),
	CHECKOUT(7, "检修出库"),
	REPAIRCHECK(8, "检修核准"),
	REWORKTRANSFER(9, "返修调拨"),
	DISTRIBUTIONTRANSFER(10, "配送"),
	FAULTREMOVEL(11, "故障拆卸"),
	PARTSINSTALLATION(12, "配件使用安装"),
	WORKSHOP_TO_DEPOT(13, "车间到班组"),
	DEPOT_TO_WORKSHOP(14, "班组到车间"),
	SPARES_TO_WORKSHOP(15, "班组备品到车间"),
	SPARES_AND_REPAIR(16, "备品库到送修库调配"),
	PURCHASE_TO_DELIVERY(17, "新购库到配送库"),
	PARTS_CONSUME(18, "配件消耗"),
	PARTS_ADD(19, "配件新增"),
	REPAIR_FACTORY(20, "班组返厂"),
	FACTORY_RETURN(21,"厂家返回班组"),
	REPAIR_AND_SPARES(22,"送修库到备品库"),
    ;
	private int id;
    private String name;
    
    private SheetInfoType(int id, String name) {
		this.id = id;
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
