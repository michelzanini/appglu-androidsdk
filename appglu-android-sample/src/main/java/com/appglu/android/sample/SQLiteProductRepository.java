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

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Data Access Object to load {@link Product} objects from SQLite
 */
public class SQLiteProductRepository {
	
	private ProductsDatabaseHelper databaseHelper;

	public SQLiteProductRepository(ProductsDatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}
	
	public List<Product> loadAllProducts() {
		List<Product> products = new ArrayList<Product>();
		
		SQLiteDatabase database = this.databaseHelper.getReadableDatabase();
		
		Cursor cursor = null;
		
		try {
			String sql = "select id, title, brief_description, long_description, price, buy_url, image_name, category_id from product";
			
			cursor = database.rawQuery(sql, new String[0]);
		    cursor.moveToFirst();
		    
		    for (int i = 0; i < cursor.getCount(); i++) {
		    	Product product = new Product();
		    	
		    	product.setId(cursor.getInt(0));
		    	product.setTitle(cursor.getString(1));
		    	product.setBriefDescription(cursor.getString(2));
		    	product.setLongDescription(cursor.getString(3));
		    	product.setPrice(cursor.getDouble(4));
		    	product.setBuyUrl(cursor.getString(5));
		    	product.setImageName(cursor.getString(6));
		    	product.setCategoryId(cursor.getInt(7));
		    	
		    	products.add(product);
		    	
		    	cursor.moveToNext();
		    }
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			database.close();
		}
		
		return products;
	}
	
}
