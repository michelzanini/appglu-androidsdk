package com.appglu.android.sync;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class SyncDatabaseHelper extends SQLiteOpenHelper {
	
	protected static final String APPGLU_STORAGE_FILES_TABLE = "appglu_storage_files";
	
	private static final int MAX_APP_DATABASE_VERSION_IN_BITS = 20;
	private static final int MAX_APP_DATABASE_VERSION_IN_DECIMAL = (1 << MAX_APP_DATABASE_VERSION_IN_BITS) - 1;
	
	private static final int MAX_SYNC_DATABASE_VERSION_IN_BITS = Integer.SIZE - MAX_APP_DATABASE_VERSION_IN_BITS;
	private static final int MAX_SYNC_DATABASE_VERSION_IN_DECIMAL = (1 << MAX_SYNC_DATABASE_VERSION_IN_BITS) - 1;
	
	private static final int SYNC_DATABASE_VERSION = 1;
	
	private boolean foreignKeysEnabled;

	public SyncDatabaseHelper(Context context, String name, int version) {
		this(context, name, version, false);
	}
	
	public SyncDatabaseHelper(Context context, String name, int version, boolean enableForeignKeys) {
		super(context, name, null, joinVersionNumbers(SYNC_DATABASE_VERSION, version));
		this.foreignKeysEnabled = enableForeignKeys;
	}
	
	public boolean getForeignKeysEnabled() {
		return foreignKeysEnabled;
	}

	@Override
	public final void onCreate(SQLiteDatabase db) {
		this.onCreateSyncDatabase(db);
		this.onCreateAppDatabase(db);
	}

	@Override
	public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		int syncOldVersion = this.extractSyncDatabaseVersion(oldVersion);
		int syncNewVersion = this.extractSyncDatabaseVersion(newVersion);
		
		this.onUpgradeSyncDatabase(db, syncOldVersion, syncNewVersion);
		
		int appOldVersion = this.extractAppDatabaseVersion(oldVersion);
		int appNewVersion = this.extractAppDatabaseVersion(newVersion);
		
		this.onUpgradeAppDatabase(db, appOldVersion, appNewVersion);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
	    super.onOpen(db);
	    if (!db.isReadOnly() && this.foreignKeysEnabled) {
	        db.execSQL("PRAGMA foreign_keys=ON;");
	    }
	}
	
	public void onCreateSyncDatabase(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE appglu_storage_files (id INTEGER PRIMARY KEY NOT NULL, key VARCHAR, name VARCHAR, content_type VARCHAR, title VARCHAR, size INTEGER, last_modified DATETIME, url VARCHAR, e_tag VARCHAR, directory_id INTEGER);");
		db.execSQL("CREATE TABLE appglu_table_versions (table_name VARCHAR PRIMARY KEY NOT NULL, version INTEGER NOT NULL DEFAULT 0);");
		db.execSQL("CREATE TABLE appglu_sync_metadata (sync_key INTEGER PRIMARY KEY NOT NULL, table_name VARCHAR NOT NULL, primary_key VARCHAR NOT NULL);");
		db.execSQL("CREATE INDEX appglu_storage_files_url ON appglu_storage_files (url);");
	}
	
	public void onUpgradeSyncDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	public abstract void onCreateAppDatabase(SQLiteDatabase db);
	
	public abstract void onUpgradeAppDatabase(SQLiteDatabase db, int oldVersion, int newVersion);
	
	/* Utility methods */
	
	private static int joinVersionNumbers(int syncDatabaseVersion, int appDatabaseVersion) {
		if (appDatabaseVersion > MAX_APP_DATABASE_VERSION_IN_DECIMAL) {
			throw new IllegalArgumentException("Version must be < " + MAX_APP_DATABASE_VERSION_IN_DECIMAL + ", was " + appDatabaseVersion);
		}
		
		if (syncDatabaseVersion > MAX_SYNC_DATABASE_VERSION_IN_DECIMAL) {
			throw new IllegalArgumentException("AppGlu sync database version must be < " + MAX_SYNC_DATABASE_VERSION_IN_DECIMAL + ", was " + syncDatabaseVersion);
		}
		
		int shiftedSyncDatabaseVersion = syncDatabaseVersion << MAX_APP_DATABASE_VERSION_IN_BITS;
		return shiftedSyncDatabaseVersion + appDatabaseVersion;
	}
	
	private int extractSyncDatabaseVersion(int joinedDatabaseVersion) {
		return joinedDatabaseVersion >> MAX_APP_DATABASE_VERSION_IN_BITS;
	}
	
	private int extractAppDatabaseVersion(int joinedDatabaseVersion) {
		return joinedDatabaseVersion & MAX_APP_DATABASE_VERSION_IN_DECIMAL;
	}

}