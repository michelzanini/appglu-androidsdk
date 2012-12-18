package com.appglu.android.sync;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.appglu.SyncOperations;
import com.appglu.TableChanges;
import com.appglu.TableVersion;
import com.appglu.android.AppGlu;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;

public class SyncService {
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.SYNC_LOG_TAG);
	
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
				continue;
			}
			
			if (remoteTableVersion.getVersion() > localTableVersion.getVersion()) {
				return false;
			}
		}
		
		return true;
	}

	public void syncDatabase() {
		List<TableVersion> tableVersions = this.syncRepository.versionsForAllTables();
		this.fetchAndApplyChangesToTables(tableVersions);
	}
	
	public void syncTables(String... tables) {
		this.syncTables(Arrays.asList(tables));
	}
	
	public void syncTables(List<String> tables) {
		List<TableVersion> tableVersions = this.syncRepository.versionsForTables(tables);
		this.fetchAndApplyChangesToTables(tableVersions);
	}
	
	private void fetchAndApplyChangesToTables(List<TableVersion> tableVersions) {
		this.logger.info("Synchronization started");
		
		if (tableVersions.isEmpty()) {
			this.logger.info("No changes were applied because no local tables were found");
			return;
		}
		
		this.logger.info("Fetching remote changes for tables " + tableVersions);
		
		List<TableChanges> tableChanges = this.syncOperations.changesForTables(tableVersions);
		
		if (!this.areThereChangesToBeApplied(tableChanges)) {
			this.logger.info("No changes were applied because tables are already synchronized");
			return;
		}
		
		this.logger.info("Applying remote changes to tables " + tableChanges);
		
		this.syncRepository.applyChangesWithTransaction(tableChanges);
		
		this.logger.info("Changes were applied with success");
	}

	private boolean areThereChangesToBeApplied(List<TableChanges> tableChanges) {
		boolean foundChanges = false;
		
		for (TableChanges table : tableChanges) {
			if (table.hasChanges()) {
				foundChanges = true;
			}
		}
		return foundChanges;
	}

}