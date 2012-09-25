package com.appglu.android.analytics;

import java.util.List;

import com.appglu.AnalyticsSession;
import com.appglu.android.AppGlu;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;

public class LogAnalyticsDispatcher implements AnalyticsDispatcher {
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.LOG_TAG);

	@Override
	public void dispatchSessions(List<AnalyticsSession> sessions) {
		logger.info(String.valueOf(sessions));
	}

}