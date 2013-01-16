package com.appglu.android.sync;

@SuppressWarnings("serial")
public class SyncFileStorageException extends RuntimeException {

	public SyncFileStorageException() {
		super();
	}
	
	public SyncFileStorageException(String msg) {
		super(msg);
	}

	public SyncFileStorageException(Throwable cause) {
		super(cause);
	}
	
}