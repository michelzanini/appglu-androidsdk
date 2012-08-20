package com.appglu.impl.json;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public abstract class RowMixin {
	
	@JsonProperty("row")
	Map<String, Object> rowColumns;
	
	@JsonIgnore
	abstract boolean isEmpty();
	
}
