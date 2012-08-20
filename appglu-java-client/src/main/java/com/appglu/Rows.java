package com.appglu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Rows {
	
	private List<Map<String, Object>> rows;

	private Integer totalRows;
	
	public Rows() {
		this.rows = new ArrayList<Map<String,Object>>();
	}

	public Rows(List<Map<String, Object>> rows, Integer totalRows) {
		this.rows = rows;
		this.totalRows = totalRows;
	}

	public List<Map<String, Object>> getRows() {
		return rows;
	}

	public void setRows(List<Map<String, Object>> rows) {
		this.rows = rows;
	}

	public Integer getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	@Override
	public String toString() {
		return "Rows [rows=" + rows + ", totalRows=" + totalRows + "]";
	}

}