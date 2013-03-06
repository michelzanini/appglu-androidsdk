package com.appglu;

/**
 * {@code AsyncStorageOperations} has all methods that {@link StorageOperations} has but they execute <strong>asynchronously</strong>. 
 * @see StorageOperations
 * @since 1.0.0
 */
public interface AsyncStorageOperations {
	
	/**
	 * Asynchronous version of {@link StorageOperations#streamStorageFile(StorageFile, InputStreamCallback)}.
	 * @see StorageOperations#streamStorageFile(StorageFile, InputStreamCallback)
	 */
	void streamStorageFileInBackground(StorageFile file, InputStreamCallback inputStreamCallback, AsyncCallback<Void> downloadCallback);
	
	/**
	 * Asynchronous version of {@link StorageOperations#streamStorageFileIfModifiedSince(StorageFile, InputStreamCallback)}.
	 * @see StorageOperations#streamStorageFileIfModifiedSince(StorageFile, InputStreamCallback)
	 */
	void streamStorageFileIfModifiedSinceInBackground(StorageFile file, InputStreamCallback inputStreamCallback, AsyncCallback<Boolean> downloadCallback);
	
	/**
	 * Asynchronous version of {@link StorageOperations#streamStorageFileIfNoneMatch(StorageFile, InputStreamCallback)}.
	 * @see StorageOperations#streamStorageFileIfNoneMatch(StorageFile, InputStreamCallback)
	 */
	void streamStorageFileIfNoneMatchInBackground(StorageFile file, InputStreamCallback inputStreamCallback, AsyncCallback<Boolean> downloadCallback);

}