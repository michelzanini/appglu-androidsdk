package com.appglu;

import java.util.List;

public interface AsyncSyncOperations {
	
	void changesForTablesInBackground(AsyncCallback<List<VersionedTableChanges>> callback, VersionedTable... tables);
	
	void changesForTablesInBackground(List<VersionedTable> tables, AsyncCallback<List<VersionedTableChanges>> callback);
	
	void changesForTableInBackground(String tableName, long version, AsyncCallback<VersionedTableChanges> callback);
	
	void versionsForTablesInBackground(AsyncCallback<List<VersionedTable>> callback, String... tables);
	
	void versionsForTablesInBackground(List<String> tables, AsyncCallback<List<VersionedTable>> callback);

}