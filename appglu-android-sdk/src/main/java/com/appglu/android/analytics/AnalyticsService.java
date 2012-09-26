package com.appglu.android.analytics;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.appglu.AnalyticsSession;
import com.appglu.AnalyticsSessionEvent;
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
		
		long sessionId = this.analyticsRepository.createSession(session);
		
		logger.debug("New analytic session created: %s", session);
		
		return sessionId;
	}
	
	public void forceCloseSessions() {
		int rowsAffected = this.analyticsRepository.forceCloseSessions();
		
		if (rowsAffected > 0) {
			logger.info("%d analytic session(s) closed on initialization", rowsAffected);
			
			this.dispatchPendingSessions();
		}
	}
	
	public void closeSessions(Date closeDate) {
		if (closeDate == null) {
			closeDate = new Date();
		}
		
		int rowsAffected = this.analyticsRepository.closeSessions(closeDate);
		
		logger.debug("%d analytic session(s) closed", rowsAffected);
		
		if (rowsAffected > 0) {
			this.dispatchPendingSessions();
		}
	}
	
	public void setSessionParameter(String name, String value) {
		long currentSessionId = this.startSessionIfNedeed();
		
		if (value == null) {
			this.analyticsRepository.removeSessionParameter(currentSessionId, name);
			logger.debug("Analytic session parameter removed: %s", name);
		} else {
			this.analyticsRepository.setSessionParameter(currentSessionId, name, value);
			logger.debug("New analytic session parameter created: %s, %s", name, value);
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
		
		logger.debug("New analytic event created: %s", event);
	}

	public void dispatchPendingSessions() {
		List<AnalyticsSession> sessions = this.analyticsRepository.getAllClosedSessions();
		if (!sessions.isEmpty() && this.analyticsDispatcher.shouldDispatchSessions(sessions)) {
			if (sessionCallback != null) {
				sessionCallback.beforeDispatchSessions(sessions);
			}

			this.analyticsDispatcher.dispatchSessions(sessions);
			this.logger.info("%d session(s) dispatched to analytics", sessions.size());
			
			this.analyticsRepository.removeAllClosedSessions();
		}
	}
	
}