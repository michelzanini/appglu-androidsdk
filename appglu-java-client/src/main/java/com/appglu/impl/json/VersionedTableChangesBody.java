package com.appglu.impl.json;

import java.util.List;

import com.appglu.VersionedTableChanges;

public class VersionedTableChangesBody {
	
	private List<VersionedTableChanges> tables;

	public VersionedTableChangesBody(List<VersionedTableChanges> tables) {
		super();
		this.tables = tables;
	}

	public List<VersionedTableChanges> getTables() {
		return tables;
	}

}
