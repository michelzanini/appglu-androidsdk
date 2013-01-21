package com.appglu.android.sync;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.appglu.StorageFile;
import com.appglu.Row;
import com.appglu.RowChanges;
import com.appglu.TableVersion;
import com.appglu.android.sync.Column;
import com.appglu.android.sync.SQLiteSyncRepository;
import com.appglu.android.sync.SyncDatabaseHelper;
import com.appglu.android.sync.TableColumns;

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
	
	public void testExecuteSyncOperation_emptyRowChanges() {
		RowChanges rowChanges = new RowChanges();
		
		boolean succeed = this.syncRepository.executeSyncOperation("other_table", rowChanges);
		Assert.assertFalse(succeed);
	}
	
	public void testExecuteSyncOperation_noPrimaryKey() {
		RowChanges rowChanges = this.getRowChanges();
		
		boolean succeed = this.syncRepository.executeSyncOperation("no_primary_key", rowChanges);
		Assert.assertFalse(succeed);
	}

	public void testExecuteSyncOperation_composePrimaryKey() {
		RowChanges rowChanges = this.getRowChanges();
		
		boolean succeed = this.syncRepository.executeSyncOperation("compose_primary_key", rowChanges);
		Assert.assertFalse(succeed);
	}
	
	public void testExecuteSyncOperation_noLocalColumns() {
		Row row = new Row();
		row.put("foo", 1);
		
		RowChanges rowChanges = new RowChanges();
		rowChanges.setRow(row);
		
		boolean succeed = this.syncRepository.executeSyncOperation("other_table", rowChanges);
		Assert.assertFalse(succeed);
	}
	
	private RowChanges getRowChanges() {
		Row row = new Row();
		
		row.put("id", 1);
		row.put("name", "name");
		
		RowChanges rowChanges = new RowChanges();
		rowChanges.setRow(row);
		return rowChanges;
	}
	
	public void testInsertOperation() {
		int rowsBefore = this.countTable("other_table");
		
		ContentValues values = new ContentValues();
		
		values.put("id", 2);
		values.put("name", "name");
		
		this.syncRepository.insertOperation("other_table", values);
		
		int rowsAfterInsert = this.countTable("other_table");
		Assert.assertEquals(rowsBefore + 1, rowsAfterInsert);
		
		Map<String, String> row = this.queryForMap("select * from other_table where id = 2", new String[0]);
		
		Assert.assertEquals("2", row.get("id"));
		Assert.assertEquals("name", row.get("name"));
	}
	
	public void testUpdateOperation() {
		int rowsBefore = this.countTable("other_table");
		
		ContentValues values = new ContentValues();
		
		values.put("id", 1);
		values.put("name", "newValue");
		
		this.syncRepository.updateOperation("other_table", values, "id", "1");
		
		int rowsAfterUpdate = this.countTable("other_table");
		Assert.assertEquals(rowsBefore, rowsAfterUpdate);
		
		Map<String, String> row = this.queryForMap("select * from other_table where id = 1", new String[0]);
		
		Assert.assertEquals("1", row.get("id"));
		Assert.assertEquals("newValue", row.get("name"));
	}
	
	public void testDeleteOperation() {
		int rowsBefore = this.countTable("other_table");
		
		this.syncRepository.deleteOperation("other_table", "id", "1");
		
		int rowsAfterDelte = this.countTable("other_table");
		Assert.assertEquals(rowsBefore - 1, rowsAfterDelte);
		
		Map<String, String> row = this.queryForMap("select * from other_table where id = 1", new String[0]);
		Assert.assertTrue(row.isEmpty());
	}

	public void testSaveTableVersions() {
		this.syncRepository.saveTableVersion(new TableVersion("appglu_storage_files", 5));
		this.syncRepository.saveTableVersion(new TableVersion("logged_table", 4));
		this.syncRepository.saveTableVersion(new TableVersion("other_table", 10));
		
		List<TableVersion> updatedTables = this.syncRepository.versionsForAllTables();
		this.assertTableVersions(updatedTables, 5, 4, 10);
	}
	
	public void testColumnsForTable() {
		TableColumns tableColumns = this.syncRepository.columnsForTable("logged_table");
		
		Assert.assertEquals(2, tableColumns.size());
		
		Assert.assertTrue(tableColumns.hasSinglePrimaryKey());
		Assert.assertFalse(tableColumns.hasComposePrimaryKey());
		Assert.assertEquals("id", tableColumns.getSinglePrimaryKeyName());
		
		Column idColumn = tableColumns.get("id");
		
		Assert.assertEquals("id", idColumn.getName());
		Assert.assertEquals("integer", idColumn.getType());
		Assert.assertEquals(true, idColumn.isPrimaryKey());
	
		Column nameColumn = tableColumns.get("name");
		
		Assert.assertEquals("name", nameColumn.getName());
		Assert.assertEquals("varchar", nameColumn.getType());
		Assert.assertEquals(false, nameColumn.isPrimaryKey());
	}
	
	public void testColumnsForTable_NoPrimaryKey() {
		TableColumns tableColumns = this.syncRepository.columnsForTable("no_primary_key");
		
		Assert.assertFalse(tableColumns.hasSinglePrimaryKey());
		Assert.assertFalse(tableColumns.hasComposePrimaryKey());
	}

	public void testColumnsForTable_ComposePrimaryKey() {
		TableColumns tableColumns = this.syncRepository.columnsForTable("compose_primary_key");
		Assert.assertFalse(tableColumns.hasSinglePrimaryKey());
		Assert.assertTrue(tableColumns.hasComposePrimaryKey());
	}
	
	public void testAreForeignKeysEnabled() {
		Assert.assertFalse(this.syncRepository.areForeignKeysEnabled());
		
		SyncDatabaseHelper fkDatabaseHelper = new SyncDatabaseHelper(this.getContext(), "fk.sqlite", 1, true) {
			public void onCreateAppDatabase(SQLiteDatabase db) {
			}
			public void onUpgradeAppDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
			}
		};
		
		SQLiteSyncRepository fkEnabledRepository = new SQLiteSyncRepository(fkDatabaseHelper);
		Assert.assertTrue(fkEnabledRepository.areForeignKeysEnabled());
		
		fkDatabaseHelper.close();
	}
	
	public void testSetForeignKeysEnabled() {
		Assert.assertFalse(this.syncRepository.areForeignKeysEnabled());
		
		this.syncRepository.setForeignKeysEnabled(true);
		Assert.assertTrue(this.syncRepository.areForeignKeysEnabled());
		
		this.syncRepository.setForeignKeysEnabled(false);
		Assert.assertFalse(this.syncRepository.areForeignKeysEnabled());
	}
	
	public void testPrimaryKeyForSyncKey() {
		String primaryKey = this.syncRepository.primaryKeyForSyncKey(1, "other_table");
		Assert.assertEquals("1", primaryKey);
	}

	public void testSaveSyncMetadata() {
		int rowsBefore = this.countTable("appglu_sync_metadata");
		
		this.syncRepository.saveSyncMetadata(123, "logged_table", "456");
		
		int rowsAfterInsert = this.countTable("appglu_sync_metadata");
		Assert.assertEquals(rowsBefore + 1, rowsAfterInsert);
		
		String primaryKeyAfterInsert = this.syncRepository.primaryKeyForSyncKey(123, "logged_table");
		Assert.assertEquals("456", primaryKeyAfterInsert);
		
		this.syncRepository.saveSyncMetadata(123, "logged_table", "123");
		
		int rowsAfterUpdate = this.countTable("appglu_sync_metadata");
		Assert.assertEquals(rowsAfterInsert, rowsAfterUpdate);
		
		String primaryKeyAfterUpdate = this.syncRepository.primaryKeyForSyncKey(123, "logged_table");
		Assert.assertEquals("123", primaryKeyAfterUpdate);
	}
	
	public void testDeleteSyncMetadata() {
		int rowsBefore = this.countTable("appglu_sync_metadata");
		
		this.syncRepository.deleteSyncMetadata(1, "other_table");
		
		int rowsAfterDelete = this.countTable("appglu_sync_metadata");
		Assert.assertEquals(rowsBefore - 1, rowsAfterDelete);
		
		String primaryKeyAfterDelete = this.syncRepository.primaryKeyForSyncKey(1, "other_table");
		Assert.assertNull(primaryKeyAfterDelete);
	}
	
	public void testGetAllFiles() {
		List<StorageFile> files = this.syncRepository.getAllFiles();

		Assert.assertNotNull(files);
		Assert.assertEquals(2, files.size());
		
		StorageFile fileOne = files.get(0);
		this.assertFileOne(fileOne);
	}

	public void testGetStorageFileByIdOrUrl() {
		StorageFile fileOneById = this.syncRepository.getStorageFileByIdOrUrl(1001, null);
		this.assertFileOne(fileOneById);
		
		StorageFile fileOneByUrl = this.syncRepository.getStorageFileByIdOrUrl(0, "https://s3.amazonaws.com/cbs-startrek1/1ee26276-b773-4eaa-9762-49c380e604c7-app-icon.png");
		this.assertFileOne(fileOneByUrl);
		
		StorageFile fileOneByIdOrUrl = this.syncRepository.getStorageFileByIdOrUrl(1001, "https://s3.amazonaws.com/cbs-startrek1/1ee26276-b773-4eaa-9762-49c380e604c7-app-icon.png");
		this.assertFileOne(fileOneByIdOrUrl);
	}
	
	private void assertFileOne(StorageFile fileOne) {
		Assert.assertEquals(1001, fileOne.getId());
		Assert.assertEquals("1ee26276-b773-4eaa-9762-49c380e604c7", fileOne.getKey());
		Assert.assertEquals("app-icon.png", fileOne.getName());
		Assert.assertEquals("image/png", fileOne.getContentType());
		Assert.assertEquals("app-icon", fileOne.getTitle());
		Assert.assertEquals(10125, fileOne.getSize());
		Assert.assertEquals(1357309209000L, fileOne.getLastModified().getTime());
		Assert.assertEquals("https://s3.amazonaws.com/cbs-startrek1/1ee26276-b773-4eaa-9762-49c380e604c7-app-icon.png", fileOne.getUrl());
		Assert.assertEquals("abdca54e4ca831ec8013ef9f597adf1c", fileOne.getETag());
		Assert.assertEquals(1, fileOne.getDirectoryId());
	}

}