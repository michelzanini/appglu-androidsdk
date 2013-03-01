package com.appglu;

import java.io.Serializable;
import java.util.List;

/**
 * Represents the result of a executed query.<br>
 * A select query will return a list of rows, use {@link #getRows()} to get it.<br>
 * A insert, update or delete query will return the quantity of rows affected, use {@link #getRowsAffected()} to get it.<br>
 * 
 * @see SavedQueriesOperations
 * @since 1.0.0
 */
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