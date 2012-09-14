package com.appglu.impl;

import java.util.concurrent.Callable;

import com.appglu.AsyncCallback;
import com.appglu.AsyncSavedQueriesOperations;
import com.appglu.QueryParams;
import com.appglu.QueryResult;
import com.appglu.SavedQueriesOperations;

public final class AsyncSavedQueriesTemplate implements AsyncSavedQueriesOperations {
	
	private final AsyncExecutor asyncExecutor;
	
	private final SavedQueriesOperations savedQueriesOperations;
	
	public AsyncSavedQueriesTemplate(AsyncExecutor asyncExecutor, SavedQueriesOperations savedQueriesOperations) {
		this.asyncExecutor = asyncExecutor;
		this.savedQueriesOperations = savedQueriesOperations;
	}

	@Override
	public void runQueryInBackground(final String queryName, AsyncCallback<QueryResult> queryResultCallback) {
		asyncExecutor.execute(queryResultCallback, new Callable<QueryResult>() {
			public QueryResult call() {
				return savedQueriesOperations.runQuery(queryName);
			}
		});
	}

	@Override
	public void runQueryInBackground(final String queryName, final QueryParams params, AsyncCallback<QueryResult> queryResultCallback) {
		asyncExecutor.execute(queryResultCallback, new Callable<QueryResult>() {
			public QueryResult call() {
				return savedQueriesOperations.runQuery(queryName, params);
			}
		});
	}

}