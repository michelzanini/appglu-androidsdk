package com.appglu.impl;

import org.springframework.web.client.RestOperations;

import com.appglu.QueryParams;
import com.appglu.QueryResult;
import com.appglu.SavedQueriesOperations;

public class SavedQueriesTemplate implements SavedQueriesOperations {
	
	static final String QUERY_RUN_URL = "/v1/queries/{queryName}/run";

	private final RestOperations restOperations;
	
	public SavedQueriesTemplate(RestOperations restOperations) {
		this.restOperations = restOperations;
	}
	
	public QueryResult executeQuery(String queryName, QueryParams params) {
		return this.restOperations.postForObject(QUERY_RUN_URL, params, QueryResult.class, queryName);
	}
	
}