package com.appglu.android;

import com.appglu.android.impl.AsyncTaskExecutor;
import com.appglu.impl.AppGluTemplate;

public final class AppGlu {
	
	private static AppGlu instance;
	
	private AppGluTemplate appGluTemplate;
	
	private AppGluSettings settings;
	
	private CrudApi crudApi;
	
	private SavedQueriesApi savedQueriesApi;
	
	protected AppGlu() { 
		
	}
	
	static synchronized AppGlu getInstance() {
		if (instance == null) {
			instance = new AppGlu();
		}
		return instance;
	}
	
	protected void doInitialize(AppGluSettings settings) {
		this.settings = settings;
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
	
	protected CrudApi getCrudApi() {
		if (this.crudApi == null) {
			this.crudApi = new CrudApi(this.getAppGluTemplate().crudOperations(), this.getAppGluTemplate().asyncCrudOperations());
		}
		return crudApi;
	}
	
	protected SavedQueriesApi getSavedQueriesApi() {
		if (this.savedQueriesApi == null) {
			this.savedQueriesApi = new SavedQueriesApi(this.getAppGluTemplate().savedQueriesOperations(), this.getAppGluTemplate().asyncSavedQueriesOperations());
		}
		return savedQueriesApi;
	}
	
	//Public Methods
	
	public static void initialize(AppGluSettings settings) {
		getInstance().doInitialize(settings);
	}
	
	public static CrudApi crudApi() {
		return getInstance().getCrudApi();
	}
	
	public static SavedQueriesApi savedQueriesApi() {
		return getInstance().getSavedQueriesApi();
	}
	
}