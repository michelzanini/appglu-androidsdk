package com.appglu;

import java.util.ArrayList;
import java.util.List;

public class Rows {
	
	private List<Row> rows;

	private Integer totalRows;
	
	public Rows() {
		this.rows = new ArrayList<Row>();
	}

	public Rows(List<Row> rows, Integer totalRows) {
		this.rows = rows;
		this.totalRows = totalRows;
	}

	public List<Row> getRows() {
		return rows;
	}

	public Integer getTotalRows() {
		return totalRows;
	}

	@Override
	public String toString() {
		return "Rows [rows=" + rows + ", totalRows=" + totalRows + "]";
	}

}