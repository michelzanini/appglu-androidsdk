package com.appglu;

import java.util.List;

public interface AsyncSyncOperations {
	
	void changesForTablesInBackground(AsyncCallback<List<TableChanges>> callback, TableVersion... tables);
	
	void changesForTablesInBackground(List<TableVersion> tables, AsyncCallback<List<TableChanges>> callback);
	
	void changesForTableInBackground(String tableName, long version, AsyncCallback<TableChanges> callback);
	
	void versionsForTablesInBackground(AsyncCallback<List<TableVersion>> callback, String... tables);
	
	void versionsForTablesInBackground(List<String> tables, AsyncCallback<List<TableVersion>> callback);

}