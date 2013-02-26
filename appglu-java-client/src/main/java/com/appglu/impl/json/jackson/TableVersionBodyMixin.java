package com.appglu.impl.json.jackson;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.appglu.TableVersion;

public abstract class TableVersionBodyMixin {

	public TableVersionBodyMixin(@JsonProperty("tables") List<TableVersion> tables) {
		
	}
	
}