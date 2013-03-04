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