package com.kthw.pmis.helper;

import com.kthw.pmis.entiy.dto.ComprehensiveStatisticsDTO;

import java.math.BigDecimal;

public class StatiscsReturnDataHelper {
    public static void initData(ComprehensiveStatisticsDTO cs) {
        // 添加返回参数
        cs.setDepotId(0L);
        cs.setDepotName("合计");
        //送修总数
        cs.setDepotToTestCount(0);
        //探头总数
        cs.setDetectorCount(0);
        //其他
        cs.setOtherCount(0);
        //所内修复总数
        cs.setTestRepairedCount(0);
        cs.setTestRepairWellDetectorCount(0);
        //所内修复其他数量
        cs.setTestRepairWellOtherCount(0);
        cs.setTestRepairPrice(0d);//万元
        // 在所内的数量
        cs.setInTestCount(0);
        // 报废数量
        cs.setScrapOutCount(0);
        //送厂检修数量
        cs.setRepairToFactoryCount(0);
        //送厂检修探头数量
        cs.setRepairToFactoryDetectorCount(0);
        //送厂检修其他数量
        cs.setRepairToFactoryOtherCount(0);
        //厂家修复数量
        cs.setRepairOutFactoryCount(0);
        //厂家修复探头数量
        cs.setFactoryRepairWellDetectorCount(0);
        //厂家修复其他数量
        cs.setFactoryRepairWellOtherCount(0);

        //获取厂家修复金额
        cs.setFactoryRepairPrice(0d);//万元

        //厂家修复探头金额
        cs.setFactoryRepairDetectorPrice(0d);//万元
        //厂家修复其他金额
        cs.setFactoryRepairOtherPrice(0d);//万元
        cs.setRepairInFactoryCount(0);
        cs.setInFactoryDetectorCount(0);
        cs.setInFactoryOtherCount(0);


    }

    public static void addData(ComprehensiveStatisticsDTO cs, ComprehensiveStatisticsDTO row) {
        //送修总数
        cs.setDepotToTestCount(cs.getDepotToTestCount() + row.getDepotToTestCount());
        //探头总数
        cs.setDetectorCount(cs.getDetectorCount() + row.getDetectorCount());
        //其他
        cs.setOtherCount(cs.getOtherCount() + row.getOtherCount());
        //所内修复总数
        cs.setTestRepairedCount(cs.getTestRepairedCount() + row.getTestRepairedCount());
        cs.setTestRepairWellDetectorCount(cs.getTestRepairWellDetectorCount() + row.getTestRepairWellDetectorCount());
        //所内修复其他数量
        cs.setTestRepairWellOtherCount(cs.getTestRepairWellOtherCount() + row.getTestRepairWellOtherCount());

        if (row.getTestRepairPrice()!=null) {
            BigDecimal b1 = new BigDecimal(Double.toString(cs.getTestRepairPrice()));
            BigDecimal b2 = new BigDecimal(Double.toString(row.getTestRepairPrice()));
            cs.setTestRepairPrice(b1.add(b2).doubleValue());
        }
        //在所内的数量
        if(row.getInTestCount()!=null){
            cs.setInTestCount(cs.getInTestCount()+row.getInTestCount());
        }
        // 报废数量
        cs.setScrapOutCount(cs.getScrapOutCount() + row.getScrapOutCount());
        //送厂检修数量
        cs.setRepairToFactoryCount(cs.getRepairToFactoryCount() + row.getRepairToFactoryCount());
        //送厂检修探头数量
        cs.setRepairToFactoryDetectorCount(cs.getRepairToFactoryDetectorCount() + row.getRepairToFactoryDetectorCount());
        //送厂检修其他数量
        cs.setRepairToFactoryOtherCount(cs.getRepairToFactoryOtherCount() + row.getRepairToFactoryOtherCount());
        //厂家修复数量
        cs.setRepairOutFactoryCount(cs.getRepairOutFactoryCount() + row.getRepairOutFactoryCount());
        //厂家修复探头数量
        cs.setFactoryRepairWellDetectorCount(cs.getFactoryRepairWellDetectorCount() + row.getFactoryRepairWellDetectorCount());
        //厂家修复其他数量
        cs.setFactoryRepairWellOtherCount(cs.getFactoryRepairWellOtherCount() + row.getFactoryRepairWellOtherCount());

        //获取厂家修复金额
        if (row.getFactoryRepairPrice() !=null) {
            BigDecimal b1 = new BigDecimal(Double.toString(cs.getFactoryRepairPrice()));
            BigDecimal b2 = new BigDecimal(Double.toString(row.getFactoryRepairPrice()));
            cs.setFactoryRepairPrice(b1.add(b2).doubleValue());
        }

        //厂家修复探头金额
        if (row.getFactoryRepairDetectorPrice() !=null) {
            BigDecimal b1 = new BigDecimal(Double.toString(cs.getFactoryRepairDetectorPrice()));
            BigDecimal b2 = new BigDecimal(Double.toString(row.getFactoryRepairDetectorPrice()));
            cs.setFactoryRepairDetectorPrice(b1.add(b2).doubleValue());
        }
        //厂家修复其他金额
        if (row.getFactoryRepairOtherPrice() !=null) {
            BigDecimal b1 = new BigDecimal(Double.toString(cs.getFactoryRepairOtherPrice()));
            BigDecimal b2 = new BigDecimal(Double.toString(row.getFactoryRepairOtherPrice()));
            cs.setFactoryRepairOtherPrice(b1.add(b2).doubleValue());
        }
        //获取在厂家数量
        if (row.getRepairInFactoryCount() !=null) {
            cs.setRepairInFactoryCount(cs.getRepairInFactoryCount() + row.getRepairInFactoryCount());//万元
        }

        //获取在厂家探头数量
        if (row.getInFactoryDetectorCount() !=null) {
            cs.setInFactoryDetectorCount(cs.getInFactoryDetectorCount() + row.getInFactoryDetectorCount());//万元
        }
        //获取在厂家其他数量
        if (row.getInFactoryOtherCount() !=null) {
            cs.setInFactoryOtherCount(cs.getInFactoryOtherCount() + row.getInFactoryOtherCount());//万元
        }
    }
}
