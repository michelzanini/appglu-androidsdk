package com.appglu.impl;

import org.springframework.web.client.RestOperations;

import com.appglu.QueryParams;
import com.appglu.QueryResult;
import com.appglu.SavedQueriesOperations;
import com.appglu.impl.json.QueryParamsBody;

public final class SavedQueriesTemplate implements SavedQueriesOperations {
	
	static final String QUERY_RUN_URL = "/v1/queries/{queryName}/run";

	private final RestOperations restOperations;
	
	public SavedQueriesTemplate(RestOperations restOperations) {
		this.restOperations = restOperations;
	}
	
	public QueryResult runQuery(String queryName) {
		return this.runQuery(queryName, null);
	}
	
	public QueryResult runQuery(String queryName, QueryParams params) {
		return this.restOperations.postForObject(QUERY_RUN_URL, new QueryParamsBody(params), QueryResult.class, queryName);
	}
	
}