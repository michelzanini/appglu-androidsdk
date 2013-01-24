package com.appglu;

public interface AsyncStorageOperations {
	
	void downloadStorageFileInBackground(StorageFile file, AsyncCallback<byte[]> downloadCallback);
	
	void streamStorageFileInBackground(StorageFile file, InputStreamCallback inputStreamCallback, AsyncCallback<Void> downloadCallback);

}