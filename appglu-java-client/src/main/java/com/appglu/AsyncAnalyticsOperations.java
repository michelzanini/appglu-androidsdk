package com.appglu;

import java.util.List;

/**
 * {@code AsyncAnalyticsOperations} has all methods that {@link AnalyticsOperations} has but they execute <strong>asynchronously</strong>. 
 * @see AnalyticsOperations
 * @since 1.0.0
 */
public interface AsyncAnalyticsOperations {
	
	/**
	 * Asynchronous version of {@link AnalyticsOperations#uploadSession(AnalyticsSession)}.
	 * @see AnalyticsOperations#uploadSession(AnalyticsSession)
	 */
	void uploadSessionInBackground(AnalyticsSession session, AsyncCallback<Void> callback);
	
	/**
	 * Asynchronous version of {@link AnalyticsOperations#uploadSessions(List)}.
	 * @see AnalyticsOperations#uploadSessions(List)
	 */
	void uploadSessionsInBackground(List<AnalyticsSession> sessions, AsyncCallback<Void> callback);
	
	/**
	 * Asynchronous version of {@link AnalyticsOperations#uploadSessions(AnalyticsSession...)}.
	 * @see AnalyticsOperations#uploadSessions(AnalyticsSession...)
	 */
	void uploadSessionsInBackground(AsyncCallback<Void> callback, AnalyticsSession... sessions);

}