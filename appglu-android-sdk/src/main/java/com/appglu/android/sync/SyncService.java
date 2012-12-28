package com.appglu.android.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.appglu.SyncOperations;
import com.appglu.TableVersion;
import com.appglu.android.AppGlu;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;

public class SyncService {
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.LOG_TAG);
	
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
	
	public void syncTables(List<String> tables) {
		List<TableVersion> tableVersions = this.syncRepository.versionsForTables(tables);
		this.fetchAndApplyChangesToTables(tableVersions);
	}
	
	private void fetchAndApplyChangesToTables(final List<TableVersion> tableVersions) {
		this.logger.info("Synchronization started");
		try {
			if (tableVersions.isEmpty()) {
				this.logger.info("No changes were applied because no local tables were found");
				return;
			}
			
			this.logger.info("Fetching and applying remote changes for tables " + tableVersions);
			
			this.syncRepository.applyChangesWithTransaction(new TransactionCallback() {
				public void doInTransaction() {
					syncOperations.changesForTables(tableVersions, syncRepository);
					
				}
			});
			
			this.logger.info("Changes were applied with success");
		} catch (RuntimeException e) {
			this.logger.error("Synchronization failed with exception", e);
			throw e;
		} finally {
			this.logger.info("Synchronization finished");
		}
	}

}