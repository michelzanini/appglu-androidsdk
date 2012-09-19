package com.appglu.android.impl.analytics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.util.StringUtils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.appglu.AnalyticsSession;
import com.appglu.AnalyticsSessionEvent;

public class SQLiteAnalyticsRepository implements AnalyticsRepository {
	
	private static final int SESSION_ID_COLUMN = 0;
	private static final int SESSION_START_DATE_COLUMN = 1;
	private static final int SESSION_END_DATE_COLUMN = 2;
	private static final int SESSION_CLIENT_UUID_COLUMN = 3;
	
	private static final int SESSION_PARAM_ID_COLUMN = 4;
	private static final int SESSION_PARAM_NAME_COLUMN = 5;
	private static final int SESSION_PARAM_VALUE_COLUMN = 6;
	
	private static final int EVENT_ID_COLUMN = 7;
	private static final int EVENT_NAME_COLUMN = 8;
	private static final int EVENT_DATE_COLUMN = 9;
	
	private static final int EVENT_PARAM_ID_COLUMN = 10;
	private static final int EVENT_PARAM_NAME_COLUMN = 11;
	private static final int EVENT_PARAM_VALUE_COLUMN = 12;
	
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
	
	private String sessionSelectStatement(String whereClause) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("select s.id, s.start_date, s.end_date, s.client_uuid, p.id, p.name, p.value, e.id, e.name, e.date, ep.id, ep.name, ep.value ");
		builder.append("from sessions s ");
		builder.append("left outer join session_parameters p on s.id = p.session_id ");
		builder.append("left outer join session_events e on s.id = e.session_id ");
		builder.append("left outer join session_event_parameters ep on e.id = ep.event_id ");
		if (whereClause != null) {
			builder.append("where ");
			builder.append(whereClause);
		}
		
		return builder.toString();
	}

	@Override
	public List<AnalyticsSession> getAllClosedSessions() {
		String sql = this.sessionSelectStatement("s.end_date is not null");
	    return this.queryForSessions(sql, null);
	}
	
	@Override
	public AnalyticsSession getCurrentSession() {
		String sql = this.sessionSelectStatement("s.end_date is null");
		List<AnalyticsSession> sessions = this.queryForSessions(sql, null);
		
		if (sessions.isEmpty()) {
			return null;
		}
		
		return sessions.get(0);
	}
	
	@Override
	public AnalyticsSession getSessionById(long id) {
		String sql = this.sessionSelectStatement("s.id = ?");
		List<AnalyticsSession> sessions = this.queryForSessions(sql, new String[] { String.valueOf(id) });
		
		if (sessions.isEmpty()) {
			return null;
		}
		
		return sessions.get(0);
	}
	
	@Override
	public long createSession(AnalyticsSession session) {
		if (session == null || session.getStartDate() == null) {
			throw new AnalyticsRepositoryException();
		}
		
		SQLiteDatabase database = this.getWritableDatabase();
	    try {
			ContentValues values = new ContentValues();
			values.put("start_date", session.getStartDate().getTime());
			values.put("client_uuid", session.getClientUUID());
			
			return database.insertOrThrow("sessions", null, values);
		} catch (SQLException e) {
			throw new AnalyticsRepositoryException(e);
		} finally {
			database.close();
		}
	}

	@Override
	public void closeSessions(Date endDate) {
		if (endDate == null) {
			throw new AnalyticsRepositoryException();
		}
		
		SQLiteDatabase database = this.getWritableDatabase();
	    try {
			ContentValues values = new ContentValues();
			values.put("end_date", endDate.getTime());
			
			database.update("sessions", values, "end_date is null", null);
		} catch (SQLException e) {
			throw new AnalyticsRepositoryException(e);
		} finally {
			database.close();
		}
	}

	@Override
	public void addSessionParameter(Long sessionId, String name, String value) {
		if (sessionId == null || !StringUtils.hasText(name)) {
			throw new AnalyticsRepositoryException();
		}
		
		SQLiteDatabase database = this.getWritableDatabase();
	    try {
			ContentValues values = new ContentValues();
			values.put("session_id", sessionId);
			values.put("name", name);
			values.put("value", value);
			
			database.insertOrThrow("session_parameters", null, values);
		} catch (SQLException e) {
			throw new AnalyticsRepositoryException(e);
		} finally {
			database.close();
		}
	}

	@Override
	public long createEvent(Long sessionId, AnalyticsSessionEvent event) {
		if (sessionId == null || event == null || !StringUtils.hasText(event.getName())) {
			throw new AnalyticsRepositoryException();
		}
		
		SQLiteDatabase database = this.getWritableDatabase();
	    try {
			ContentValues values = new ContentValues();
			values.put("session_id", sessionId);
			values.put("name", event.getName());
			if (event.getDate() == null) {
				values.putNull("date");
			} else {
				values.put("date", event.getDate().getTime());
			}
			
			return database.insertOrThrow("session_events", null, values);
		} catch (SQLException e) {
			throw new AnalyticsRepositoryException(e);
		} finally {
			database.close();
		}
	}

	@Override
	public void addEventParameter(Long eventId, String name, String value) {
		if (eventId == null || !StringUtils.hasText(name)) {
			throw new AnalyticsRepositoryException();
		}
		
		SQLiteDatabase database = this.getWritableDatabase();
	    try {
			ContentValues values = new ContentValues();
			values.put("event_id", eventId);
			values.put("name", name);
			values.put("value", value);
			
			database.insertOrThrow("session_event_parameters", null, values);
		} catch (SQLException e) {
			throw new AnalyticsRepositoryException(e);
		} finally {
			database.close();
		}
	}
	
	private List<AnalyticsSession> queryForSessions(String sql, String[] selectionArgs) {
		List<AnalyticsSession> sessions = new ArrayList<AnalyticsSession>();
	    
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = null;
		
		try {
			AnalyticsSession lastSession = null;
			AnalyticsSessionEvent lastEvent = null;
			
			long lastSessionId = 0;
			long lastSessionParamId = 0;
			long lastEventId = 0;
			long lastEventParamId = 0;
			
		    cursor = database.rawQuery(sql, selectionArgs);
		    cursor.moveToFirst();
		    
		    for (int i = 0; i < cursor.getCount(); i++) {
		        int sessionId = cursor.getInt(SESSION_ID_COLUMN);
		        if (sessionId != lastSessionId) {
		        	lastSession = this.mapSessionRow(cursor);
		        	sessions.add(lastSession);
		        	lastSessionId = sessionId;
		        	lastSessionParamId = 0;
					lastEventId = 0;
		        }
		    	
		        int sessionParamId = cursor.getInt(SESSION_PARAM_ID_COLUMN);
		    	if (sessionParamId != lastSessionParamId) {
		    		String name = cursor.getString(SESSION_PARAM_NAME_COLUMN);
		    		String value = cursor.getString(SESSION_PARAM_VALUE_COLUMN);
		    		
		    		lastSession.addParameter(name, value);
		    		lastSessionParamId = sessionParamId;
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
		        
		        cursor.moveToNext();
		    }
		} catch (SQLException e) {
			throw new AnalyticsRepositoryException(e);
		} finally {
		    cursor.close();
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
		
		session.setClientUUID(cursor.getString(SESSION_CLIENT_UUID_COLUMN));
		
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