package com.appglu.android.sync;

import java.util.List;

import junit.framework.Assert;

import com.appglu.TableVersion;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

public abstract class AbstractSyncSQLiteTest extends AndroidTestCase {

	protected SyncDatabaseHelper syncDatabaseHelper;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		this.syncDatabaseHelper = new SyncDatabaseHelper(getContext(), "sync_test_cases.sqlite", 1) {

			public void onCreateAppDatabase(SQLiteDatabase db) {
				db.execSQL("CREATE TABLE logged_table (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name VARCHAR);");
				db.execSQL("CREATE TABLE other_table (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name VARCHAR);");
			}

			public void onUpgradeAppDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
				
			}
			
		};
		
		this.setUpScript();
	}
	
	protected void setUpScript() {
		SQLiteDatabase database = this.syncDatabaseHelper.getWritableDatabase();
		database.beginTransaction();
		
		try {
			database.execSQL("delete from appglu_table_versions");
			database.execSQL("insert into appglu_table_versions (table_name, version) values ('logged_table', 1)");
			database.execSQL("insert into appglu_table_versions (table_name, version) values ('other_table', 2)");
			
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
			database.close();
		}
	}
	
	protected void assertTableVersions(List<TableVersion> tables, int storageVersion, int loggedVersion, int otherVersion) {
		Assert.assertNotNull(tables);
		Assert.assertEquals(3, tables.size());
		
		Assert.assertEquals("appglu_storage_files", tables.get(0).getTableName());
		Assert.assertEquals(storageVersion, tables.get(0).getVersion());
		
		Assert.assertEquals("logged_table", tables.get(1).getTableName());
		Assert.assertEquals(loggedVersion, tables.get(1).getVersion());
		
		Assert.assertEquals("other_table", tables.get(2).getTableName());
		Assert.assertEquals(otherVersion, tables.get(2).getVersion());
	}
	
}