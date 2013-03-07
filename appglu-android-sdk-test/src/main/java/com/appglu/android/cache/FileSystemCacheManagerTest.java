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

import com.appglu.android.cache.CacheManager;
import com.appglu.android.cache.FileSystemCacheManager;

import junit.framework.Assert;

public class FileSystemCacheManagerTest extends AbstractCacheManagerTest {

	protected CacheManager setupCacheManager() {
		return new FileSystemCacheManager(getContext());
	}
	
	public void testPruneFilesIfNecessary() {
		this.cacheManager = new FileSystemCacheManager(getContext(), 10);
		this.cacheManager.removeAll();
		
		Assert.assertEquals(0, this.cacheManager.cacheSize());
		
		this.createFile("1.txt");
		
		Assert.assertEquals(5, this.cacheManager.cacheSize());
		
		this.createFile("2.txt");
		
		Assert.assertEquals(10, this.cacheManager.cacheSize());
		
		this.createFile("3.txt");
		this.createFile("4.txt");
		this.createFile("5.txt");
		
		Assert.assertEquals(10, this.cacheManager.cacheSize());
		
		Assert.assertFalse(this.cacheManager.exists("1.txt"));
		Assert.assertFalse(this.cacheManager.exists("2.txt"));
		Assert.assertFalse(this.cacheManager.exists("3.txt"));
		Assert.assertTrue(this.cacheManager.exists("4.txt"));
		Assert.assertTrue(this.cacheManager.exists("5.txt"));
	}
	
	public void testPruneFilesIfNecessary_fileLargerThenMaxCacheSize() {
		this.cacheManager = new FileSystemCacheManager(getContext(), 10);
		this.cacheManager.removeAll();
		
		Assert.assertEquals(0, this.cacheManager.cacheSize());
		
		this.createFile("1.txt");
		this.createFile("2.txt");
		
		Assert.assertEquals(10, this.cacheManager.cacheSize());
		
		String fileNameAndContent = "this_file_is_larger_then_cache_size.txt";
		this.createFile(fileNameAndContent);
		
		Assert.assertEquals(fileNameAndContent.length(), this.cacheManager.cacheSize());
		
		Assert.assertFalse(this.cacheManager.exists("1.txt"));
		Assert.assertFalse(this.cacheManager.exists("2.txt"));
		Assert.assertTrue(this.cacheManager.exists(fileNameAndContent));
	}
	
}
