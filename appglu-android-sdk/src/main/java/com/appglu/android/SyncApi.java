package com.appglu.android;

import com.appglu.SyncOperations;
import com.appglu.android.sync.SyncRepository;
import com.appglu.android.sync.SyncService;
import com.appglu.android.sync.sqlite.SQLiteSyncRepository;
import com.appglu.android.sync.sqlite.SyncDatabaseHelper;

public final class SyncApi {
	
	private SyncOperations syncOperations;
	
	public SyncApi(SyncOperations syncOperations) {
		this.syncOperations = syncOperations;
	}

	public void syncDatabase(SyncDatabaseHelper syncDatabaseHelper) {
		SyncService syncService = this.createSyncService(syncDatabaseHelper);
		syncService.syncDatabase();
	}

	private SyncService createSyncService(SyncDatabaseHelper syncDatabaseHelper) {
		SyncRepository syncRepository = new SQLiteSyncRepository(syncDatabaseHelper);
		return new SyncService(syncOperations, syncRepository);
	}

}