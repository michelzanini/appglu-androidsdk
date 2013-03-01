package com.appglu.impl;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import com.appglu.AppGluRestClientException;
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
	
	/**
	 * {@inheritDoc}
	 */
	public QueryResult runQuery(String queryName) throws AppGluRestClientException {
		return this.runQuery(queryName, null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public QueryResult runQuery(String queryName, QueryParams params) throws AppGluRestClientException {
		try {
			return this.restOperations.postForObject(QUERY_RUN_URL, new QueryParamsBody(params), QueryResult.class, queryName);
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
	}
	
}