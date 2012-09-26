package com.appglu.android.analytics;

import java.util.List;

import com.appglu.AnalyticsOperations;
import com.appglu.AnalyticsSession;
import com.appglu.AppGluHttpClientException;
import com.appglu.android.AppGlu;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;

public class ApiAnalyticsDispatcher implements AnalyticsDispatcher {
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.LOG_TAG);
	
	private AnalyticsOperations analyticsOperations;
	
	public ApiAnalyticsDispatcher(AnalyticsOperations analyticsOperations) {
		this.analyticsOperations = analyticsOperations;
	}
	
	@Override
	public boolean shouldDispatchSessions(List<AnalyticsSession> sessions) {
		return AppGlu.hasInternetConnection();
	}
	
	@Override
	public void dispatchSessions(List<AnalyticsSession> sessions) {
		try {
			this.analyticsOperations.uploadSessions(sessions);
		} catch (AppGluHttpClientException e) {
			this.logger.error(e);
		}
	}
	
}