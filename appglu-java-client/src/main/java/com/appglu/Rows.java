package com.appglu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class Rows implements Serializable {
	
	private static final long serialVersionUID = 1L;

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
	
	public boolean hasRows() {
		return rows != null && !rows.isEmpty();
	}
	
	public boolean hasTotalRows() {
		return totalRows != null && totalRows > 0;
	}

	@Override
	public String toString() {
		return "Rows [rows=" + rows + ", totalRows=" + totalRows + "]";
	}

}