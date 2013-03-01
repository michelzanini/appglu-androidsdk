package com.appglu;

/**
 * TODO
 */
public class TableVersion {
	
	private String tableName;
	
	private long version;
	
	public TableVersion() {
		
	}
	
	public TableVersion(String tableName) {
		this.tableName = tableName;
	}

	public TableVersion(String tableName, long version) {
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
		return "[tableName=" + tableName + ", version=" + version + "]";
	}

}