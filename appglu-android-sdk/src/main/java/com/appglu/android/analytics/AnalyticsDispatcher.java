package com.appglu.android.analytics;

import java.util.List;

import com.appglu.AnalyticsSession;

public interface AnalyticsDispatcher {
	
	boolean shouldDispatchSessions(List<AnalyticsSession> sessions);
	
	void dispatchSessions(List<AnalyticsSession> sessions);
	
}