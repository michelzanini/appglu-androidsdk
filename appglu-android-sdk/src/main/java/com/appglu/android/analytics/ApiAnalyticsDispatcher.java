package com.appglu.android.analytics;

import java.util.List;

import com.appglu.AnalyticsOperations;
import com.appglu.AnalyticsSession;

public class ApiAnalyticsDispatcher implements AnalyticsDispatcher {
	
	private AnalyticsOperations analyticsOperations;
	
	public ApiAnalyticsDispatcher(AnalyticsOperations analyticsOperations) {
		this.analyticsOperations = analyticsOperations;
	}
	
	@Override
	public void uploadSessions(List<AnalyticsSession> sessions) {
		this.analyticsOperations.uploadSessions(sessions);
	}
	
}
