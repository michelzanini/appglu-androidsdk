package com.appglu;

import java.util.List;

public interface SyncOperations {
	
	List<VersionedTableChanges> changesForTables(VersionedTable... tables) throws AppGluRestClientException;
	
	List<VersionedTableChanges> changesForTables(List<VersionedTable> tables) throws AppGluRestClientException;
	
	VersionedTableChanges changesForTable(String tableName, long version) throws AppGluRestClientException;
	
	List<VersionedTable> versionsForTables(String... tables) throws AppGluRestClientException;
	
	List<VersionedTable> versionsForTables(List<String> tables) throws AppGluRestClientException;

}