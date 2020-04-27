package com.kthw.pmis.model;

import java.util.List;

/**
 * Created by YFZX-WB on 2016/8/23.
 */
public class TableView<T> {

    public List<T> data;

    public String key;
    
    private int draw; // Client request times
    private int recordsTotal;  // Total records number without conditions
    private int recordsFiltered;  // Total records number with conditions
    private String error;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public int getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public int getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(int recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
