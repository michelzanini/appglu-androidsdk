package com.appglu.android.sync.sqlite;

import java.util.List;

import junit.framework.Assert;

import com.appglu.TableVersion;
import com.appglu.android.sync.sqlite.SyncDatabaseHelper;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

public abstract class AbstractSyncSQLiteTest extends AndroidTestCase {

	protected SyncDatabaseHelper syncDatabaseHelper;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		this.syncDatabaseHelper = new SyncDatabaseHelper(getContext(), "sync_test_cases.sqlite", 1) {

			public void onCreateAppDatabase(SQLiteDatabase db) {
				
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
			database.execSQL("DROP TABLE IF EXISTS logged_table");
			database.execSQL("DROP TABLE IF EXISTS other_table");
			database.execSQL("DROP TABLE IF EXISTS no_primary_key");
			database.execSQL("DROP TABLE IF EXISTS compose_primary_key");
			database.execSQL("DROP TABLE IF EXISTS reserverd_words");
			
			database.execSQL("CREATE TABLE logged_table (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name VARCHAR);");
			database.execSQL("CREATE TABLE other_table (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name VARCHAR);");
			database.execSQL("CREATE TABLE no_primary_key (id INTEGER, name VARCHAR);");
			database.execSQL("CREATE TABLE compose_primary_key (id INTEGER, name VARCHAR, PRIMARY KEY(id, name));");
			database.execSQL("CREATE TABLE reserverd_words (id INTEGER PRIMARY KEY, 'order' VARCHAR);");
			
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
		Assert.assertTrue(tables.size() >= 3);
		
		boolean foundStorageTable = false;
		boolean foundLoggedTable = false;
		boolean foundOtherTable = false;
		
		for (TableVersion tableVersion : tables) {
			if ("appglu_storage_files".equals(tableVersion.getTableName())) {
				Assert.assertEquals(storageVersion, tableVersion.getVersion());
				foundStorageTable = true;
			}
			if ("logged_table".equals(tableVersion.getTableName())) {
				Assert.assertEquals(loggedVersion, tableVersion.getVersion());
				foundLoggedTable = true;
			}
			if ("other_table".equals(tableVersion.getTableName())) {
				Assert.assertEquals(otherVersion, tableVersion.getVersion());
				foundOtherTable = true;
			}
		}
		
		Assert.assertTrue(foundStorageTable);
		Assert.assertTrue(foundLoggedTable);
		Assert.assertTrue(foundOtherTable);
	}
	
}