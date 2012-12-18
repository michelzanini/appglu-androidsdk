package com.appglu.android.sync;

import java.util.List;

import com.appglu.TableChanges;
import com.appglu.TableVersion;

public interface SyncRepository {
	
	public List<TableVersion> versionsForAllTables();
	
	public List<TableVersion> versionsForTables(List<String> tables);
	
	public void applyChangesWithTransaction(List<TableChanges> changes);
	
}