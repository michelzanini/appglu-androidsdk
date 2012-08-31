package com.appglu.impl.json;

import java.util.List;

import com.appglu.AnalyticsSession;

public class AnalyticsSessionsBody {
	
	private List<AnalyticsSession> sessions;
	
	public AnalyticsSessionsBody(List<AnalyticsSession> sessions) {
		super();
		this.sessions = sessions;
	}

	public List<AnalyticsSession> getSessions() {
		return sessions;
	}

}