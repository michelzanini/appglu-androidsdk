package com.appglu;

public interface StorageOperations {

	/**
	 * Download a file from a URL specified in file.getUrl() and gives the stream to inputStreamCallback.doWithInputStream() method.
	 * 
	 * @param file StorageFile containing an URL
	 * @param inputStreamCallback access to the raw InputStream
	 */
	void streamStorageFile(StorageFile file, InputStreamCallback inputStreamCallback) throws AppGluRestClientException;
	
	/**
	 * Will use file.getLastModified() to send in If-Modified-Since header. 
	 * If was not modified since then it will return false and inputStreamCallback.doWithInputStream() will not be called.
	 * 
	 * @param file StorageFile containing an URL and a last modified date
	 * @param inputStreamCallback access to the raw InputStream when the file was modified
	 * @return true if it was modified, false if it was not modified
	 */
	boolean streamStorageFileIfModifiedSince(StorageFile file, InputStreamCallback inputStreamCallback) throws AppGluRestClientException;
	
	/**
	 * Will use file.getETag() to send in If-None-Match header. 
	 * If the eTag matches will return false and inputStreamCallback.doWithInputStream() will not be called.
	 * 
	 * @param file StorageFile containing an URL and a eTag
	 * @param inputStreamCallback access to the raw InputStream when the file was modified
	 * @return true if it was modified, false if it was not modified
	 */
	boolean streamStorageFileIfNoneMatch(StorageFile file, InputStreamCallback inputStreamCallback) throws AppGluRestClientException;
	
}