package com.appglu.android.impl.analytics;

import java.util.Date;
import java.util.List;

import com.appglu.AnalyticsSession;
import com.appglu.AnalyticsSessionEvent;

public interface AnalyticsRepository {
	
	public Long getCurrentSessionId();
	
	public List<AnalyticsSession> getAllClosedSessions();
	
	public AnalyticsSession getSessionById(long sessionId);

	public void removeAllClosedSessions();

	public int closeSessions(Date endDate);
	
	public long createSession(AnalyticsSession session);
	
	public void addSessionParameter(long sessionId, String name, String value);
	
	public long createEvent(long sessionId, AnalyticsSessionEvent event);
	
	public void addEventParameter(long eventId, String name, String value);
	
}