package com.appglu;

/**
 * TODO
 */
public enum SyncOperation {
	
	INSERT,
	DELETE,
	UPDATE;

	public static SyncOperation getSyncOperation(String type) {
		for (SyncOperation operation : values()) {
			if (operation.toString().equalsIgnoreCase(type)) {
				return operation;
			}
		}
		return null;
	}
	
	public boolean isInsert() {
		return this == INSERT;
	}
	
	public boolean isDelete() {
		return this == DELETE;
	}
	
	public boolean isUpdate() {
		return this == UPDATE;
	}
	
	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}

}