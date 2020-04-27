package com.kthw.pmis._enum;

public enum StoreHouseEnum {
	TEST_PURCHASE(1, "机辆检测所新购库"),
	TEST_OVERHAUL(2, "机辆检测所检修室"),
	TEST_DELIVERY(3, "机辆检测所配送库"),
	TEST_SEND_REPAIR(4, "机辆检测所送修库"),
	TEST_SEND_FACTORY(5, "机辆检测所返厂库"),
	FACTORY_SEND_TEST(6, "机辆检测所修返库"),
	TEST_UNCHECK(7, "机辆检测所待检库"),
	SCRAP(8, "机辆检测所报废库"),
	RECEIPT(9, "收货库"),
	REWORKTRANSFER(10, "备品库"),
	USE(11, "使用库"),
	SEND_REPAIR(12, "送修库"),
	TEST_PRODUCE(13, "机辆检测所生产库"),
	TEST_PREPARE_PURCHASE(14, "机辆检测预采购库"),
	SUPPLIER_FACTORY(15, "厂家库"),
	MID_SHAPGUAN_SEND_TEST(18, "韶关班组库中间库"),
	SHAPGUAN_PREPARE_USE(19, "韶关班组备品库"),
	SHAPGUAN_SEND_TEST(20, "韶关班组送修库")
    ;
	private int id;
    private String name;
    
    private StoreHouseEnum(int id, String name) {
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
