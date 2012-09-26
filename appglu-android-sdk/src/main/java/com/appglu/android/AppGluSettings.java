package com.appglu.android;

import com.appglu.AnalyticsOperations;
import com.appglu.android.analytics.AnalyticsDispatcher;
import com.appglu.android.analytics.AppGluAnalyticsDispatcher;
import com.appglu.impl.AppGluTemplate;

public class AppGluSettings {
	
	private String baseUrl;
	
	private String applicationKey;
	
	private String applicationSecret;
	
	private boolean analyticsDeveloperModeEnabled;
	
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
	
	public boolean isAnalyticsDeveloperModeEnabled() {
		return analyticsDeveloperModeEnabled;
	}

	public void setAnalyticsDeveloperModeEnabled(boolean analyticsDeveloperModeEnabled) {
		this.analyticsDeveloperModeEnabled = analyticsDeveloperModeEnabled;
	}

	protected AppGluTemplate createAppGluTemplate() {
		return new AppGluTemplate(this.getBaseUrl(), this.getApplicationKey(), this.getApplicationSecret());
	}
	
	protected AnalyticsDispatcher createAnalyticsDispatcher(AnalyticsOperations analyticsOperations) {
		AppGluAnalyticsDispatcher appGluAnalyticsDispatcher = new AppGluAnalyticsDispatcher(analyticsOperations);
		appGluAnalyticsDispatcher.setDeveloperMode(this.isAnalyticsDeveloperModeEnabled());
		return appGluAnalyticsDispatcher;
	}
	
}