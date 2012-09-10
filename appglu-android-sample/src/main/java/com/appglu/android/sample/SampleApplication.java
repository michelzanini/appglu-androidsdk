package com.appglu.android.sample;

import com.appglu.android.AppGlu;
import com.appglu.android.AppGluSettings;

import android.app.Application;

public class SampleApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		AppGluSettings settings = new AppGluSettings("https://dashboard.appglu.com", "2856G3EX7p1042m", "YE79wRR2e81RW977AT563UP25o2ctd");
		AppGlu.initialize(settings);
	}
	
}