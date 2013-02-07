package com.appglu;

public interface AsyncStorageOperations {
	
	void streamStorageFileInBackground(StorageFile file, InputStreamCallback inputStreamCallback, AsyncCallback<Void> downloadCallback);
	
	void streamStorageFileIfModifiedSinceInBackground(StorageFile file, InputStreamCallback inputStreamCallback, AsyncCallback<Boolean> downloadCallback);
	
	void streamStorageFileIfNoneMatchInBackground(StorageFile file, InputStreamCallback inputStreamCallback, AsyncCallback<Boolean> downloadCallback);

}