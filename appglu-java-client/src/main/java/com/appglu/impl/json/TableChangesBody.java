package com.appglu.impl.json;

import java.util.List;

import com.appglu.TableChanges;

public class TableChangesBody {
	
	private List<TableChanges> tables;

	public TableChangesBody(List<TableChanges> tables) {
		super();
		this.tables = tables;
	}

	public List<TableChanges> getTables() {
		return tables;
	}

}
