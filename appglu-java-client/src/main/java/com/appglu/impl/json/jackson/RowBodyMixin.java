package com.appglu.impl.json.jackson;

import org.codehaus.jackson.annotate.JsonProperty;

import com.appglu.Row;

public abstract class RowBodyMixin {
	
	public RowBodyMixin(@JsonProperty("row") Row row) {
		
	}
	
}