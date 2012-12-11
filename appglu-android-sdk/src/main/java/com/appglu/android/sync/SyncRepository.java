package com.appglu.android.sync;

import java.util.List;

import com.appglu.VersionedRow;
import com.appglu.VersionedTable;
import com.appglu.VersionedTableChanges;

public interface SyncRepository {
	
	public void beginTransaction();
	
	public void setTransactionSuccessful();
	
	public void endTransaction();
	
	public List<VersionedTable> listTables();
	
	public void updateLocalTableVersions(List<VersionedTableChanges> tables);

	public void processChangesToTable(String tableName, VersionedRow row);

}