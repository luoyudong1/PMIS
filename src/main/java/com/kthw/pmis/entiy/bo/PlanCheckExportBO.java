package com.kthw.pmis.entiy.bo;

import com.kthw.pmis.entiy.PlanCheck;

import java.util.List;

public class PlanCheckExportBO {
    private String sheetId;
    private Integer year;
    private Integer month;
    private List<PlanCheck> list;

    public String getSheetId() {
        return sheetId;
    }

    public void setSheetId(String sheetId) {
        this.sheetId = sheetId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public List<PlanCheck> getList() {
        return list;
    }

    public void setList(List<PlanCheck> list) {
        this.list = list;
    }
}
