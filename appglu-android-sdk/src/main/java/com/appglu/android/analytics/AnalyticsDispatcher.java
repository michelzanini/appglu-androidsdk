package com.appglu.android.analytics;

import java.util.List;

import com.appglu.AnalyticsSession;

/**
 * Defines an strategy to where the {@link AnalyticsSession} are going to be sent.<br>
 * {@link ApiAnalyticsDispatcher} is a implementation that will send the events to the AppGlu server.
 * {@link LogAnalyticsDispatcher} is a implementation that will only log the events to the console.
 * 
 * @see ApiAnalyticsDispatcher
 * @see LogAnalyticsDispatcher
 * @since 1.0.0
 */
public interface AnalyticsDispatcher {
	
	/**
	 * Return <code>true</code> if you want {@link AnalyticsDispatcher#dispatchSessions(List)} to be called.
	 */
	boolean shouldDispatchSessions(List<AnalyticsSession> sessions);

	/**
	 * Applies the strategy of where to send the list of {@link AnalyticsSession} objects.
	 */
	void dispatchSessions(List<AnalyticsSession> sessions);
	
}