package com.appglu.android.sync;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.appglu.RowChanges;
import com.appglu.SyncOperations;
import com.appglu.TableChanges;
import com.appglu.TableVersion;

public class SyncService {
	
	private SyncOperations syncOperations;
	
	private SyncRepository syncRepository;
	
	public SyncService(SyncOperations syncOperations, SyncRepository syncRepository) {
		this.syncOperations = syncOperations;
		this.syncRepository = syncRepository;
	}
	
	public boolean checkIfDatabaseIsSynchronized() {
		List<TableVersion> localTableVersions = this.syncRepository.versionsForAllTables();
		return this.checkForChangesInTables(localTableVersions);
	}
	
	public boolean checkIfTablesAreSynchronized(String... tables) {
		return this.checkIfTablesAreSynchronized(Arrays.asList(tables));
	}
	
	public boolean checkIfTablesAreSynchronized(List<String> tables) {
		List<TableVersion> localTableVersions = this.syncRepository.versionsForTables(tables);
		return this.checkForChangesInTables(localTableVersions);
	}

	private boolean checkForChangesInTables(List<TableVersion> localTableVersions) {
		if (localTableVersions.isEmpty()) {
			return true;
		}
		
		Map<String, TableVersion> localTables = new HashMap<String, TableVersion>();
		
		for (TableVersion table : localTableVersions) {
			localTables.put(table.getTableName(), table);
		}
		
		List<String> localTableNames = new ArrayList<String>(localTables.keySet());
		List<TableVersion> remoteTableVersions = this.syncOperations.versionsForTables(localTableNames);
		
		for (TableVersion remoteTableVersion : remoteTableVersions) {
			TableVersion localTableVersion = localTables.get(remoteTableVersion.getTableName());
			
			if (localTableVersion == null) {
				return false;
			}
			
			if (remoteTableVersion.getVersion() > localTableVersion.getVersion()) {
				return false;
			}
		}
		
		return true;
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