package com.appglu.impl;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import com.appglu.AsyncCallback;
import com.appglu.AsyncSyncOperations;
import com.appglu.SyncOperations;
import com.appglu.VersionedTable;
import com.appglu.VersionedTableChanges;

public final class AsyncSyncTemplate implements AsyncSyncOperations {
	
	private final AsyncExecutor asyncExecutor;
	
	private final SyncOperations syncOperations;
	
	public AsyncSyncTemplate(AsyncExecutor asyncExecutor, SyncOperations syncOperations) {
		this.asyncExecutor = asyncExecutor;
		this.syncOperations = syncOperations;
	}

	public void changesForTablesInBackground(AsyncCallback<List<VersionedTableChanges>> callback, VersionedTable... tables) {
		this.changesForTablesInBackground(Arrays.asList(tables), callback);
	}

	public void changesForTablesInBackground(final List<VersionedTable> tables, AsyncCallback<List<VersionedTableChanges>> callback) {
		asyncExecutor.execute(callback, new Callable<List<VersionedTableChanges>>() {
			public List<VersionedTableChanges> call() {
				return syncOperations.changesForTables(tables);
			}
		});
	}

	public void changesForTableInBackground(final String tableName, final long version, AsyncCallback<VersionedTableChanges> callback) {
		asyncExecutor.execute(callback, new Callable<VersionedTableChanges>() {
			public VersionedTableChanges call() {
				return syncOperations.changesForTable(tableName, version);
			}
		});
	}

	public void versionsForTablesInBackground(AsyncCallback<List<VersionedTable>> callback, String... tables) {
		this.versionsForTablesInBackground(Arrays.asList(tables), callback);
	}

	public void versionsForTablesInBackground(final List<String> tables, AsyncCallback<List<VersionedTable>> callback) {
		asyncExecutor.execute(callback, new Callable<List<VersionedTable>>() {
			public List<VersionedTable> call() {
				return syncOperations.versionsForTables(tables);
			}
		});
	}

}
