package com.appglu.android;

import android.content.Context;

import com.appglu.AsyncPushOperations;
import com.appglu.AsyncSavedQueriesOperations;
import com.appglu.PushOperations;
import com.appglu.SavedQueriesOperations;
import com.appglu.SyncOperations;
import com.appglu.User;
import com.appglu.UserSessionPersistence;
import com.appglu.android.analytics.AnalyticsDatabaseHelper;
import com.appglu.android.analytics.AnalyticsDispatcher;
import com.appglu.android.analytics.AnalyticsRepository;
import com.appglu.android.analytics.ApiAnalyticsDispatcher;
import com.appglu.android.analytics.LogAnalyticsDispatcher;
import com.appglu.android.analytics.SQLiteAnalyticsRepository;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;
import com.appglu.android.sync.SQLiteSyncRepository;
import com.appglu.android.sync.SyncDatabaseHelper;
import com.appglu.android.sync.SyncRepository;
import com.appglu.android.util.AppGluUtils;
import com.appglu.impl.AppGluTemplate;
import com.appglu.impl.AsyncExecutor;

public final class AppGlu {
	
	public static final String LOG_TAG = "AppGlu";
	
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
	
	protected AppGlu() {
		
	}
	
	protected static synchronized AppGlu getInstance() {
		if (instance == null) {
			instance = new AppGlu();
		}
		return instance;
	}
	
	protected static AppGlu getRequiredInstance() {
		if (instance == null) {
			throw new AppGluNotInitializedException();
		}
		return instance;
	}
	
	protected void doInitialize(Context context, AppGluSettings settings) {
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

	protected AppGluTemplate getAppGluTemplate() {
		if (this.appGluTemplate == null) {
			throw new AppGluNotInitializedException();
		}
		return appGluTemplate;
	}

	protected AppGluSettings getSettings() {
		if (this.settings == null) {
			throw new AppGluNotInitializedException();
		}
		return settings;
	}
	
	protected DeviceInstallation getDeviceInstallation() {
		if (this.deviceInstallation == null) {
			throw new AppGluNotInitializedException();
		}
		return deviceInstallation;
	}
	
	protected CrudApi getCrudApi() {
		if (this.crudApi == null) {
			this.crudApi = new CrudApi(this.getAppGluTemplate().crudOperations(), this.getAppGluTemplate().asyncCrudOperations());
		}
		return this.crudApi;
	}
	
	protected SavedQueriesApi getSavedQueriesApi() {
		if (this.savedQueriesApi == null) {
			SavedQueriesOperations savedQueriesOperations = this.getAppGluTemplate().savedQueriesOperations();
			AsyncSavedQueriesOperations asyncSavedQueriesOperations = this.getAppGluTemplate().asyncSavedQueriesOperations();
			
			this.savedQueriesApi = new SavedQueriesApi(savedQueriesOperations, asyncSavedQueriesOperations);
		}
		return this.savedQueriesApi;
	}
	
	protected PushApi getPushApi() {
		if (this.pushApi == null) {
			PushOperations pushOperations = this.getAppGluTemplate().pushOperations();
			AsyncPushOperations asyncPushOperations = this.getAppGluTemplate().asyncPushOperations();
			
			this.pushApi = new PushApi(pushOperations, asyncPushOperations, this.getDeviceInstallation());
		}
		return this.pushApi;
	}
	
	protected AnalyticsApi getAnalyticsApi() {
		if (this.analyticsApi == null) {
			AnalyticsDatabaseHelper analyticsDatabaseHelper = new AnalyticsDatabaseHelper(this.context);
			AnalyticsRepository analyticsRepository = new SQLiteAnalyticsRepository(analyticsDatabaseHelper);
			
			AnalyticsDispatcher analyticsDispatcher = this.createAnalyticsDispatcher();
			this.analyticsApi = new AnalyticsApi(analyticsDispatcher, analyticsRepository);
			this.analyticsApi.setSessionCallback(this.getSettings().getAnalyticsSessionCallback());
		}
		return this.analyticsApi;
	}
	
	protected AnalyticsDispatcher createAnalyticsDispatcher() {
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
	
	protected UserApi getUserApi() {
		if (this.userApi == null) {
			this.userApi = new UserApi(this.getAppGluTemplate().userOperations(), this.getAppGluTemplate().asyncUserOperations());
		}
		return this.userApi;
	}
	
	protected SyncApi getSyncApi() {
		if (this.syncApi == null) {
			SyncDatabaseHelper defaultSyncDatabaseHelper = this.getSettings().getDefaultSyncDatabaseHelper();
			
			if (defaultSyncDatabaseHelper == null) {
				throw new IllegalStateException("The 'defaultSyncDatabaseHelper' property was not set on AppGluSettings. " +
					"It is required to set a default database helper on initialization.");
			}
			
			this.syncApi = this.getSyncApi(defaultSyncDatabaseHelper);
		}
		return this.syncApi;
	}
	
	protected SyncApi getSyncApi(SyncDatabaseHelper syncDatabaseHelper) {
		AppGluUtils.assertNotNull(syncDatabaseHelper, "SyncDatabaseHelper cannot be null");
		
		SyncOperations syncOperations = this.getAppGluTemplate().syncOperations();
		SyncRepository syncRepository = new SQLiteSyncRepository(syncDatabaseHelper);
		AsyncExecutor asyncExecutor = this.getAppGluTemplate().getAsyncExecutor();
		
		return new SyncApi(syncOperations, syncRepository, asyncExecutor);
	}
	
	protected boolean checkInternetConnection() {
		return AppGluUtils.hasInternetConnection(context);
	}

	//Public Methods
	
	public static void initialize(Context context, AppGluSettings settings) {
		if (instance == null) {
			getInstance().doInitialize(context, settings);
		}
	}
	
	public static boolean hasInternetConnection() {
		return getRequiredInstance().checkInternetConnection();
	}
	
	public static boolean isUserAuthenticated() {
		return getRequiredInstance().getAppGluTemplate().isUserAuthenticated();
	}
	
	public static User getAuthenticatedUser() {
		return getRequiredInstance().getAppGluTemplate().getAuthenticatedUser();
	}
	
	public static AppGluSettings settings() {
		return getRequiredInstance().getSettings();
	}
	
	public static DeviceInstallation deviceInstallation() {
		return getRequiredInstance().getDeviceInstallation();
	}
	
	public static CrudApi crudApi() {
		return getRequiredInstance().getCrudApi();
	}
	
	public static SavedQueriesApi savedQueriesApi() {
		return getRequiredInstance().getSavedQueriesApi();
	}
	
	public static PushApi pushApi() {
		return getRequiredInstance().getPushApi();
	}
	
	public static AnalyticsApi analyticsApi() {
		return getRequiredInstance().getAnalyticsApi();
	}
	
	public static UserApi userApi() {
		return getRequiredInstance().getUserApi();
	}
	
	public static SyncApi syncApi() {
		return getRequiredInstance().getSyncApi();
	}
	
	public static SyncApi syncApi(SyncDatabaseHelper syncDatabaseHelper) {
		return getRequiredInstance().getSyncApi(syncDatabaseHelper);
	}
	
}