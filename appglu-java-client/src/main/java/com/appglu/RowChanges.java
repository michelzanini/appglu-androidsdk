package com.appglu;

public class RowChanges {
	
	private Row row = new Row();
	
	private long syncKey;
	
	private SyncOperation syncOperation;

	public Row getRow() {
		return row;
	}

	public long getSyncKey() {
		return syncKey;
	}

	public SyncOperation getSyncOperation() {
		return syncOperation;
	}

	public void setRow(Row row) {
		this.row = row;
	}

	public void setSyncKey(long appgluKey) {
		this.syncKey = appgluKey;
	}

	public void setSyncOperation(SyncOperation appgluSyncOperation) {
		this.syncOperation = appgluSyncOperation;
	}
	
	public void addRowProperty(String key, Object value) {
		row.put(key, value);
	}

	@Override
	public String toString() {
		return "RowChanges [syncKey=" + syncKey + ", syncOperation=" + syncOperation + "]";
	}

}