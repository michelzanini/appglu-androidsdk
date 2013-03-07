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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import com.appglu.android.cache.CacheManager;
import com.appglu.impl.util.IOUtils;

import junit.framework.Assert;
import android.test.AndroidTestCase;

public abstract class AbstractCacheManagerTest extends AndroidTestCase {
	
	protected CacheManager cacheManager;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		this.cacheManager = this.setupCacheManager();
		
		this.createFile("setup.txt");
		this.createFile("test.txt");
	}

	protected abstract CacheManager setupCacheManager();
	
	protected void createFile(String fileName) {
		byte[] test = new String(fileName).getBytes();
		boolean success = this.cacheManager.store(fileName, new ByteArrayInputStream(test));
		Assert.assertTrue(success);
	}
	
	public void testExists() {
		Assert.assertTrue(this.cacheManager.exists("setup.txt"));
		Assert.assertTrue(this.cacheManager.exists("test.txt"));
		Assert.assertFalse(this.cacheManager.exists("does_not_exist.txt"));
	}
	
	public void testLastModifiedDate() {
		Date lastModifiedDate = this.cacheManager.lastModifiedDate("setup.txt");
		Assert.assertNotNull(lastModifiedDate);
		
		int tenSecondsInMilliseconds = 1000 * 10;
		long nowLessTenSeconds = System.currentTimeMillis() - tenSecondsInMilliseconds;
		
		//test if was last modified less then 10 seconds ago
		Assert.assertTrue(lastModifiedDate.getTime() > nowLessTenSeconds);
		
		Assert.assertNull(this.cacheManager.lastModifiedDate("does_not_exist.txt"));
	}
	
	public void testUpdateLastModifiedDate() throws InterruptedException, IOException {
		Date lastModifiedDate = this.cacheManager.lastModifiedDate("setup.txt");
		Assert.assertNotNull(lastModifiedDate);
		
		//sleep for a second to update the last modified date with a higher time stamp
		Thread.sleep(1000);
		
		boolean success = this.cacheManager.updateLastModifiedDate("setup.txt");
		Assert.assertTrue(success);
		
		Date updatedLastModifiedDate = this.cacheManager.lastModifiedDate("setup.txt");
		Assert.assertNotNull(updatedLastModifiedDate);
		
		Assert.assertTrue(updatedLastModifiedDate.getTime() > lastModifiedDate.getTime());
		
		InputStream input = this.cacheManager.retrieve("setup.txt");
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		IOUtils.copy(input, output);
		
		Assert.assertEquals("setup.txt", new String(output.toByteArray()));
	}
	
	public void testStore() {
		Assert.assertFalse(this.cacheManager.exists("store.txt"));
		
		this.createFile("store.txt");
		
		Assert.assertTrue(this.cacheManager.exists("store.txt"));
	}
	
	public void testRetrieve() throws IOException {
		Assert.assertTrue(this.cacheManager.exists("setup.txt"));
		
		InputStream input = this.cacheManager.retrieve("setup.txt");
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		IOUtils.copy(input, output);
		
		Assert.assertEquals("setup.txt", new String(output.toByteArray()));
		
		Assert.assertNull(this.cacheManager.retrieve("does_not_exist.txt"));
	}
	
	public void testRemove() {
		Assert.assertTrue(this.cacheManager.exists("setup.txt"));
		Assert.assertTrue(this.cacheManager.exists("test.txt"));
		
		boolean success = this.cacheManager.remove("setup.txt");
		Assert.assertTrue(success);
		
		Assert.assertFalse(this.cacheManager.exists("setup.txt"));
		Assert.assertTrue(this.cacheManager.exists("test.txt"));
	}
	
	public void testRemoveAll() {
		Assert.assertTrue(this.cacheManager.exists("setup.txt"));
		Assert.assertTrue(this.cacheManager.exists("test.txt"));
		
		this.cacheManager.removeAll();
		
		Assert.assertFalse(this.cacheManager.exists("setup.txt"));
		Assert.assertFalse(this.cacheManager.exists("test.txt"));
	}

}
