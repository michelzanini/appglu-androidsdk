package com.appglu.impl;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import com.appglu.AsyncCallback;
import com.appglu.AsyncSyncOperations;
import com.appglu.InputStreamCallback;
import com.appglu.SyncOperations;
import com.appglu.TableChangesCallback;
import com.appglu.TableVersion;
import com.appglu.TableChanges;

public final class AsyncSyncTemplate implements AsyncSyncOperations {
	
	private final AsyncExecutor asyncExecutor;
	
	private final SyncOperations syncOperations;
	
	public AsyncSyncTemplate(AsyncExecutor asyncExecutor, SyncOperations syncOperations) {
		this.asyncExecutor = asyncExecutor;
		this.syncOperations = syncOperations;
	}
	
	public void changesForTableInBackground(final String tableName, final long version, AsyncCallback<TableChanges> callback) {
		asyncExecutor.execute(callback, new Callable<TableChanges>() {
			public TableChanges call() {
				return syncOperations.changesForTable(tableName, version);
			}
		});
	}

	public void changesForTablesInBackground(AsyncCallback<List<TableChanges>> callback, TableVersion... tables) {
		this.changesForTablesInBackground(Arrays.asList(tables), callback);
	}

	public void changesForTablesInBackground(final List<TableVersion> tables, AsyncCallback<List<TableChanges>> callback) {
		asyncExecutor.execute(callback, new Callable<List<TableChanges>>() {
			public List<TableChanges> call() {
				return syncOperations.changesForTables(tables);
			}
		});
	}
	
	public void changesForTablesInBackground(TableChangesCallback tableChangesCallback, AsyncCallback<Void> asyncCallback, TableVersion... tables) {
		this.changesForTablesInBackground(Arrays.asList(tables), tableChangesCallback, asyncCallback);
	}
	
	public void changesForTablesInBackground(final List<TableVersion> tables, final TableChangesCallback tableChangesCallback, AsyncCallback<Void> asyncCallback) {
		asyncExecutor.execute(asyncCallback, new Callable<Void>() {
			public Void call() {
				syncOperations.changesForTables(tables, tableChangesCallback);
				return null;
			}
		});
	}

	public void downloadChangesForTables(InputStreamCallback inputStreamCallback, AsyncCallback<Void> asyncCallback, TableVersion... tables) {
		this.downloadChangesForTables(Arrays.asList(tables), inputStreamCallback, asyncCallback);
	}
	
	public void downloadChangesForTables(final List<TableVersion> tables, final InputStreamCallback inputStreamCallback, AsyncCallback<Void> asyncCallback) {
		asyncExecutor.execute(asyncCallback, new Callable<Void>() {
			public Void call() {
				syncOperations.downloadChangesForTables(tables, inputStreamCallback);
				return null;
			}
		});
	}

	public void versionsForTablesInBackground(AsyncCallback<List<TableVersion>> callback, String... tables) {
		this.versionsForTablesInBackground(Arrays.asList(tables), callback);
	}

	public void versionsForTablesInBackground(final List<String> tables, AsyncCallback<List<TableVersion>> callback) {
		asyncExecutor.execute(callback, new Callable<List<TableVersion>>() {
			public List<TableVersion> call() {
				return syncOperations.versionsForTables(tables);
			}
		});
	}

}
