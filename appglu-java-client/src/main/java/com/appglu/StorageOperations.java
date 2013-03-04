package com.appglu;

/**
 * {@code StorageOperations} has methods to download and cache files from AppGlu server.<br><br>
 * 
 * @see AsyncStorageOperations
 * @since 1.0.0
 */
public interface StorageOperations {

	/**
	 * Download a file from a URL specified in file.getUrl() and gives the stream back to {@link InputStreamCallback#doWithInputStream(java.io.InputStream)}.
	 * 
	 * @param file StorageFile containing an URL
	 * @param inputStreamCallback access to the raw InputStream
	 */
	void streamStorageFile(StorageFile file, InputStreamCallback inputStreamCallback) throws AppGluRestClientException;
	
	/**
	 * Will use file.getLastModified() to add a If-Modified-Since header.<br>
	 * If the file was not modified since, then it will return <code>false</code> and {@link InputStreamCallback#doWithInputStream(java.io.InputStream)} will not be called.
	 * 
	 * @param file StorageFile containing an URL and a last modified date
	 * @param inputStreamCallback access to the raw InputStream when the file was modified
	 * @return true if it was modified, false if it was not modified
	 */
	boolean streamStorageFileIfModifiedSince(StorageFile file, InputStreamCallback inputStreamCallback) throws AppGluRestClientException;
	
	/**
	 * Will use file.getETag() to add a If-None-Match header.<br>
	 * If the eTag matches with the server, then it will return <code>false</code> and {@link InputStreamCallback#doWithInputStream(java.io.InputStream)} will not be called.
	 * 
	 * @param file StorageFile containing an URL and a eTag
	 * @param inputStreamCallback access to the raw InputStream when the file was modified
	 * @return true if it was modified, false if it was not modified
	 */
	boolean streamStorageFileIfNoneMatch(StorageFile file, InputStreamCallback inputStreamCallback) throws AppGluRestClientException;
	
}