package com.appglu.impl.json;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.appglu.TableChanges;

public abstract class TableChangesBodyMixin {
	
	public TableChangesBodyMixin(@JsonProperty("tables") List<TableChanges> tables) {
		
	}

}
