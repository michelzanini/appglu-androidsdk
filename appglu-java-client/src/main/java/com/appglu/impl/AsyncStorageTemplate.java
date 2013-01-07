package com.appglu.impl;

import java.util.concurrent.Callable;

import com.appglu.AsyncCallback;
import com.appglu.AsyncStorageOperations;
import com.appglu.StorageFile;
import com.appglu.StorageOperations;
import com.appglu.StorageStreamCallback;

public final class AsyncStorageTemplate implements AsyncStorageOperations {
	
	private final AsyncExecutor asyncExecutor;
	
	private final StorageOperations storageOperations;
	
	public AsyncStorageTemplate(AsyncExecutor asyncExecutor, StorageOperations storageOperations) {
		this.asyncExecutor = asyncExecutor;
		this.storageOperations = storageOperations;
	}

	public void downloadStorageFileInBackground(final StorageFile file, AsyncCallback<byte[]> downloadCallback) {
		asyncExecutor.execute(downloadCallback, new Callable<byte[]>() {
			public byte[] call() {
				return storageOperations.downloadStorageFile(file);
			}
		});
	}

	public void streamStorageFileInBackground(final StorageFile file, final StorageStreamCallback callback, AsyncCallback<Void> downloadCallback) {
		asyncExecutor.execute(downloadCallback, new Callable<Void>() {
			public Void call() {
				storageOperations.streamStorageFile(file, callback);
				return null;
			}
		});
	}

}