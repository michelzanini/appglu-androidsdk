package com.appglu.android.analytics;

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
	
	public void setSessionParameter(long sessionId, String name, String value);
	
	public void removeSessionParameter(long sessionId, String name);
	
	public long createEvent(long sessionId, AnalyticsSessionEvent event);
	
	public void setEventParameter(long eventId, String name, String value);
	
}