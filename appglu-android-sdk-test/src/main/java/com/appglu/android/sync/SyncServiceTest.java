package com.appglu.android.sync;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import com.appglu.TableVersion;
import com.appglu.android.sync.sqlite.AbstractSyncSQLiteTest;
import com.appglu.android.sync.sqlite.SQLiteSyncRepository;

public class SyncServiceTest extends AbstractSyncSQLiteTest {

	private SyncService syncService;
	
	private SQLiteSyncRepository syncRepository;
	
	private void defineSyncServiceWithMockOperations(String changesForTablesJson, String versionsForTablesJson) {
		MockSyncOperations syncOperations = new MockSyncOperations(changesForTablesJson, versionsForTablesJson);
		
		this.syncRepository = new SQLiteSyncRepository(this.syncDatabaseHelper);
		this.syncService = new SyncService(syncOperations, this.syncRepository);
	}
	
	public void testSyncDatabase() {
		this.defineSyncServiceWithMockOperations("sync_database", null);
		
		this.syncService.syncDatabase();
		
		List<TableVersion> updatedTables = this.syncRepository.versionsForAllTables();
		this.assertTableVersions(updatedTables, 4, 9, 3);
	}
	
	public void testSyncTables() {
		this.defineSyncServiceWithMockOperations("sync_tables", null);
		
		List<String> tables = new ArrayList<String>();
		
		tables.add("logged_table");
		tables.add("other_table");
		
		this.syncService.syncTables(tables);
		
		List<TableVersion> updatedTables = this.syncRepository.versionsForAllTables();
		this.assertTableVersions(updatedTables, 0, 9, 3);
	}
	
	public void testSyncTables_withReservedWords() {
		this.defineSyncServiceWithMockOperations("sync_tables_with_reserved_words", null);
		
		List<String> tables = new ArrayList<String>();
		
		tables.add("reserverd_words");
		
		this.syncService.syncTables(tables);
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