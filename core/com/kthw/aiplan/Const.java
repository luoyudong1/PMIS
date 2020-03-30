package com.kthw.aiplan;

public class Const {

	public final static int GROUPED_STATUS_NOGROUP = 0;
	public final static int GROUPED_STATUS_LESS = 1;
	public final static int GROUPED_STATUS_MORE = 2;
	public final static int GROUPED_STATUS_NORMAL = 3;
	public final static int GROUPED_STATUS_COMMITED = 9;
	
	public final static int GROUPED_NUM_SUB = 2;
	public final static int GROUPED_NUM_SUP = 6;
	
	public final static int DATE_THRESHOLD = 32;
	public final static int MILE_THRESHOLD = 50000;
	public final static int WHEEL_FIX_THRESHOLD = 30000;
	public final static double THRESHOLD_FLOAT_PERCENTAGE = 0.15;//匹配不到时，以该变化率修改阈值范围
	public final static int DATE_THRESHOLD_MAX = 62;//date差值的上限
	public final static int MILE_THRESHOLD_MAX = 150000;//
	public final static int WHEEL_FIX_THRESHOLD_MAX = 100000;//
	
	public final static int VEH_CLASS_YW = 1;
	public final static int VEH_CLASS_YZ = 2;
	public final static int VEH_CLASS_RW = 3;
	
	public static void main(String[] args) {

	}

}
