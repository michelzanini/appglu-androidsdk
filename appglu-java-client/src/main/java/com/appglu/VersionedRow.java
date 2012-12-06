package com.appglu;

public class VersionedRow {
	
	private Row row = new Row();
	
	private long appgluKey;
	
	private AppgluSyncOperation appgluSyncOperation;

	public Row getRow() {
		return row;
	}

	public long getAppgluKey() {
		return appgluKey;
	}

	public AppgluSyncOperation getAppgluSyncOperation() {
		return appgluSyncOperation;
	}

	public void setRow(Row row) {
		this.row = row;
	}

	public void setAppgluKey(long appgluKey) {
		this.appgluKey = appgluKey;
	}

	public void setAppgluSyncOperation(AppgluSyncOperation appgluSyncOperation) {
		this.appgluSyncOperation = appgluSyncOperation;
	}
	
	public void addRowProperty(String key, Object value) {
		row.put(key, value);
	}

	@Override
	public String toString() {
		return "VersionedRow [appgluKey=" + appgluKey + ", appgluSyncOperation=" + appgluSyncOperation + "]";
	}

}