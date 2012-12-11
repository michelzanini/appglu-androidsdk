package com.appglu.android.sync;

import java.util.ArrayList;
import java.util.List;

import com.appglu.AppGluRestClientException;
import com.appglu.SyncOperations;
import com.appglu.VersionedTable;
import com.appglu.VersionedTableChanges;

public class MockSyncOperations implements SyncOperations {

	public List<VersionedTableChanges> changesForTables(VersionedTable... tables) throws AppGluRestClientException {
		return null;
	}

	public List<VersionedTableChanges> changesForTables(List<VersionedTable> tables) throws AppGluRestClientException {
		List<VersionedTableChanges> tableList = new ArrayList<VersionedTableChanges>();
		
		for (VersionedTable versionedTable : tables) {
			VersionedTableChanges changes = new VersionedTableChanges();
			
			changes.setTableName(versionedTable.getTableName());
			changes.setVersion(versionedTable.getVersion() + 2);
			
			tableList.add(changes);
		}
		
		return tableList;
	}

	public VersionedTableChanges changesForTable(String tableName, long version) throws AppGluRestClientException {
		return null;
	}

	public List<VersionedTable> versionsForTables(String... tables) throws AppGluRestClientException {
		return null;
	}

	public List<VersionedTable> versionsForTables(List<String> tables) throws AppGluRestClientException {
		return null;
	}

}
