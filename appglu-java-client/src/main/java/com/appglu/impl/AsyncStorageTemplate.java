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
package com.appglu.impl;

import java.util.concurrent.Callable;

import com.appglu.AsyncCallback;
import com.appglu.AsyncStorageOperations;
import com.appglu.StorageFile;
import com.appglu.StorageOperations;
import com.appglu.InputStreamCallback;

public final class AsyncStorageTemplate implements AsyncStorageOperations {
	
	private final AsyncExecutor asyncExecutor;
	
	private final StorageOperations storageOperations;
	
	public AsyncStorageTemplate(AsyncExecutor asyncExecutor, StorageOperations storageOperations) {
		this.asyncExecutor = asyncExecutor;
		this.storageOperations = storageOperations;
	}

	/**
	 * {@inheritDoc}
	 */
	public void streamStorageFileInBackground(final StorageFile file, final InputStreamCallback inputStreamCallback, AsyncCallback<Void> downloadCallback) {
		asyncExecutor.execute(downloadCallback, new Callable<Void>() {
			public Void call() {
				storageOperations.streamStorageFile(file, inputStreamCallback);
				return null;
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void streamStorageFileIfModifiedSinceInBackground(final StorageFile file, final InputStreamCallback inputStreamCallback, AsyncCallback<Boolean> downloadCallback) {
		asyncExecutor.execute(downloadCallback, new Callable<Boolean>() {
			public Boolean call() {
				return storageOperations.streamStorageFileIfModifiedSince(file, inputStreamCallback);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public void streamStorageFileIfNoneMatchInBackground(final StorageFile file, final InputStreamCallback inputStreamCallback, AsyncCallback<Boolean> downloadCallback) {
		asyncExecutor.execute(downloadCallback, new Callable<Boolean>() {
			public Boolean call() {
				return storageOperations.streamStorageFileIfNoneMatch(file, inputStreamCallback);
			}
		});
	}

}
