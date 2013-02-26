package com.appglu.android.sync;

import java.util.HashMap;
import java.util.Map;

import com.appglu.android.sync.SyncDatabaseHelper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

public abstract class AbstractDatabaseHelperTest extends AndroidTestCase {
	
	protected SyncDatabaseHelper syncDatabaseHelper;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		this.syncDatabaseHelper = new SyncDatabaseHelper(getContext(), this.getDatabaseName(), getDatabaseVersion(), enableForeignKeys()) {

			public void onCreateAppDatabase(SQLiteDatabase db) {
				
			}

			public void onUpgradeAppDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
				
			}
			
		};
		
		this.setUpScript();
	}

	@Override
	protected void tearDown() throws Exception {
		this.syncDatabaseHelper.close();
	}
	
	protected abstract String getDatabaseName();
	
	protected abstract int getDatabaseVersion();
	
	protected abstract boolean enableForeignKeys();
	
	protected abstract void setUpScript();
	
	protected int countTable(String tableName) {
		Cursor cursor = null;
		
		try {
			SQLiteDatabase database = this.syncDatabaseHelper.getReadableDatabase();
			cursor = database.rawQuery("select count(*) from " + tableName, new String[0]);
		    cursor.moveToFirst();
		    
		    if (cursor.getCount() > 0) {
		    	return cursor.getInt(0); 
		    }
		    
		    return 0;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	protected Map<String, String> queryForMap(String query, String[] selectionArgs) {
		Map<String, String> queryResult = new HashMap<String, String>();
		
		Cursor cursor = null;
		
		try {
			SQLiteDatabase database = this.syncDatabaseHelper.getReadableDatabase();
			cursor = database.rawQuery(query, selectionArgs);
			
		    cursor.moveToFirst();
		    
		    if (cursor.getCount() > 0) {
		    	int columnCount = cursor.getColumnCount();
		    	
		    	for (int i = 0; i < columnCount; i++) {
		    		String key = cursor.getColumnName(i);
			    	String value = cursor.getString(i);
			    	
			    	queryResult.put(key, value);
		    	}
		    }
		    
		    return queryResult;
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

}
