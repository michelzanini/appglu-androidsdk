package com.appglu.impl.json;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.appglu.VersionedTableChanges;

public abstract class VersionedTableChangesBodyMixin {
	
	public VersionedTableChangesBodyMixin(@JsonProperty("tables") List<VersionedTableChanges> tables) {
		
	}

}
