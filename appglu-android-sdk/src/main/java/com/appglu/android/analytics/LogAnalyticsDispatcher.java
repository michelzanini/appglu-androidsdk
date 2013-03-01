package com.appglu.android.analytics;

import java.util.List;

import com.appglu.AnalyticsSession;
import com.appglu.android.AppGlu;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;

/**
 * {@code LogAnalyticsDispatcher} is a implementation that will only log the events to the console.
 * @since 1.0.0
 */
public class LogAnalyticsDispatcher implements AnalyticsDispatcher {
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.LOG_TAG);
	
	@Override
	public boolean shouldDispatchSessions(List<AnalyticsSession> sessions) {
		return true;
	}

	@Override
	public void dispatchSessions(List<AnalyticsSession> sessions) {
		logger.info(String.valueOf(sessions));
	}

}