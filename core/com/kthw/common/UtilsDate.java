package com.kthw.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilsDate {

	/**
	 * Date类型的时间转换为String类型
	 * */
	public static String DateTransString(Date date){
		String dateStr=null;
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateStr=format.format(date);
		return dateStr;
	}
}
