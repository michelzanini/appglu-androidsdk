/*******************************************************************************
 * Copyright 2013 AppGlu, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.appglu.android.sync;

import java.io.Serializable;

import com.appglu.AppGluHttpClientException;
import com.appglu.AppGluHttpServerException;
import com.appglu.AppGluHttpStatusCodeException;
import com.appglu.AppGluRestClientException;

/**
 * Wraps an exception thrown by the {@link SyncApi} to provide a easy way of extracting error code and error messages.
 * 
 * @since 1.0.0
 */
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
