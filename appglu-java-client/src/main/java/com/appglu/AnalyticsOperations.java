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
package com.appglu;

import java.util.List;

/**
 * {@code AnalyticsOperations} is used to upload events to AppGlu allowing it to collect mobile app usage statistics.<br>
 * 
 * @see AsyncAnalyticsOperations
 * @since 1.0.0
 */
public interface AnalyticsOperations {
	
	/**
	 * Uploads a single session to AppGlu.
	 * @param session the session object
	 */
	void uploadSession(AnalyticsSession session) throws AppGluRestClientException;
	
	/**
	 * Uploads a list of sessions to AppGlu.
	 * @param sessions the session list
	 */
	void uploadSessions(List<AnalyticsSession> sessions) throws AppGluRestClientException;
	
	/**
	 * Uploads a list of sessions to AppGlu.
	 * @param sessions the session list
	 */
	void uploadSessions(AnalyticsSession... sessions) throws AppGluRestClientException;

}
