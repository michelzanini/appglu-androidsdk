package com.appglu;

import java.util.ArrayList;
import java.util.List;

public class TableChanges {
	
	private String tableName;
	
	private long version;
	
	private List<RowChanges> changes = new ArrayList<RowChanges>();
	
	public TableChanges() {
		
	}
	
	public TableChanges(String tableName) {
		this.tableName = tableName;
	}

	public TableChanges(String tableName, long version) {
		this.tableName = tableName;
		this.version = version;
	}

	public String getTableName() {
		return tableName;
	}

	public long getVersion() {
		return version;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setVersion(long version) {
		this.version = version;
	}
	
	public List<RowChanges> getChanges() {
		return changes;
	}
	
	public void setChanges(List<RowChanges> changes) {
		this.changes = changes;
	}
	
	public boolean hasChanges() {
		return !this.changes.isEmpty();
	}

	@Override
	public String toString() {
		return "[tableName=" + tableName + ", version=" + version + "]";
	}
	
}