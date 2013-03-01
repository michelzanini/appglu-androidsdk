package com.appglu;

import java.io.InputStream;
import java.util.List;

/**
 * TODO
 */
public interface AsyncSyncOperations {
	
	void changesForTableInBackground(String tableName, long version, AsyncCallback<TableChanges> callback);
	
	void changesForTablesInBackground(AsyncCallback<List<TableChanges>> callback, TableVersion... tables);
	
	void changesForTablesInBackground(List<TableVersion> tables, AsyncCallback<List<TableChanges>> callback);
	
	void changesForTablesInBackground(TableChangesCallback tableChangesCallback, AsyncCallback<Void> asyncCallback, TableVersion... tables);
	
	void changesForTablesInBackground(List<TableVersion> tables, TableChangesCallback tableChangesCallback, AsyncCallback<Void> asyncCallback);
	
	void downloadChangesForTablesInBackground(InputStreamCallback inputStreamCallback, AsyncCallback<Void> asyncCallback, TableVersion... tables);
	
	void downloadChangesForTablesInBackground(List<TableVersion> tables, InputStreamCallback inputStreamCallback, AsyncCallback<Void> asyncCallback);
	
	void parseTableChangesInBackground(InputStream inputStream, TableChangesCallback tableChangesCallback, AsyncCallback<Void> asyncCallback);
	
	void versionsForTablesInBackground(AsyncCallback<List<TableVersion>> callback, String... tables);
	
	void versionsForTablesInBackground(List<String> tables, AsyncCallback<List<TableVersion>> callback);

}