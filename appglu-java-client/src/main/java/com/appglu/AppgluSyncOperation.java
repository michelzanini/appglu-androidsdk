package com.appglu;

public enum AppgluSyncOperation {
	
	INSERT,
	DELETE,
	UPDATE;

	public static AppgluSyncOperation getAppgluSyncOperation(String type) {
		for (AppgluSyncOperation operation : values()) {
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