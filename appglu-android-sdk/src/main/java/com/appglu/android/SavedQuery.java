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
package com.appglu.android;

import java.io.Serializable;

import com.appglu.AsyncCallback;
import com.appglu.QueryParams;
import com.appglu.QueryResult;

/**
 * Simple wrapper around {@link SavedQueriesApi} to provide a more model driven way of executing saved queries.
 * 
 * @see com.appglu.android.SavedQueriesApi
 * @since 1.0.0
 */
public class SavedQuery implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String name;
	
	private QueryParams queryParams;

	public SavedQuery(String name) {
		this(name, new QueryParams());
	}
	
	public SavedQuery(String name, QueryParams queryParams) {
		this.name = name;
		this.queryParams = queryParams;
	}

	public String getName() {
		return name;
	}
	
	public QueryParams getQueryParams() {
		return queryParams;
	}

	public SavedQuery setQueryParams(QueryParams queryParams) {
		this.queryParams = queryParams;
		return this;
	}
	
	public SavedQuery addQueryParam(String name, Object value) {
		this.queryParams.put(name, value);
		return this;
	}

	public QueryResult run() {
		return AppGlu.savedQueriesApi().runQuery(name, queryParams);
	}

	public void runInBackground(AsyncCallback<QueryResult> queryResultCallback) {
		AppGlu.savedQueriesApi().runQueryInBackground(name, queryParams, queryResultCallback);
	}

	@Override
	public String toString() {
		return "SavedQuery [name=" + name + ", queryParams=" + queryParams+ "]";
	}
	
}
