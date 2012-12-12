package com.appglu.android.sync;

import java.util.ArrayList;
import java.util.List;

import com.appglu.TableVersion;
import com.appglu.TableChanges;

public class SQLiteSyncRepositoryTest extends AbstractSyncSQLiteTest {
	
	private SQLiteSyncRepository syncRepository;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.syncRepository = new SQLiteSyncRepository(this.syncDatabaseHelper);
	}
	
	public void testVersionsForAllTables() {
		List<TableVersion> tableVersions = this.syncRepository.versionsForAllTables();
		this.assertTableVersions(tableVersions, 0, 1, 2);
	}
	
	public void testVersionsForTables() {
		List<String> tables = new ArrayList<String>();
		
		tables.add("appglu_storage_files");
		tables.add("logged_table");
		tables.add("other_table");
		
		List<TableVersion> tableVersions = this.syncRepository.versionsForTables(tables);
		this.assertTableVersions(tableVersions, 0, 1, 2);
	}

	public void testSaveTableVersions() {
		List<TableChanges> tables = new ArrayList<TableChanges>();
		
		tables.add(new TableChanges("appglu_storage_files", 5));
		tables.add(new TableChanges("logged_table", 4));
		tables.add(new TableChanges("other_table", 10));
		
		this.syncRepository.saveTableVersions(tables);
		
		List<TableVersion> updatedTables = this.syncRepository.versionsForAllTables();
		this.assertTableVersions(updatedTables, 5, 4, 10);
	}

}