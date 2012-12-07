package com.appglu;

import java.util.List;

public class VersionedTableChanges {
	
	private String tableName;
	
	private long version;
	
	private List<VersionedRow> changes;

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
	
	public List<VersionedRow> getChanges() {
		return changes;
	}
	
	public void setChanges(List<VersionedRow> changes) {
		this.changes = changes;
	}

	@Override
	public String toString() {
		return "VersionedTableChanges [tableName=" + tableName + ", version=" + version + "]";
	}
	
}