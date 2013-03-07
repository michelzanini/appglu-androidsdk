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

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import com.appglu.TableVersion;
import com.appglu.android.sync.SQLiteSyncRepository;
import com.appglu.android.sync.SyncService;

public class SyncServiceTest extends AbstractSyncSQLiteTest {

	private SyncService syncService;
	
	private SQLiteSyncRepository syncRepository;
	
	private void defineSyncServiceWithMockOperations(String changesForTablesJson, String versionsForTablesJson) {
		MockSyncOperations syncOperations = new MockSyncOperations(changesForTablesJson, versionsForTablesJson);
		SyncFileStorageService syncStorageService = new SyncFileStorageService(this.getContext(), new MockStorageOperations());
		
		this.syncRepository = new SQLiteSyncRepository(this.syncDatabaseHelper);
		this.syncService = new SyncService(syncOperations, this.syncRepository, syncStorageService);
	}
	
	public void testSyncDatabase() {
		this.defineSyncServiceWithMockOperations("sync_database", "check_if_database_is_synchronized_with_changes");
		
		int rowsBeforeForStorageFiles = this.countTable("appglu_storage_files");
		int rowsBeforeForLoggedTable = this.countTable("logged_table");
		int rowsBeforeForOtherTable = this.countTable("other_table");
		
		boolean changesWereApplied = this.syncService.syncDatabase();
		Assert.assertTrue(changesWereApplied);
		
		List<TableVersion> updatedTables = this.syncRepository.versionsForAllTables();
		this.assertTableVersions(updatedTables, 1, 3, 3);
		
		int rowsAfterForStorageFiles = this.countTable("appglu_storage_files");
		int rowsAfterForLoggedTable = this.countTable("logged_table");
		int rowsAfterForOtherTable = this.countTable("other_table");
		
		Assert.assertEquals(rowsBeforeForStorageFiles + 1, rowsAfterForStorageFiles);
		Assert.assertEquals(rowsBeforeForLoggedTable + 1, rowsAfterForLoggedTable);
		Assert.assertEquals(rowsBeforeForOtherTable - 1, rowsAfterForOtherTable);
	}
	
	public void testSyncTables() {
		this.defineSyncServiceWithMockOperations("sync_tables", "check_if_tables_are_synchronized_with_changes");
		
		int rowsBeforeForLoggedTable = this.countTable("logged_table");
		int rowsBeforeForOtherTable = this.countTable("other_table");
		
		List<String> tables = new ArrayList<String>();
		
		tables.add("logged_table");
		tables.add("other_table");
		
		boolean changesWereApplied = this.syncService.syncTables(tables);
		Assert.assertTrue(changesWereApplied);
		
		List<TableVersion> updatedTables = this.syncRepository.versionsForAllTables();
		this.assertTableVersions(updatedTables, 0, 3, 3);
		
		int rowsAfterForLoggedTable = this.countTable("logged_table");
		int rowsAfterForOtherTable = this.countTable("other_table");
		
		Assert.assertEquals(rowsBeforeForLoggedTable + 1, rowsAfterForLoggedTable);
		Assert.assertEquals(rowsBeforeForOtherTable - 1, rowsAfterForOtherTable);
	}
	
	public void testSyncTables_withReservedWords() {
		this.defineSyncServiceWithMockOperations("sync_tables_with_reserved_words", "table_version_reserved_words");
		
		List<String> tables = new ArrayList<String>();
		
		tables.add("reserverd_words");
		
		boolean changesWereApplied = this.syncService.syncTables(tables);
		Assert.assertTrue(changesWereApplied);
	}
	
	public void testCheckIfDatabaseIsSynchronized_withChanges() {
		this.defineSyncServiceWithMockOperations(null, "check_if_database_is_synchronized_with_changes");
		
		boolean result = this.syncService.checkIfDatabaseIsSynchronized();
		Assert.assertFalse(result);
	}
	
	public void testCheckIfTablesAreSynchronized_withChanges() {
		this.defineSyncServiceWithMockOperations(null, "check_if_tables_are_synchronized_with_changes");
		
		List<String> tables = new ArrayList<String>();
		
		tables.add("logged_table");
		tables.add("other_table");
		
		boolean result = this.syncService.checkIfTablesAreSynchronized(tables);
		Assert.assertFalse(result);
	}
	
	public void testCheckIfDatabaseIsSynchronized_noChanges() {
		this.defineSyncServiceWithMockOperations(null, "check_if_database_is_synchronized_no_changes");
		
		boolean result = this.syncService.checkIfDatabaseIsSynchronized();
		Assert.assertTrue(result);
	}
	
	public void testCheckIfTablesAreSynchronized_noChanges() {
		this.defineSyncServiceWithMockOperations(null, "check_if_tables_are_synchronized_no_changes");
		
		List<String> tables = new ArrayList<String>();
		
		tables.add("logged_table");
		tables.add("other_table");
		
		boolean result = this.syncService.checkIfTablesAreSynchronized(tables);
		Assert.assertTrue(result);
	}
	
}
