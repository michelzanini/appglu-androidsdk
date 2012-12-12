package com.appglu.impl.json;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"tableName", "version"})
public abstract class TableVersionMixin {
	
	@JsonProperty("tableName")
	String tableName;
	
	@JsonProperty("version")
	long version;

}