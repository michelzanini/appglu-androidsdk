package com.appglu;

import java.util.List;

public interface AnalyticsOperations {
	
	void uploadSession(AnalyticsSession session) throws AppGluRestClientException;
	
	void uploadSessions(List<AnalyticsSession> sessions) throws AppGluRestClientException;
	
	void uploadSessions(AnalyticsSession... sessions) throws AppGluRestClientException;

}