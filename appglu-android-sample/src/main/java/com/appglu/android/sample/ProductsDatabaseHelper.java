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
package com.appglu.android.sample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.appglu.android.sync.SyncDatabaseHelper;

/**
 * Creates a local SQLite database to be used when synchronizing data from the AppGlu server. 
 */
public class ProductsDatabaseHelper extends SyncDatabaseHelper {

	public ProductsDatabaseHelper(Context context) {
		super(context, "products.sqlite", 1);
	}

	@Override
	public void onCreateAppDatabase(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE product (" +
				  "id int(11) NOT NULL, " + 
				  "title varchar, " +
				  "brief_description varchar, " +
				  "long_description varchar, " +
				  "price double, " +
				  "buy_url varchar, " +
				  "image_name varchar, " +
				  "category_id int(11), " +
				  "PRIMARY KEY (id))");
	}

	@Override
	public void onUpgradeAppDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}