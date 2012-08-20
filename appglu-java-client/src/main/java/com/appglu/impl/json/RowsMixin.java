package com.appglu.impl.json;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

public abstract class RowsMixin {
	
	@JsonProperty("rows")
	List<Map<String, Object>> rows;

	@JsonProperty("totalRows")
	Integer totalRows;

}
