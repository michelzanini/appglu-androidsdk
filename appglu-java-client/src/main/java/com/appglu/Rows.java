package com.appglu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Contains a list of {@link Row} objects and the total number of rows that table has.<br>
 * <p>The size of {@link #getRows()} if not always the same as {@link #getTotalRows()}.<br>
 * Sometimes the number of rows returned are not all the rows of that table (usually happens when paginating trough the table data).<br>
 * 
 * @see Row
 * @since 1.0.0
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