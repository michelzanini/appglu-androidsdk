package com.appglu.android;

import com.appglu.UserSessionPersistence;
import com.appglu.android.analytics.AnalyticsDispatcher;
import com.appglu.android.analytics.AnalyticsSessionCallback;
import com.appglu.android.log.LoggerFactory;
import com.appglu.android.log.LoggerLevel;
import com.appglu.impl.AppGluTemplate;

public class AppGluSettings {
	
	private String baseUrl;
	
	private String applicationKey;
	
	private String applicationSecret;
	
	private boolean uploadAnalyticsSessionsToServer = true;
	
	private AnalyticsDispatcher analyticsDispatcher;
	
	private AnalyticsSessionCallback analyticsSessionCallback;
	
	private UserSessionPersistence userSessionPersistence;
	
	public AppGluSettings(String baseUrl, String applicationKey, String applicationSecret) {
		this.baseUrl = baseUrl;
		this.applicationKey = applicationKey;
		this.applicationSecret = applicationSecret;
	}
	
	public String getBaseUrl() {
		return baseUrl;
	}

	public String getApplicationKey() {
		return applicationKey;
	}

	public String getApplicationSecret() {
		return applicationSecret;
	}
	
	public LoggerLevel getLoggerLevel() {
		return LoggerFactory.getLevel();
	}

	public void setLoggerLevel(LoggerLevel loggerLevel) {
		LoggerFactory.setLevel(loggerLevel);
	}

	public boolean isUploadAnalyticsSessionsToServer() {
		return uploadAnalyticsSessionsToServer;
	}

	public void setUploadAnalyticsSessionsToServer(boolean uploadAnalyticsSessionsToServer) {
		this.uploadAnalyticsSessionsToServer = uploadAnalyticsSessionsToServer;
	}
	
	public void setAnalyticsDispatcher(AnalyticsDispatcher analyticsDispatcher) {
		this.analyticsDispatcher = analyticsDispatcher;
	}

	public void setAnalyticsSessionCallback(AnalyticsSessionCallback analyticsSessionCallback) {
		this.analyticsSessionCallback = analyticsSessionCallback;
	}
	
	public void setUserSessionPersistence(UserSessionPersistence userSessionPersistence) {
		this.userSessionPersistence = userSessionPersistence;
	}

	protected AnalyticsDispatcher getAnalyticsDispatcher() {
		return this.analyticsDispatcher;
	}
	
	protected AnalyticsSessionCallback getAnalyticsSessionCallback() {
		return this.analyticsSessionCallback;
	}
	
	protected UserSessionPersistence getUserSessionPersistence() {
		return this.userSessionPersistence;
	}
	
	protected AppGluTemplate createAppGluTemplate() {
		return new AppGluTemplate(this.getBaseUrl(), this.getApplicationKey(), this.getApplicationSecret());
	}
	
}