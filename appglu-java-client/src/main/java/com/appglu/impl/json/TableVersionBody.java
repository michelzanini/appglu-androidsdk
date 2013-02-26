package com.appglu.impl.json;

import java.util.List;

import com.appglu.TableVersion;

public class TableVersionBody {
	
	private List<TableVersion> tables;

	public TableVersionBody(List<TableVersion> tables) {
		super();
		this.tables = tables;
	}

	public List<TableVersion> getTables() {
		return tables;
	}
	
}