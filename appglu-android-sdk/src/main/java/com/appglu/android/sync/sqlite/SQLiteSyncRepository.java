package com.appglu.android.sync.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.appglu.Row;
import com.appglu.RowChanges;
import com.appglu.SyncOperation;
import com.appglu.TableChanges;
import com.appglu.TableVersion;
import com.appglu.android.AppGlu;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;
import com.appglu.android.sync.SyncRepository;
import com.appglu.android.sync.SyncRepositoryException;
import com.appglu.impl.util.StringUtils;

public class SQLiteSyncRepository implements SyncRepository {
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.SYNC_LOG_TAG);
	
	private static final int TABLE_NAME_INDEX = 0;
	private static final int VERSION_INDEX = 1;
	
	private static final int COLUMN_NAME_INDEX = 1;
	private static final int COLUMN_TYPE_INDEX = 2;
	private static final int IS_NOT_NULL_INDEX = 3;
	private static final int PRIMARY_KEY_INDEX = 5;
	
	private SyncDatabaseHelper syncDatabaseHelper;
	
	public SQLiteSyncRepository(SyncDatabaseHelper syncDatabaseHelper) {
		this.syncDatabaseHelper = syncDatabaseHelper;
	}
	
	private SQLiteDatabase getReadableDatabase() {
		return this.syncDatabaseHelper.getReadableDatabase();
	}
	
	private SQLiteDatabase getWritableDatabase() {
		return this.syncDatabaseHelper.getWritableDatabase();
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
	
	public void applyChangesWithTransaction(List<TableChanges> changes) {
		SQLiteDatabase database = this.getWritableDatabase();
		
		boolean foreignKeysWereEnabled = this.areForeignKeysEnabled(database);
		try {
			if (foreignKeysWereEnabled) {
				this.setForeignKeysEnabled(database, false);
			}
			
			database.beginTransaction();
			try {
				this.applyChangesToDatabase(changes);
				this.saveTableVersions(changes);
				database.setTransactionSuccessful();
			} finally {
				database.endTransaction();
			}
		} finally {
			if (foreignKeysWereEnabled) {
				this.setForeignKeysEnabled(database, true);
			}
			
			database.close();
		}
	}

	protected void applyChangesToDatabase(List<TableChanges> changes) {
		for (TableChanges tableChanges : changes) {
			this.applyChangesToTable(tableChanges);
		}
	}

	protected void applyChangesToTable(TableChanges tableChanges) {
		String tableName = tableChanges.getTableName();

		if (this.logger.isDebugEnabled()) {
			if (tableChanges.hasChanges()) {
				this.logger.info("Applying remote changes to table '" + tableName + "'");
			} else {
				this.logger.info("Table '" + tableName + "' is already synchronized");
			}
		}
		
		for (RowChanges rowChanges : tableChanges.getChanges()) {
			this.executeSyncOperation(tableName, rowChanges);
		}
	}

	protected void executeSyncOperation(String tableName, RowChanges rowChanges) {
		Row row = rowChanges.getRow();
		
		if (row.isEmpty()) {
			logger.warn("Ignoring changes to table '" + tableName + "' because they are empty");
			return;
		}
		
		SQLiteDatabase database = this.getWritableDatabase();
	    try {
	    	TableColumns columns = this.columnsForTable(tableName, database);
	    	
	    	if (!columns.hasSinglePrimaryKey()) {
	    		logger.warn("Ignoring changes to table '" + tableName + "' because it should have one and only one column as primary key");
	    		return;
	    	}
	    	
	    	ContentValuesRowMapper contentValuesRowMapper = new ContentValuesRowMapper(columns);
    		ContentValues values = contentValuesRowMapper.mapRow(row);
    		
    		if (values.size() == 0) {
    			logger.warn("Ignoring changes to table '" + tableName + "' because there is no matching column to sync");
    			return;
    		}
    		
    		String primaryKeyName = columns.getSinglePrimaryKeyName();
    		Object primaryKeyValue = values.get(primaryKeyName);
    		
			long syncKey = rowChanges.getSyncKey();
			SyncOperation syncOperation = rowChanges.getSyncOperation();

			if (syncOperation == SyncOperation.INSERT) {
				this.insertOperation(database, tableName, values);
				this.saveSyncMetadata(database, tableName, primaryKeyValue, syncKey);
				return;
			}
			
			String primaryKeyForSyncKey = this.primaryKeyForSyncKey(database, syncKey, tableName);
			
			if (syncOperation == SyncOperation.UPDATE) {
				if (StringUtils.isEmpty(primaryKeyForSyncKey)) {
					this.insertOperation(database, tableName, values);
				} else {
					this.updateOperation(database, tableName, values, primaryKeyName, primaryKeyForSyncKey);
				}
				this.saveSyncMetadata(database, tableName, primaryKeyValue, syncKey);
			}

			if (syncOperation == SyncOperation.DELETE) {
				if (StringUtils.isNotEmpty(primaryKeyForSyncKey)) {
					this.deleteOperation(database, tableName, primaryKeyName, primaryKeyForSyncKey);
					this.deleteSyncMetadata(database, tableName, syncKey);
				}
			}
			
		} catch (SQLException e) {
			throw new SyncRepositoryException(e);
		}
	}
	
	protected void insertOperation(SQLiteDatabase database, String tableName, ContentValues values) {
		if (this.logger.isDebugEnabled()) {
			logger.debug("insert into '" + tableName + "' values " + "(" + values + ")");
		}

		database.insertOrThrow(tableName, null, values);
	}

	protected void updateOperation(SQLiteDatabase database, String tableName, ContentValues values, String primaryKeyName, String primaryKeyForSyncKey) {
		if (this.logger.isDebugEnabled()) {
			logger.debug("update '" + tableName + "' set values (" + values + ") where '" + primaryKeyName + "' = '" + primaryKeyForSyncKey + "'");
		}

		String whereClause = "'" + primaryKeyName + "' = ?";
		database.update(tableName, values, whereClause, new String[] { primaryKeyForSyncKey });
	}

	protected void deleteOperation(SQLiteDatabase database, String tableName, String primaryKeyName, String primaryKeyForSyncKey) {
		if (this.logger.isDebugEnabled()) {
			logger.debug("delete from '" + tableName + "' where '" + primaryKeyName + "' = '" + primaryKeyForSyncKey + "'");
		}

		String whereClause = "'" + primaryKeyName + "' = ?";
		database.delete(tableName, whereClause, new String[] { primaryKeyForSyncKey });
	}
	
	protected void saveTableVersions(List<TableChanges> tables) {
		SQLiteDatabase database = this.getWritableDatabase();
	    try {
	    	for (TableChanges table : tables) {
	    		ContentValues values = new ContentValues();
	    		
	    		values.put("table_name", table.getTableName());
		    	values.put("version", table.getVersion());
				
				database.replaceOrThrow("appglu_table_versions", null, values);
			}
		} catch (SQLException e) {
			throw new SyncRepositoryException(e);
		}
	}
	
	protected TableColumns columnsForTable(String tableName, SQLiteDatabase database) {
		TableColumns tableColumns = new TableColumns();
		
		Cursor cursor = null;
		
		try {
			cursor = database.rawQuery("PRAGMA table_info (" + tableName + ")", new String[0]);
		    cursor.moveToFirst();
		    
		    for (int i = 0; i < cursor.getCount(); i++) {
		    	Column column = new Column();
		    	
		    	column.setName(cursor.getString(COLUMN_NAME_INDEX));
		    	column.setType(cursor.getString(COLUMN_TYPE_INDEX));
		    	column.setNullable(cursor.getInt(IS_NOT_NULL_INDEX) == 1 ? false : true);
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
	
	protected boolean areForeignKeysEnabled(SQLiteDatabase database) {
		Cursor cursor = null;
		
		try {
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
	
	protected void setForeignKeysEnabled(SQLiteDatabase database, boolean enabled) {
		try {
			database.execSQL("PRAGMA foreign_keys = " + enabled);
		} catch (SQLException e) {
			throw new SyncRepositoryException(e);
		}
	}
	
	protected String primaryKeyForSyncKey(SQLiteDatabase database, long syncKey, String tableName) {
		Cursor cursor = null;
		
		try {
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
	
	protected void saveSyncMetadata(SQLiteDatabase database, String tableName, Object primaryKeyValue, long syncKey) {
		ContentValues metadata = new ContentValues();
		
		metadata.put("sync_key", syncKey);
		metadata.put("table_name", tableName);
		metadata.put("primary_key", String.valueOf(primaryKeyValue));
		
		database.replaceOrThrow("appglu_sync_metadata", null, metadata);
	}
	
	protected void deleteSyncMetadata(SQLiteDatabase database, String tableName, long syncKey) {
		database.delete("appglu_sync_metadata", "sync_key = ? and table_name = ?", new String[] { tableName, String.valueOf(syncKey) });
	}
	
}