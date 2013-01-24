package com.appglu;

public interface StorageOperations {

	byte[] downloadStorageFile(StorageFile file) throws AppGluRestClientException;
	
	void streamStorageFile(StorageFile file, InputStreamCallback inputStreamCallback) throws AppGluRestClientException;
	
}