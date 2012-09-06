package com.appglu.android;

import android.content.Context;

import com.appglu.impl.AppGluTemplate;

public final class AppGlu {
	
	private static AppGlu instance;
	
	private Context context;
	
	private AppGluTemplate appGluTemplate;
	
	private AppGluSettings settings;
	
	protected AppGlu() { 
		
	}
	
	static synchronized AppGlu getInstance() {
		if (instance == null) {
			instance = new AppGlu();
		}
		return instance;
	}
	
	protected void doInitialize(Context context, AppGluSettings settings) {
		this.context = context.getApplicationContext();
		this.settings = settings;
		
		this.appGluTemplate = settings.createAppGluTemplate();
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
	
	//Public Methods
	
	public static void initialize(Context context, AppGluSettings settings) {
		getInstance().doInitialize(context, settings);
	}
	
}