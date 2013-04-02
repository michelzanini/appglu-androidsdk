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
package com.appglu.android.storage;

import java.io.InputStream;
import java.util.concurrent.Callable;

import android.graphics.Bitmap;

import com.appglu.AsyncCallback;
import com.appglu.StorageFile;
import com.appglu.android.AsyncTaskExecutor;
import com.appglu.android.ImageViewAsyncCallback;

/**
 * {@code StorageApi} has methods to download and cache files from AppGlu server.<br>
 * 
 * @see com.appglu.android.AppGlu
 * @see com.appglu.StorageOperations
 * @see com.appglu.AsyncStorageOperations
 * @since 1.0.0
 */
public final class StorageApi {
	
	private StorageService storageService;
	
	public StorageApi(StorageService storageService) {
		this.storageService = storageService;
	}
	
	/* Methods to return files from storage API using a URL */

	/**
	 * Download a file using the URL and return it as an <code>InputStream</code>.
	 * @param url the file URL
	 * @return an <code>InputStream</code> with the content of the file 
	 */
	public InputStream downloadAsInputStream(String url) {
		return this.storageService.downloadAsInputStream(new StorageFile(url));
	}

	/**
	 * Download a file using the URL and return it as a <code>byte[]</code>.
	 * @param url the file URL - must be a image file
	 * @return a <code>byte[]</code> with the content of the file 
	 */
	public byte[] downloadAsByteArray(String url) {
		return this.storageService.downloadAsByteArray(new StorageFile(url));
	}
	
	/**
	 * Download a file using the URL and return it as a <code>Bitmap</code>.
	 * @param url the file URL - must be a image file
	 * @return a <code>Bitmap</code> with the content of the file
	 * 
	 * @see com.appglu.android.util.AppGluUtils#decodeSampledBitmapFromInputStream(InputStream)
	 */
	public Bitmap downloadAsBitmap(String url) {
		return this.storageService.downloadAsBitmap(new StorageFile(url));
	}
	
	/**
	 * Download a file using the URL and return it as a <code>Bitmap</code>.
	 * @param url the file URL
	 * @param inSampleSize how much smaller that the image will be, for example, <code>inSampleSize</code> equals 2 will return an image 1/2 the size of the original
	 * @return a <code>Bitmap</code> with the content of the file 
	 * 
	 * @see com.appglu.android.util.AppGluUtils#decodeSampledBitmapFromInputStream(InputStream, int)
	 */
	public Bitmap downloadAsBitmap(String url, int inSampleSize) {
		return this.storageService.downloadAsBitmap(new StorageFile(url), inSampleSize);
	}
	
	/* Methods to return files from storage API using a URL in a background thread */
	
	/**
	 * Asynchronous version of {@link #downloadAsInputStream(String)}.
	 * @see #downloadAsInputStream(String)
	 */
	public void downloadAsInputStreamInBackground(final String url, AsyncCallback<InputStream> asyncCallback) {
		AsyncTaskExecutor executor = new AsyncTaskExecutor(false);
		executor.execute(asyncCallback, new Callable<InputStream>() {
			public InputStream call() throws Exception {
				return downloadAsInputStream(url);
			}
		});
	}
	
	/**
	 * Asynchronous version of {@link #downloadAsByteArray(String)}.
	 * @see #downloadAsByteArray(String)
	 */
	public void downloadAsByteArrayInBackground(final String url, AsyncCallback<byte[]> asyncCallback) {
		AsyncTaskExecutor executor = new AsyncTaskExecutor(false);
		executor.execute(asyncCallback, new Callable<byte[]>() {
			public byte[] call() throws Exception {
				return downloadAsByteArray(url);
			}
		});
	}
	
	/**
	 * Asynchronous version of {@link #downloadAsBitmap(String)}.
	 * @see #downloadAsBitmap(String)
	 */
	public void downloadAsBitmapInBackground(final String url, AsyncCallback<Bitmap> asyncCallback) {
		AsyncTaskExecutor executor = new AsyncTaskExecutor(false);
		executor.execute(asyncCallback, new Callable<Bitmap>() {
			public Bitmap call() throws Exception {
				return downloadAsBitmap(url);
			}
		});
	}
	
	/**
	 * Asynchronous version of {@link #downloadAsBitmap(String, int)}.
	 * @see #downloadAsBitmap(String, int)
	 */
	public void downloadAsBitmapInBackground(final String url, final int inSampleSize, AsyncCallback<Bitmap> asyncCallback) {
		AsyncTaskExecutor executor = new AsyncTaskExecutor(false);
		executor.execute(asyncCallback, new Callable<Bitmap>() {
			public Bitmap call() throws Exception {
				return downloadAsBitmap(url, inSampleSize);
			}
		});
	}
	
	/* Methods to download an image in background using an ImageView and ProgressBar to update the UI */
	
	/**
	 * Download a file using the URL, convert it to a <code>Bitmap</code>, and then load it into the provided <code>ImageView</code> reference.<br>
	 * If a <code>ProgressBar</code> is provided, then it will be displayed as long as the image is loading.
	 * 
	 * @param url the file URL - must be a image file
	 * @param imageViewCallback a {@link com.appglu.android.ImageViewAsyncCallback} can be instantiated either with a listener ({@link com.appglu.android.ImageViewAsyncCallback.ImageDownloadListener})
	 * or references to a <code>ImageView</code>, <code>ProgressBar</code> and a <code>View</code> (place holder to display if error occur)
	 * 
	 * @see com.appglu.android.ImageViewAsyncCallback
	 * @see com.appglu.android.util.AppGluUtils#decodeSampledBitmapFromInputStream(InputStream)
	 */
	public void downloadToImageViewInBackground(final String url, ImageViewAsyncCallback imageViewCallback) {
		AsyncTaskExecutor executor = new AsyncTaskExecutor(false);
		executor.execute(imageViewCallback, new Callable<Bitmap>() {
			public Bitmap call() throws Exception {
				return downloadAsBitmap(url);
			}
		});
	}
	
	/**
	 * Download a file using the URL, convert it to a <code>Bitmap</code>, and then load it into the provided <code>ImageView</code> reference.<br>
	 * If a <code>ProgressBar</code> is provided, then it will be displayed as long as the image is loading.
	 * 
	 * @param url the file URL - must be a image file
	 * @param inSampleSize how much smaller that the image will be, for example, <code>inSampleSize</code> equals 2 will return an image 1/2 the size of the original
	 * @param imageViewCallback a {@link com.appglu.android.ImageViewAsyncCallback} can be instantiated either with a listener ({@link com.appglu.android.ImageViewAsyncCallback.ImageDownloadListener})
	 * or references to a <code>ImageView</code>, <code>ProgressBar</code> and a <code>View</code> (place holder to display if error occur)
	 * 
	 * @see com.appglu.android.ImageViewAsyncCallback
	 * @see com.appglu.android.util.AppGluUtils#decodeSampledBitmapFromInputStream(InputStream, int)
	 */
	public void downloadToImageViewInBackground(final String url, final int inSampleSize, ImageViewAsyncCallback imageViewCallback) {
		AsyncTaskExecutor executor = new AsyncTaskExecutor(false);
		executor.execute(imageViewCallback, new Callable<Bitmap>() {
			public Bitmap call() throws Exception {
				return downloadAsBitmap(url, inSampleSize);
			}
		});
	}
	
}