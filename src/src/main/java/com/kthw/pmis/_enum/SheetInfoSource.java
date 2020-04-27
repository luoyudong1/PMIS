package com.kthw.pmis._enum;

public enum SheetInfoSource {
	REPAIRE(1, "预采购入库"),
	PURCHASE(2, "采购入库"),
	PRODUCTION(3, "生产入库"),
	REPAIRIN(4, "返修入库"),
	REPAIROUT(5, "返修出库"),
	SCRAPIN(6, "报废出库"),
	CHECKIN(7, "检修入库"),
	CHECKOUT(8, "检修出库"),
	REPAIRCHECK(9, "检修核准"),
	REWORKTRANSFER(10, "返修调拨"),
	DISTRIBUTIONTRANSFER(11, "配送"),
	FAULTREMOVEL(12, "故障拆卸"),
	PARTSINSTALLATION(13, "配件使用安装"),
    ;
	private int id;
    private String name;
    
    private SheetInfoSource(int id, String name) {
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
