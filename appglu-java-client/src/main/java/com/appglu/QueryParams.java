package com.appglu;

import java.util.HashMap;

/**
 * Represents the names and values of parameters to be replaced before a query is executed.
 * 
 * @see SavedQueriesOperations
 * @since 1.0.0
 */
public class QueryParams extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;
	
	public QueryParams add(String name, Object value) {
		this.put(name, value);
		return this;
	}
	
}