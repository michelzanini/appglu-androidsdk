package com.appglu.android.analytics;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.appglu.AnalyticsSession;
import com.appglu.AnalyticsSessionEvent;
import com.appglu.android.DeviceInformation;
import com.appglu.android.analytics.AnalyticsDatabaseHelper;
import com.appglu.android.analytics.AnalyticsRepository;
import com.appglu.android.analytics.SQLiteAnalyticsRepository;

public class AnalyticsRepositoryTest extends AndroidTestCase {

	private DeviceInformation deviceInformation;
	
	private AnalyticsDatabaseHelper analyticsDatabaseHelper;
	
	private AnalyticsRepository analyticsRepository;
	
	private long lastTimestamp;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		this.deviceInformation = new DeviceInformation(getContext());
		
		this.analyticsDatabaseHelper = new AnalyticsDatabaseHelper(getContext());
		this.analyticsDatabaseHelper.onUpgrade(analyticsDatabaseHelper.getWritableDatabase(), AnalyticsDatabaseHelper.DATABASE_VERSION, AnalyticsDatabaseHelper.DATABASE_VERSION);
		
		this.setUpScript();
		
		this.analyticsRepository = new SQLiteAnalyticsRepository(this.analyticsDatabaseHelper);
	}

	/* --- Helper methods for tests --- */
	
	private void setUpScript() {
		this.lastTimestamp = new Date().getTime();
		
		SQLiteDatabase database = this.analyticsDatabaseHelper.getWritableDatabase();
		database.beginTransaction();
		
		try {
			database.execSQL("insert into sessions (id, start_date, end_date, client_uuid) values (1, " + lastTimestamp + ", null, '123')");
			database.execSQL("insert into sessions (id, start_date, end_date, client_uuid) values (2, " + lastTimestamp + ", " + lastTimestamp + ", '12345')");
			database.execSQL("insert into sessions (id, start_date, end_date, client_uuid) values (3, " + lastTimestamp + ", null, '123')");
			database.execSQL("insert into session_parameters (id, session_id, name, value) values (1, 1, 'name1', 'value1')");
			database.execSQL("insert into session_parameters (id, session_id, name, value) values (2, 1, 'name2', 'value2')");
			database.execSQL("insert into session_parameters (id, session_id, name, value) values (3, 1, 'name3', 'value3')");
			database.execSQL("insert into session_parameters (id, session_id, name, value) values (4, 3, 'name7', 'value4')");
			database.execSQL("insert into session_parameters (id, session_id, name, value) values (5, 3, 'name8', 'value5')");
			database.execSQL("insert into session_parameters (id, session_id, name, value) values (6, 3, 'name9', 'value6')");
			database.execSQL("insert into session_events (id, session_id, name, date) values (1, 1, 'event1', " + lastTimestamp + ")");
			database.execSQL("insert into session_events (id, session_id, name, date) values (2, 2, 'event2', " + lastTimestamp + ")");
			database.execSQL("insert into session_events (id, session_id, name, date) values (3, 2, 'event3', " + lastTimestamp + ")");
			database.execSQL("insert into session_event_parameters (id, event_id, name, value) values (1, 1, 'name1', 'value1')");
			database.execSQL("insert into session_event_parameters (id, event_id, name, value) values (2, 1, 'name2', 'value2')");
			database.execSQL("insert into session_event_parameters (id, event_id, name, value) values (3, 1, 'name3', 'value3')");
			database.execSQL("insert into session_event_parameters (id, event_id, name, value) values (4, 3, 'name4', 'value4')");
			database.execSQL("insert into session_event_parameters (id, event_id, name, value) values (5, 3, 'name5', 'value5')");
			database.execSQL("insert into session_event_parameters (id, event_id, name, value) values (6, 3, 'name6', 'value6')");
			
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
			database.close();
		}
	}
	
	private int countTable(String tableName) {
		SQLiteDatabase database = this.analyticsDatabaseHelper.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = database.query(tableName, null, null, null, null, null, null);
			return cursor.getCount();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			database.close();
		}
	}
	
	private AnalyticsSessionEvent event(String name, Map<String, String> parameters) {
		AnalyticsSessionEvent event = new AnalyticsSessionEvent();
		event.setName(name);
		event.setDate(new Date(this.lastTimestamp));
		event.setParameters(parameters);
		return event;
	}
	
	private List<AnalyticsSessionEvent> events(AnalyticsSessionEvent... eventArray) {
		return Arrays.asList(eventArray);
	}
	
	private Map<String, String> parameters123() {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("name1", "value1");
		parameters.put("name2", "value2");
		parameters.put("name3", "value3");
		return parameters;
	}
	
	private Map<String, String> parameters456() {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("name4", "value4");
		parameters.put("name5", "value5");
		parameters.put("name6", "value6");
		return parameters;
	}
	
	private void assertOpenSession(AnalyticsSession session, Long startDate, String clientUUID) {
		this.assertSession(session, startDate, null, clientUUID);
	}
	
	private void assertOpenSession(AnalyticsSession session, String clientUUID) {
		this.assertSession(session, this.lastTimestamp, null, clientUUID);
	}
	
	private void assertClosedSession(AnalyticsSession session, String clientUUID) {
		this.assertSession(session, this.lastTimestamp, this.lastTimestamp, clientUUID);
	}
	
	private void assertSession(AnalyticsSession session, Long startDate, Long endDate, String clientUUID) {
		Assert.assertNotNull(session);
		
		if (startDate == null) {
			Assert.assertNull(session.getStartDate());
		} else {
			Assert.assertNotNull(session.getStartDate());
			Assert.assertEquals(startDate, Long.valueOf((session.getStartDate().getTime())));
		}
		
		if (endDate == null) {
			Assert.assertNull(session.getEndDate());
		} else {
			Assert.assertNotNull(session.getEndDate());
			Assert.assertEquals(endDate, Long.valueOf((session.getEndDate().getTime())));
		}
		
		Assert.assertEquals(clientUUID, session.getClientUUID());
	}
	
	private void assertSessionParameters(AnalyticsSession session, Map<String, String> parameters) {
		Assert.assertNotNull(session);
		if (parameters == null) {
			Assert.assertTrue(session.getParameters().isEmpty());
		} else {
			Assert.assertEquals(parameters, session.getParameters());
		}
	}
	
	private void assertSessionEvents(AnalyticsSession session, List<AnalyticsSessionEvent> events) {
		Assert.assertNotNull(session);
		if (events == null) {
			Assert.assertTrue(session.getEvents().isEmpty());
			return;
		}
		
		Assert.assertNotNull(session.getEvents());
		Assert.assertEquals(events.size(), session.getEvents().size());
		
		for (int i = 0; i < events.size(); i++) {
			AnalyticsSessionEvent expectedEvent = events.get(i);
			AnalyticsSessionEvent returnedEvent = session.getEvents().get(i);
			
			Assert.assertEquals(expectedEvent.getName(), returnedEvent.getName());
			Assert.assertEquals(expectedEvent.getDate(), returnedEvent.getDate());
			Assert.assertEquals(expectedEvent.getParameters(), returnedEvent.getParameters());
		}
	}
	
	/* --- Test methods --- */
	
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
		this.assertClosedSession(sessionTwo, "12345");
		this.assertSessionParameters(sessionTwo, null);
		
		List<AnalyticsSessionEvent> expectedEvents = events(event("event2", null), event("event3", parameters456()));
		this.assertSessionEvents(sessionTwo, expectedEvents);
	}
	
	public void testGetSessionById() {
		AnalyticsSession sessionOne = this.analyticsRepository.getSessionById(1L);
		this.assertOpenSession(sessionOne, "123");
		
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
		session.setClientUUID(this.deviceInformation.getDeviceUUID());
		
		this.analyticsRepository.createSession(session);
		
		Assert.assertEquals(4, this.countTable("sessions"));
		
		AnalyticsSession sessionFour = this.analyticsRepository.getSessionById(4L);
		this.assertOpenSession(sessionFour, timestamp, sessionFour.getClientUUID());
		this.assertSessionParameters(sessionFour, null);
		this.assertSessionEvents(sessionFour, null);
	}
	
	public void testCreateSessionWithParamsAndEvents() {
		Assert.assertEquals(3, this.countTable("sessions"));
		
		AnalyticsSession session = new AnalyticsSession();
		
		Date now = new Date();
		long timestamp = now.getTime();
		
		session.setStartDate(now);
		session.setClientUUID(this.deviceInformation.getDeviceUUID());
		
		List<AnalyticsSessionEvent> events = events(event("eventOne", parameters456()), event("eventTwo", parameters123()), event("eventThree", parameters123()));
		
		session.setParameters(parameters123());
		session.setEvents(events);
		
		this.analyticsRepository.createSession(session);
		
		Assert.assertEquals(4, this.countTable("sessions"));
		
		AnalyticsSession sessionFour = this.analyticsRepository.getSessionById(4L);
		this.assertOpenSession(sessionFour, timestamp, sessionFour.getClientUUID());
		this.assertSessionParameters(sessionFour, parameters123());
		this.assertSessionEvents(sessionFour, events);
	}
	
	public void testCloseSessions() {
		Date now = new Date();
		long timestamp = now.getTime();
		
		int affectedRows = this.analyticsRepository.closeSessions(now);
		Assert.assertEquals(2, affectedRows);
		
		List<AnalyticsSession> closedSessions = this.analyticsRepository.getAllClosedSessions();
		Assert.assertEquals(3, closedSessions.size());
		
		AnalyticsSession sessionOne = this.analyticsRepository.getSessionById(1L);
		this.assertSession(sessionOne, this.lastTimestamp, timestamp, "123");
		
		AnalyticsSession sessionTwo = this.analyticsRepository.getSessionById(2L);
		this.assertSession(sessionTwo, this.lastTimestamp, this.lastTimestamp, "12345");
		
		AnalyticsSession sessionThree = this.analyticsRepository.getSessionById(3L);
		this.assertSession(sessionThree, this.lastTimestamp, timestamp, "123");
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