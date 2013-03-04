package com.appglu.android;

import com.appglu.AppGluRestClientException;
import com.appglu.AsyncCallback;
import com.appglu.AsyncSavedQueriesOperations;
import com.appglu.QueryParams;
import com.appglu.QueryResult;
import com.appglu.SavedQueriesOperations;

/**
 * {@code SavedQueriesApi} allow you to run your previously created SQLs on AppGlu and obtain the results.
 * 
 * @see com.appglu.android.AppGlu
 * @see com.appglu.SavedQueriesOperations
 * @see com.appglu.AsyncSavedQueriesOperations
 * @since 1.0.0
 */
public final class SavedQueriesApi implements SavedQueriesOperations, AsyncSavedQueriesOperations {

	private SavedQueriesOperations savedQueriesOperations;
	
	private AsyncSavedQueriesOperations asyncSavedQueriesOperations;

	public SavedQueriesApi(SavedQueriesOperations savedQueriesOperations, AsyncSavedQueriesOperations asyncSavedQueriesOperations) {
		this.savedQueriesOperations = savedQueriesOperations;
		this.asyncSavedQueriesOperations = asyncSavedQueriesOperations;
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryResult runQuery(String queryName) throws AppGluRestClientException {
		return savedQueriesOperations.runQuery(queryName);
	}

	/**
	 * {@inheritDoc}
	 */
	public QueryResult runQuery(String queryName, QueryParams params) throws AppGluRestClientException {
		return savedQueriesOperations.runQuery(queryName, params);
	}

	/**
	 * {@inheritDoc}
	 */
	public void runQueryInBackground(String queryName, AsyncCallback<QueryResult> queryResultCallback) {
		asyncSavedQueriesOperations.runQueryInBackground(queryName, queryResultCallback);
	}

	/**
	 * {@inheritDoc}
	 */
	public void runQueryInBackground(String queryName, QueryParams params, AsyncCallback<QueryResult> queryResultCallback) {
		asyncSavedQueriesOperations.runQueryInBackground(queryName, params, queryResultCallback);
	}
	
}