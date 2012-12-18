package com.appglu.android.sync.sqlite;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import android.database.sqlite.SQLiteDatabase;

import com.appglu.TableVersion;
import com.appglu.TableChanges;
import com.appglu.android.sync.sqlite.SQLiteSyncRepository;

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
	
	public void testColumnsForTable() {
		TableColumns tableColumns = this.columnsForTable("logged_table");
		
		Assert.assertEquals(2, tableColumns.size());
		
		Assert.assertTrue(tableColumns.hasSinglePrimaryKey());
		Assert.assertFalse(tableColumns.hasComposePrimaryKey());
		Assert.assertEquals("id", tableColumns.getSinglePrimaryKeyName());
		
		Column idColumn = tableColumns.get("id");
		
		Assert.assertEquals("id", idColumn.getName());
		Assert.assertEquals("integer", idColumn.getType());
		Assert.assertEquals(false, idColumn.isNullable());
		Assert.assertEquals(true, idColumn.isPrimaryKey());
	
		Column nameColumn = tableColumns.get("name");
		
		Assert.assertEquals("name", nameColumn.getName());
		Assert.assertEquals("varchar", nameColumn.getType());
		Assert.assertEquals(true, nameColumn.isNullable());
		Assert.assertEquals(false, nameColumn.isPrimaryKey());
	}
	
	public void testColumnsForTable_NoPrimaryKey() {
		TableColumns tableColumns = this.columnsForTable("no_primary_key");
		
		Assert.assertFalse(tableColumns.hasSinglePrimaryKey());
		Assert.assertFalse(tableColumns.hasComposePrimaryKey());
	}

	public void testColumnsForTable_ComposePrimaryKey() {
		TableColumns tableColumns = this.columnsForTable("compose_primary_key");
		Assert.assertFalse(tableColumns.hasSinglePrimaryKey());
		Assert.assertTrue(tableColumns.hasComposePrimaryKey());
	}
	
	private TableColumns columnsForTable(String table) {
		SQLiteDatabase database = this.syncDatabaseHelper.getReadableDatabase();
		try {
			return this.syncRepository.columnsForTable(table, database);
		} finally {
			database.close();
		}
	}

}