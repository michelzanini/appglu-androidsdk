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