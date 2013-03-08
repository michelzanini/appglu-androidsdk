/*******************************************************************************
 * Copyright 2013 AppGlu, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.appglu.android.sync;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.appglu.Row;
import com.appglu.RowChanges;
import com.appglu.StorageFile;
import com.appglu.SyncOperation;
import com.appglu.TableVersion;
import com.appglu.android.AppGlu;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;
import com.appglu.impl.util.StringUtils;

public class SQLiteSyncRepository implements SyncRepository {
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.LOG_TAG);
	
	private static final int TABLE_NAME_INDEX = 0;
	private static final int VERSION_INDEX = 1;
	
	private static final int COLUMN_NAME_INDEX = 1;
	private static final int COLUMN_TYPE_INDEX = 2;
	private static final int PRIMARY_KEY_INDEX = 5;
	
	private static final int FILE_ID_INDEX = 0;
	private static final int FILE_KEY_INDEX = 1;
	private static final int FILE_NAME_INDEX = 2;
	private static final int FILE_CONTENT_TYPE_INDEX = 3;
	private static final int FILE_TITLE_INDEX = 4;
	private static final int FILE_SIZE_INDEX = 5;
	private static final int FILE_LAST_MODIFIED_INDEX = 6;
	private static final int FILE_URL_INDEX = 7;
	private static final int FILE_ETAG_INDEX = 8;
	private static final int FILE_VERSION_INDEX = 9;
	private static final int FILE_DIRECTORY_ID_INDEX = 10;
	
	private SyncDatabaseHelper syncDatabaseHelper;
	
	private ContentValuesRowMapper contentValuesRowMapper;
	
	public SQLiteSyncRepository(SyncDatabaseHelper syncDatabaseHelper) {
		this.syncDatabaseHelper = syncDatabaseHelper;
	}
	
	private SQLiteDatabase getReadableDatabase() {
		return this.syncDatabaseHelper.getReadableDatabase();
	}
	
	private SQLiteDatabase getWritableDatabase() {
		return this.syncDatabaseHelper.getWritableDatabase();
	}
	
	protected void buildContentValuesRowMapper(String tableName) {
		TableColumns tableColumns = this.columnsForTable(tableName);
		this.contentValuesRowMapper = new ContentValuesRowMapper(tableColumns);
	}
	
	public List<TableVersion> versionsForAllTables() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT m.name, t.version FROM sqlite_master m ");
		sql.append("LEFT OUTER JOIN appglu_table_versions t ON m.name = t.table_name ");
		sql.append("WHERE m.type = 'table' and m.name not in ('appglu_table_versions', 'appglu_sync_metadata') and m.name not like 'android_%' and m.name not like 'sqlite_%' ORDER BY m.name;");
		
		return this.queryForTableVersions(sql.toString(), new String[0]);
	}

	public List<TableVersion> versionsForTables(List<String> tables) {
		String tablesParameter = StringUtils.collectionToDelimitedString(tables, ",", "'", "'");
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT m.name, t.version FROM sqlite_master m ");
		sql.append("LEFT OUTER JOIN appglu_table_versions t ON m.name = t.table_name ");
		sql.append("WHERE m.type = 'table' and m.name in (");
		sql.append(tablesParameter);
		sql.append(") ORDER BY m.name;");
		
		return this.queryForTableVersions(sql.toString(), new String[0]);
	}
	
	protected List<TableVersion> queryForTableVersions(String sql, String[] selectionArgs) {
		List<TableVersion> tables = new ArrayList<TableVersion>();
		
		SQLiteDatabase database = this.getReadableDatabase();
		
		Cursor cursor = null;
		
		try {
			cursor = database.rawQuery(sql, selectionArgs);
		    cursor.moveToFirst();
		    
		    for (int i = 0; i < cursor.getCount(); i++) {
		    	TableVersion table = new TableVersion();
		    	
		    	table.setTableName(cursor.getString(TABLE_NAME_INDEX));
		    	table.setVersion(cursor.getLong(VERSION_INDEX));
		    	
		    	tables.add(table);
		    	
		    	cursor.moveToNext();
		    }
		} catch (SQLException e) {
			throw new SyncRepositoryException(e);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			database.close();
		}
		
		return tables;
	}
	
	public StorageFile getStorageFileByIdOrUrl(long id, String url) {
		List<StorageFile> files = new ArrayList<StorageFile>();
				
		SQLiteDatabase database = this.getReadableDatabase();
		
		try {
			String urlParam = url != null ? url : "";
			files = this.queryForFiles(database, "select * from appglu_storage_files where id = ? or url = ?", new String[] { String.valueOf(id), urlParam });
		} finally {
			database.close();
		}
		
		if (files.isEmpty()) {
			return null;
		}
		
		return files.get(0);
	}
	
	public List<StorageFile> getAllFiles() {
		SQLiteDatabase database = this.getReadableDatabase();
		
		try {
			return this.queryForFiles(database, "select * from appglu_storage_files", new String[0]);
		} finally {
			database.close();
		}
	}
	
	protected List<StorageFile> queryForFiles(SQLiteDatabase database, String sql, String[] selectionArgs) {
		List<StorageFile> files = new ArrayList<StorageFile>();
		
		Cursor cursor = null;
		
		try {
			cursor = database.rawQuery(sql, selectionArgs);
		    cursor.moveToFirst();
		    
		    for (int i = 0; i < cursor.getCount(); i++) {
		    	StorageFile file = new StorageFile();
		    	
		    	file.setId(cursor.getLong(FILE_ID_INDEX));
		    	file.setKey(cursor.getString(FILE_KEY_INDEX));
		    	file.setName(cursor.getString(FILE_NAME_INDEX));
		    	file.setContentType(cursor.getString(FILE_CONTENT_TYPE_INDEX));
		    	file.setTitle(cursor.getString(FILE_TITLE_INDEX));
		    	file.setSize(cursor.getInt(FILE_SIZE_INDEX));
		    	file.setLastModified(new Date(cursor.getLong(FILE_LAST_MODIFIED_INDEX)));
		    	file.setUrl(cursor.getString(FILE_URL_INDEX));
		    	file.setETag(cursor.getString(FILE_ETAG_INDEX));
		    	file.setVersion(cursor.getString(FILE_VERSION_INDEX));
		    	file.setDirectoryId(cursor.getInt(FILE_DIRECTORY_ID_INDEX));
		    	
		    	files.add(file);
		    	
		    	cursor.moveToNext();
		    }
		} catch (SQLException e) {
			throw new SyncRepositoryException(e);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		
		return files;
	}

	public void applyChangesWithTransaction(SyncTransactionCallback transactionCallback) {
		SQLiteDatabase database = this.getWritableDatabase();
		
		boolean foreignKeysWereEnabled = false;
		try {
			foreignKeysWereEnabled = this.areForeignKeysEnabled();
			
			if (foreignKeysWereEnabled) {
				this.setForeignKeysEnabled(false);
			}
			
			database.beginTransaction();
			try {
				transactionCallback.doInTransaction();
				database.setTransactionSuccessful();
			} finally {
				database.endTransaction();
			}
		} finally {
			if (foreignKeysWereEnabled) {
				this.setForeignKeysEnabled(true);
			}
			
			database.close();
		}
	}
	
	public boolean doWithTableVersion(TableVersion tableVersion, boolean hasChanges) {
		String tableName = tableVersion.getTableName();
		
		if (this.logger.isInfoEnabled()) {
			if (hasChanges) {
				this.logger.info("Applying remote changes to table '" + tableVersion.getTableName() + "'");
			} else {
				this.logger.info("Table '" + tableName + "' is already synchronized");
			}
		}
		
		this.buildContentValuesRowMapper(tableName);
    	
		TableColumns tableColumns = this.contentValuesRowMapper.getTableColumns();
    	if (!tableColumns.hasSinglePrimaryKey()) {
    		logger.warn("Ignoring changes to table '" + tableName + "' because it should have one and only one column as primary key");
    		return false;
    	}
    	
		this.saveTableVersion(tableVersion);
		
		//process the changes of every table
		return true;
	}

	public void doWithRowChanges(TableVersion tableVersion, RowChanges rowChanges) {
		this.executeSyncOperation(tableVersion.getTableName(), rowChanges);
	}

	protected boolean executeSyncOperation(String tableName, RowChanges rowChanges) {
		Row row = rowChanges.getRow();
		
		if (row.isEmpty()) {
			logger.warn("Ignoring changes to table '" + tableName + "' because changes are empty");
			return false;
		}
		
		if (this.contentValuesRowMapper == null) {
			logger.warn("Ignoring changes to table '" + tableName + "' contentValuesRowMapper was not initialized");
			return false;
		}
		
	    try {
	    	TableColumns tableColumns = this.contentValuesRowMapper.getTableColumns();
	    	
	    	if (!tableColumns.hasSinglePrimaryKey()) {
	    		logger.warn("Ignoring changes to table '" + tableName + "' because it should have one and only one column as primary key");
	    		return false;
	    	}
	    	
	    	ContentValues values = this.contentValuesRowMapper.mapRow(row);
			
    		if (values.size() == 0) {
    			logger.warn("Ignoring changes to table '" + tableName + "' because there is no matching column to sync");
    			return false;
    		}
    		
			String primaryKeyName = tableColumns.getSinglePrimaryKeyName();
    		Object primaryKeyValue = values.get(StringUtils.escapeColumn(primaryKeyName));
    		
    		SyncOperation syncOperation = rowChanges.getSyncOperation();
    		
    		if (primaryKeyValue == null && syncOperation != SyncOperation.DELETE) {
    			logger.warn("Ignoring changes to table '" + tableName + "' because the primary key value could not be found");
    			return false;
    		}
    		
			long syncKey = rowChanges.getSyncKey();
			
			if (syncKey == 0) {
    			logger.warn("Ignoring changes to table '" + tableName + "' because the sync key is zero");
    			return false;
    		}
			
			if (syncOperation == SyncOperation.INSERT) {
				this.insertOperation(tableName, values);
				this.saveSyncMetadata(syncKey, tableName, primaryKeyValue);
			}
			
			if (syncOperation == SyncOperation.UPDATE) {
				String primaryKeyForSyncKey = this.primaryKeyForSyncKey(syncKey, tableName);
				
				if (StringUtils.isEmpty(primaryKeyForSyncKey)) {
					this.insertOperation(tableName, values);
				} else {
					this.updateOperation(tableName, values, primaryKeyName, primaryKeyForSyncKey);
				}
				this.saveSyncMetadata(syncKey, tableName, primaryKeyValue);
			}

			if (syncOperation == SyncOperation.DELETE) {
				String primaryKeyForSyncKey = this.primaryKeyForSyncKey(syncKey, tableName);
				
				if (StringUtils.isNotEmpty(primaryKeyForSyncKey)) {
					this.deleteOperation(tableName, primaryKeyName, primaryKeyForSyncKey);
					this.deleteSyncMetadata(syncKey, tableName);
				}
			}
			
			return true;
		} catch (SQLException e) {
			throw new SyncRepositoryException(e);
		}
	}
	
	protected void insertOperation(String tableName, ContentValues values) {
		if (this.logger.isDebugEnabled()) {
			logger.debug("insert into '" + tableName + "' values " + "(" + values + ")");
		}
		
		SQLiteDatabase database = this.getWritableDatabase();
		database.insertOrThrow(StringUtils.escapeColumn(tableName), null, values);
	}

	protected void updateOperation(String tableName, ContentValues values, String primaryKeyName, String primaryKeyForSyncKey) {
		if (this.logger.isDebugEnabled()) {
			logger.debug("update '" + tableName + "' set values (" + values + ") where '" + primaryKeyName + "' = '" + primaryKeyForSyncKey + "'");
		}
		
		SQLiteDatabase database = this.getWritableDatabase();
		String whereClause = StringUtils.escapeColumn(primaryKeyName) + " = ?";
		
		database.update(StringUtils.escapeColumn(tableName), values, whereClause, new String[] { primaryKeyForSyncKey });
	}

	protected void deleteOperation(String tableName, String primaryKeyName, String primaryKeyForSyncKey) {
		if (this.logger.isDebugEnabled()) {
			logger.debug("delete from '" + tableName + "' where '" + primaryKeyName + "' = '" + primaryKeyForSyncKey + "'");
		}
		
		SQLiteDatabase database = this.getWritableDatabase();
		String whereClause = StringUtils.escapeColumn(primaryKeyName) + " = ?";
		
		database.delete(StringUtils.escapeColumn(tableName), whereClause, new String[] { primaryKeyForSyncKey });
	}
	
	protected void saveTableVersion(TableVersion tableVersion) {
	    try {
	    	SQLiteDatabase database = this.getWritableDatabase();
	    	
    		ContentValues values = new ContentValues();
    		
    		values.put("table_name", tableVersion.getTableName());
	    	values.put("version", tableVersion.getVersion());
			
			database.replaceOrThrow("appglu_table_versions", null, values);
		} catch (SQLException e) {
			throw new SyncRepositoryException(e);
		}
	}
	
	protected TableColumns columnsForTable(String tableName) {
		TableColumns tableColumns = new TableColumns();
		tableColumns.setTableName(tableName);
		
		Cursor cursor = null;
		
		try {
			SQLiteDatabase database = this.getReadableDatabase();
			cursor = database.rawQuery("PRAGMA table_info (" + tableName + ")", new String[0]);
		    cursor.moveToFirst();
		    
		    for (int i = 0; i < cursor.getCount(); i++) {
		    	Column column = new Column();
		    	
		    	column.setName(cursor.getString(COLUMN_NAME_INDEX));
		    	column.setType(cursor.getString(COLUMN_TYPE_INDEX));
		    	column.setPrimaryKey(cursor.getInt(PRIMARY_KEY_INDEX) == 1 ? true : false);
		    	
		    	tableColumns.put(column.getName(), column);
		    	
		    	if (column.isPrimaryKey()) {
		    		tableColumns.addPrimaryKey(column.getName());
		    	}
		    	
		    	cursor.moveToNext();
		    }
		} catch (SQLException e) {
			throw new SyncRepositoryException(e);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		
		return tableColumns;
	}
	
	protected boolean areForeignKeysEnabled() {
		Cursor cursor = null;
		
		try {
			SQLiteDatabase database = this.getReadableDatabase();
			cursor = database.rawQuery("PRAGMA foreign_keys", new String[0]);
		    cursor.moveToFirst();
		    
		    if (cursor.getCount() > 0) {
		    	return cursor.getInt(0) == 1 ? true : false; 
		    }
		    
		    return false;
		} catch (SQLException e) {
			throw new SyncRepositoryException(e);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	protected void setForeignKeysEnabled(boolean enabled) {
		try {
			SQLiteDatabase database = this.getWritableDatabase();
			database.execSQL("PRAGMA foreign_keys = " + enabled);
		} catch (SQLException e) {
			throw new SyncRepositoryException(e);
		}
	}
	
	protected String primaryKeyForSyncKey(long syncKey, String tableName) {
		Cursor cursor = null;
		
		try {
			SQLiteDatabase database = this.getReadableDatabase();
			
			String[] selectionArgs = new String[] { String.valueOf(syncKey), tableName };
			cursor = database.rawQuery("select primary_key from appglu_sync_metadata where sync_key = ? and table_name = ?", selectionArgs);
		    cursor.moveToFirst();
		    
		    if (cursor.getCount() > 0) {
		    	return cursor.getString(0);
		    }
		    
		    return null;
		} catch (SQLException e) {
			throw new SyncRepositoryException(e);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	protected void saveSyncMetadata(long syncKey, String tableName, Object primaryKeyValue) {
		SQLiteDatabase database = this.getWritableDatabase();
		
		Object[] params = new Object[] {syncKey, tableName, String.valueOf(primaryKeyValue)};
		database.execSQL("insert or replace into appglu_sync_metadata (sync_key, table_name, primary_key) values (?,?,?)", params);
	}

	protected void deleteSyncMetadata(long syncKey, String tableName) {
		SQLiteDatabase database = this.getWritableDatabase();
		database.delete("appglu_sync_metadata", "sync_key = ? and table_name = ?", new String[] { String.valueOf(syncKey), tableName });
	}

}
