package com.appglu.android.analytics;

import java.util.List;

import com.appglu.AnalyticsOperations;
import com.appglu.AnalyticsSession;

public class AppGluAnalyticsDispatcher implements AnalyticsDispatcher {
	
	private AnalyticsOperations analyticsOperations;
	
	private boolean developerMode;
	
	public AppGluAnalyticsDispatcher(AnalyticsOperations analyticsOperations) {
		this.analyticsOperations = analyticsOperations;
	}
	
	public boolean isDeveloperMode() {
		return developerMode;
	}

	public void setDeveloperMode(boolean developerMode) {
		this.developerMode = developerMode;
	}

	@Override
	public void uploadSessions(List<AnalyticsSession> sessions) {
		if (this.isDeveloperMode()) {
			//TODO log sessions
		} else {
			this.analyticsOperations.uploadSessions(sessions);
		}
	}
	
}
