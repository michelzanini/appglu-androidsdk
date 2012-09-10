package com.appglu;

public interface AsyncSavedQueriesOperations {
	
	void runQueryInBackground(String queryName, AsyncCallback<QueryResult> queryResultCallback);
	
	void runQueryInBackground(String queryName, QueryParams params, AsyncCallback<QueryResult> queryResultCallback);

}