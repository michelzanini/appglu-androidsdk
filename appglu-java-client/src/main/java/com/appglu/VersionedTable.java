package com.appglu;

public class VersionedTable {
	
	private String tableName;
	
	private long version;
	
	public VersionedTable() {
		
	}
	
	public VersionedTable(String tableName) {
		this.tableName = tableName;
	}

	public VersionedTable(String tableName, long version) {
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

	@Override
	public String toString() {
		return "VersionedTable [tableName=" + tableName + ", version=" + version + "]";
	}

}