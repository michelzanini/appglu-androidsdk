package com.appglu.impl.json.jackson;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import com.appglu.RowChanges;

@JsonPropertyOrder({"tableName", "version", "changes"})
public abstract class TableChangesMixin {
	
	@JsonProperty("tableName")
	String tableName;
	
	@JsonProperty("version")
	long version;
	
	@JsonProperty("changes")
	List<RowChanges> changes;

}