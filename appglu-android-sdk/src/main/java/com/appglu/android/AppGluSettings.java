package com.appglu.android;

import com.appglu.UserSessionPersistence;
import com.appglu.android.analytics.AnalyticsDispatcher;
import com.appglu.android.analytics.AnalyticsSessionCallback;
import com.appglu.android.cache.CacheManager;
import com.appglu.android.log.LoggerFactory;
import com.appglu.android.log.LoggerLevel;
import com.appglu.android.sync.SyncDatabaseHelper;
import com.appglu.impl.AppGluTemplate;

public class AppGluSettings {
	
	private String baseUrl;
	
	private String applicationKey;
	
	private String applicationSecret;
	
	private String applicationEnvironment;
	
	private String applicationVersion;
	
	private boolean uploadAnalyticsSessionsToServer = true;
	
	private AnalyticsDispatcher analyticsDispatcher;
	
	private AnalyticsSessionCallback analyticsSessionCallback;
	
	private UserSessionPersistence userSessionPersistence;
	
	private SyncDatabaseHelper defaultSyncDatabaseHelper;
	
	private CacheManager storageCacheManager;
	
	private long storageCacheTimeToLiveInMilliseconds;
	
	public AppGluSettings(String baseUrl, String applicationKey, String applicationSecret) {
		this(baseUrl, applicationKey, applicationSecret, null);
	}
	
	public AppGluSettings(String baseUrl, String applicationKey, String applicationSecret, String applicationEnvironment) {
		this.baseUrl = baseUrl;
		this.applicationKey = applicationKey;
		this.applicationSecret = applicationSecret;
		this.applicationEnvironment = applicationEnvironment;
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
	
	public String getApplicationEnvironment() {
		return applicationEnvironment;
	}
	
	public String getApplicationVersion() {
		return applicationVersion;
	}

	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
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
	
	public void setDefaultSyncDatabaseHelper(SyncDatabaseHelper defaultSyncDatabaseHelper) {
		this.defaultSyncDatabaseHelper = defaultSyncDatabaseHelper;
	}
	
	public void setStorageCacheManager(CacheManager storageCacheManager) {
		this.storageCacheManager = storageCacheManager;
	}
	
	public void setStorageCacheTimeToLiveInMilliseconds(long storageCacheTimeToLiveInMilliseconds) {
		this.storageCacheTimeToLiveInMilliseconds = storageCacheTimeToLiveInMilliseconds;
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
	
	protected SyncDatabaseHelper getDefaultSyncDatabaseHelper() {
		return this.defaultSyncDatabaseHelper;
	}
	
	protected CacheManager getStorageCacheManager() {
		return this.storageCacheManager;
	}
	
	protected long getStorageCacheTimeToLiveInMilliseconds() {
		return storageCacheTimeToLiveInMilliseconds;
	}

	protected AppGluTemplate createAppGluTemplate() {
		return new AppGluTemplate(this.getBaseUrl(), this.getApplicationKey(), this.getApplicationSecret());
	}
	
}