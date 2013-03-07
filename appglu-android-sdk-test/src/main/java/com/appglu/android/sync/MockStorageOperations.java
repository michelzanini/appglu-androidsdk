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
