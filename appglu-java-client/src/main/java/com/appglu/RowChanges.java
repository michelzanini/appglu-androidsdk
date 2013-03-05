package com.appglu;

/**
 * {@code RowChanges} contains the {@link SyncOperation} to be applied for this {@link Row}.<br>
 * If the operation is {@link SyncOperation#INSERT} or {@link SyncOperation#UPDATE} then {@link #getRow()} will contain the data to be used for that insert or update.
 */
public class RowChanges {
	
	private Row row;
	
	private long syncKey;
	
	private SyncOperation syncOperation;

	public Row getRow() {
		if (row == null) {
			row = new Row();
		}
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
		getRow().put(key, value);
	}

	@Override
	public String toString() {
		return "RowChanges [syncKey=" + syncKey + ", syncOperation=" + syncOperation + "]";
	}

}