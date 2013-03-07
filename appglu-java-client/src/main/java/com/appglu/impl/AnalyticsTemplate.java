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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import com.appglu.AnalyticsOperations;
import com.appglu.AnalyticsSession;
import com.appglu.AppGluRestClientException;
import com.appglu.impl.json.AnalyticsSessionsBody;

public final class AnalyticsTemplate implements AnalyticsOperations {
	
	static final String CREATE_SESSION_URL = "/v1/analytics";
	
	private RestOperations restOperations;
	
	public AnalyticsTemplate(RestOperations restOperations) {
		this.restOperations = restOperations;
	}

	/**
	 * {@inheritDoc}
	 */
	public void uploadSession(AnalyticsSession session) throws AppGluRestClientException {
		List<AnalyticsSession> sessions = new ArrayList<AnalyticsSession>();
		sessions.add(session);
		this.uploadSessions(sessions);
	}

	/**
	 * {@inheritDoc}
	 */
	public void uploadSessions(List<AnalyticsSession> sessions) throws AppGluRestClientException {
		try {
			this.restOperations.postForObject(CREATE_SESSION_URL, new AnalyticsSessionsBody(sessions), String.class);
		} catch (RestClientException e) {
			throw new AppGluRestClientException(e.getMessage(), e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void uploadSessions(AnalyticsSession... sessions) throws AppGluRestClientException {
		this.uploadSessions(Arrays.asList(sessions));
	}

}
