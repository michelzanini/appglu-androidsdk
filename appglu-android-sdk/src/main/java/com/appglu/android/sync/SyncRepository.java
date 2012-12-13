package com.appglu.android.sync;

import java.util.List;

import com.appglu.RowChanges;
import com.appglu.TableChanges;
import com.appglu.TableVersion;

public interface SyncRepository {
	
	public void beginTransaction();
	
	public void setTransactionSuccessful();
	
	public void endTransaction();
	
	public List<TableVersion> versionsForAllTables();
	
	public List<TableVersion> versionsForTables(List<String> tables);
	
	public void saveTableVersions(List<TableChanges> tables);
	
	public void insertRowInTable(String tableName, RowChanges rowChanges);

	public void updateRowInTable(String tableName, RowChanges rowChanges);

	public void deleteRowInTable(String tableName, RowChanges rowChanges);

}