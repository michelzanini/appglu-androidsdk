package com.appglu.android.impl.analytics;

import java.util.Date;
import java.util.List;

import com.appglu.AnalyticsOperations;
import com.appglu.AnalyticsSession;
import com.appglu.AnalyticsSessionEvent;
import com.appglu.android.DeviceInformation;

public class AnalyticsService {
	
	private AnalyticsOperations analyticsOperations;
	
	private AnalyticsRepository analyticsRepository;
	
	private DeviceInformation deviceInformation;

	public AnalyticsService(AnalyticsOperations analyticsOperations, AnalyticsRepository analyticsRepository, DeviceInformation deviceInformation) {
		this.analyticsOperations = analyticsOperations;
		this.analyticsRepository = analyticsRepository;
		this.deviceInformation = deviceInformation;
	}
	
	public long startSessionIfNedeed() {
		Long currentSessionId = this.analyticsRepository.getCurrentSessionId();
		if (currentSessionId != null) {
			return this.startSessionIfNedeed();
		}
		
		AnalyticsSession session = new AnalyticsSession();
		
		session.setStartDate(new Date());
		session.setClientUUID(this.deviceInformation.getDeviceUUID());
		
		session.addParameter("appglu.client_device", this.deviceInformation.getDeviceModel());
		session.addParameter("appglu.client_device_manufacturer", this.deviceInformation.getDeviceManufacturer());
		session.addParameter("appglu.client_os", this.deviceInformation.getDeviceOS());
		session.addParameter("appglu.client_os_version", this.deviceInformation.getDeviceOSVersion());
		session.addParameter("appglu.app_name", this.deviceInformation.getAppName());
		session.addParameter("appglu.app_version", this.deviceInformation.getAppVersion());
		session.addParameter("appglu.app_identifier", this.deviceInformation.getAppIdentifier());
		
		return this.analyticsRepository.createSession(session);
	}
	
	public void closeSessions() {
		int rowsAffected = this.analyticsRepository.closeSessions(new Date());
		if (rowsAffected > 0) {
			this.uploadClosedSessions();
		}
	}
	
	public void addSessionParameter(String name, String value) {
		Long currentSessionId = this.startSessionIfNedeed();
		this.analyticsRepository.addSessionParameter(currentSessionId, name, value);
	}
	
	public void createEvent(AnalyticsSessionEvent event) {
		Long currentSessionId = this.startSessionIfNedeed();
		this.analyticsRepository.createEvent(currentSessionId, event);
	}

	public void uploadClosedSessions() {
		List<AnalyticsSession> sessions = this.analyticsRepository.getAllClosedSessions();
		if (!sessions.isEmpty()) {
			this.analyticsOperations.uploadSessions(sessions);
			this.analyticsRepository.removeAllClosedSessions();
		}
	}
	
}