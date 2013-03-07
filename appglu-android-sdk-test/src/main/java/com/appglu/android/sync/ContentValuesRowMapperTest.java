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

import java.util.Date;
import java.util.TimeZone;

import junit.framework.Assert;
import android.content.ContentValues;
import android.test.AndroidTestCase;

import com.appglu.Row;
import com.appglu.android.sync.Column;
import com.appglu.android.sync.ContentValuesRowMapper;
import com.appglu.android.sync.TableColumns;
import com.appglu.impl.util.DateUtils;
import com.appglu.impl.util.StringUtils;

public class ContentValuesRowMapperTest extends AndroidTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		/* Setting time zone to GMT+0 avoiding time zone issues in tests */
		TimeZone gmt = TimeZone.getTimeZone("GMT");
		TimeZone.setDefault(gmt);
	}
	
	public void testDatabaseHasDifferentSetOfColumnsThenServer() {
		TableColumns columns = new TableColumns();
		
		columns.put("one", new Column("one", "varchar"));
		columns.put("two", new Column("two", "varchar"));
		columns.put("three", new Column("three", "varchar"));
		
		Row row = new Row();
		
		row.put("one", "valueOne");
		row.put("two", "valueTwo");
		row.put("four", "valueFour");
		
		ContentValuesRowMapper contentValuesRowMapper = new ContentValuesRowMapper(columns);
		ContentValues values = contentValuesRowMapper.mapRow(row);
		
		Assert.assertEquals(2, values.size());
		
		Assert.assertEquals("valueOne", values.get(StringUtils.escapeColumn("one")));
		Assert.assertEquals("valueTwo", values.get(StringUtils.escapeColumn("two")));
		Assert.assertFalse(values.containsKey(StringUtils.escapeColumn("three")));
		Assert.assertFalse(values.containsKey(StringUtils.escapeColumn("four")));
	}
	
	public void testColumnTypes() {
		TableColumns columns = new TableColumns();
		
		columns.put("int", new Column("int", "int"));
		columns.put("integer", new Column("integer", "integer"));
		columns.put("tinyint", new Column("tinyint", "tinyint"));
		columns.put("smallint", new Column("smallint", "smallint"));
		columns.put("mediumint", new Column("mediumint", "mediumint"));
		columns.put("bigint", new Column("bigint", "bigint"));
		
		columns.put("char", new Column("char", "char"));
		columns.put("nchar", new Column("nchar", "nchar"));
		columns.put("character", new Column("character", "character"));
		columns.put("varchar", new Column("varchar", "varchar"));
		columns.put("nvarchar", new Column("nvarchar", "nvarchar"));
		columns.put("text", new Column("text", "text"));
		columns.put("clob", new Column("clob", "clob"));
		
		columns.put("blob", new Column("blob", "blob"));
		
		columns.put("real", new Column("real", "real"));
		columns.put("double", new Column("double", "double"));
		columns.put("float", new Column("float", "float"));
		
		columns.put("numeric", new Column("numeric", "numeric"));
		columns.put("decimal", new Column("decimal", "decimal"));
		
		columns.put("boolean", new Column("boolean", "boolean"));
		columns.put("date", new Column("date", "date"));
		columns.put("datetime", new Column("datetime", "datetime"));
		
		Row row = new Row();
		
		row.put("int", 1);
		row.put("integer", 2);
		row.put("tinyint", 3);
		row.put("smallint", 4);
		row.put("mediumint", 5);
		row.put("bigint", 6);
		
		row.put("char", "char");
		row.put("nchar", "nchar");
		row.put("character", "character");
		row.put("varchar", "varchar");
		row.put("nvarchar", "nvarchar");
		row.put("text", "text");
		row.put("clob", "clob");
		
		row.put("blob", "YmxvYg==");
		
		row.put("real", 1.5);
		row.put("double", 2.5);
		row.put("float", 3.5);
		
		row.put("numeric", 1L);
		row.put("decimal", 2L);
		
		row.put("boolean", true);
		row.put("date", "2010-01-15");
		row.put("datetime", "2010-01-15T12:10:00+0000");
		
		ContentValuesRowMapper contentValuesRowMapper = new ContentValuesRowMapper(columns);
		ContentValues values = contentValuesRowMapper.mapRow(row);
		
		Assert.assertEquals(columns.size(), values.size());
		
		Assert.assertEquals(1, values.get(StringUtils.escapeColumn("int")));
		Assert.assertEquals(2, values.get(StringUtils.escapeColumn("integer")));
		Assert.assertEquals(3, values.get(StringUtils.escapeColumn("tinyint")));
		Assert.assertEquals(4, values.get(StringUtils.escapeColumn("smallint")));
		Assert.assertEquals(5, values.get(StringUtils.escapeColumn("mediumint")));
		Assert.assertEquals(6, values.get(StringUtils.escapeColumn("bigint")));
		
		Assert.assertEquals("char", values.get(StringUtils.escapeColumn("char")));
		Assert.assertEquals("nchar", values.get(StringUtils.escapeColumn("nchar")));
		Assert.assertEquals("character", values.get(StringUtils.escapeColumn("character")));
		Assert.assertEquals("varchar", values.get(StringUtils.escapeColumn("varchar")));
		Assert.assertEquals("nvarchar", values.get(StringUtils.escapeColumn("nvarchar")));
		Assert.assertEquals("text", values.get(StringUtils.escapeColumn("text")));
		Assert.assertEquals("clob", values.get(StringUtils.escapeColumn("clob")));
		
		byte[] byteArray = (byte[]) values.get(StringUtils.escapeColumn("blob"));
		Assert.assertEquals(new String("blob"), new String(byteArray));
		
		Assert.assertEquals(true, values.get(StringUtils.escapeColumn("boolean")));
		
		Long date = (Long) values.get(StringUtils.escapeColumn("date"));
		String dateFormatted = DateUtils.formatDate(new Date(date));
		Assert.assertEquals("2010-01-15", dateFormatted);
		
		Long dateTime = (Long) values.get(StringUtils.escapeColumn("datetime"));
		String dateTimeFormatted = DateUtils.formatDatetime(new Date(dateTime));
		Assert.assertEquals("2010-01-15T12:10:00+0000", dateTimeFormatted);
	}

}
