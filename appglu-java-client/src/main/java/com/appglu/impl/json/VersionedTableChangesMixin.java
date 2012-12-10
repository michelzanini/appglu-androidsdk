package com.appglu.impl.json;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import com.appglu.VersionedRow;

@JsonPropertyOrder({"tableName", "version", "changes"})
public abstract class VersionedTableChangesMixin {
	
	@JsonProperty("tableName")
	String tableName;
	
	@JsonProperty("version")
	long version;
	
	@JsonProperty("changes")
	List<VersionedRow> changes;

}