package com.appglu;

public interface SavedQueriesOperations {
	
	QueryResult runQuery(String queryName) throws AppGluRestClientException;
	
	QueryResult runQuery(String queryName, QueryParams params) throws AppGluRestClientException;

}