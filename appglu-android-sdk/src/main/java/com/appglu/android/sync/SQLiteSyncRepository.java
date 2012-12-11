package com.appglu.android.sync;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.appglu.VersionedRow;
import com.appglu.VersionedTable;
import com.appglu.VersionedTableChanges;

public class SQLiteSyncRepository implements SyncRepository {
	
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

	public List<VersionedTable> listTables() {
		List<VersionedTable> tables = new ArrayList<VersionedTable>();
		
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor = null;
		
		try {
			StringBuilder sql = new StringBuilder();
			
			sql.append("SELECT m.name, t.version FROM sqlite_master m ");
			sql.append("LEFT OUTER JOIN appglu_versioned_tables t ON m.name = t.table_name ");
			sql.append("WHERE m.type = 'table' and m.name not in ('appglu_versioned_tables', 'android_metadata', 'sqlite_sequence') ORDER BY m.name;");
			
			cursor = database.rawQuery(sql.toString(), new String[0]);
		    cursor.moveToFirst();
		    
		    for (int i = 0; i < cursor.getCount(); i++) {
		    	VersionedTable table = new VersionedTable();
		    	
		    	table.setTableName(cursor.getString(0));
		    	table.setVersion(cursor.getLong(1));
		    	
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
	
	public void updateLocalTableVersions(List<VersionedTableChanges> tables) {
		SQLiteDatabase database = this.getWritableDatabase();
	    try {
	    	for (VersionedTableChanges versionedTable : tables) {
	    		ContentValues values = new ContentValues();
	    		
	    		values.put("table_name", versionedTable.getTableName());
		    	values.put("version", versionedTable.getVersion());
				
				database.replaceOrThrow("appglu_versioned_tables", null, values);
			}
		} catch (SQLException e) {
			throw new SyncRepositoryException(e);
		}
	}

	public void processChangesToTable(String tableName, VersionedRow row) {
		
	}
	
}