package com.appglu.android.sync;

import java.util.List;

import com.appglu.SyncOperations;
import com.appglu.VersionedRow;
import com.appglu.VersionedTable;
import com.appglu.VersionedTableChanges;

public class SyncService {
	
	private SyncOperations syncOperations;
	
	private SyncRepository syncRepository;
	
	public SyncService(SyncOperations syncOperations, SyncRepository syncRepository) {
		this.syncOperations = syncOperations;
		this.syncRepository = syncRepository;
	}

	public void synchronizeLocalDatabase() {
		List<VersionedTable> versionedTables = this.syncRepository.listTables();
		List<VersionedTableChanges> changes = this.syncOperations.changesForTables(versionedTables);
		
		this.applyChanges(changes);
	}

	private void applyChanges(List<VersionedTableChanges> changes) {
		this.syncRepository.beginTransaction();
		try {
			this.processChangesToLocalDatabase(changes);
			this.syncRepository.updateLocalTableVersions(changes);
			this.syncRepository.setTransactionSuccessful();
		} finally {
			this.syncRepository.endTransaction();
		}
	}

	private void processChangesToLocalDatabase(List<VersionedTableChanges> changes) {
		for (VersionedTableChanges tableChanges : changes) {
			this.processChangesToTable(tableChanges);
		}
	}

	private void processChangesToTable(VersionedTableChanges tableChanges) {
		for (VersionedRow row : tableChanges.getChanges()) {
			this.syncRepository.processChangesToTable(tableChanges.getTableName(), row);
		}
	}

}