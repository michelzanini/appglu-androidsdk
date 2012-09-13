package com.appglu.android;

import android.content.Context;

import com.appglu.android.impl.AsyncTaskExecutor;
import com.appglu.impl.AppGluTemplate;

public final class AppGlu {
	
	static final String APPGLU_PREFERENCES_KEY = "com.appglu.android.AppGlu.APPGLU_PREFERENCES_KEY";
	
	private static AppGlu instance;
	
	private AppGluTemplate appGluTemplate;
	
	private AppGluSettings settings;
	
	private DeviceInformation deviceInformation;
	
	private CrudApi crudApi;
	
	private SavedQueriesApi savedQueriesApi;
	
	private PushApi pushApi;
	
	protected AppGlu() { 
		
	}
	
	protected static synchronized AppGlu getInstance() {
		if (instance == null) {
			instance = new AppGlu();
		}
		return instance;
	}
	
	protected void doInitialize(Context context, AppGluSettings settings) {
		this.settings = settings;
		this.deviceInformation = new DeviceInformation(context);
		this.appGluTemplate = settings.createAppGluTemplate();
		this.appGluTemplate.setAsyncExecutor(new AsyncTaskExecutor());
	}

	protected AppGluTemplate getAppGluTemplate() {
		if (this.appGluTemplate == null) {
			throw new AppGluException("AppGlu not initialized");
		}
		return appGluTemplate;
	}

	protected AppGluSettings getSettings() {
		if (this.settings == null) {
			throw new AppGluException("AppGlu not initialized");
		}
		return settings;
	}
	
	protected DeviceInformation getDeviceInformation() {
		if (this.deviceInformation == null) {
			throw new AppGluException("AppGlu not initialized");
		}
		return deviceInformation;
	}
	
	protected CrudApi getCrudApi() {
		if (this.crudApi == null) {
			this.crudApi = new CrudApi(this.getAppGluTemplate().crudOperations(), this.getAppGluTemplate().asyncCrudOperations());
		}
		return this.crudApi;
	}
	
	protected SavedQueriesApi getSavedQueriesApi() {
		if (this.savedQueriesApi == null) {
			this.savedQueriesApi = new SavedQueriesApi(this.getAppGluTemplate().savedQueriesOperations(), this.getAppGluTemplate().asyncSavedQueriesOperations());
		}
		return this.savedQueriesApi;
	}
	
	protected PushApi getPushApi() {
		if (this.pushApi == null) {
			this.pushApi = new PushApi(this.getAppGluTemplate().pushOperations(), this.getAppGluTemplate().asyncPushOperations());
		}
		return this.pushApi;
	}
	
	//Public Methods
	
	public static void initialize(Context context, AppGluSettings settings) {
		getInstance().doInitialize(context, settings);
	}
	
	public static DeviceInformation deviceInformation() {
		return getInstance().getDeviceInformation();
	}
	
	public static CrudApi crudApi() {
		return getInstance().getCrudApi();
	}
	
	public static SavedQueriesApi savedQueriesApi() {
		return getInstance().getSavedQueriesApi();
	}
	
	public static PushApi pushApi() {
		return getInstance().getPushApi();
	}
	
}