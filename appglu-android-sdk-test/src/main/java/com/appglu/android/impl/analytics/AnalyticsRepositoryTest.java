package com.appglu.android.impl.analytics;

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
		
		this.lastTimestamp = new Date().getTime();
		
		this.execSQL("insert into sessions (id, start_date, end_date, client_uuid) values (1, " + lastTimestamp + ", null, '123')");
		this.execSQL("insert into sessions (id, start_date, end_date, client_uuid) values (2, " + lastTimestamp + ", " + lastTimestamp + ", '12345')");
		this.execSQL("insert into sessions (id, start_date, end_date, client_uuid) values (3, " + lastTimestamp + ", null, '123')");
		
		this.execSQL("insert into session_parameters (id, session_id, name, value) values (1, 1, 'name1', 'value1')");
		this.execSQL("insert into session_parameters (id, session_id, name, value) values (2, 1, 'name2', 'value2')");
		this.execSQL("insert into session_parameters (id, session_id, name, value) values (3, 1, 'name3', 'value3')");
		this.execSQL("insert into session_parameters (id, session_id, name, value) values (4, 3, 'name7', 'value4')");
		this.execSQL("insert into session_parameters (id, session_id, name, value) values (5, 3, 'name8', 'value5')");
		this.execSQL("insert into session_parameters (id, session_id, name, value) values (6, 3, 'name9', 'value6')");
		
		this.execSQL("insert into session_events (id, session_id, name, date) values (1, 1, 'event1', " + lastTimestamp + ")");
		this.execSQL("insert into session_events (id, session_id, name, date) values (2, 2, 'event2', " + lastTimestamp + ")");
		this.execSQL("insert into session_events (id, session_id, name, date) values (3, 2, 'event3', " + lastTimestamp + ")");
		
		this.execSQL("insert into session_event_parameters (id, event_id, name, value) values (1, 1, 'name1', 'value1')");
		this.execSQL("insert into session_event_parameters (id, event_id, name, value) values (2, 1, 'name2', 'value2')");
		this.execSQL("insert into session_event_parameters (id, event_id, name, value) values (3, 1, 'name3', 'value3')");
		this.execSQL("insert into session_event_parameters (id, event_id, name, value) values (4, 3, 'name4', 'value4')");
		this.execSQL("insert into session_event_parameters (id, event_id, name, value) values (5, 3, 'name5', 'value5')");
		this.execSQL("insert into session_event_parameters (id, event_id, name, value) values (6, 3, 'name6', 'value6')");
		
		this.analyticsRepository = new SQLiteAnalyticsRepository(this.analyticsDatabaseHelper);
	}
	
	/* --- Helper methods for tests --- */
	
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
	
	private Map<String, String> parametersOne() {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("name1", "value1");
		parameters.put("name2", "value2");
		parameters.put("name3", "value3");
		return parameters;
	}
	
	private Map<String, String> parametersTwo() {
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
		Assert.assertEquals(session.getEvents().size(), events.size());
		
		for (int i = 0; i < events.size(); i++) {
			AnalyticsSessionEvent expectedEvent = events.get(i);
			AnalyticsSessionEvent returnedEvent = session.getEvents().get(i);
			
			Assert.assertEquals(expectedEvent.getName(), returnedEvent.getName());
			Assert.assertEquals(expectedEvent.getDate(), returnedEvent.getDate());
			Assert.assertEquals(expectedEvent.getParameters(), returnedEvent.getParameters());
		}
	}
	
	private void execSQL(String sql) {
		SQLiteDatabase database = this.analyticsDatabaseHelper.getWritableDatabase();
		database.execSQL(sql);
		database.close();
	}
	
	private int countTable(String tableName) {
		SQLiteDatabase database = this.analyticsDatabaseHelper.getReadableDatabase();
		Cursor cursor = database.query(tableName, null, null, null, null, null, null);
		int count = cursor.getCount();
		cursor.close();
		database.close();
		return count;
	}
	
	/* --- Test methods --- */
	
	public void testGetAllClosedSessions() {
		List<AnalyticsSession> closedSessions = this.analyticsRepository.getAllClosedSessions();
		Assert.assertEquals(1, closedSessions.size());
		
		AnalyticsSession sessionTwo = closedSessions.get(0);
		this.assertClosedSession(sessionTwo, "12345");
		
		this.assertSessionParameters(sessionTwo, null);
		
		List<AnalyticsSessionEvent> expectedEvents = this.events(event("event2", null), event("event3", parametersTwo()));
		this.assertSessionEvents(sessionTwo, expectedEvents);
	}
	
	public void testGetSessionById() {
		AnalyticsSession sessionOne = this.analyticsRepository.getSessionById(1L);
		this.assertOpenSession(sessionOne, "123");
		
		this.assertSessionParameters(sessionOne, parametersOne());
		
		List<AnalyticsSessionEvent> expectedEvents = this.events(event("event1", parametersOne()));
		this.assertSessionEvents(sessionOne, expectedEvents);
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
	
	public void testCloseSessions() {
		Date now = new Date();
		long timestamp = now.getTime();
		
		this.analyticsRepository.closeSessions(now);
		
		List<AnalyticsSession> closedSessions = this.analyticsRepository.getAllClosedSessions();
		Assert.assertEquals(3, closedSessions.size());
		
		AnalyticsSession sessionOne = this.analyticsRepository.getSessionById(1L);
		this.assertSession(sessionOne, this.lastTimestamp, timestamp, "123");
		
		AnalyticsSession sessionTwo = this.analyticsRepository.getSessionById(2L);
		this.assertSession(sessionTwo, this.lastTimestamp, this.lastTimestamp, "12345");
		
		AnalyticsSession sessionThree = this.analyticsRepository.getSessionById(3L);
		this.assertSession(sessionThree, this.lastTimestamp, timestamp, "123");
	}
	
	public void testAddSessionParameter() {
		Assert.assertEquals(6, this.countTable("session_parameters"));
		
		this.analyticsRepository.addSessionParameter(1L, "name7", "value7");
		
		Assert.assertEquals(7, this.countTable("session_parameters"));
		
		AnalyticsSession sessionOne = this.analyticsRepository.getSessionById(1L);
		
		Map<String, String> parameters = parametersOne();
		parameters.put("name7", "value7");
		
		this.assertSessionParameters(sessionOne, parameters);
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
	
	public void testAddEventParameter() {
		AnalyticsSessionEvent event = new AnalyticsSessionEvent();
		event.setName("name");
		event.setDate(new Date());
		event.addParameter("name", "value");
		
		long eventId = this.analyticsRepository.createEvent(3L, event);
		
		Assert.assertEquals(6, this.countTable("session_event_parameters"));
		
		this.analyticsRepository.addEventParameter(eventId, "name", "value");
		
		Assert.assertEquals(7, this.countTable("session_event_parameters"));
		
		AnalyticsSession sessionThree = this.analyticsRepository.getSessionById(3L);
		this.assertSessionEvents(sessionThree, events(event));
	}
	
}
