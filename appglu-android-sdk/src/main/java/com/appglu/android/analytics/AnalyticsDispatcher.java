package com.appglu.android.analytics;

import java.util.List;

import com.appglu.AnalyticsSession;

public interface AnalyticsDispatcher {
	
	void dispatchSessions(List<AnalyticsSession> sessions);
	
}