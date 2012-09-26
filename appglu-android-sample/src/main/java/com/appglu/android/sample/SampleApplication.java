package com.appglu.android.sample;

import java.util.List;

import android.app.Application;

import com.appglu.AnalyticsSession;
import com.appglu.android.AppGlu;
import com.appglu.android.AppGluSettings;
import com.appglu.android.analytics.AnalyticsSessionCallback;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;
import com.appglu.android.log.LoggerLevel;

public class SampleApplication extends Application {
	
	public static final String LOG_TAG = "AppGluSample";
	
	private Logger logger = LoggerFactory.getLogger(LOG_TAG);
	
	private AnalyticsSessionCallback analyticsSessionCallback = new AnalyticsSessionCallback() {
		
		@Override
		public void onStartSession(AnalyticsSession session) {
			logger.info("onStartSession");
			session.addParameter("onStartSession", "onStartSession");
		}
		
		@Override
		public void beforeDispatchSessions(List<AnalyticsSession> sessions) {
			logger.info("beforeUploadSessions");
			for (AnalyticsSession analyticsSession : sessions) {
				analyticsSession.addParameter("beforeUploadSessions", "beforeUploadSessions");
			}
		}
		
	};

	@Override
	public void onCreate() {
		super.onCreate();
		
		AppGluSettings settings = new AppGluSettings("https://dashboard.appglu.com", "2856G3EX7p1042m", "YE79wRR2e81RW977AT563UP25o2ctd");
		
		settings.setAnalyticsSessionCallback(this.analyticsSessionCallback);
		settings.setLoggerLevel(LoggerLevel.DEBUG);
		
		AppGlu.initialize(this, settings);
	}
	
}