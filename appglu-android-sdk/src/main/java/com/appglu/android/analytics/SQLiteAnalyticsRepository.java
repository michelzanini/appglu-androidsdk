package com.appglu.android.analytics;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import com.appglu.AnalyticsSession;
import com.appglu.AnalyticsSessionEvent;
import com.appglu.impl.util.StringUtils;

public class SQLiteAnalyticsRepository implements AnalyticsRepository {
	
	private static final int SESSION_ID_COLUMN = 0;
	private static final int SESSION_START_DATE_COLUMN = 1;
	private static final int SESSION_END_DATE_COLUMN = 2;
	
	private static final int EVENT_ID_COLUMN = 3;
	private static final int EVENT_NAME_COLUMN = 4;
	private static final int EVENT_DATE_COLUMN = 5;
	
	private static final int EVENT_PARAM_ID_COLUMN = 6;
	private static final int EVENT_PARAM_NAME_COLUMN = 7;
	private static final int EVENT_PARAM_VALUE_COLUMN = 8;
	
	private static final int SESSION_PARAM_ID_COLUMN = 9;
	private static final int SESSION_PARAM_NAME_COLUMN = 10;
	private static final int SESSION_PARAM_VALUE_COLUMN = 11;
	
	private AnalyticsDatabaseHelper analyticsDatabaseHelper;

	public SQLiteAnalyticsRepository(AnalyticsDatabaseHelper analyticsDatabaseHelper) {
		this.analyticsDatabaseHelper = analyticsDatabaseHelper;
	}
	
	private SQLiteDatabase getReadableDatabase() {
		return this.analyticsDatabaseHelper.getReadableDatabase();
	}
	
	private SQLiteDatabase getWritableDatabase() {
		return this.analyticsDatabaseHelper.getWritableDatabase();
	}

	public Long getCurrentSessionId() {
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = null;
		try {
		    cursor = database.rawQuery("select id from sessions where end_date is null", null);
		    cursor.moveToFirst();
		    if (cursor.getCount() > 0) {
		    	return cursor.getLong(SESSION_ID_COLUMN);
		    }
			return null;
		} catch (SQLException e) {
			throw new AnalyticsRepositoryException(e);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		    database.close();
		}
	}
	
	public List<AnalyticsSession> getAllClosedSessions() {
		String sql = this.sessionSelectStatement("s.end_date is not null");
	    return this.queryForSessions(sql, null);
	}

	public AnalyticsSession getSessionById(long sessionId) {
		String sql = this.sessionSelectStatement("s.id = ?");
		List<AnalyticsSession> sessions = this.queryForSessions(sql, new String[] { String.valueOf(sessionId) });
		
		if (sessions.isEmpty()) {
			return null;
		}
		
		return sessions.get(0);
	}
	
	public void removeAllClosedSessions() {
		SQLiteDatabase database = this.getWritableDatabase();
		database.beginTransaction();
	    try {
			database.delete("sessions", "end_date is not null", null);
			database.setTransactionSuccessful();
		} catch (SQLException e) {
			throw new AnalyticsRepositoryException(e);
		} finally {
			database.endTransaction();
			database.close();
		}
	}
	
	public int forceCloseSessions() {
		SQLiteDatabase database = this.getWritableDatabase();
		database.beginTransaction();
	    try {
			Map<Integer, Date> endDates = this.getEndDateForOpenSessions(database);
	    	
			for (Entry<Integer, Date> entry : endDates.entrySet()) {
				ContentValues values = new ContentValues();
				values.put("end_date", entry.getValue().getTime());
				
				database.update("sessions", values, "id = ?", new String[] { String.valueOf(entry.getKey()) });
			}
			
			database.setTransactionSuccessful();
			return endDates.size();
		} catch (SQLException e) {
			throw new AnalyticsRepositoryException(e);
		} finally {
			database.endTransaction();
			database.close();
		}
	}
	
	public int closeSessions(Date endDate) {
		if (endDate == null) {
			throw new AnalyticsRepositoryException("endDate cannot be null");
		}
		
		SQLiteDatabase database = this.getWritableDatabase();
		database.beginTransaction();
	    try {
			ContentValues values = new ContentValues();
			values.put("end_date", endDate.getTime());
			
			int rowsAffected = database.update("sessions", values, "end_date is null", null);
			database.setTransactionSuccessful();
			return rowsAffected;
		} catch (SQLException e) {
			throw new AnalyticsRepositoryException(e);
		} finally {
			database.endTransaction();
			database.close();
		}
	}
	
	public long createSession(AnalyticsSession session) {
		if (session == null || session.getStartDate() == null) {
			throw new AnalyticsRepositoryException("session or it's startDate cannot be null");
		}
		
		SQLiteDatabase database = this.getWritableDatabase();
		database.beginTransaction();
	    try {
			long sessionId = this.doCreateSession(database, session);
			database.setTransactionSuccessful();
			return sessionId;
		} catch (SQLException e) {
			throw new AnalyticsRepositoryException(e);
		} finally {
			database.endTransaction();
			database.close();
		}
	}

	public void setSessionParameter(long sessionId, String name, String value) {
		if (StringUtils.isEmpty(name)) {
			throw new AnalyticsRepositoryException("name cannot be null");
		}
		
		SQLiteDatabase database = this.getWritableDatabase();
		database.beginTransaction();
	    try {
			try {
				this.doAddSessionParameter(database, sessionId, name, value);
			} catch (SQLiteConstraintException e) {
				this.doUpdateSessionParameter(database, sessionId, name, value);
			}
			database.setTransactionSuccessful();
		} catch (SQLException e) {
			throw new AnalyticsRepositoryException(e);
		} finally {
			database.endTransaction();
			database.close();
		}
	}

	public void removeSessionParameter(long sessionId, String name) {
		SQLiteDatabase database = this.getWritableDatabase();
		database.beginTransaction();
	    try {
	    	database.delete("session_parameters", "session_id = ? and name = ?", new String[] { String.valueOf(sessionId), name });
			database.setTransactionSuccessful();
		} catch (SQLException e) {
			throw new AnalyticsRepositoryException(e);
		} finally {
			database.endTransaction();
			database.close();
		}
	}

	public long createEvent(long sessionId, AnalyticsSessionEvent event) {
		if (event == null || StringUtils.isEmpty(event.getName())) {
			throw new AnalyticsRepositoryException("event or it's name cannot be null");
		}
		
		SQLiteDatabase database = this.getWritableDatabase();
		database.beginTransaction();
	    try {
	    	long eventId = this.doCreateEvent(database, sessionId, event);
	    	database.setTransactionSuccessful();
			return eventId;
		} catch (SQLException e) {
			throw new AnalyticsRepositoryException(e);
		} finally {
			database.endTransaction();
			database.close();
		}
	}

	public void setEventParameter(long eventId, String name, String value) {
		if (StringUtils.isEmpty(name)) {
			throw new AnalyticsRepositoryException("name cannot be null");
		}
		
		SQLiteDatabase database = this.getWritableDatabase();
		database.beginTransaction();
	    try {
			try {
				this.doAddEventParameter(database, eventId, name, value);
			} catch (SQLiteConstraintException e) {
				this.doUpdateEventParameter(database, eventId, name, value);
			}
			database.setTransactionSuccessful();
		} catch (SQLException e) {
			throw new AnalyticsRepositoryException(e);
		} finally {
			database.endTransaction();
			database.close();
		}
	}
	
	private long doCreateSession(SQLiteDatabase database, AnalyticsSession session) {
		ContentValues values = new ContentValues();
		
		values.put("start_date", session.getStartDate().getTime());
		
		long sessionId = database.insertOrThrow("sessions", null, values);
		
		for (Entry<String, String> parameter : session.getParameters().entrySet()) {
			this.doAddSessionParameter(database, sessionId, parameter.getKey(), parameter.getValue());
		}
		
		for (AnalyticsSessionEvent event : session.getEvents()) {
			this.doCreateEvent(database, sessionId, event);
		}
		
		return sessionId;
	}
	
	private void doAddSessionParameter(SQLiteDatabase database, long sessionId, String name, String value) {
		ContentValues values = new ContentValues();
		
		values.put("session_id", sessionId);
		values.put("name", name);
		values.put("value", value);
		
		if (name != null) {
			database.insertOrThrow("session_parameters", null, values);
		}
	}
	
	private long doCreateEvent(SQLiteDatabase database, long sessionId, AnalyticsSessionEvent event) {
		ContentValues values = new ContentValues();
		
		values.put("session_id", sessionId);
		values.put("name", event.getName());
		if (event.getDate() == null) {
			values.putNull("date");
		} else {
			values.put("date", event.getDate().getTime());
		}
		
		long eventId = database.insertOrThrow("session_events", null, values);
		
		for (Entry<String, String> parameter : event.getParameters().entrySet()) {
			this.doAddEventParameter(database, eventId, parameter.getKey(), parameter.getValue());
		}
		
		return eventId;
	}

	private void doAddEventParameter(SQLiteDatabase database, long eventId, String name, String value) {
		ContentValues values = new ContentValues();
		
		values.put("event_id", eventId);
		values.put("name", name);
		values.put("value", value);
		
		if (name != null) {
			database.insertOrThrow("session_event_parameters", null, values);
		}
	}
	
	private void doUpdateSessionParameter(SQLiteDatabase database, long sessionId, String name, String value) {
		ContentValues values = new ContentValues();
		
		values.put("session_id", sessionId);
		values.put("value", value);
		
		database.update("session_parameters", values, "name = ?", new String[] { name });
	}
	
	private void doUpdateEventParameter(SQLiteDatabase database, long eventId, String name, String value) {
		ContentValues values = new ContentValues();
		
		values.put("event_id", eventId);
		values.put("value", value);
		
		database.update("session_event_parameters", values, "name = ?", new String[] { name });
	}
	
	private Map<Integer, Date> getEndDateForOpenSessions(SQLiteDatabase database) {
		Map<Integer, Date> endDates = new HashMap<Integer, Date>();

		Cursor cursor = null;
		
		try {
			String sql = "select s.id, ifnull(max(e.date), s.start_date) from sessions s " +
				"left outer join session_events e on s.id = e.session_id where s.end_date is null group by s.id";
			
			cursor = database.rawQuery(sql, null);
		    cursor.moveToFirst();
		    
		    for (int i = 0; i < cursor.getCount(); i++) {
		    	Integer sessionId = new Integer(cursor.getInt(0));
		    	Date endDate = new Date(cursor.getLong(1));
		    	
		    	endDates.put(sessionId, endDate);
		    	cursor.moveToNext();
		    }
		
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		
		return endDates;
	}
	
	private String sessionSelectStatement(String whereClause) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("select s.id, s.start_date, s.end_date, e.id, e.name, e.date, ep.id, ep.name, ep.value, p.id, p.name, p.value ");
		builder.append("from sessions s ");
		builder.append("left outer join session_parameters p on s.id = p.session_id ");
		builder.append("left outer join session_events e on s.id = e.session_id ");
		builder.append("left outer join session_event_parameters ep on e.id = ep.event_id ");
		if (whereClause != null) {
			builder.append("where ");
			builder.append(whereClause);
		}
		builder.append(" order by s.id, e.id, ep.id, p.id");
		
		return builder.toString();
	}
	
	private List<AnalyticsSession> queryForSessions(String sql, String[] selectionArgs) {
		List<AnalyticsSession> sessions = new ArrayList<AnalyticsSession>();
	    
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = null;
		
		try {
			AnalyticsSession lastSession = null;
			AnalyticsSessionEvent lastEvent = null;
			
			long lastSessionId = 0;
			long lastEventId = 0;
			long lastEventParamId = 0;
			
			List<Integer> lastSessionParamsIds = null;

		    cursor = database.rawQuery(sql, selectionArgs);
		    cursor.moveToFirst();
		    
		    for (int i = 0; i < cursor.getCount(); i++) {
		        int sessionId = cursor.getInt(SESSION_ID_COLUMN);
		        if (sessionId != lastSessionId) {
		        	lastSession = this.mapSessionRow(cursor);
		        	sessions.add(lastSession);
		        	lastSessionId = sessionId;
		        	lastEventId = 0;
		        	lastEventParamId = 0;
		        	lastSessionParamsIds = new ArrayList<Integer>();
		        }
		    	
		    	int eventId = cursor.getInt(EVENT_ID_COLUMN);
		    	if (eventId != lastEventId) {
		    		lastEvent = this.mapEventRow(cursor);
		    		lastSession.addEvent(lastEvent);
		    		lastEventId = eventId;
		    		lastEventParamId = 0;
		    	}
		    	
		    	int eventParamId = cursor.getInt(EVENT_PARAM_ID_COLUMN);
		    	if (eventParamId != lastEventParamId) {
		    		String name = cursor.getString(EVENT_PARAM_NAME_COLUMN);
		    		String value = cursor.getString(EVENT_PARAM_VALUE_COLUMN);
		    		
		    		lastEvent.addParameter(name, value);
		    		lastEventParamId = eventParamId;
		    	}
		    	
		    	int sessionParamId = cursor.getInt(SESSION_PARAM_ID_COLUMN);
		    	if (sessionParamId != 0 && !lastSessionParamsIds.contains(sessionParamId)) {
		    		String name = cursor.getString(SESSION_PARAM_NAME_COLUMN);
		    		String value = cursor.getString(SESSION_PARAM_VALUE_COLUMN);
		    		
		    		lastSession.addParameter(name, value);
		    		lastSessionParamsIds.add(sessionParamId);
		    	}
		        
		        cursor.moveToNext();
		    }
		} catch (SQLException e) {
			throw new AnalyticsRepositoryException(e);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		    database.close();
		}
	    
	    return sessions;
	}

	private AnalyticsSession mapSessionRow(Cursor cursor) {
		AnalyticsSession session = new AnalyticsSession();
		
		if (!cursor.isNull(SESSION_START_DATE_COLUMN)) {
			session.setStartDate(new Date(cursor.getLong(SESSION_START_DATE_COLUMN)));
		}
		
		if (!cursor.isNull(SESSION_END_DATE_COLUMN)) {
			session.setEndDate(new Date(cursor.getLong(SESSION_END_DATE_COLUMN)));
		}
		
		return session;
	}
	
	private AnalyticsSessionEvent mapEventRow(Cursor cursor) {
		AnalyticsSessionEvent event = new AnalyticsSessionEvent();
		
		event.setName(cursor.getString(EVENT_NAME_COLUMN));
		
		if (!cursor.isNull(EVENT_DATE_COLUMN)) {
			event.setDate(new Date(cursor.getLong(EVENT_DATE_COLUMN)));
		}
		
		return event;
	}
	
}