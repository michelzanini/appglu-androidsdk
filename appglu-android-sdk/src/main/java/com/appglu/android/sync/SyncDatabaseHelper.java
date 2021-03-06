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
package com.appglu.android.sync;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <p>{@code SyncDatabaseHelper} extends {@code android.database.sqlite.SQLiteOpenHelper} to provide custom functionality: it creates meta-data tables required to perform a Sync operation with the AppGlu server.<br>
 * <p>Before having access to a {@link SyncApi} instance you will need to extend this class. By extending this class two methods will require overriding: {@link #onCreateAppDatabase(SQLiteDatabase)} and {@link #onUpgradeAppDatabase(SQLiteDatabase, int, int)}.<br> 
 * 
 * @see #onCreateAppDatabase(SQLiteDatabase)
 * @see #onUpgradeAppDatabase(SQLiteDatabase, int, int)
 * @see SyncApi
 * @since 1.0.0
 */
public abstract class SyncDatabaseHelper extends SQLiteOpenHelper {
	
	protected static final String APPGLU_STORAGE_FILES_TABLE = "appglu_storage_files";
	
	private static final int MAX_APP_DATABASE_VERSION_IN_BITS = 20;
	private static final int MAX_APP_DATABASE_VERSION_IN_DECIMAL = (1 << MAX_APP_DATABASE_VERSION_IN_BITS) - 1;
	
	private static final int MAX_SYNC_DATABASE_VERSION_IN_BITS = Integer.SIZE - MAX_APP_DATABASE_VERSION_IN_BITS;
	private static final int MAX_SYNC_DATABASE_VERSION_IN_DECIMAL = (1 << MAX_SYNC_DATABASE_VERSION_IN_BITS) - 1;
	
	private static final int SYNC_DATABASE_VERSION = 1;
	
	private boolean foreignKeysEnabled;

	/**
	 * @param context an Activity or Application
	 * @param name the database file name
	 * @param version the database version used to manage migrations
	 * @see #onUpgradeAppDatabase(SQLiteDatabase, int, int)
	 */
	public SyncDatabaseHelper(Context context, String name, int version) {
		this(context, name, version, false);
	}
	
	/**
	 * @param context an Activity or Application
	 * @param name the database file name
	 * @param version the database version used to manage migrations
	 * @param enableForeignKeys if <code>true</code> foreign key constraints will be enforced. By default, constraints are not enforced
	 * @see #onUpgradeAppDatabase(SQLiteDatabase, int, int)
	 */
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
	
	private void onCreateSyncDatabase(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE appglu_storage_files (id INTEGER PRIMARY KEY NOT NULL, key VARCHAR, name VARCHAR, content_type VARCHAR, title VARCHAR, size INTEGER, last_modified DATETIME, url VARCHAR, etag VARCHAR, version VARCHAR, directory_id INTEGER);");
		db.execSQL("CREATE TABLE appglu_table_versions (table_name VARCHAR PRIMARY KEY NOT NULL, version INTEGER NOT NULL DEFAULT 0);");
		db.execSQL("CREATE TABLE appglu_sync_metadata (sync_key INTEGER PRIMARY KEY NOT NULL, table_name VARCHAR NOT NULL, primary_key VARCHAR NOT NULL);");
		db.execSQL("CREATE INDEX appglu_storage_files_url ON appglu_storage_files (url);");
	}
	
	private void onUpgradeSyncDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	/**
     * <p>This method is similar to {@code android.database.sqlite.SQLiteOpenHelper#onCreate(SQLiteDatabase)}:<br>
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     * 
     * @param db The database
	 */
	public abstract void onCreateAppDatabase(SQLiteDatabase db);
	
	/**
	 * <p>This method is similar to {@code android.database.sqlite.SQLiteOpenHelper#onUpgrade(SQLiteDatabase, int, int)}:<br>
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * 
     * @param db The database
     * @param oldVersion The old database version
     * @param newVersion The new database version
	 */
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
