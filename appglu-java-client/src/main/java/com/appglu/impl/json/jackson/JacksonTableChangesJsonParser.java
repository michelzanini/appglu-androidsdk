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

public class JacksonTableChangesJsonParser implements TableChangesJsonParser {

	private ObjectMapper objectMapper;
	
	public JacksonTableChangesJsonParser(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public void parseTableChanges(InputStream inputStream, TableChangesCallback tableChangesCallback) throws IOException {
		JsonFactory jsonFactory = objectMapper.getJsonFactory();
		JsonParser jsonParser = jsonFactory.createJsonParser(inputStream);
		
		this.doParse(jsonParser, tableChangesCallback);
	}
	
	private void doParse(JsonParser jsonParser, TableChangesCallback tableChangesCallback) throws IOException {
		jsonParser.nextToken();
		
		if (jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
			throw new JsonParseException("Json must start with {", jsonParser.getCurrentLocation());
		}
		
		jsonParser.nextToken();
		
		if (!"tables".equals(jsonParser.getCurrentName())) {
			throw new JsonParseException("tables property expected", jsonParser.getCurrentLocation());
		}
		
		jsonParser.nextToken();
		
		if (jsonParser.getCurrentToken() != JsonToken.START_ARRAY) {
			throw new JsonParseException("Start array char [ expected", jsonParser.getCurrentLocation());
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
		
		jsonParser.nextToken();
		if (!"tableName".equals(jsonParser.getCurrentName())) {
			throw new JsonParseException("tableName property expected", jsonParser.getCurrentLocation());
		}
		tableVersion.setTableName(jsonParser.nextTextValue());
		
		jsonParser.nextToken();
		if (!"version".equals(jsonParser.getCurrentName())) {
			throw new JsonParseException("version property expected", jsonParser.getCurrentLocation());
		}
		tableVersion.setVersion(jsonParser.nextIntValue(0));
		
		jsonParser.nextToken();
		if (!"changes".equals(jsonParser.getCurrentName())) {
			throw new JsonParseException("changes property expected", jsonParser.getCurrentLocation());
		}
		this.parseRowChanges(jsonParser, tableVersion, tableChangesCallback);
	}

	private void parseRowChanges(JsonParser jsonParser, TableVersion tableVersion, TableChangesCallback tableChangesCallback) throws IOException {
		jsonParser.nextToken();
		
		if (jsonParser.getCurrentToken() != JsonToken.START_ARRAY) {
			throw new JsonParseException("End array char ] expected", jsonParser.getCurrentLocation());
		}
		
		jsonParser.nextToken();
		
		if (jsonParser.getCurrentToken() == JsonToken.END_ARRAY) {
			tableChangesCallback.doWithTableVersion(tableVersion, false);
		} else {
			tableChangesCallback.doWithTableVersion(tableVersion, true);
		}
		
		while (jsonParser.getCurrentToken() != JsonToken.END_ARRAY) {
			
			if (jsonParser.getCurrentToken() == JsonToken.START_OBJECT) {
				RowChanges rowChanges = (RowChanges) jsonParser.readValueAs(RowChanges.class);
				tableChangesCallback.doWithRowChanges(tableVersion, rowChanges);
			}
			
			jsonParser.nextToken();
		}
	}

}
