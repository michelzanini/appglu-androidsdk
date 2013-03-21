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
			database.execSQL("insert into appglu_storage_files (id, key, name, content_type, title, size, last_modified, url, etag, version, directory_id) values (1001, '1ee26276-b773-4eaa-9762-49c380e604c7', 'app-icon.png', 'image/png', 'app-icon', 10125, 1357309209000, 'https://s3.amazonaws.com/cbs-startrek1/1ee26276-b773-4eaa-9762-49c380e604c7-app-icon.png', 'abdca54e4ca831ec8013ef9f597adf1c', '2', 1)");
			database.execSQL("insert into appglu_storage_files (id, key, name, content_type, title, size, last_modified, url, etag, version, directory_id) values (1002, '12312312-b773-4eaa-9762-123123123123', 'splash.png', 'image/png', 'splash', 3410125, 1357309209000, 'https://s3.amazonaws.com/cbs-startrek1/1ee26276-b773-4eaa-9762-49c380e604c7-app-icon.png', 'e1518f15ce668ab198508939822225d1', '2', 1)");
			
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
