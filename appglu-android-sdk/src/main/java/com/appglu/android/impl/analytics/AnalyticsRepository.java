package com.appglu.android.impl.analytics;

import java.util.Date;
import java.util.List;

import com.appglu.AnalyticsSession;
import com.appglu.AnalyticsSessionEvent;

public interface AnalyticsRepository {

	public List<AnalyticsSession> getAllClosedSessions();
	
	public AnalyticsSession getCurrentSession();
	
	public AnalyticsSession getSessionById(long id);
	
	public long createSession(AnalyticsSession session);
	
	public void closeSessions(Date endDate);
	
	public void addSessionParameter(Long sessionId, String name, String value);
	
	public long createEvent(Long sessionId, AnalyticsSessionEvent event);
	
	public void addEventParameter(Long eventId, String name, String value);
	
}