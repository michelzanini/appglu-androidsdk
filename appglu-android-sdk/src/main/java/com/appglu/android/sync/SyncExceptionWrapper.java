package com.appglu.android.sync;

import java.io.Serializable;

import com.appglu.AppGluHttpClientException;
import com.appglu.AppGluHttpServerException;
import com.appglu.AppGluHttpStatusCodeException;
import com.appglu.AppGluRestClientException;

public class SyncExceptionWrapper implements Serializable {
	
	public enum SyncErrorCode {
		UNKNOWN_ERROR,
		NETWORK_ERROR,
		DATABASE_ACCESS_ERROR,
		FILE_STORAGE_ERROR;
	}
	
	private static final long serialVersionUID = 1L;

	private Exception exception;

	public SyncExceptionWrapper(Exception exception) {
		this.exception = exception;
	}
	
	public Exception getException() {
		return exception;
	}
	
	public SyncErrorCode getErrorCode() {
		if (this.isRestClientException()) {
			return SyncErrorCode.NETWORK_ERROR;
		}
		
		if (this.isSyncRepositoryException()) {
			return SyncErrorCode.DATABASE_ACCESS_ERROR;
		} 
		
		if (this.isSyncFileStorageException()) {
			return SyncErrorCode.FILE_STORAGE_ERROR;
		} 
			
		return SyncErrorCode.UNKNOWN_ERROR;
	}
	
	public boolean isSyncFileStorageException() {
		return exception instanceof SyncFileStorageException;
	}
	
	public boolean isSyncRepositoryException() {
		return exception instanceof SyncRepositoryException;
	}
	
	public boolean isRestClientException() {
		return exception instanceof AppGluRestClientException;
	}
	
	public boolean isHttpStatusCodeException() {
		return exception instanceof AppGluHttpStatusCodeException;
	}
	
	public boolean isHttpClientException() {
		return exception instanceof AppGluHttpClientException;
	}
	
	public boolean isHttpServerException() {
		return exception instanceof AppGluHttpServerException;
	}
	
	public SyncFileStorageException getSyncFileStorageException() {
		return (SyncFileStorageException) this.exception;
	}
	
	public SyncRepositoryException getSyncRepositoryException() {
		return (SyncRepositoryException) this.exception;
	}
	
	public AppGluRestClientException getRestClientException() {
		return (AppGluRestClientException) this.exception;
	}
	
	public AppGluHttpStatusCodeException getHttpStatusCodeException() {
		return (AppGluHttpStatusCodeException) this.exception;
	}
	
	public AppGluHttpClientException getHttpClientException() {
		return (AppGluHttpClientException) this.exception;
	}
	
	public AppGluHttpServerException getHttpServerException() {
		return (AppGluHttpServerException) this.exception;
	}

}