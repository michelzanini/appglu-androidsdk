package com.appglu.android.sync;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class SyncDatabaseHelper extends SQLiteOpenHelper {
	
	private static final int MAX_APP_DATABASE_VERSION_IN_BITS = 20;
	private static final int MAX_APP_DATABASE_VERSION_IN_INTEGER = (1 << MAX_APP_DATABASE_VERSION_IN_BITS) - 1;
	
	private static final int MAX_SYNC_DATABASE_VERSION_IN_BITS = Integer.SIZE - MAX_APP_DATABASE_VERSION_IN_BITS;
	private static final int MAX_SYNC_DATABASE_VERSION_IN_INTEGER = (1 << MAX_SYNC_DATABASE_VERSION_IN_BITS) - 1;
	
	private static final int SYNC_DATABASE_VERSION = 1;

	public SyncDatabaseHelper(Context context, String name, int version) {
		super(context, name, null, joinVersionNumbers(SYNC_DATABASE_VERSION, version));
	}

	public final void onCreate(SQLiteDatabase db) {
		this.onCreateSyncDatabase(db);
		this.onCreateAppDatabase(db);
	}

	public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		int syncOldVersion = this.extractSyncDatabaseVersion(oldVersion);
		int syncNewVersion = this.extractSyncDatabaseVersion(newVersion);
		
		this.onUpgradeSyncDatabase(db, syncOldVersion, syncNewVersion);
		
		int appOldVersion = this.extractAppDatabaseVersion(oldVersion);
		int appNewVersion = this.extractAppDatabaseVersion(newVersion);
		
		this.onUpgradeAppDatabase(db, appOldVersion, appNewVersion);
	}
	
	public void onCreateSyncDatabase(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE appglu_storage_files (id INTEGER PRIMARY KEY NOT NULL, key VARCHAR, name VARCHAR, content_type VARCHAR, title VARCHAR, size INTEGER, last_modified NUMERIC, url VARCHAR, directory_id INTEGER);");
		db.execSQL("CREATE TABLE appglu_table_versions (table_name VARCHAR PRIMARY KEY NOT NULL, version INTEGER NOT NULL DEFAULT 0);");
		db.execSQL("CREATE INDEX appglu_storage_files_url ON appglu_storage_files (url);");
	}
	
	public void onUpgradeSyncDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	public abstract void onCreateAppDatabase(SQLiteDatabase db);
	
	public abstract void onUpgradeAppDatabase(SQLiteDatabase db, int oldVersion, int newVersion);
	
	/* Utility methods */
	
	private static int joinVersionNumbers(int syncDatabaseVersion, int appDatabaseVersion) {
		if (appDatabaseVersion > MAX_APP_DATABASE_VERSION_IN_INTEGER) {
			throw new IllegalArgumentException("Version must be < " + MAX_APP_DATABASE_VERSION_IN_INTEGER + ", was " + appDatabaseVersion);
		}
		
		if (syncDatabaseVersion > MAX_SYNC_DATABASE_VERSION_IN_INTEGER) {
			throw new IllegalArgumentException("AppGlu sync database version must be < " + MAX_SYNC_DATABASE_VERSION_IN_INTEGER + ", was " + syncDatabaseVersion);
		}
		
		int shiftedSyncDatabaseVersion = syncDatabaseVersion << MAX_APP_DATABASE_VERSION_IN_BITS;
		return shiftedSyncDatabaseVersion + appDatabaseVersion;
	}
	
	private int extractSyncDatabaseVersion(int joinedDatabaseVersion) {
		return joinedDatabaseVersion >> MAX_APP_DATABASE_VERSION_IN_BITS;
	}
	
	private int extractAppDatabaseVersion(int joinedDatabaseVersion) {
		return joinedDatabaseVersion & MAX_APP_DATABASE_VERSION_IN_INTEGER;
	}

}