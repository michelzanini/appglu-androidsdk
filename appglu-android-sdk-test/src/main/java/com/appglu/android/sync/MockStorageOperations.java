package com.appglu.android.sync;

import com.appglu.AppGluRestClientException;
import com.appglu.StorageFile;
import com.appglu.StorageOperations;
import com.appglu.StorageStreamCallback;

public class MockStorageOperations implements StorageOperations {

	public byte[] downloadStorageFile(StorageFile file) throws AppGluRestClientException {
		return null;
	}

	public void streamStorageFile(StorageFile file, StorageStreamCallback callback) throws AppGluRestClientException {
		
	}

}
