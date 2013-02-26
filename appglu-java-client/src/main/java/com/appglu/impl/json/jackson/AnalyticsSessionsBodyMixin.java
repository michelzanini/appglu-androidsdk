package com.appglu.impl.json.jackson;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.appglu.AnalyticsSession;

public abstract class AnalyticsSessionsBodyMixin {
	
	public AnalyticsSessionsBodyMixin(@JsonProperty("sessions") List<AnalyticsSession> sessions) {
		
	}

}
