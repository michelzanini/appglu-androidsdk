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
 * This callback allow you receive the {@link com.appglu.AnalyticsSession} objects before they saved or sent to the server.<br>
 * This will allow you to change them before they are sent to the REST API.
 * @since 1.0.0
 */
public interface AnalyticsSessionCallback {

	/**
	 * Called before a newly created session is stored locally.<br>
	 * <strong>Warning</strong>: this method does <strong>NOT</strong> run on the main thread
	 */
	void onStartSession(AnalyticsSession session);
	
	/**
	 * Called before the sessions are sent to the {@link AnalyticsDispatcher}.<br>
	 * <strong>Warning</strong>: this method does <strong>NOT</strong> run on the main thread
	 */
	void beforeDispatchSessions(List<AnalyticsSession> sessions);
	
}
