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
import java.util.Map;

import junit.framework.Assert;

import com.appglu.AnalyticsSession;
import com.appglu.AnalyticsSessionEvent;

public class SQLiteAnalyticsRepositoryTest extends AbstractAnalyticsSQLiteTest {

	private SQLiteAnalyticsRepository analyticsRepository;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		this.analyticsRepository = new SQLiteAnalyticsRepository(this.analyticsDatabaseHelper);
	}

	public void testGetCurrentSessionId()  {
		Long currentSessionId = this.analyticsRepository.getCurrentSessionId();
		Assert.assertEquals(Long.valueOf(1L), currentSessionId);
		
		int affectedRows = this.analyticsRepository.closeSessions(new Date());
		Assert.assertEquals(2, affectedRows);
		
		Long noSessionId = this.analyticsRepository.getCurrentSessionId();
		Assert.assertNull(noSessionId);
	}
	
	public void testGetAllClosedSessions() {
		List<AnalyticsSession> closedSessions = this.analyticsRepository.getAllClosedSessions();
		Assert.assertEquals(1, closedSessions.size());
		
		AnalyticsSession sessionTwo = closedSessions.get(0);
		this.assertClosedSession(sessionTwo);
		this.assertSessionParameters(sessionTwo, null);
		
		List<AnalyticsSessionEvent> expectedEvents = events(event("event2", null), event("event3", parameters456()));
		this.assertSessionEvents(sessionTwo, expectedEvents);
	}
	
	public void testGetSessionById() {
		AnalyticsSession sessionOne = this.analyticsRepository.getSessionById(1L);
		this.assertOpenSession(sessionOne);
		
		this.assertSessionParameters(sessionOne, parameters123());
		
		List<AnalyticsSessionEvent> expectedEvents = events(event("event1", parameters123()));
		this.assertSessionEvents(sessionOne, expectedEvents);
	}
	
	public void testRemoveAllClosedSessions() {
		Assert.assertEquals(3, this.countTable("sessions"));
		Assert.assertEquals(6, this.countTable("session_parameters"));
		Assert.assertEquals(3, this.countTable("session_events"));
		Assert.assertEquals(6, this.countTable("session_event_parameters"));
		
		this.analyticsRepository.removeAllClosedSessions();
		
		List<AnalyticsSession> closedSessions = this.analyticsRepository.getAllClosedSessions();
		Assert.assertEquals(0, closedSessions.size());
		
		Assert.assertEquals(2, this.countTable("sessions"));
		Assert.assertEquals(6, this.countTable("session_parameters"));
		Assert.assertEquals(1, this.countTable("session_events"));
		Assert.assertEquals(3, this.countTable("session_event_parameters"));
	}

	public void testCreateSession() {
		Assert.assertEquals(3, this.countTable("sessions"));
		
		AnalyticsSession session = new AnalyticsSession();
		
		Date now = new Date();
		long timestamp = now.getTime();
		
		session.setStartDate(now);
		
		this.analyticsRepository.createSession(session);
		
		Assert.assertEquals(4, this.countTable("sessions"));
		
		AnalyticsSession sessionFour = this.analyticsRepository.getSessionById(4L);
		this.assertOpenSession(sessionFour, timestamp);
		this.assertSessionParameters(sessionFour, null);
		this.assertSessionEvents(sessionFour, null);
	}
	
	public void testCreateSessionWithParamsAndEvents() {
		Assert.assertEquals(3, this.countTable("sessions"));
		
		AnalyticsSession session = new AnalyticsSession();
		
		Date now = new Date();
		long timestamp = now.getTime();
		
		session.setStartDate(now);
		
		List<AnalyticsSessionEvent> events = events(event("eventOne", parameters456()), event("eventTwo", parameters123()), event("eventThree", parameters123()));
		
		session.setParameters(parameters123());
		session.setEvents(events);
		
		this.analyticsRepository.createSession(session);
		
		Assert.assertEquals(4, this.countTable("sessions"));
		
		AnalyticsSession sessionFour = this.analyticsRepository.getSessionById(4L);
		this.assertOpenSession(sessionFour, timestamp);
		this.assertSessionParameters(sessionFour, parameters123());
		this.assertSessionEvents(sessionFour, events);
	}
	
	public void testForceCloseSessions() {
		int affectedRows = this.analyticsRepository.forceCloseSessions();
		Assert.assertEquals(2, affectedRows);
		
		List<AnalyticsSession> closedSessions = this.analyticsRepository.getAllClosedSessions();
		Assert.assertEquals(3, closedSessions.size());
		
		AnalyticsSession sessionOne = this.analyticsRepository.getSessionById(1L);
		this.assertSession(sessionOne, this.lastTimestamp, this.lastTimestamp);
		
		AnalyticsSession sessionTwo = this.analyticsRepository.getSessionById(2L);
		this.assertSession(sessionTwo, this.lastTimestamp, this.lastTimestamp);
		
		AnalyticsSession sessionThree = this.analyticsRepository.getSessionById(3L);
		this.assertSession(sessionThree, this.lastTimestamp, this.lastTimestamp);
	}
	
	public void testCloseSessions() {
		Date now = new Date();
		long timestamp = now.getTime();
		
		int affectedRows = this.analyticsRepository.closeSessions(now);
		Assert.assertEquals(2, affectedRows);
		
		List<AnalyticsSession> closedSessions = this.analyticsRepository.getAllClosedSessions();
		Assert.assertEquals(3, closedSessions.size());
		
		AnalyticsSession sessionOne = this.analyticsRepository.getSessionById(1L);
		this.assertSession(sessionOne, this.lastTimestamp, timestamp);
		
		AnalyticsSession sessionTwo = this.analyticsRepository.getSessionById(2L);
		this.assertSession(sessionTwo, this.lastTimestamp, this.lastTimestamp);
		
		AnalyticsSession sessionThree = this.analyticsRepository.getSessionById(3L);
		this.assertSession(sessionThree, this.lastTimestamp, timestamp);
	}
	
	public void testSetSessionParameter() {
		Assert.assertEquals(6, this.countTable("session_parameters"));
		
		this.analyticsRepository.setSessionParameter(1L, "name7", "value7");
		
		Assert.assertEquals(7, this.countTable("session_parameters"));
		
		AnalyticsSession sessionOne = this.analyticsRepository.getSessionById(1L);
		
		Map<String, String> parameters = parameters123();
		parameters.put("name7", "value7");
		
		this.assertSessionParameters(sessionOne, parameters);
	}
	
	public void testSetSessionParameterMoreThenOnce() {
		Assert.assertEquals(6, this.countTable("session_parameters"));
		
		this.analyticsRepository.setSessionParameter(2L, "name", "value1");
		this.analyticsRepository.setSessionParameter(2L, "name", "value2");
		
		Assert.assertEquals(7, this.countTable("session_parameters"));
		
		AnalyticsSession sessionTwo = this.analyticsRepository.getSessionById(2L);
		Assert.assertEquals(1, sessionTwo.getParameters().size());
		Assert.assertEquals("value2", sessionTwo.getParameters().get("name"));
	}
	
	public void testRemoveSessionParameter() {
		Assert.assertEquals(6, this.countTable("session_parameters"));
		
		this.analyticsRepository.removeSessionParameter(1L, "name1");
		this.analyticsRepository.removeSessionParameter(1L, "name2");
		this.analyticsRepository.removeSessionParameter(1L, "does_not_exist");
		
		Assert.assertEquals(4, this.countTable("session_parameters"));
		
		AnalyticsSession sessionOne = this.analyticsRepository.getSessionById(1L);
		Assert.assertEquals(1, sessionOne.getParameters().size());
	}
	
	public void testCreateEvent() {
		Assert.assertEquals(3, this.countTable("session_events"));
		
		AnalyticsSessionEvent event = new AnalyticsSessionEvent();
		event.setName("name");
		event.setDate(new Date());
		
		this.analyticsRepository.createEvent(3L, event);
		
		Assert.assertEquals(4, this.countTable("session_events"));
		
		AnalyticsSession sessionThree = this.analyticsRepository.getSessionById(3L);
		this.assertSessionEvents(sessionThree, events(event));
	}
	
	public void testCreateEventWithParams() {
		Assert.assertEquals(3, this.countTable("session_events"));
		
		AnalyticsSessionEvent event = new AnalyticsSessionEvent();
		event.setName("name");
		event.setDate(new Date());
		event.setParameters(parameters123());
		
		this.analyticsRepository.createEvent(3L, event);
		
		Assert.assertEquals(4, this.countTable("session_events"));
		
		AnalyticsSession sessionThree = this.analyticsRepository.getSessionById(3L);
		this.assertSessionEvents(sessionThree, events(event));
	}
	
	public void testSetEventParameter() {
		AnalyticsSessionEvent event = new AnalyticsSessionEvent();
		event.setName("name");
		event.setDate(new Date());
		
		long eventId = this.analyticsRepository.createEvent(3L, event);
		
		Assert.assertEquals(6, this.countTable("session_event_parameters"));

		this.analyticsRepository.setEventParameter(eventId, "name", "value");
		
		Assert.assertEquals(7, this.countTable("session_event_parameters"));

		event.addParameter("name", "value");

		AnalyticsSession sessionThree = this.analyticsRepository.getSessionById(3L);
		this.assertSessionEvents(sessionThree, events(event));
	}
	
	public void testSetEventParameterMoreThenOnce() {
		Assert.assertEquals(6, this.countTable("session_event_parameters"));

		this.analyticsRepository.setEventParameter(2L, "name", "value1");
		this.analyticsRepository.setEventParameter(2L, "name", "value2");
		
		Assert.assertEquals(7, this.countTable("session_event_parameters"));
	}
	
}
