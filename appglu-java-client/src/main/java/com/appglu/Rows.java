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

	public Rows(List<Row> rows, Integer totalRows) {
		this.rows = this.convertRowColumns(rows);
		this.totalRows = totalRows;
	}

	public List<Row> getRows() {
		List<Row> rowsList = new ArrayList<Row>();
		for (Map<String, Object> rowColumns : rows) {
			Row row = new Row(rowColumns);
			rowsList.add(row);
		}
		return rowsList;
	}

	public Integer getTotalRows() {
		return totalRows;
	}
	
	private List<Map<String, Object>> convertRowColumns(List<Row> rows) {
		if (rows == null) {
			return new ArrayList<Map<String,Object>>();
		}
		List<Map<String, Object>> rowsList = new ArrayList<Map<String,Object>>();
		for (Row row : rows) {
			rowsList.add(row.getRowColumns());
		}
		return rowsList;
	}

	@Override
	public String toString() {
		return "Rows [rows=" + rows + ", totalRows=" + totalRows + "]";
	}

}