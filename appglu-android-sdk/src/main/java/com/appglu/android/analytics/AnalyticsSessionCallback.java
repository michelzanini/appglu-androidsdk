package com.appglu.android.analytics;

import java.util.List;

import com.appglu.AnalyticsSession;

/**
 * This callback allow you receive the {@link com.appglu.AnalyticsSession} objects before they saved or sent to the server.<br>
 * This will allow you to change them before they are sent to the REST API.
 * @since 1.0.0
 */
public interface AnalyticsSessionCallback {

	/**
	 * Called before a newly created session is stored locally.
	 */
	void onStartSession(AnalyticsSession session);
	
	/**
	 * Called before the sessions are sent to the {@link AnalyticsDispatcher}.
	 */
	void beforeDispatchSessions(List<AnalyticsSession> sessions);
	
}