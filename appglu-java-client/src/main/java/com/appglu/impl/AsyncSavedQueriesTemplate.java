/*******************************************************************************
 * Copyright 2013 AppGlu, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

	/**
	 * {@inheritDoc}
	 */
	public void runQueryInBackground(final String queryName, AsyncCallback<QueryResult> queryResultCallback) {
		asyncExecutor.execute(queryResultCallback, new Callable<QueryResult>() {
			public QueryResult call() {
				return savedQueriesOperations.runQuery(queryName);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void runQueryInBackground(final String queryName, final QueryParams params, AsyncCallback<QueryResult> queryResultCallback) {
		asyncExecutor.execute(queryResultCallback, new Callable<QueryResult>() {
			public QueryResult call() {
				return savedQueriesOperations.runQuery(queryName, params);
			}
		});
	}

}
