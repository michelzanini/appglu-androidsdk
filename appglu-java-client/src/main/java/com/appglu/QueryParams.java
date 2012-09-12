package com.appglu;

import java.util.HashMap;

public class QueryParams extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;
	
	public QueryParams add(String name, Object value) {
		this.put(name, value);
		return this;
	}
	
}