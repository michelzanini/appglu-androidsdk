/*******************************************************************************
 * Copyright 2013 AppGlu, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.appglu.impl;

import java.io.IOException;
import java.io.InputStream;
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
	
	/**
	 * {@inheritDoc}
	 */
	public void changesForTableInBackground(final String tableName, final long version, AsyncCallback<TableChanges> callback) {
		asyncExecutor.execute(callback, new Callable<TableChanges>() {
			public TableChanges call() {
				return syncOperations.changesForTable(tableName, version);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void changesForTablesInBackground(AsyncCallback<List<TableChanges>> callback, TableVersion... tables) {
		this.changesForTablesInBackground(Arrays.asList(tables), callback);
	}

	/**
	 * {@inheritDoc}
	 */
	public void changesForTablesInBackground(final List<TableVersion> tables, AsyncCallback<List<TableChanges>> callback) {
		asyncExecutor.execute(callback, new Callable<List<TableChanges>>() {
			public List<TableChanges> call() {
				return syncOperations.changesForTables(tables);
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void changesForTablesInBackground(TableChangesCallback tableChangesCallback, AsyncCallback<Void> asyncCallback, TableVersion... tables) {
		this.changesForTablesInBackground(Arrays.asList(tables), tableChangesCallback, asyncCallback);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void changesForTablesInBackground(final List<TableVersion> tables, final TableChangesCallback tableChangesCallback, AsyncCallback<Void> asyncCallback) {
		asyncExecutor.execute(asyncCallback, new Callable<Void>() {
			public Void call() {
				syncOperations.changesForTables(tables, tableChangesCallback);
				return null;
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void downloadChangesForTablesInBackground(InputStreamCallback inputStreamCallback, AsyncCallback<Void> asyncCallback, TableVersion... tables) {
		this.downloadChangesForTablesInBackground(Arrays.asList(tables), inputStreamCallback, asyncCallback);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void downloadChangesForTablesInBackground(final List<TableVersion> tables, final InputStreamCallback inputStreamCallback, AsyncCallback<Void> asyncCallback) {
		asyncExecutor.execute(asyncCallback, new Callable<Void>() {
			public Void call() {
				syncOperations.downloadChangesForTables(tables, inputStreamCallback);
				return null;
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void parseTableChangesInBackground(final InputStream inputStream, final TableChangesCallback tableChangesCallback, AsyncCallback<Void> asyncCallback) {
		asyncExecutor.execute(asyncCallback, new Callable<Void>() {
			public Void call() throws IOException {
				syncOperations.parseTableChanges(inputStream, tableChangesCallback);
				return null;
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void versionsForTablesInBackground(AsyncCallback<List<TableVersion>> callback, String... tables) {
		this.versionsForTablesInBackground(Arrays.asList(tables), callback);
	}

	/**
	 * {@inheritDoc}
	 */
	public void versionsForTablesInBackground(final List<String> tables, AsyncCallback<List<TableVersion>> callback) {
		asyncExecutor.execute(callback, new Callable<List<TableVersion>>() {
			public List<TableVersion> call() {
				return syncOperations.versionsForTables(tables);
			}
		});
	}

}
