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

import com.appglu.AnalyticsSession;

/**
 * Defines an strategy to where the {@link AnalyticsSession} are going to be sent.<br>
 * {@link ApiAnalyticsDispatcher} is a implementation that will send the events to the AppGlu server.
 * {@link LogAnalyticsDispatcher} is a implementation that will only log the events to the console.
 * 
 * @see ApiAnalyticsDispatcher
 * @see LogAnalyticsDispatcher
 * @since 1.0.0
 */
public interface AnalyticsDispatcher {
	
	/**
	 * Return <code>true</code> if you want {@link AnalyticsDispatcher#dispatchSessions(List)} to be called.
	 */
	boolean shouldDispatchSessions(List<AnalyticsSession> sessions);

	/**
	 * Applies the strategy of where to send the list of {@link AnalyticsSession} objects.
	 */
	void dispatchSessions(List<AnalyticsSession> sessions);
	
}
