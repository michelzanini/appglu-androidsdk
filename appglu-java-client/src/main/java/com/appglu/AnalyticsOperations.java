package com.appglu;

import java.util.List;

public interface AnalyticsOperations {
	
	public void createSession(AnalyticsSession session);
	
	public void createSessions(List<AnalyticsSession> sessions);

}
