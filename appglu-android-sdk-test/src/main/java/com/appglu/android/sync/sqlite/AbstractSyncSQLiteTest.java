package com.appglu.android.sync.sqlite;

import java.util.List;

import junit.framework.Assert;
import android.database.sqlite.SQLiteDatabase;

import com.appglu.TableVersion;

public abstract class AbstractSyncSQLiteTest extends AbstractDatabaseHelperTest {

	protected String getDatabaseName() {
		return "sync_test_cases.sqlite";
	}
	
	protected int getDatabaseVersion() {
		return 1;
	}
	
	protected boolean enableForeignKeys() {
		return false;
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
			
			database.execSQL("insert into logged_table (id, name) values (1, 'value')");
			database.execSQL("insert into logged_table (id, name) values (2, 'value')");
			database.execSQL("insert into logged_table (id, name) values (3, 'value')");
			
			database.execSQL("insert into other_table (id, name) values (1, 'value')");
			
			database.execSQL("delete from appglu_table_versions");
			database.execSQL("insert into appglu_table_versions (table_name, version) values ('logged_table', 1)");
			database.execSQL("insert into appglu_table_versions (table_name, version) values ('other_table', 2)");
			
			database.execSQL("delete from appglu_sync_metadata");
			database.execSQL("insert into appglu_sync_metadata(sync_key, table_name, primary_key) values (1, 'other_table', '1');");
			database.execSQL("insert into appglu_sync_metadata(sync_key, table_name, primary_key) values (2, 'logged_table', '1');");
			database.execSQL("insert into appglu_sync_metadata(sync_key, table_name, primary_key) values (3, 'logged_table', '2');");
			database.execSQL("insert into appglu_sync_metadata(sync_key, table_name, primary_key) values (4, 'logged_table', '3');");
			
			database.execSQL("delete from appglu_storage_files");
			
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