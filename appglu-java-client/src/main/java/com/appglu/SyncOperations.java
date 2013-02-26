package com.appglu;

import java.util.List;

import com.appglu.impl.json.TableChangesJsonParser;

public interface SyncOperations extends TableChangesJsonParser {
	
	TableChanges changesForTable(String tableName, long version) throws AppGluRestClientException;
	
	List<TableChanges> changesForTables(TableVersion... tables) throws AppGluRestClientException;
	
	List<TableChanges> changesForTables(List<TableVersion> tables) throws AppGluRestClientException;
	
	void changesForTables(TableChangesCallback tableChangesCallback, TableVersion... tables) throws AppGluRestClientException;
	
	void changesForTables(List<TableVersion> tables, TableChangesCallback tableChangesCallback) throws AppGluRestClientException;
	
	void downloadChangesForTables(InputStreamCallback inputStreamCallback, TableVersion... tables) throws AppGluRestClientException;
	
	void downloadChangesForTables(List<TableVersion> tables, InputStreamCallback inputStreamCallback) throws AppGluRestClientException;
	
	List<TableVersion> versionsForTables(String... tables) throws AppGluRestClientException;
	
	List<TableVersion> versionsForTables(List<String> tables) throws AppGluRestClientException;

}