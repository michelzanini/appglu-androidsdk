package com.appglu;

public interface SavedQueriesOperations {
	
	QueryResult runQuery(String queryName);
	
	QueryResult runQuery(String queryName, QueryParams params);

}