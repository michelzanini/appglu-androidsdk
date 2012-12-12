package com.appglu.android.sync;

import java.util.ArrayList;
import java.util.List;

import com.appglu.SyncOperations;
import com.appglu.TableVersion;

public class SyncServiceTest extends AbstractSyncSQLiteTest {

	private SyncService syncService;
	
	private SQLiteSyncRepository syncRepository;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		SyncOperations syncOperations = new MockSyncOperations();
		this.syncRepository = new SQLiteSyncRepository(this.syncDatabaseHelper);
		
		this.syncService = new SyncService(syncOperations, this.syncRepository);
	}
	
	public void testSyncDatabase() {
		this.syncService.syncDatabase();
		
		List<TableVersion> updatedTables = this.syncRepository.versionsForAllTables();
		this.assertTableVersions(updatedTables, 2, 3, 4);
	}
	
	public void testSyncTables() {
		List<String> tables = new ArrayList<String>();
		
		tables.add("logged_table");
		tables.add("other_table");
		
		this.syncService.syncTables(tables);
		
		List<TableVersion> updatedTables = this.syncRepository.versionsForAllTables();
		this.assertTableVersions(updatedTables, 0, 3, 4);
	}
	
}