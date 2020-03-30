package com.kthw.pmis.helper;

import com.kthw.pmis.entiy.SheetDetail;
import com.kthw.pmis.entiy.SheetDetailKey;
import com.kthw.pmis.entiy.StockInfo1;
import com.kthw.pmis.entiy.dto.StockInfoDTO;
import com.kthw.pmis.mapper.common.SheetDetailMapper;
import com.kthw.pmis.mapper.common.StockInfo1Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Component("sheetDetailHelper")
public class SheetDetailHelper {
    @Autowired
    private StockInfo1Mapper stockInfoMapper;
    @Autowired
    private SheetDetailMapper sheetDetailMapper;

    public SheetDetail repalce(SheetDetail sheetDetail) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        StockInfo1 stockInfo = stockInfoMapper.selectByPrimaryKey(sheetDetail.getPartCode());
        if (stockInfo != null && stockInfo.getSheetId() != null) {
            SheetDetailKey sheetDetailKey = new SheetDetailKey();
            sheetDetailKey.setPartCode(stockInfo.getFactoryPartsCode());
            sheetDetailKey.setSheetId(stockInfo.getSheetId());
            SheetDetail sourceSheetDetail = sheetDetailMapper.selectByPrimaryKey(sheetDetailKey);
            if (sourceSheetDetail != null) {
                Field[] field = sourceSheetDetail.getClass().getDeclaredFields(); //获取实体类的所有属性，返回Field数组
                for (int j = 0; j < field.length; j++) { //遍历所有属性
                    String name = field[j].getName(); //获取属性的名字
                    name = name.substring(0, 1).toUpperCase() + name.substring(1); //将属性的首字符大写，方便构造get，set方法
                    String type = field[j].getGenericType().toString(); //获取属性的类型
                    Class[] paramType = new Class[field.length];
                    paramType[j] = field[j].getType();
                    Method getM1 = sourceSheetDetail.getClass().getMethod("get" + name);
                    Method getM2 = sheetDetail.getClass().getMethod("get" + name);
                    Method setM2 = sheetDetail.getClass().getMethod("set" + name, paramType[j]);
                    if (type.equals("class java.lang.String")) { //如果type是类类型，则前面包含"class "，后面跟类名
                        String value1 = (String) getM1.invoke(sourceSheetDetail);//调用getter方法获取属性值
                        String value2 = (String) getM2.invoke(sheetDetail);//调用getter方法获取属性值

                        if (value2 == null && value1 != null) {
                            setM2.invoke(sheetDetail, value1);
                        }
                    }
                    if (type.equals("class java.lang.Integer")) {

                        Integer value1 = (Integer) getM1.invoke(sourceSheetDetail);//调用getter方法获取属性值
                        Integer value2 = (Integer) getM2.invoke(sheetDetail);//调用getter方法获取属性值
                        if (value2 == null && value1 != null) {
                            setM2.invoke(sheetDetail, value1);
                        }
                    }if (type.equals("class java.math.BigDecimal")) {

                        BigDecimal value1 = (BigDecimal) getM1.invoke(sourceSheetDetail);//调用getter方法获取属性值
                        BigDecimal value2 = (BigDecimal) getM2.invoke(sheetDetail);//调用getter方法获取属性值
                        if (value2 == null && value1 != null) {
                            setM2.invoke(sheetDetail, value1);
                        }
                    }
                    if (type.equals("class java.lang.Short")) {
                        Short value1 = (Short) getM1.invoke(sourceSheetDetail);//调用getter方法获取属性值
                        Short value2 = (Short) getM2.invoke(sheetDetail);//调用getter方法获取属性值
                        if (value2 == null && value1 != null) {
                            setM2.invoke(sheetDetail, value1);
                        }
                    }
                    if (type.equals("class java.lang.Double")) {
                        Double value1 = (Double) getM1.invoke(sourceSheetDetail);//调用getter方法获取属性值
                        Double value2 = (Double) getM2.invoke(sheetDetail);//调用getter方法获取属性值
                        if (value2 == null && value1 != null) {
                            setM2.invoke(sheetDetail, value1);
                        }
                    }
                    if (type.equals("class java.lang.Boolean")) {
                        Boolean value1 = (Boolean) getM1.invoke(sourceSheetDetail);//调用getter方法获取属性值
                        Boolean value2 = (Boolean) getM2.invoke(sheetDetail);//调用getter方法获取属性值
                        if (value2 == null && value1 != null) {
                            setM2.invoke(sheetDetail, value1);
                        }
                    }
                    if (type.equals("class java.util.Date")) {
                        Date value1 =(Date) getM1.invoke(sourceSheetDetail);//调用getter方法获取属性值
                        Date value2 = (Date) getM2.invoke(sheetDetail);//调用getter方法获取属性值
                        if (value2 == null && value1 != null) {
                            setM2.invoke(sheetDetail, value1);
                        }
                    }
                    if (type.equals("class java.sql.Timestamp")) {
                        Timestamp value1 =(Timestamp) getM1.invoke(sourceSheetDetail);//调用getter方法获取属性值
                        Timestamp value2 = (Timestamp) getM2.invoke(sheetDetail);//调用getter方法获取属性值
                        if (value2 == null && value1 != null) {
                            setM2.invoke(sheetDetail, value1);
                        }
                    }

                }
            }

        }
        return sheetDetail;

    }
    public SheetDetail clearCheckedRecord(SheetDetail sheetDetail){
            sheetDetail.setPrepareCheck(null);
            sheetDetail.setMachineCheck(null);
            sheetDetail.setReplaceComponentCheck(null);
            sheetDetail.setCopyMachineStartTime(null);
            sheetDetail.setCopyMachineEndTime(null);
            sheetDetail.setCopyMachineCheck(null);
            sheetDetail.setCheckedPrice(null);
            sheetDetail.setCheckedDate(null);
            sheetDetail.setRepaireState(null);
            return sheetDetail;
    }
}


//    public static void main(String[] args) {
//        SheetDetail sheetDetail = new SheetDetail();
//        SheetDetail sourceSheetDetail = new SheetDetail();
//        sheetDetail.setPartId("xxxxxxxx");
//        sheetDetail.setPartCode("asdasd");
//        sourceSheetDetail.setFaultInfo("asdasdasdasd");
//        SheetDetailHelper sheetDetailHelper=new SheetDetailHelper();
//        try {
//            SheetDetail ret=sheetDetailHelper.repalce(sheetDetail, sourceSheetDetail);
//            System.out.println(ret.getFaultInfo());
//        }catch (Exception e){
//            System.out.println("error****************");
//        }
//
//    }

