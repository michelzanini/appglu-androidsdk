package com.appglu.android;

import com.appglu.AppGluRestClientException;
import com.appglu.AsyncCallback;
import com.appglu.AsyncStorageOperations;
import com.appglu.StorageStreamCallback;
import com.appglu.StorageFile;
import com.appglu.StorageOperations;

public final class StorageApi implements StorageOperations, AsyncStorageOperations {
	
	private StorageOperations storageOperations;
	
	private AsyncStorageOperations asyncStorageOperations;
	
	public StorageApi(StorageOperations storageOperations) {
		this.storageOperations = storageOperations;
	}

	public byte[] downloadStorageFile(StorageFile file) throws AppGluRestClientException {
		return this.storageOperations.downloadStorageFile(file);
	}

	public void streamStorageFile(StorageFile file, StorageStreamCallback callback) throws AppGluRestClientException {
		this.storageOperations.streamStorageFile(file, callback);
	}

	public void downloadStorageFileInBackground(StorageFile file, AsyncCallback<byte[]> downloadCallback) {
		this.asyncStorageOperations.downloadStorageFileInBackground(file, downloadCallback);
	}

	public void streamStorageFileInBackground(StorageFile file, StorageStreamCallback callback, AsyncCallback<Void> downloadCallback) {
		this.asyncStorageOperations.streamStorageFileInBackground(file, callback, downloadCallback);
	}

}