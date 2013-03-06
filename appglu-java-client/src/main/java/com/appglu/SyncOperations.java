package com.appglu;

import java.util.List;

import com.appglu.impl.json.TableChangesJsonParser;

/**
 * {@code SyncOperations} is used to synchronize the data in your local SQLite tables with the AppGlu server.
 * 
 * @see AsyncSyncOperations
 * @since 1.0.0
 */
public interface SyncOperations extends TableChangesJsonParser {
	
	/**
	 * The returned {@link TableChanges} will contain rows if the server version of the table is higher that the local one.
	 * @param tableName the name of the table
	 * @param version this is the local version from the last time this table was synchronized
	 * @return new, updated or deleted rows for this table if the server version of the table is higher that the local one
	 */
	TableChanges changesForTable(String tableName, long version) throws AppGluRestClientException;
	
	/**
	 * Return a list of {@link TableChanges}, one for each specified {@link TableVersion}.<br>
	 * For each {@link TableChanges} returned, it will contain rows if the server version of that table is higher that the local one.<br>
	 * Since the response can be very large, to consume less memory, the use of {@link #changesForTables(TableChangesCallback, TableVersion...)} may be more appropriate.
	 * @param tables table names and versions
	 * @return a list of {@link TableChanges}
	 */
	List<TableChanges> changesForTables(TableVersion... tables) throws AppGluRestClientException;
	
	/**
	 * Return a list of {@link TableChanges}, one for each specified {@link TableVersion}.<br>
	 * For each {@link TableChanges} returned, it will contain rows if the server version of that table is higher that the local one.<br>
	 * Since the response can be very large, to consume less memory, the use of {@link #changesForTables(List, TableChangesCallback)} may be more appropriate.
	 * @param tables table names and versions
	 * @return a list of {@link TableChanges}
	 */
	List<TableChanges> changesForTables(List<TableVersion> tables) throws AppGluRestClientException;
	
	/**
	 * Returns the same result as {@link #changesForTables(TableVersion...)}.
	 * However, instead of parsing the whole response and return it all together, it will parse one table and one row at a time and send that to the provided {@link TableChangesCallback}.<br>
	 * The advantage is that it will consume less memory, but the parsing code is more complex. Since changes can be a very large response, this method is more appropriate.
	 * @param tableChangesCallback a callback to receive the tables and rows that were changed
	 * @param tables table names and versions
	 */
	void changesForTables(TableChangesCallback tableChangesCallback, TableVersion... tables) throws AppGluRestClientException;
	
	/**
	 * Returns the same result as {@link #changesForTables(List)}.
	 * However, instead of parsing the whole response and return it all together, it will parse one table and one row at a time and send that to the provided {@link TableChangesCallback}.<br>
	 * The advantage is that it will consume less memory, but the parsing code is more complex. Since changes can be a very large response, this method is more appropriate.
	 * @param tableChangesCallback a callback to receive the tables and rows that were changed
	 * @param tables table names and versions
	 */
	void changesForTables(List<TableVersion> tables, TableChangesCallback tableChangesCallback) throws AppGluRestClientException;
	
	/**
	 * Returns the same result as {@link #changesForTables(TableVersion...)}.
	 * However, a raw <code>InputStream</code> is returned so it is possible to save the JSON response to disk or other type of storage.
	 * @param inputStreamCallback the callback that will receive the raw <code>InputStream</code> 
	 * @param tables table names and versions
	 */
	void downloadChangesForTables(InputStreamCallback inputStreamCallback, TableVersion... tables) throws AppGluRestClientException;
	
	/**
	 * Returns the same result as {@link #changesForTables(List)}.
	 * However, a raw <code>InputStream</code> is returned so it is possible to save the JSON response to disk or other type of storage.
	 * @param inputStreamCallback the callback that will receive the raw <code>InputStream</code> 
	 * @param tables table names and versions
	 */
	void downloadChangesForTables(List<TableVersion> tables, InputStreamCallback inputStreamCallback) throws AppGluRestClientException;
	
	/**
	 * Return the current server table version for the specified table names.
	 * @param tables table names
	 * @return the versions of those tables
	 */
	List<TableVersion> versionsForTables(String... tables) throws AppGluRestClientException;
	
	/**
	 * Return the current server table version for the specified table names.
	 * @param tables table names
	 * @return the versions of those tables
	 */
	List<TableVersion> versionsForTables(List<String> tables) throws AppGluRestClientException;

}