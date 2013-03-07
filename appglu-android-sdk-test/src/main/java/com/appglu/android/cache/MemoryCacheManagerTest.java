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
import com.appglu.android.cache.MemoryCacheManager;

import junit.framework.Assert;

public class MemoryCacheManagerTest extends AbstractCacheManagerTest {
	
	protected CacheManager setupCacheManager() {
		return new MemoryCacheManager(4);
	}
	
	public void testCacheSizeDoNotIncreaseMoreThenLimit() {
		this.cacheManager.removeAll();
		
		Assert.assertEquals(0, this.cacheManager.cacheSize());
		
		this.createFile("1.txt");
		this.createFile("2.txt");
		this.createFile("3.txt");
		this.createFile("4.txt");
		this.createFile("5.txt");
		this.createFile("6.txt");
		
		Assert.assertEquals(4, this.cacheManager.cacheSize());
		
		Assert.assertFalse(this.cacheManager.exists("1.txt"));
		Assert.assertFalse(this.cacheManager.exists("2.txt"));
		Assert.assertTrue(this.cacheManager.exists("3.txt"));
		Assert.assertTrue(this.cacheManager.exists("4.txt"));
		Assert.assertTrue(this.cacheManager.exists("5.txt"));
		Assert.assertTrue(this.cacheManager.exists("6.txt"));
	}
	
	public void testUpdateLastModifiedWillHaveEffectOnPruning() {
		this.cacheManager.removeAll();
		
		Assert.assertEquals(0, this.cacheManager.cacheSize());
		
		this.createFile("1.txt");
		this.createFile("2.txt");
		this.createFile("3.txt");
		
		Assert.assertEquals(3, this.cacheManager.cacheSize());
		
		this.createFile("2.txt");
		
		Assert.assertEquals(3, this.cacheManager.cacheSize());
		
		this.createFile("4.txt");
		this.createFile("5.txt");
		this.createFile("6.txt");
		
		Assert.assertFalse(this.cacheManager.exists("1.txt"));
		Assert.assertTrue(this.cacheManager.exists("2.txt"));
		Assert.assertFalse(this.cacheManager.exists("3.txt"));
		Assert.assertTrue(this.cacheManager.exists("4.txt"));
		Assert.assertTrue(this.cacheManager.exists("5.txt"));
		Assert.assertTrue(this.cacheManager.exists("6.txt"));
		
		this.cacheManager.updateLastModifiedDate("4.txt");
		
		this.createFile("7.txt");
		this.createFile("8.txt");
		
		Assert.assertFalse(this.cacheManager.exists("1.txt"));
		Assert.assertFalse(this.cacheManager.exists("2.txt"));
		Assert.assertFalse(this.cacheManager.exists("3.txt"));
		Assert.assertTrue(this.cacheManager.exists("4.txt"));
		Assert.assertFalse(this.cacheManager.exists("5.txt"));
		Assert.assertTrue(this.cacheManager.exists("6.txt"));
		Assert.assertTrue(this.cacheManager.exists("7.txt"));
		Assert.assertTrue(this.cacheManager.exists("8.txt"));
	}

}
