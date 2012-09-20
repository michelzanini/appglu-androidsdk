package com.appglu;

import java.util.List;

public interface AnalyticsOperations {
	
	void uploadSession(AnalyticsSession session);
	
	void uploadSessions(List<AnalyticsSession> sessions);

}