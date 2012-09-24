package com.appglu.android.analytics;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.appglu.AnalyticsSession;
import com.appglu.AnalyticsSessionEvent;
import com.appglu.AppGluHttpClientException;
import com.appglu.android.AppGlu;
import com.appglu.android.DeviceInformation;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;

public class AnalyticsService {
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.LOG_TAG);
	
	private AnalyticsDispatcher analyticsDispatcher;
	
	private AnalyticsRepository analyticsRepository;
	
	private DeviceInformation deviceInformation;
	
	private AnalyticsSessionCallback sessionCallback;
	
	public AnalyticsService(AnalyticsDispatcher analyticsDispatcher, AnalyticsRepository analyticsRepository, DeviceInformation deviceInformation) {
		this.analyticsDispatcher = analyticsDispatcher;
		this.analyticsRepository = analyticsRepository;
		this.deviceInformation = deviceInformation;
	}
	
	public void setSessionCallback(AnalyticsSessionCallback sessionCallback) {
		this.sessionCallback = sessionCallback;
	}

	public long startSessionIfNedeed() {
		Long currentSessionId = this.analyticsRepository.getCurrentSessionId();
		if (currentSessionId != null) {
			return currentSessionId;
		}
		
		AnalyticsSession session = new AnalyticsSession();
		
		session.setStartDate(new Date());
		session.setClientUUID(this.deviceInformation.getDeviceUUID());
		
		session.addParameter("appglu.client_device", this.deviceInformation.getDeviceModel());
		session.addParameter("appglu.client_device_manufacturer", this.deviceInformation.getDeviceManufacturer());
		session.addParameter("appglu.client_os", this.deviceInformation.getDeviceOS());
		session.addParameter("appglu.client_os", this.deviceInformation.getDeviceOSVersion());
		session.addParameter("appglu.app_name", this.deviceInformation.getAppName());
		session.addParameter("appglu.app_version", this.deviceInformation.getAppVersion());
		session.addParameter("appglu.app_identifier", this.deviceInformation.getAppIdentifier());
		
		if (sessionCallback != null) {
			sessionCallback.onStartSession(session);
		}
		
		return this.analyticsRepository.createSession(session);
	}
	
	public void closeSessions() {
		int rowsAffected = this.analyticsRepository.closeSessions(new Date());
		if (rowsAffected > 0) {
			this.uploadPendingSessions();
		}
	}
	
	public void setSessionParameter(String name, String value) {
		long currentSessionId = this.startSessionIfNedeed();
		
		if (value == null) {
			this.analyticsRepository.removeSessionParameter(currentSessionId, name);
		} else {
			this.analyticsRepository.setSessionParameter(currentSessionId, name, value);
		}
	}
	
	public void removeSessionParameter(String name) {
		this.setSessionParameter(name, null);
	}
	
	public void logEvent(String name) {
		this.logEvent(name, null);
	}
	
	public void logEvent(String name, Map<String, String> parameters) {
		AnalyticsSessionEvent event = new AnalyticsSessionEvent();
		event.setName(name);
		event.setParameters(parameters);
		this.logEvent(event);
	}
	
	public void logEvent(AnalyticsSessionEvent event) {
		if (event != null && event.getDate() == null) {
			event.setDate(new Date());
		}
		long currentSessionId = this.startSessionIfNedeed();
		this.analyticsRepository.createEvent(currentSessionId, event);
	}

	public void uploadPendingSessions() {
		List<AnalyticsSession> sessions = this.analyticsRepository.getAllClosedSessions();
		if (!sessions.isEmpty() && this.deviceInformation.hasInternetConnection()) {
			if (sessionCallback != null) {
				sessionCallback.beforeUploadSessions(sessions);
			}
			try {
				this.analyticsDispatcher.uploadSessions(sessions);
				this.logger.info("%d sessions were uploaded to analytics", sessions.size());
			} catch (AppGluHttpClientException e) {
				this.logger.error(e);
			}
			this.analyticsRepository.removeAllClosedSessions();
		}
	}
	
}