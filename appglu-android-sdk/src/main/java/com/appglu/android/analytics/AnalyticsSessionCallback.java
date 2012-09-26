package com.appglu.android.analytics;

import java.util.List;

import com.appglu.AnalyticsSession;

public interface AnalyticsSessionCallback {

	void onStartSession(AnalyticsSession session);
	
	void beforeDispatchSessions(List<AnalyticsSession> sessions);
	
}