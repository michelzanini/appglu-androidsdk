package com.appglu.impl.json;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.appglu.VersionedTable;

public abstract class VersionedTableBodyMixin {

	public VersionedTableBodyMixin(@JsonProperty("tables") List<VersionedTable> tables) {
		
	}
	
}