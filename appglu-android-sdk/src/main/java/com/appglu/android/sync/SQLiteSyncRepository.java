package com.appglu.android.sync;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.appglu.RowChanges;
import com.appglu.TableVersion;
import com.appglu.TableChanges;
import com.appglu.impl.util.StringUtils;

public class SQLiteSyncRepository implements SyncRepository {
	
	private static final int TABLE_NAME_COLUMN = 0;
	private static final int VERSION_COLUMN = 1;
	
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
		sql.append("WHERE m.type = 'table' and m.name not in ('appglu_table_versions', 'android_metadata', 'sqlite_sequence') ORDER BY m.name;");
		
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
	
	private List<TableVersion> queryForTableVersions(String sql, String[] selectionArgs) {
		List<TableVersion> tables = new ArrayList<TableVersion>();
		
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = null;
		
		try {
			cursor = database.rawQuery(sql, selectionArgs);
		    cursor.moveToFirst();
		    
		    for (int i = 0; i < cursor.getCount(); i++) {
		    	TableVersion table = new TableVersion();
		    	
		    	table.setTableName(cursor.getString(TABLE_NAME_COLUMN));
		    	table.setVersion(cursor.getLong(VERSION_COLUMN));
		    	
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

	public void applyRowChangesToTable(String tableName, RowChanges row) {
		
	}
	
}