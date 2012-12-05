package com.appglu.android.analytics;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AnalyticsDatabaseHelper extends SQLiteOpenHelper {
	
	protected static final String DATABASE_NAME = "appglu_analytics.sqlite";

	protected static final int DATABASE_VERSION = 1;

	public AnalyticsDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL("CREATE TABLE sessions (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, start_date NUMERIC NOT NULL, end_date NUMERIC);");
		database.execSQL("CREATE TABLE session_parameters (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, session_id INTEGER NOT NULL, name TEXT NOT NULL, value TEXT, FOREIGN KEY(session_id) REFERENCES sessions(id) ON DELETE CASCADE);");
		database.execSQL("CREATE TABLE session_events (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, session_id INTEGER NOT NULL, name TEXT NOT NULL, date NUMERIC, FOREIGN KEY(session_id) REFERENCES sessions(id) ON DELETE CASCADE);");
		database.execSQL("CREATE TABLE session_event_parameters (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, event_id INTEGER NOT NULL, name TEXT NOT NULL, value TEXT, FOREIGN KEY(event_id) REFERENCES session_events(id) ON DELETE CASCADE);");
		database.execSQL("CREATE UNIQUE INDEX session_parameters_name_unique ON session_parameters (session_id, name);");
		database.execSQL("CREATE UNIQUE INDEX session_event_parameters_name_unique ON session_event_parameters (event_id, name);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS session_event_parameters;");
		database.execSQL("DROP TABLE IF EXISTS session_events;");
		database.execSQL("DROP TABLE IF EXISTS session_parameters;");
		database.execSQL("DROP TABLE IF EXISTS sessions;");

        this.onCreate(database);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
	    super.onOpen(db);
	    if (!db.isReadOnly()) {
	        db.execSQL("PRAGMA foreign_keys=ON;");
	    }
	}

}