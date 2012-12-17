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
		super();
		this.syncDatabaseHelper = syncDatabaseHelper;
	}
	
	private SQLiteDatabase getReadableDatabase() {
		return this.syncDatabaseHelper.getReadableDatabase();
	}
	
	private SQLiteDatabase getWritableDatabase() {
		return this.syncDatabaseHelper.getWritableDatabase();
	}
	
	public void beginTransaction() {
		this.getWritableDatabase().beginTransaction();
	}

	public void setTransactionSuccessful() {
		this.getWritableDatabase().setTransactionSuccessful();
	}
	
	public void endTransaction() {
		SQLiteDatabase database = this.getWritableDatabase();
		database.endTransaction();
		database.close();
	}

	public List<TableVersion> versionsForAllTables() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT m.name, t.version FROM sqlite_master m ");
		sql.append("LEFT OUTER JOIN appglu_table_versions t ON m.name = t.table_name ");
		sql.append("WHERE m.type = 'table' and m.name not in ('appglu_table_versions') and m.name not like 'android_%' and m.name not like 'sqlite_%' ORDER BY m.name;");
		
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
	
	public void saveTableVersions(List<TableChanges> tables) {
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
	
	public void executeSyncOperation(String tableName, RowChanges rowChanges) {
		Row row = rowChanges.getRow();
		
		if (row.isEmpty()) {
			logger.warn("Ignoring changes to table " + tableName + " because they are empty");
			return;
		}
		
		SQLiteDatabase database = this.getWritableDatabase();
	    try {
	    	TableColumns columns = this.columnsForTable(tableName, database);
	    	
	    	if (!columns.hasSinglePrimaryKey()) {
	    		logger.warn("Ignoring changes to table " + tableName + " because it should have one and only one column as primary key");
	    		return;
	    	}
	    	
	    	ContentValuesRowMapper contentValuesRowMapper = new ContentValuesRowMapper(columns);
    		ContentValues values = contentValuesRowMapper.mapRow(row);
    		
    		if (values.size() == 0) {
    			logger.warn("Ignoring changes to table " + tableName + " because there is no matching column to sync");
    			return;
    		}
    		
			String primaryKeyName = columns.getSinglePrimaryKeyName();
			Object primaryKeyValue = values.get(primaryKeyName);

			String whereClause = "'" + primaryKeyName + "' = ?";
			String whereArg = String.valueOf(primaryKeyValue);

			SyncOperation syncOperation = rowChanges.getSyncOperation();

			if (syncOperation == SyncOperation.INSERT) {
				if (this.logger.isDebugEnabled()) {
					logger.debug("insert into '" + tableName + "' values " + "(" + values + ")");
				}

				database.insertOrThrow(tableName, null, values);
			}

			if (syncOperation == SyncOperation.UPDATE) {
				if (this.logger.isDebugEnabled()) {
					logger.debug("update '" + tableName + "' set values (" + values + ") where '" + primaryKeyName + "' = '" + whereArg + "'");
				}

				database.update(tableName, values, whereClause, new String[] { whereArg });
			}

			if (syncOperation == SyncOperation.DELETE) {
				if (this.logger.isDebugEnabled()) {
					logger.debug("delete from '" + tableName + "' where '" + primaryKeyName + "' = '" + whereArg + "'");
				}

				database.delete(tableName, whereClause, new String[] { whereArg });
			}
			
		} catch (SQLException e) {
			throw new SyncRepositoryException(e);
		}
	}
	
	protected TableColumns columnsForTable(String tableName) {
		SQLiteDatabase database = this.getReadableDatabase();
		try {
			return this.columnsForTable(tableName, database);
		} finally {
			database.close();
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
	
}