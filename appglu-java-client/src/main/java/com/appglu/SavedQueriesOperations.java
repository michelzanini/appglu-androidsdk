package com.appglu;

public interface SavedQueriesOperations {
	
	public QueryResult executeQuery(String queryName, QueryParams params);

}