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
