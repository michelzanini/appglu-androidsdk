package com.appglu.android.sample;

import android.annotation.TargetApi;
import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.appglu.android.AppGlu;
import com.appglu.android.AppGluSettings;
import com.appglu.android.log.LoggerLevel;

public class SampleApplication extends Application {
	
	public static final String LOG_TAG = "AppGluSample";
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		this.enableStrictMode();
		
		AppGluSettings settings = new AppGluSettings("https://dashboard.appglu.com", "2856G3EX7p1042m", "YE79wRR2e81RW977AT563UP25o2ctd");
		settings.setLoggerLevel(LoggerLevel.DEBUG);
		
		AppGlu.initialize(this, settings);
	}
	
	/**
	 * If device is superior to Gingerbread then enable strict mode to detect any error the SDK may have
	 */
	@TargetApi(10)
	private void enableStrictMode() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads()
		        .detectDiskWrites()
		        .detectNetwork()
		        .penaltyLog()
		        .build());
			
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		        .detectLeakedSqlLiteObjects()
		        .penaltyLog()
		        .penaltyDeath()
		        .build());
		}
	}
	
}