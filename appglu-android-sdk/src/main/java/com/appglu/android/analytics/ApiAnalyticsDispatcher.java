/*******************************************************************************
 * Copyright 2013 AppGlu, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.appglu.android.analytics;

import java.util.List;

import com.appglu.AnalyticsOperations;
import com.appglu.AnalyticsSession;
import com.appglu.AppGluHttpClientException;
import com.appglu.android.AppGlu;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;

/**
 * {@code ApiAnalyticsDispatcher} is a implementation that will send the events to the AppGlu server.
 * @since 1.0.0
 */
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
