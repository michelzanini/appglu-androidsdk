package com.appglu;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all the delta changes of a particular table. The changes are new, updated or deleted rows of this table, since the last time the sync has executed.
 * 
 * @since 1.0.0
 */
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