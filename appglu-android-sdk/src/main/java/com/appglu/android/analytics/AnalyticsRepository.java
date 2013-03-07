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

import java.util.Date;
import java.util.List;

import com.appglu.AnalyticsSession;
import com.appglu.AnalyticsSessionEvent;

public interface AnalyticsRepository {
	
	public Long getCurrentSessionId();
	
	public List<AnalyticsSession> getAllClosedSessions();
	
	public void removeAllClosedSessions();
	
	public int forceCloseSessions();

	public int closeSessions(Date endDate);
	
	public long createSession(AnalyticsSession session);
	
	public void setSessionParameter(long sessionId, String name, String value);
	
	public void removeSessionParameter(long sessionId, String name);
	
	public long createEvent(long sessionId, AnalyticsSessionEvent event);
	
	public void setEventParameter(long eventId, String name, String value);
	
}
