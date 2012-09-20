package com.appglu.android;

import com.appglu.AnalyticsOperations;
import com.appglu.AnalyticsSessionEvent;
import com.appglu.android.impl.analytics.AnalyticsRepository;
import com.appglu.android.impl.analytics.AnalyticsService;

public final class AnalyticsApi {
	
	private AnalyticsService analyticsService;

	public AnalyticsApi(AnalyticsOperations analyticsOperations, AnalyticsRepository analyticsRepository, DeviceInformation deviceInformation) {
		this.analyticsService = new AnalyticsService(analyticsOperations, analyticsRepository, deviceInformation);
	}

	public void startSessionIfNeeded() {
		this.analyticsService.startSessionIfNedeed();
	}
	
	public void closeSessionIfNeeded() {
		this.analyticsService.closeSessions();
	}

	public void addSessionParameter(String name, String value) {
		this.analyticsService.addSessionParameter(name, value);
	}

	public void createEvent(AnalyticsSessionEvent event) {
		this.analyticsService.createEvent(event);
	}

}
