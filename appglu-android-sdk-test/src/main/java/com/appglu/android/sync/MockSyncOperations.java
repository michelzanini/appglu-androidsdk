package com.appglu.android.sync;

import java.util.ArrayList;
import java.util.List;

import com.appglu.AppGluRestClientException;
import com.appglu.SyncOperations;
import com.appglu.TableVersion;
import com.appglu.TableChanges;

public class MockSyncOperations implements SyncOperations {

	public List<TableChanges> changesForTables(TableVersion... tables) throws AppGluRestClientException {
		return null;
	}

	public List<TableChanges> changesForTables(List<TableVersion> tables) throws AppGluRestClientException {
		List<TableChanges> tableList = new ArrayList<TableChanges>();
		
		for (TableVersion table : tables) {
			TableChanges changes = new TableChanges();
			
			changes.setTableName(table.getTableName());
			changes.setVersion(table.getVersion() + 2);
			
			tableList.add(changes);
		}
		
		return tableList;
	}

	public TableChanges changesForTable(String tableName, long version) throws AppGluRestClientException {
		return null;
	}

	public List<TableVersion> versionsForTables(String... tables) throws AppGluRestClientException {
		return null;
	}

	public List<TableVersion> versionsForTables(List<String> tables) throws AppGluRestClientException {
		return null;
	}

}
