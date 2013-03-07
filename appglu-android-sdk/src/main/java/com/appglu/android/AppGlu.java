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

import android.content.Context;

import com.appglu.AsyncCallback;
import com.appglu.AsyncPushOperations;
import com.appglu.AsyncSavedQueriesOperations;
import com.appglu.PushOperations;
import com.appglu.SavedQueriesOperations;
import com.appglu.StorageOperations;
import com.appglu.SyncOperations;
import com.appglu.User;
import com.appglu.UserSessionPersistence;
import com.appglu.android.analytics.AnalyticsApi;
import com.appglu.android.analytics.AnalyticsDatabaseHelper;
import com.appglu.android.analytics.AnalyticsDispatcher;
import com.appglu.android.analytics.AnalyticsRepository;
import com.appglu.android.analytics.ApiAnalyticsDispatcher;
import com.appglu.android.analytics.LogAnalyticsDispatcher;
import com.appglu.android.analytics.SQLiteAnalyticsRepository;
import com.appglu.android.cache.CacheManager;
import com.appglu.android.cache.FileSystemCacheManager;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;
import com.appglu.android.storage.StorageApi;
import com.appglu.android.storage.StorageService;
import com.appglu.android.sync.SQLiteSyncRepository;
import com.appglu.android.sync.SyncApi;
import com.appglu.android.sync.SyncDatabaseHelper;
import com.appglu.android.sync.SyncFileStorageService;
import com.appglu.android.sync.SyncRepository;
import com.appglu.android.util.AppGluUtils;
import com.appglu.impl.AppGluTemplate;

/**
 * <strong>The central class for AppGlu Android SDK.</strong>
 * 
 * <p>This is always going to be the starting point for doing anything with the AppGlu Android SDK.<br>
 * It is a singleton and needs to be initialized before it is used.<br>
 * It is recommended, but not required, to initialize it on your {@code Application.onCreate()} method like below:
 * 
 * <p><code>
 * AppGluSettings settings = new AppGluSettings("appKey", "appSecret");<br>
 * AppGlu.initialize(this, settings);
 * </code>
 * 
 * <p>To create the {@link AppGluSettings} class you will need your credentials to authenticate your account with AppGlu. This class also contains configuration and customization settings.
 * 
 * <p>After {@code AppGlu} is initialized you will have access to AppGlu API classes. See the table below:
 * 
 * <p><table>
 * <tr><th>{@code AppGlu} method</th><th>API class description</th></tr>
 * <tr><td>{@link #crudApi()}</td><td>{@link CrudApi} contains create, read, update and delete operations to be applied on your tables</td></tr>
 * <tr><td>{@link #savedQueriesApi()}</td><td>{@link SavedQueriesApi} allow you to run your previously created SQLs on AppGlu and obtain the results</td></tr>
 * <tr><td>{@link #pushApi()}</td><td>{@link PushApi} is used to register / unregister the device making it eligible to receive push notifications</td></tr>
 * <tr><td>{@link #analyticsApi()}</td><td>{@link AnalyticsApi} is used to log events to AppGlu allowing it to collect mobile app usage statistics</td></tr>
 * <tr><td>{@link #userApi()}</td><td>{@link UserApi} has methods to login / logout and sign up new mobile app users</td></tr>
 * <tr><td>{@link #syncApi()}</td><td>{@link SyncApi} is used to synchronize the data in your local SQLite tables with the AppGlu server</td></tr>
 * <tr><td>{@link #storageApi()}</td><td>{@link StorageApi} has methods to download and cache files from AppGlu server</td></tr>
 * </table>
 * 
 * <p>All the API classes described on the table above provides two type of methods: <strong>synchronous</strong> and <strong>asynchronous</strong>.<br>
 * A method that is <strong>asynchronous</strong> has the suffix {@code inBackground} and it always has an {@link AsyncCallback} as parameter.<br>
 * 
 * <p>For example, look at this <strong>synchronous</strong> method example:
 * 
 * <p><code>
 * Row row = AppGlu.crudApi().read("tableName", "rowId");
 * </code>
 * 
 * <p><strong>Be careful NOT to call this on the UI thread</strong>.<br>
 * It will execute in the same thread as it is called. Use it inside your own AsyncTask or equivalent.
 * 
 * <p>Now an <strong>asynchronous</strong> example:
 * 
 * <p><code>
 * AppGlu.crudApi().readInBackground("tableName", "rowId", new AsyncCallback<Row>() {<br>
 *			
 *		&nbsp;&nbsp;&nbsp; public void onPreExecute() {<br>
 *			<br>
 *		&nbsp;&nbsp;&nbsp; }<br>
 *		
 *		&nbsp;&nbsp;&nbsp; public void onResult(Row result) {<br>
 *			<br>
 *		&nbsp;&nbsp;&nbsp; }<br>
 *
 *		&nbsp;&nbsp;&nbsp; public void onException(ExceptionWrapper exceptionWrapper) {<br>
 *			<br>
 *		&nbsp;&nbsp;&nbsp; }<br>
 *
 *		&nbsp;&nbsp;&nbsp; public void onNoInternetConnection() {<br>
 *			<br>
 *		&nbsp;&nbsp;&nbsp; }<br>
 *
 *		&nbsp;&nbsp;&nbsp; public void onFinish() {<br>
 *			<br>
 *		&nbsp;&nbsp;&nbsp; }<br>
 *		
 *	});
 * </code>
 * 
 * <p><strong>Make sure to call this method ON the UI thread</strong>.<br>
 * It will execute in a separated thread and return the result to the {@link AsyncCallback#onResult(Object)} method.
 * 
 * @see com.appglu.android.AppGluSettings
 * @since 1.0.0
 */
public final class AppGlu {
	
	/**
	 * Log tag used for all logging that the AppGlu Android SDK does
	 */
	public static final String LOG_TAG = "AppGlu";
	
	/**
	 * Current AppGlu Android SDK version
	 */
	public static final String VERSION = "1.0.0";
	
	static final String APPGLU_PREFERENCES_KEY = "com.appglu.android.AppGlu.APPGLU_PREFERENCES_KEY";
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.LOG_TAG);
	
	private static AppGlu instance;
	
	private Context context;
	
	private AppGluTemplate appGluTemplate;
	
	private AppGluSettings settings;
	
	private DeviceInstallation deviceInstallation;
	
	private CrudApi crudApi;
	
	private SavedQueriesApi savedQueriesApi;
	
	private PushApi pushApi;
	
	private AnalyticsApi analyticsApi;
	
	private UserApi userApi;
	
	private SyncApi syncApi;
	
	private StorageApi storageApi;
	
	private AppGlu() {
		
	}
	
	private static synchronized AppGlu getInstance() {
		if (instance == null) {
			instance = new AppGlu();
		}
		return instance;
	}
	
	private static AppGlu getRequiredInstance() {
		if (instance == null) {
			throw new AppGluNotInitializedException();
		}
		return instance;
	}
	
	private void doInitialize(Context context, AppGluSettings settings) {
		AppGluUtils.assertNotNull(context, "Context cannot be null");
		AppGluUtils.assertNotNull(settings, "AppGluSettings cannot be null");
		
		this.context = context.getApplicationContext();
		
		this.settings = settings;
		this.deviceInstallation = new DeviceInstallation(this.context);
		
		this.appGluTemplate = settings.createAppGluTemplate();
		this.appGluTemplate.setAsyncExecutor(new AsyncTaskExecutor());
		this.appGluTemplate.setDefaultHeaders(this.deviceInstallation.createDefaultHeaders(settings));
		
		UserSessionPersistence userSessionPersistence = this.settings.getUserSessionPersistence();
		if (userSessionPersistence == null) {
			userSessionPersistence = new SharedPreferencesUserSessionPersistence(this.context);
		}
		this.appGluTemplate.setUserSessionPersistence(userSessionPersistence);
		
		logger.info("AppGlu was initialized");
	}

	private AppGluTemplate getAppGluTemplate() {
		if (this.appGluTemplate == null) {
			throw new AppGluNotInitializedException();
		}
		return appGluTemplate;
	}

	private AppGluSettings getSettings() {
		if (this.settings == null) {
			throw new AppGluNotInitializedException();
		}
		return settings;
	}
	
	private DeviceInstallation getDeviceInstallation() {
		if (this.deviceInstallation == null) {
			throw new AppGluNotInitializedException();
		}
		return deviceInstallation;
	}
	
	private CrudApi getCrudApi() {
		if (this.crudApi == null) {
			this.crudApi = new CrudApi(this.getAppGluTemplate().crudOperations(), this.getAppGluTemplate().asyncCrudOperations());
		}
		return this.crudApi;
	}
	
	private SavedQueriesApi getSavedQueriesApi() {
		if (this.savedQueriesApi == null) {
			SavedQueriesOperations savedQueriesOperations = this.getAppGluTemplate().savedQueriesOperations();
			AsyncSavedQueriesOperations asyncSavedQueriesOperations = this.getAppGluTemplate().asyncSavedQueriesOperations();
			
			this.savedQueriesApi = new SavedQueriesApi(savedQueriesOperations, asyncSavedQueriesOperations);
		}
		return this.savedQueriesApi;
	}
	
	private PushApi getPushApi() {
		if (this.pushApi == null) {
			PushOperations pushOperations = this.getAppGluTemplate().pushOperations();
			AsyncPushOperations asyncPushOperations = this.getAppGluTemplate().asyncPushOperations();
			
			this.pushApi = new PushApi(pushOperations, asyncPushOperations, this.getDeviceInstallation());
		}
		return this.pushApi;
	}
	
	private AnalyticsApi getAnalyticsApi() {
		if (this.analyticsApi == null) {
			AnalyticsDatabaseHelper analyticsDatabaseHelper = new AnalyticsDatabaseHelper(this.context);
			AnalyticsRepository analyticsRepository = new SQLiteAnalyticsRepository(analyticsDatabaseHelper);
			
			AnalyticsDispatcher analyticsDispatcher = this.createAnalyticsDispatcher();
			this.analyticsApi = new AnalyticsApi(analyticsDispatcher, analyticsRepository);
			this.analyticsApi.setSessionCallback(this.getSettings().getAnalyticsSessionCallback());
		}
		return this.analyticsApi;
	}
	
	private AnalyticsDispatcher createAnalyticsDispatcher() {
		AnalyticsDispatcher analyticsDispatcher = this.getSettings().getAnalyticsDispatcher();
		if (analyticsDispatcher != null) {
			return analyticsDispatcher;
		}
		if (this.getSettings().isUploadAnalyticsSessionsToServer()) {
			return new ApiAnalyticsDispatcher(this.getAppGluTemplate().analyticsOperations());
		} else {
			return new LogAnalyticsDispatcher();
		}
	}
	
	private UserApi getUserApi() {
		if (this.userApi == null) {
			this.userApi = new UserApi(this.getAppGluTemplate().userOperations(), this.getAppGluTemplate().asyncUserOperations());
		}
		return this.userApi;
	}
	
	private SyncApi getSyncApi() {
		if (this.syncApi == null) {
			SyncDatabaseHelper defaultSyncDatabaseHelper = this.getSettings().getDefaultSyncDatabaseHelper();
			
			if (defaultSyncDatabaseHelper == null) {
				throw new AppGluNotProperlyConfiguredException("The 'defaultSyncDatabaseHelper' property was not set on AppGluSettings. " +
					"It is required to set a default database helper on initialization.");
			}
			
			this.syncApi = this.getSyncApi(defaultSyncDatabaseHelper);
		}
		return this.syncApi;
	}
	
	private SyncApi getSyncApi(SyncDatabaseHelper syncDatabaseHelper) {
		AppGluUtils.assertNotNull(syncDatabaseHelper, "SyncDatabaseHelper cannot be null");
		
		SyncOperations syncOperations = this.getAppGluTemplate().syncOperations();
		SyncRepository syncRepository = new SQLiteSyncRepository(syncDatabaseHelper);
		
		StorageOperations storageOperations = this.getAppGluTemplate().storageOperations();
		SyncFileStorageService syncStorageService = new SyncFileStorageService(this.context, storageOperations);
		
		return new SyncApi(this.context, syncOperations, syncRepository, syncStorageService);
	}
	
	private StorageApi getStorageApi() {
		if (this.storageApi == null) {
			StorageOperations storageOperations = this.getAppGluTemplate().storageOperations();
			StorageService storageService = new StorageService(storageOperations, this.createCacheManager());
			
			long timeToLive = this.getSettings().getStorageCacheTimeToLiveInMilliseconds();
			storageService.setCacheTimeToLiveInMilliseconds(timeToLive);
			
			this.storageApi = new StorageApi(storageService);
		}
		return this.storageApi;
	}
	
	private StorageApi getStorageApi(CacheManager storageCacheManager) {
		StorageOperations storageOperations = this.getAppGluTemplate().storageOperations();
		StorageService storageService = new StorageService(storageOperations, storageCacheManager);
		
		long timeToLive = this.getSettings().getStorageCacheTimeToLiveInMilliseconds();
		storageService.setCacheTimeToLiveInMilliseconds(timeToLive);
		
		return new StorageApi(storageService);
	}
	
	private CacheManager createCacheManager() {
		CacheManager cacheManager = this.getSettings().getDefaultStorageCacheManager();
		if (cacheManager != null) {
			return cacheManager;
		}
		return new FileSystemCacheManager(context);
	}
	
	private boolean checkInternetConnection() {
		return AppGluUtils.hasInternetConnection(context);
	}
	
	//Public Methods
	
	/**
	 * Initializes {@code AppGlu} singleton using {@link AppGluSettings}.<br>
	 * This must be called before using any other method of {@code AppGlu} class otherwise a {@link AppGluNotInitializedException} will be thrown.
	 * @param context this can be an Activity or an Application object
	 * @param settings contains credentials and configuration settings
	 * @see AppGluSettings
	 */
	public static synchronized void initialize(Context context, AppGluSettings settings) {
		getInstance().doInitialize(context, settings);
	}
	
	/**
	 * Returns true if the Internet connection is available on the device, false otherwise.
	 */
	public static boolean hasInternetConnection() {
		return getRequiredInstance().checkInternetConnection();
	}
	
	/**
	 * Returns true if a mobile app user is authenticated either by signing up or logging in, false otherwise.
	 * @see UserApi#signup(User)
	 * @see UserApi#login(String, String)
	 * @see UserApi#logout()
	 */
	public static boolean isUserAuthenticated() {
		return getRequiredInstance().getAppGluTemplate().isUserAuthenticated();
	}
	
	/**
	 * Returns the mobile app {@link User} authenticated or null if no user is authenticated.
	 * @see UserApi#signup(User)
	 * @see UserApi#login(String, String)
	 * @see UserApi#logout()
	 */
	public static User getAuthenticatedUser() {
		return getRequiredInstance().getAppGluTemplate().getAuthenticatedUser();
	}
	
	/**
	 * Returns the {@link AppGluSettings} previously used to initialize {@code AppGlu}.
	 * @see AppGlu#initialize(Context, AppGluSettings)
	 * @see AppGluSettings
	 */
	public static AppGluSettings settings() {
		return getRequiredInstance().getSettings();
	}
	
	/**
	 * Returns a {@link DeviceInstallation} object that contains runtime information about the device that this application is running on.
	 */
	public static DeviceInstallation deviceInstallation() {
		return getRequiredInstance().getDeviceInstallation();
	}
	
	/**
	 * Returns a {@link CrudApi} object that contains create, read, update and delete operations to be applied on your tables.
	 * @see CrudApi
	 */
	public static CrudApi crudApi() {
		return getRequiredInstance().getCrudApi();
	}
	
	/**
	 * Returns a {@link SavedQueriesApi} object that allow you to run your previously created SQLs on AppGlu and obtain the results.
	 * @see SavedQueriesApi
	 */
	public static SavedQueriesApi savedQueriesApi() {
		return getRequiredInstance().getSavedQueriesApi();
	}
	
	/**
	 * Returns a {@link PushApi} object that is used to register / unregister the device making it eligible to receive push notifications.
	 * @see PushApi
	 */
	public static PushApi pushApi() {
		return getRequiredInstance().getPushApi();
	}
	
	/**
	 * Returns a {@link AnalyticsApi} object that is used to log events to AppGlu allowing it to collect mobile app usage statistics.
	 * @see AnalyticsApi
	 */
	public static AnalyticsApi analyticsApi() {
		return getRequiredInstance().getAnalyticsApi();
	}
	
	/**
	 * Returns a {@link UserApi} object that has methods to login / logout and sign up new mobile app users.
	 * @see UserApi
	 */
	public static UserApi userApi() {
		return getRequiredInstance().getUserApi();
	}
	
	/**
	 * Returns a {@link SyncApi} object that is used to synchronize the data in your local SQLite tables with the AppGlu server.<br>
	 * To use the SyncApi with success you must first define the default SQLite database to be used using {@link AppGluSettings#setDefaultSyncDatabaseHelper(SyncDatabaseHelper)}.<br>
	 * If the default database is not set then a {@link AppGluNotProperlyConfiguredException} will be thrown.
	 * @see SyncApi
	 */
	public static SyncApi syncApi() {
		return getRequiredInstance().getSyncApi();
	}
	
	/**
	 * Returns a {@link SyncApi} object that is used to synchronize the data in your local SQLite tables with the AppGlu server.
	 * @param syncDatabaseHelper the SQLite database to be used by {@link SyncApi}
	 * @see SyncApi
	 */
	public static SyncApi syncApi(SyncDatabaseHelper syncDatabaseHelper) {
		return getRequiredInstance().getSyncApi(syncDatabaseHelper);
	}
	
	/**
	 * Returns a {@link StorageApi} object that has methods to download and cache files from AppGlu server.<br>
	 * {@link StorageApi} will use a default {@link CacheManager} implementation defined by using {@link AppGluSettings#setDefaultStorageCacheManager(CacheManager)}.<br>
	 * If you do not set the default {@link CacheManager} implementation then {@link FileSystemCacheManager} is used.
	 * @see StorageApi
	 */
	public static StorageApi storageApi() {
		return getRequiredInstance().getStorageApi();
	}
	
	/**
	 * Returns a {@link StorageApi} object that has methods to download and cache files from AppGlu server.<br>
	 * @param storageCacheManager Defines the {@link CacheManager} implementation to be used by {@link StorageApi}
	 * @see StorageApi
	 */
	public static StorageApi storageApi(CacheManager storageCacheManager) {
		return getRequiredInstance().getStorageApi(storageCacheManager);
	}
	
}
