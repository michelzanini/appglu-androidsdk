package com.appglu.impl.json;

import org.codehaus.jackson.annotate.JsonProperty;

import com.appglu.Tuple;

public abstract class RowBodyMixin {
	
	public RowBodyMixin(@JsonProperty("row") Tuple row) {
		
	}
	
}
