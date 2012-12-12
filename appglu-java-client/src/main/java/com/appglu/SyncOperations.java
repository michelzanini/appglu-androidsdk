package com.appglu;

import java.util.List;

public interface SyncOperations {
	
	List<TableChanges> changesForTables(TableVersion... tables) throws AppGluRestClientException;
	
	List<TableChanges> changesForTables(List<TableVersion> tables) throws AppGluRestClientException;
	
	TableChanges changesForTable(String tableName, long version) throws AppGluRestClientException;
	
	List<TableVersion> versionsForTables(String... tables) throws AppGluRestClientException;
	
	List<TableVersion> versionsForTables(List<String> tables) throws AppGluRestClientException;

}