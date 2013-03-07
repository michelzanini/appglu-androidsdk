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
package com.appglu.android.cache;

import java.io.InputStream;
import java.util.Date;

/**
 * A {@code CacheManager} is used for storing and caching files while using the {@link com.appglu.android.storage.StorageApi}.<br>
 * There are three {@code CacheManager} implementations available: memory ({@link MemoryCacheManager}), disk ({@link FileSystemCacheManager}) or both ({@link MemoryAndFileSystemCacheManager}).<br>
 * By default {@link FileSystemCacheManager} is used and it will cache the files on disk.
 * 
 * @see MemoryCacheManager
 * @see FileSystemCacheManager
 * @see MemoryAndFileSystemCacheManager
 * @since 1.0.0
 */
public interface CacheManager {
	
	boolean exists(String fileName);
	
	Date lastModifiedDate(String fileName);
	
	boolean updateLastModifiedDate(String fileName);
	
	InputStream retrieve(String fileName);
	
	boolean store(String fileName, InputStream inputStream);
	
	boolean remove(String fileName);
	
	void removeAll();
	
	long cacheSize();

}
