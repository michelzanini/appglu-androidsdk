package com.appglu;

import java.io.Serializable;
import java.util.List;

public class QueryResult implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List<Row> rows;
	
	private Integer rowsAffected;

	public List<Row> getRows() {
		return rows;
	}

	public Integer getRowsAffected() {
		return rowsAffected;
	}
	
	public boolean hasRows() {
		return rows != null && !rows.isEmpty();
	}
	
	public boolean hasRowsAffected() {
		return rowsAffected != null && rowsAffected > 0;
	}

	@Override
	public String toString() {
		return "QueryResult [rows=" + rows + ", rowsAffected=" + rowsAffected + "]";
	}

}