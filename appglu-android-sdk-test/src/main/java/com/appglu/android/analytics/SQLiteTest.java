package com.appglu.android.analytics;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import com.appglu.AnalyticsSession;
import com.appglu.AnalyticsSessionEvent;
import com.appglu.android.DeviceInformation;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

public abstract class SQLiteTest extends AndroidTestCase {
	
	protected DeviceInformation deviceInformation;
	
	protected AnalyticsDatabaseHelper analyticsDatabaseHelper;
	
	protected long lastTimestamp;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		// always say the device has Internet connection since we do not need to make HTTP calls
		this.deviceInformation = new DeviceInformation(getContext()) {
			@Override
			public boolean hasInternetConnection() {
				return true;
			}
		};
		
		this.analyticsDatabaseHelper = new AnalyticsDatabaseHelper(getContext());
		this.analyticsDatabaseHelper.onUpgrade(analyticsDatabaseHelper.getWritableDatabase(), AnalyticsDatabaseHelper.DATABASE_VERSION, AnalyticsDatabaseHelper.DATABASE_VERSION);
		
		this.setUpScript();
	}
	
	protected void setUpScript() {
		this.lastTimestamp = new Date().getTime();
		
		SQLiteDatabase database = this.analyticsDatabaseHelper.getWritableDatabase();
		database.beginTransaction();
		
		try {
			database.execSQL("insert into sessions (id, start_date, end_date) values (1, " + lastTimestamp + ", null)");
			database.execSQL("insert into sessions (id, start_date, end_date) values (2, " + lastTimestamp + ", " + lastTimestamp + ")");
			database.execSQL("insert into sessions (id, start_date, end_date) values (3, " + lastTimestamp + ", null)");
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
	
	protected int countTable(String tableName) {
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
	
	protected AnalyticsSessionEvent event(String name, Map<String, String> parameters) {
		AnalyticsSessionEvent event = new AnalyticsSessionEvent();
		event.setName(name);
		event.setDate(new Date(this.lastTimestamp));
		event.setParameters(parameters);
		return event;
	}
	
	protected List<AnalyticsSessionEvent> events(AnalyticsSessionEvent... eventArray) {
		return Arrays.asList(eventArray);
	}
	
	protected Map<String, String> parameters123() {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("name1", "value1");
		parameters.put("name2", "value2");
		parameters.put("name3", "value3");
		return parameters;
	}
	
	protected Map<String, String> parameters456() {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("name4", "value4");
		parameters.put("name5", "value5");
		parameters.put("name6", "value6");
		return parameters;
	}
	
	protected void assertOpenSession(AnalyticsSession session, Long startDate) {
		this.assertSession(session, startDate, null);
	}
	
	protected void assertOpenSession(AnalyticsSession session) {
		this.assertSession(session, this.lastTimestamp, null);
	}
	
	protected void assertClosedSession(AnalyticsSession session) {
		this.assertSession(session, this.lastTimestamp, this.lastTimestamp);
	}
	
	protected void assertSession(AnalyticsSession session, Long startDate, Long endDate) {
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
	}
	
	protected void assertSessionParameters(AnalyticsSession session, Map<String, String> parameters) {
		Assert.assertNotNull(session);
		if (parameters == null) {
			Assert.assertTrue(session.getParameters().isEmpty());
		} else {
			Assert.assertEquals(parameters, session.getParameters());
		}
	}
	
	protected void assertSessionEvents(AnalyticsSession session, List<AnalyticsSessionEvent> events) {
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

}
