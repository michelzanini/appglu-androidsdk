package com.appglu.android;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import com.appglu.AsyncCallback;
import com.appglu.SyncOperations;
import com.appglu.android.sync.SyncRepository;
import com.appglu.android.sync.SyncService;
import com.appglu.impl.AsyncExecutor;

public final class SyncApi {
	
	private SyncService syncService;
	
	private AsyncExecutor asyncExecutor;
	
	public SyncApi(SyncOperations syncOperations, SyncRepository syncRepository, AsyncExecutor asyncExecutor) {
		this.syncService = new SyncService(syncOperations, syncRepository);
		this.asyncExecutor = asyncExecutor;
	}
	
	public boolean checkIfDatabaseIsSynchronized() {
		synchronized (SyncApi.class) {
			return this.syncService.checkIfDatabaseIsSynchronized();
		}
	}

	public boolean checkIfTablesAreSynchronized(String... tables) {
		synchronized (SyncApi.class) {
			return this.checkIfTablesAreSynchronized(Arrays.asList(tables));
		}
	}

	public boolean checkIfTablesAreSynchronized(List<String> tables) {
		synchronized (SyncApi.class) {
			return this.syncService.checkIfTablesAreSynchronized(tables);
		}
	}
	
	public void syncDatabase() {
		synchronized (SyncApi.class) {
			this.syncService.syncDatabase();
		}
	}

	public void syncTables(String... tables) {
		synchronized (SyncApi.class) {
			this.syncTables(Arrays.asList(tables));
		}
	}

	public void syncTables(List<String> tables) {
		synchronized (SyncApi.class) {
			this.syncService.syncTables(tables);
		}
	}
	
	public void checkIfDatabaseIsSynchronizedInBackground(AsyncCallback<Boolean> callback) {
		this.asyncExecutor.execute(callback, new Callable<Boolean>() {
			public Boolean call() {
				return checkIfDatabaseIsSynchronized();
			}
		});
	}

	public void checkIfTablesAreSynchronizedInBackground(AsyncCallback<Boolean> callback, final String... tables) {
		this.asyncExecutor.execute(callback, new Callable<Boolean>() {
			public Boolean call() {
				return checkIfTablesAreSynchronized(tables);
			}
		});
	}

	public void checkIfTablesAreSynchronizedInBackground(final List<String> tables, AsyncCallback<Boolean> callback) {
		this.asyncExecutor.execute(callback, new Callable<Boolean>() {
			public Boolean call() {
				return checkIfTablesAreSynchronized(tables);
			}
		});
	}
	
	public void syncDatabaseInBackground(AsyncCallback<Void> callback) {
		this.asyncExecutor.execute(callback, new Callable<Void>() {
			public Void call() {
				syncDatabase();
				return null;
			}
		});
	}

	public void syncTablesInBackground(AsyncCallback<Void> callback, final String... tables) {
		this.asyncExecutor.execute(callback, new Callable<Void>() {
			public Void call() {
				syncTables(tables);
				return null;
			}
		});
	}

	public void syncTablesInBackground(final List<String> tables, AsyncCallback<Void> callback) {
		this.asyncExecutor.execute(callback, new Callable<Void>() {
			public Void call() {
				syncTables(tables);
				return null;
			}
		});
	}
	
}