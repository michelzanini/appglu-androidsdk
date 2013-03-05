package com.appglu;

import java.io.InputStream;
import java.util.List;

/**
 * {@code AsyncSyncOperations} has all methods that {@link SyncOperations} has but they execute <strong>asynchronously</strong>. 
 * @see SyncOperations
 * @since 1.0.0
 */
public interface AsyncSyncOperations {
	
	/**
	 * Asynchronous version of {@link SyncOperations#changesForTable(String, long)}.
	 * @see SyncOperations#changesForTable(String, long)
	 */
	void changesForTableInBackground(String tableName, long version, AsyncCallback<TableChanges> callback);
	
	/**
	 * Asynchronous version of {@link SyncOperations#changesForTables(TableVersion...)}.
	 * @see SyncOperations#changesForTables(TableVersion...)
	 */
	void changesForTablesInBackground(AsyncCallback<List<TableChanges>> callback, TableVersion... tables);
	
	/**
	 * Asynchronous version of {@link SyncOperations#changesForTables(List)}.
	 * @see SyncOperations#changesForTables(List)
	 */
	void changesForTablesInBackground(List<TableVersion> tables, AsyncCallback<List<TableChanges>> callback);
	
	/**
	 * Asynchronous version of {@link SyncOperations#changesForTables(TableChangesCallback, TableVersion...)}.
	 * @see SyncOperations#changesForTables(TableChangesCallback, TableVersion...)
	 */
	void changesForTablesInBackground(TableChangesCallback tableChangesCallback, AsyncCallback<Void> asyncCallback, TableVersion... tables);
	
	/**
	 * Asynchronous version of {@link SyncOperations#changesForTables(List, TableChangesCallback)}.
	 * @see SyncOperations#changesForTables(List, TableChangesCallback)
	 */
	void changesForTablesInBackground(List<TableVersion> tables, TableChangesCallback tableChangesCallback, AsyncCallback<Void> asyncCallback);
	
	/**
	 * Asynchronous version of {@link SyncOperations#downloadChangesForTables(InputStreamCallback, TableVersion...)}.
	 * @see SyncOperations#downloadChangesForTables(InputStreamCallback, TableVersion...)
	 */
	void downloadChangesForTablesInBackground(InputStreamCallback inputStreamCallback, AsyncCallback<Void> asyncCallback, TableVersion... tables);
	
	/**
	 * Asynchronous version of {@link SyncOperations#downloadChangesForTables(List, InputStreamCallback)}.
	 * @see SyncOperations#downloadChangesForTables(List, InputStreamCallback)
	 */
	void downloadChangesForTablesInBackground(List<TableVersion> tables, InputStreamCallback inputStreamCallback, AsyncCallback<Void> asyncCallback);
	
	/**
	 * Asynchronous version of {@link SyncOperations#parseTableChanges(InputStream, TableChangesCallback)}.
	 * @see SyncOperations#parseTableChanges(InputStream, TableChangesCallback)
	 */
	void parseTableChangesInBackground(InputStream inputStream, TableChangesCallback tableChangesCallback, AsyncCallback<Void> asyncCallback);
	
	/**
	 * Asynchronous version of {@link SyncOperations#versionsForTables(String...)}.
	 * @see SyncOperations#versionsForTables(String...)
	 */
	void versionsForTablesInBackground(AsyncCallback<List<TableVersion>> callback, String... tables);
	
	/**
	 * Asynchronous version of {@link SyncOperations#versionsForTables(List)}.
	 * @see SyncOperations#versionsForTables(List)
	 */
	void versionsForTablesInBackground(List<String> tables, AsyncCallback<List<TableVersion>> callback);

}