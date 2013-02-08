package com.appglu.android.sync;

import com.appglu.AppGluRestClientException;
import com.appglu.StorageFile;
import com.appglu.StorageOperations;
import com.appglu.InputStreamCallback;

public class MockStorageOperations implements StorageOperations {

	public void streamStorageFile(StorageFile file, InputStreamCallback callback) throws AppGluRestClientException {
		
	}

	public boolean streamStorageFileIfModifiedSince(StorageFile file, InputStreamCallback inputStreamCallback) throws AppGluRestClientException {
		return false;
	}

	public boolean streamStorageFileIfNoneMatch(StorageFile file, InputStreamCallback inputStreamCallback) throws AppGluRestClientException {
		return false;
	}

}
