/*******************************************************************************
 * Copyright 2013 AppGlu, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.appglu.android;

import com.appglu.UserSessionPersistence;
import com.appglu.android.analytics.AnalyticsDispatcher;
import com.appglu.android.analytics.AnalyticsSessionCallback;
import com.appglu.android.cache.CacheManager;
import com.appglu.android.log.LoggerFactory;
import com.appglu.android.log.LoggerLevel;
import com.appglu.android.sync.SyncDatabaseHelper;
import com.appglu.impl.AppGluTemplate;

/**
 * This class contains configuration and customization settings and is used to initialize the {@link AppGlu} singleton.<br>
 * To create it, you will need access credentials to authenticate the SDK on the server side. You will have access to it by logging in on AppGlu's dashboard web site.<br>
 * To initialize {@link AppGlu} you can use the following code:
 * 
 * <p><code>
 * AppGluSettings settings = new AppGluSettings("appKey", "appSecret");<br>
 * AppGlu.initialize(this, settings);
 * </code>
 * 
 * <p>You can also set variables to customize {@link AppGlu} before initializing it like below:
 * 
 * <p><code>
 * AppGluSettings settings = new AppGluSettings("appKey", "appSecret");<br>
 * <br>
 * settings.setLoggerLevel(LoggerLevel.DEBUG);<br>
 * settings.setDefaultStorageCacheManager(new MemoryAndFileSystemCacheManager(this));<br>
 * <br>
 * AppGlu.initialize(this, settings);
 * </code>
 * 
 * @see com.appglu.android.AppGlu
 * @since 1.0.0
 */
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
	
	private CacheManager defaultStorageCacheManager;
	
	private long storageCacheTimeToLiveInMilliseconds;
	
	/**
	 * @param applicationKey a randomly generated unique key specific for each mobile application
	 * @param applicationSecret the secret key used to authenticate this application
	 */
	public AppGluSettings(String applicationKey, String applicationSecret) {
		this(AppGluTemplate.DEFAULT_BASE_URL, applicationKey, applicationSecret);
	}
	
	/**
	 * @param baseUrl the server URL to point to, if different from the default {@link com.appglu.impl.AppGluTemplate#DEFAULT_BASE_URL}
	 * @param applicationKey a randomly generated unique key specific for each mobile application
	 * @param applicationSecret the secret key used to authenticate this application
	 */
	public AppGluSettings(String baseUrl, String applicationKey, String applicationSecret) {
		this(baseUrl, applicationKey, applicationSecret, AppGluTemplate.DEFAULT_ENVIRONMENT);
	}
	
	/**
	 * @param baseUrl the server URL to point to, if different from the default {@link com.appglu.impl.AppGluTemplate#DEFAULT_BASE_URL}
	 * @param applicationKey a randomly generated unique key specific for each mobile application
	 * @param applicationSecret the secret key used to authenticate this application
	 * @param applicationEnvironment The environment name to be accessed on the AppGlu server. 
	 * Normally set to "staging" or "production", if not set {@link com.appglu.impl.AppGluTemplate#DEFAULT_ENVIRONMENT} is assumed
	 */
	public AppGluSettings(String baseUrl, String applicationKey, String applicationSecret, String applicationEnvironment) {
		this.baseUrl = baseUrl;
		this.applicationKey = applicationKey;
		this.applicationSecret = applicationSecret;
		this.applicationEnvironment = applicationEnvironment;
	}
	
	/**
	 * Returns the server URL the SDK is pointing to, by default is {@link com.appglu.impl.AppGluTemplate#DEFAULT_BASE_URL}.
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Returns a randomly generated unique key specific for each mobile application.
	 */
	public String getApplicationKey() {
		return applicationKey;
	}

	/**
	 * Returns the secret key used to authenticate this application.
	 */
	public String getApplicationSecret() {
		return applicationSecret;
	}
	
	/**
	 * Returns the environment name that is being accessed on the AppGlu server. 
	 * Normally is "staging" or "production", by default is {@link com.appglu.impl.AppGluTemplate#DEFAULT_ENVIRONMENT}.
	 */
	public String getApplicationEnvironment() {
		return applicationEnvironment;
	}
	
	public String getApplicationVersion() {
		return applicationVersion;
	}

	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}

	/**
	 * Returns the {@link com.appglu.android.log.LoggerLevel} used by the SDK. By default is {@link com.appglu.android.log.LoggerLevel#INFO}.
	 * @see com.appglu.android.log.Logger
	 * @see com.appglu.android.log.LoggerFactory
	 */
	public LoggerLevel getLoggerLevel() {
		return LoggerFactory.getLevel();
	}

	/**
	 * Sets the {@link com.appglu.android.log.LoggerLevel}.
	 * @see com.appglu.android.log.Logger
	 * @see com.appglu.android.log.LoggerFactory
	 */
	public void setLoggerLevel(LoggerLevel loggerLevel) {
		LoggerFactory.setLevel(loggerLevel);
	}

	/**
	 * If set to <code>false</code>, analytics events will not be sent to AppGlu server, instead they will only be logged to the console.<br>
	 * Only set it to <code>false</code> for testing purposes. By default is <code>true</code>.<br>
	 * If you set a custom {@link com.appglu.android.analytics.AnalyticsDispatcher} then this flag will not have any use.
	 */
	public void setUploadAnalyticsSessionsToServer(boolean uploadAnalyticsSessionsToServer) {
		this.uploadAnalyticsSessionsToServer = uploadAnalyticsSessionsToServer;
	}
	
	/**
	 * Changing the {@link com.appglu.android.analytics.AnalyticsDispatcher} implementation if you want to send analytics events to somewhere else.<br>
	 * By default {@link com.appglu.android.analytics.ApiAnalyticsDispatcher} is used and it will send the events to the AppGlu server.
	 * @see com.appglu.android.analytics.AnalyticsDispatcher
	 * @see com.appglu.android.analytics.ApiAnalyticsDispatcher
	 * @see com.appglu.android.analytics.LogAnalyticsDispatcher
	 */
	public void setAnalyticsDispatcher(AnalyticsDispatcher analyticsDispatcher) {
		this.analyticsDispatcher = analyticsDispatcher;
	}
	
	/**
	 * Set this callback to receive the {@link com.appglu.AnalyticsSession} objects before they saved or sent to the server.<br>
	 * This will allow you to change them before they are sent to the REST API.
	 */
	public void setAnalyticsSessionCallback(AnalyticsSessionCallback analyticsSessionCallback) {
		this.analyticsSessionCallback = analyticsSessionCallback;
	}
	
	/**
	 * Changing the {@link com.appglu.UserSessionPersistence} implementation if you want to save the app {@link com.appglu.User} object to somewhere else.<br>
	 * By default {@link com.appglu.android.SharedPreferencesUserSessionPersistence} is used and it will save the user data to a SharedPreferences object.
	 * @see com.appglu.UserSessionPersistence
	 * @see com.appglu.impl.MemoryUserSessionPersistence
	 * @see com.appglu.android.SharedPreferencesUserSessionPersistence
	 */
	public void setUserSessionPersistence(UserSessionPersistence userSessionPersistence) {
		this.userSessionPersistence = userSessionPersistence;
	}
	
	/**
	 * Defines the default SQLite database to be used by {@link com.appglu.android.sync.SyncApi}.<br>
	 * You must extend {@link com.appglu.android.sync.SyncDatabaseHelper} instead of Android's SQLiteOpenHelper and implement it's methods.
	 * @see com.appglu.android.sync.SyncDatabaseHelper
	 */
	public void setDefaultSyncDatabaseHelper(SyncDatabaseHelper defaultSyncDatabaseHelper) {
		this.defaultSyncDatabaseHelper = defaultSyncDatabaseHelper;
	}
	
	/**
	 * A {@link com.appglu.android.cache.CacheManager} is used for storing and caching files while using the {@link com.appglu.android.storage.StorageApi}.
	 * There are three {@link com.appglu.android.cache.CacheManager} implementations available: memory ({@link com.appglu.android.cache.MemoryCacheManager}), disk ({@link com.appglu.android.cache.FileSystemCacheManager}) or both ({@link com.appglu.android.cache.MemoryAndFileSystemCacheManager}).<br>
	 * By default {@link com.appglu.android.cache.FileSystemCacheManager} is used and it will cache the files on disk.
	 * @see com.appglu.android.cache.CacheManager
	 * @see com.appglu.android.cache.MemoryCacheManager
	 * @see com.appglu.android.cache.FileSystemCacheManager
	 * @see com.appglu.android.cache.MemoryAndFileSystemCacheManager
	 */
	public void setDefaultStorageCacheManager(CacheManager storageCacheManager) {
		this.defaultStorageCacheManager = storageCacheManager;
	}
	
	/**
	 * Sets the amount of time (in milliseconds) that the files will live on the storage cache before they are checked if they are up to date with the server side.
	 */
	public void setStorageCacheTimeToLiveInMilliseconds(long storageCacheTimeToLiveInMilliseconds) {
		this.storageCacheTimeToLiveInMilliseconds = storageCacheTimeToLiveInMilliseconds;
	}
	
	/* package */ boolean isUploadAnalyticsSessionsToServer() {
		return uploadAnalyticsSessionsToServer;
	}
	
	/* package */ AnalyticsDispatcher getAnalyticsDispatcher() {
		return this.analyticsDispatcher;
	}
	
	/* package */ AnalyticsSessionCallback getAnalyticsSessionCallback() {
		return this.analyticsSessionCallback;
	}
	
	/* package */ UserSessionPersistence getUserSessionPersistence() {
		return this.userSessionPersistence;
	}
	
	/* package */ SyncDatabaseHelper getDefaultSyncDatabaseHelper() {
		return this.defaultSyncDatabaseHelper;
	}
	
	/* package */ CacheManager getDefaultStorageCacheManager() {
		return this.defaultStorageCacheManager;
	}
	
	/* package */ long getStorageCacheTimeToLiveInMilliseconds() {
		return storageCacheTimeToLiveInMilliseconds;
	}

	/**
	 * This is an extension point that can be used if you wish to extend or customize the {@link com.appglu.impl.AppGluTemplate}.
	 */
	protected AppGluTemplate createAppGluTemplate() {
		return new AppGluTemplate(this.getBaseUrl(), this.getApplicationKey(), this.getApplicationSecret(), this.getApplicationEnvironment());
	}
	
}
