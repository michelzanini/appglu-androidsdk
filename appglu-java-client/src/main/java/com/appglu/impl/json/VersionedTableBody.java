package com.appglu.impl.json;

import java.util.List;

import com.appglu.VersionedTable;

public class VersionedTableBody {
	
	private List<VersionedTable> tables;

	public VersionedTableBody(List<VersionedTable> tables) {
		super();
		this.tables = tables;
	}

	public List<VersionedTable> getTables() {
		return tables;
	}
	
}