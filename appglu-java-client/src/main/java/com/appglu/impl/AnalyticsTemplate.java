package com.appglu.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.client.RestOperations;

import com.appglu.AnalyticsOperations;
import com.appglu.AnalyticsSession;
import com.appglu.impl.json.AnalyticsSessionsBody;

public final class AnalyticsTemplate implements AnalyticsOperations {
	
	static final String CREATE_SESSION_URL = "/v1/analytics";
	
	private RestOperations restOperations;
	
	public AnalyticsTemplate(RestOperations restOperations) {
		this.restOperations = restOperations;
	}

	public void uploadSession(AnalyticsSession session) {
		List<AnalyticsSession> sessions = new ArrayList<AnalyticsSession>();
		sessions.add(session);
		this.uploadSessions(sessions);
	}

	public void uploadSessions(List<AnalyticsSession> sessions) {
		this.restOperations.postForObject(CREATE_SESSION_URL, new AnalyticsSessionsBody(sessions), String.class);
	}

}