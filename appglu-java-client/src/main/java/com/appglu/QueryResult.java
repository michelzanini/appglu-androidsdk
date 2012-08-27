package com.appglu;

import java.util.List;

public class QueryResult {
	
	private List<Tuple> tuples;
	
	private Integer rowsAffected;

	public List<Tuple> getTuples() {
		return tuples;
	}

	public void setTuples(List<Tuple> tuples) {
		this.tuples = tuples;
	}

	public Integer getRowsAffected() {
		return rowsAffected;
	}

	public void setRowsAffected(Integer rowsAffected) {
		this.rowsAffected = rowsAffected;
	}

	@Override
	public String toString() {
		return "QueryResult [tuples=" + tuples + ", rowsAffected=" + rowsAffected + "]";
	}

}