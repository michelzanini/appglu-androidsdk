package com.appglu.android;

import com.appglu.AnalyticsOperations;
import com.appglu.android.analytics.AnalyticsDispatcher;
import com.appglu.android.analytics.AnalyticsSessionCallback;
import com.appglu.android.analytics.ApiAnalyticsDispatcher;
import com.appglu.android.analytics.LogAnalyticsDispatcher;
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
	
	protected AppGluTemplate createAppGluTemplate() {
		return new AppGluTemplate(this.getBaseUrl(), this.getApplicationKey(), this.getApplicationSecret());
	}

	protected AnalyticsDispatcher createAnalyticsDispatcher(AnalyticsOperations analyticsOperations) {
		if (this.analyticsDispatcher == null) {
			if (this.isUploadAnalyticsSessionsToServer()) {
				this.analyticsDispatcher = new ApiAnalyticsDispatcher(analyticsOperations);
			} else {
				this.analyticsDispatcher = new LogAnalyticsDispatcher();
			}
		}
		return this.analyticsDispatcher;
	}
	
	protected AnalyticsSessionCallback getAnalyticsSessionCallback() {
		return this.analyticsSessionCallback;
	}
	
}