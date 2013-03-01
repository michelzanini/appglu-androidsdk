package com.appglu.impl;

import java.util.concurrent.Callable;

import com.appglu.AsyncCallback;
import com.appglu.AsyncStorageOperations;
import com.appglu.StorageFile;
import com.appglu.StorageOperations;
import com.appglu.InputStreamCallback;

public final class AsyncStorageTemplate implements AsyncStorageOperations {
	
	private final AsyncExecutor asyncExecutor;
	
	private final StorageOperations storageOperations;
	
	public AsyncStorageTemplate(AsyncExecutor asyncExecutor, StorageOperations storageOperations) {
		this.asyncExecutor = asyncExecutor;
		this.storageOperations = storageOperations;
	}

	/**
	 * {@inheritDoc}
	 */
	public void streamStorageFileInBackground(final StorageFile file, final InputStreamCallback inputStreamCallback, AsyncCallback<Void> downloadCallback) {
		asyncExecutor.execute(downloadCallback, new Callable<Void>() {
			public Void call() {
				storageOperations.streamStorageFile(file, inputStreamCallback);
				return null;
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void streamStorageFileIfModifiedSinceInBackground(final StorageFile file, final InputStreamCallback inputStreamCallback, AsyncCallback<Boolean> downloadCallback) {
		asyncExecutor.execute(downloadCallback, new Callable<Boolean>() {
			public Boolean call() {
				return storageOperations.streamStorageFileIfModifiedSince(file, inputStreamCallback);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void streamStorageFileIfNoneMatchInBackground(final StorageFile file, final InputStreamCallback inputStreamCallback, AsyncCallback<Boolean> downloadCallback) {
		asyncExecutor.execute(downloadCallback, new Callable<Boolean>() {
			public Boolean call() {
				return storageOperations.streamStorageFileIfNoneMatch(file, inputStreamCallback);
			}
		});
	}

}