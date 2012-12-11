package com.appglu.android.sync;

import java.util.ArrayList;
import java.util.List;

import com.appglu.VersionedTable;
import com.appglu.VersionedTableChanges;

public class SQLiteSyncRepositoryTest extends AbstractSyncSQLiteTest {
	
	private SQLiteSyncRepository syncRepository;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.syncRepository = new SQLiteSyncRepository(this.syncDatabaseHelper);
	}
	
	public void testListTables() {
		List<VersionedTable> tables = this.syncRepository.listTables();
		this.assertTableVersions(tables, 0, 1, 2);
	}

	public void testUpdateLocalTableVersions() {
		List<VersionedTableChanges> tables = new ArrayList<VersionedTableChanges>();
		
		tables.add(new VersionedTableChanges("appglu_storage_files", 5));
		tables.add(new VersionedTableChanges("logged_table", 4));
		tables.add(new VersionedTableChanges("other_table", 10));
		
		this.syncRepository.updateLocalTableVersions(tables);
		
		List<VersionedTable> updatedTables = this.syncRepository.listTables();
		this.assertTableVersions(updatedTables, 5, 4, 10);
	}

}