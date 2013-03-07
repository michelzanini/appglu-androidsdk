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
package com.appglu.impl.json.jackson;

import java.io.IOException;
import java.io.InputStream;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;

import com.appglu.RowChanges;
import com.appglu.TableChangesCallback;
import com.appglu.TableVersion;
import com.appglu.impl.json.TableChangesJsonParser;
import com.appglu.impl.util.IOUtils;
import com.appglu.impl.util.StringUtils;

public class JacksonTableChangesJsonParser implements TableChangesJsonParser {

	private ObjectMapper objectMapper;
	
	public JacksonTableChangesJsonParser(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public void parseTableChanges(InputStream inputStream, TableChangesCallback tableChangesCallback) throws IOException {
		JsonFactory jsonFactory = objectMapper.getJsonFactory();
		JsonParser jsonParser = jsonFactory.createJsonParser(inputStream);
		
		try {
			this.doParse(jsonParser, tableChangesCallback);
		} finally {
			jsonParser.close();
			IOUtils.closeQuietly(inputStream);
		}
	}
	
	private void doParse(JsonParser jsonParser, TableChangesCallback tableChangesCallback) throws IOException {
		jsonParser.nextToken();
		
		if (jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
			throw new JsonParseException("Start object { expected", jsonParser.getCurrentLocation());
		}
		
		jsonParser.nextToken();
		
		if (!"tables".equals(jsonParser.getCurrentName())) {
			throw new JsonParseException("Field name 'tables' expected", jsonParser.getCurrentLocation());
		}
		
		jsonParser.nextToken();
		
		if (jsonParser.getCurrentToken() != JsonToken.START_ARRAY) {
			throw new JsonParseException("Start array [ expected", jsonParser.getCurrentLocation());
		}
		
		jsonParser.nextToken();
		
		while (jsonParser.getCurrentToken() != JsonToken.END_ARRAY) {
			
			if (jsonParser.getCurrentToken() == JsonToken.START_OBJECT) {
				this.parseTableChanges(jsonParser, tableChangesCallback);
			}
			
			jsonParser.nextToken();
		}
	}
	
	private void parseTableChanges(JsonParser jsonParser, TableChangesCallback tableChangesCallback) throws IOException {
		TableVersion tableVersion = new TableVersion();
		
		boolean versionWasReceived = false;
		
		jsonParser.nextToken();
		
		while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
			
			if ("tableName".equals(jsonParser.getCurrentName())) {
				
				tableVersion.setTableName(jsonParser.nextTextValue());
				
			} else if ("version".equals(jsonParser.getCurrentName())) {
				
				tableVersion.setVersion(jsonParser.nextIntValue(0));
				versionWasReceived = true;
				
			} else if ("changes".equals(jsonParser.getCurrentName())) {
				
				if (StringUtils.isEmpty(tableVersion.getTableName())) {
					throw new JsonParseException("Field 'tableName' is expected before field 'changes'", jsonParser.getCurrentLocation());
				}
				
				if (!versionWasReceived) {
					throw new JsonParseException("Field 'version' is expected before field 'changes'", jsonParser.getCurrentLocation());
				}
				
				this.parseRowChanges(jsonParser, tableVersion, tableChangesCallback);
				
			} else {
				//skip unknown property
				jsonParser.nextToken();
				
				//if it is an array or object, them skip its children
				jsonParser.skipChildren();
			}
			
			jsonParser.nextToken();
		}
	}

	private void parseRowChanges(JsonParser jsonParser, TableVersion tableVersion, TableChangesCallback tableChangesCallback) throws IOException {
		jsonParser.nextToken();
		
		if (jsonParser.getCurrentToken() != JsonToken.START_ARRAY) {
			throw new JsonParseException("Start array [ expected", jsonParser.getCurrentLocation());
		}
		
		jsonParser.nextToken();
		
		if (jsonParser.getCurrentToken() == JsonToken.END_ARRAY) {
			tableChangesCallback.doWithTableVersion(tableVersion, false);
			return;
		}
		
		boolean processRowChangesForTable = tableChangesCallback.doWithTableVersion(tableVersion, true);
		
		while (jsonParser.getCurrentToken() != JsonToken.END_ARRAY) {
			
			if (jsonParser.getCurrentToken() == JsonToken.START_OBJECT) {
				if (processRowChangesForTable) {
					RowChanges rowChanges = (RowChanges) jsonParser.readValueAs(RowChanges.class);
					tableChangesCallback.doWithRowChanges(tableVersion, rowChanges);
				} else {
					jsonParser.skipChildren();
				}
			}
			
			jsonParser.nextToken();
		}
	}

}
