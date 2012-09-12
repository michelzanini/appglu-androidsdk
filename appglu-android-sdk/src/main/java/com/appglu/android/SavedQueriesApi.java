package com.appglu.android;

import com.appglu.AsyncCallback;
import com.appglu.AsyncSavedQueriesOperations;
import com.appglu.QueryParams;
import com.appglu.QueryResult;
import com.appglu.SavedQueriesOperations;

public final class SavedQueriesApi implements SavedQueriesOperations, AsyncSavedQueriesOperations {

	private SavedQueriesOperations savedQueriesOperations;
	
	private AsyncSavedQueriesOperations asyncSavedQueriesOperations;

	public SavedQueriesApi(SavedQueriesOperations savedQueriesOperations, AsyncSavedQueriesOperations asyncSavedQueriesOperations) {
		this.savedQueriesOperations = savedQueriesOperations;
		this.asyncSavedQueriesOperations = asyncSavedQueriesOperations;
	}

	public QueryResult runQuery(String queryName) {
		return savedQueriesOperations.runQuery(queryName);
	}

	public QueryResult runQuery(String queryName, QueryParams params) {
		return savedQueriesOperations.runQuery(queryName, params);
	}

	public void runQueryInBackground(String queryName, AsyncCallback<QueryResult> queryResultCallback) {
		asyncSavedQueriesOperations.runQueryInBackground(queryName, queryResultCallback);
	}

	public void runQueryInBackground(String queryName, QueryParams params, AsyncCallback<QueryResult> queryResultCallback) {
		asyncSavedQueriesOperations.runQueryInBackground(queryName, params, queryResultCallback);
	}
	
}