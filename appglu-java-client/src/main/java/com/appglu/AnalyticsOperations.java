package com.appglu;

import java.util.List;

public interface AnalyticsOperations {
	
	void createSession(AnalyticsSession session);
	
	void createSessions(List<AnalyticsSession> sessions);

}