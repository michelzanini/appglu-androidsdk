package com.appglu.android.sync;

import java.util.List;

import com.appglu.SyncOperations;
import com.appglu.VersionedTable;

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
	
	public void testSynchronizeLocalDatabase() {
		this.syncService.synchronizeLocalDatabase();
		
		List<VersionedTable> updatedTables = this.syncRepository.listTables();
		this.assertTableVersions(updatedTables, 2, 3, 4);
	}
	
}