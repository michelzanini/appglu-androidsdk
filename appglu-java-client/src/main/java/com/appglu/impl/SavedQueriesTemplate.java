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
