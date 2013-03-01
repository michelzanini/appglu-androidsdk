package com.appglu;

import java.util.List;

/**
 * {@code AnalyticsOperations} is used to upload events to AppGlu allowing it to collect mobile app usage statistics.<br>
 * 
 * @see AsyncAnalyticsOperations
 * @since 1.0.0
 */
public interface AnalyticsOperations {
	
	/**
	 * Uploads a single session to AppGlu.
	 * @param session the session object
	 */
	void uploadSession(AnalyticsSession session) throws AppGluRestClientException;
	
	/**
	 * Uploads a list of sessions to AppGlu.
	 * @param sessions the session list
	 */
	void uploadSessions(List<AnalyticsSession> sessions) throws AppGluRestClientException;
	
	/**
	 * Uploads a list of sessions to AppGlu.
	 * @param sessions the session list
	 */
	void uploadSessions(AnalyticsSession... sessions) throws AppGluRestClientException;

}