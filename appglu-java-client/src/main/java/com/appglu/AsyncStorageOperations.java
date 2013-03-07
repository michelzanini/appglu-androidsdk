/*******************************************************************************
 * Copyright 2013 AppGlu, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
