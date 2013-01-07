package com.appglu;

public interface AsyncStorageOperations {
	
	void downloadStorageFileInBackground(StorageFile file, AsyncCallback<byte[]> downloadCallback);
	
	void streamStorageFileInBackground(StorageFile file, StorageStreamCallback callback, AsyncCallback<Void> downloadCallback);

}