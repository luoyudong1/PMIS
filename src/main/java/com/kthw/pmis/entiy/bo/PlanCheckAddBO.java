package com.kthw.pmis.entiy.bo;

import com.kthw.pmis.entiy.PlanCheck;

import java.util.List;

public class PlanCheckAddBO {
    private int biMonthCount;
    private int singleMonthCount;
    private int halfMonthCount;
    private List<PlanCheck> list;

    public int getBiMonthCount() {
        return biMonthCount;
    }

    public void setBiMonthCount(int biMonthCount) {
        this.biMonthCount = biMonthCount;
    }

    public int getSingleMonthCount() {
        return singleMonthCount;
    }

    public void setSingleMonthCount(int singleMonthCount) {
        this.singleMonthCount = singleMonthCount;
    }

    public int getHalfMonthCount() {
        return halfMonthCount;
    }

    public void setHalfMonthCount(int halfMonthCount) {
        this.halfMonthCount = halfMonthCount;
    }

    public List<PlanCheck> getList() {
        return list;
    }

    public void setList(List<PlanCheck> list) {
        this.list = list;
    }
}
