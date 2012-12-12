package com.appglu.android.sync;

import java.util.Arrays;
import java.util.List;

import com.appglu.SyncOperations;
import com.appglu.RowChanges;
import com.appglu.TableVersion;
import com.appglu.TableChanges;

public class SyncService {
	
	private SyncOperations syncOperations;
	
	private SyncRepository syncRepository;
	
	public SyncService(SyncOperations syncOperations, SyncRepository syncRepository) {
		this.syncOperations = syncOperations;
		this.syncRepository = syncRepository;
	}

	public void syncDatabase() {
		List<TableVersion> tableVersions = this.syncRepository.versionsForAllTables();
		this.fetchAndApplyChangesForTables(tableVersions);
	}
	
	public void syncTables(String... tables) {
		this.syncTables(Arrays.asList(tables));
	}
	
	public void syncTables(List<String> tables) {
		List<TableVersion> tableVersions = this.syncRepository.versionsForTables(tables);
		this.fetchAndApplyChangesForTables(tableVersions);
	}
	
	private void fetchAndApplyChangesForTables(List<TableVersion> tableVersions) {
		if (tableVersions.isEmpty()) {
			return;
		}
		
		List<TableChanges> tableChanges = this.syncOperations.changesForTables(tableVersions);
		
		if (tableChanges.isEmpty()) {
			return;
		}
		
		this.applyChangesWithTransaction(tableChanges);
	}

	private void applyChangesWithTransaction(List<TableChanges> changes) {
		this.syncRepository.beginTransaction();
		try {
			this.applyChangesToDatabase(changes);
			this.syncRepository.saveTableVersions(changes);
			this.syncRepository.setTransactionSuccessful();
		} finally {
			this.syncRepository.endTransaction();
		}
	}

	private void applyChangesToDatabase(List<TableChanges> changes) {
		for (TableChanges tableChanges : changes) {
			this.applyChangesToTable(tableChanges);
		}
	}

	private void applyChangesToTable(TableChanges tableChanges) {
		for (RowChanges row : tableChanges.getChanges()) {
			this.syncRepository.applyRowChangesToTable(tableChanges.getTableName(), row);
		}
	}

}