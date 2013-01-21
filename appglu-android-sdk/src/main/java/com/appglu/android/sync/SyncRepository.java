package com.appglu.android.sync;

import java.util.List;

import com.appglu.StorageFile;
import com.appglu.TableChangesCallback;
import com.appglu.TableVersion;

public interface SyncRepository extends TableChangesCallback {
	
	public List<TableVersion> versionsForAllTables();
	
	public List<TableVersion> versionsForTables(List<String> tables);
	
	public StorageFile getStorageFileByIdOrUrl(long id, String url);
	
	public void applyChangesWithTransaction(SyncRepositoryCallback transactionCallback);
	
}