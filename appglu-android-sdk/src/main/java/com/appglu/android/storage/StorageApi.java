package com.appglu.android.storage;

import java.io.InputStream;
import java.util.concurrent.Callable;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.appglu.AsyncCallback;
import com.appglu.StorageFile;
import com.appglu.android.AppGluAsyncCallbackTask;
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
	public void downloadAsInputStreamInBackground(final String url, AsyncCallback<InputStream> callback) {
		AppGluAsyncCallbackTask<InputStream> asyncTask = new AppGluAsyncCallbackTask<InputStream>(callback, new Callable<InputStream>() {
			public InputStream call() throws Exception {
				return downloadAsInputStream(url);
			}
		});
		asyncTask.execute();
	}
	
	/**
	 * Asynchronous version of {@link #downloadAsByteArray(String)}.
	 * @see #downloadAsByteArray(String)
	 */
	public void downloadAsByteArrayInBackground(final String url, AsyncCallback<byte[]> callback) {
		AppGluAsyncCallbackTask<byte[]> asyncTask = new AppGluAsyncCallbackTask<byte[]>(callback, new Callable<byte[]>() {
			public byte[] call() throws Exception {
				return downloadAsByteArray(url);
			}
		});
		asyncTask.execute();
	}
	
	/**
	 * Asynchronous version of {@link #downloadAsBitmap(String)}.
	 * @see #downloadAsBitmap(String)
	 */
	public void downloadAsBitmapInBackground(final String url, AsyncCallback<Bitmap> callback) {
		AppGluAsyncCallbackTask<Bitmap> asyncTask = new AppGluAsyncCallbackTask<Bitmap>(callback, new Callable<Bitmap>() {
			public Bitmap call() throws Exception {
				return downloadAsBitmap(url);
			}
		});
		asyncTask.execute();
	}
	
	/**
	 * Asynchronous version of {@link #downloadAsBitmap(String, int)}.
	 * @see #downloadAsBitmap(String, int)
	 */
	public void downloadAsBitmapInBackground(final String url, final int inSampleSize, AsyncCallback<Bitmap> callback) {
		AppGluAsyncCallbackTask<Bitmap> asyncTask = new AppGluAsyncCallbackTask<Bitmap>(callback, new Callable<Bitmap>() {
			public Bitmap call() throws Exception {
				return downloadAsBitmap(url, inSampleSize);
			}
		});
		asyncTask.execute();
	}
	
	/* Methods to download an image in background using an ImageView and ProgressBar to update the UI */
	
	/**
	 * Download a file using the URL, convert it to a <code>Bitmap</code>, and then load it into the provided <code>ImageView</code> reference.<br>
	 * If a <code>ProgressBar</code> is provided, then it will be displayed as long as the image is loading.
	 * 
	 * @param url the file URL - must be a image file
	 * @param imageView a <code>ImageView</code> reference from your Activity
	 * @param progressBar a <code>ProgressBar</code> reference from your Activity or <code>null</code> if you don't want to show a progress bar
	 * 
	 * @see com.appglu.android.util.AppGluUtils#decodeSampledBitmapFromInputStream(InputStream)
	 */
	public void downloadToImageViewInBackground(final String url, ImageView imageView, ProgressBar progressBar) {
		ImageViewAsyncCallback callback = new ImageViewAsyncCallback(imageView, progressBar);
		
		AppGluAsyncCallbackTask<Bitmap> asyncTask = new AppGluAsyncCallbackTask<Bitmap>(callback, new Callable<Bitmap>() {
			public Bitmap call() throws Exception {
				return downloadAsBitmap(url);
			}
		});
		asyncTask.execute();
	}
	
	/**
	 * Download a file using the URL, convert it to a <code>Bitmap</code>, and then load it into the provided <code>ImageView</code> reference.<br>
	 * If a <code>ProgressBar</code> is provided, then it will be displayed as long as the image is loading.
	 * 
	 * @param url the file URL - must be a image file
	 * @param inSampleSize how much smaller that the image will be, for example, <code>inSampleSize</code> equals 2 will return an image 1/2 the size of the original
	 * @param imageView a <code>ImageView</code> reference from your Activity
	 * @param progressBar a <code>ProgressBar</code> reference from your Activity or <code>null</code> if you don't want to show a progress bar
	 * 
	 * @see com.appglu.android.util.AppGluUtils#decodeSampledBitmapFromInputStream(InputStream, int)
	 */
	public void downloadToImageViewInBackground(final String url, final int inSampleSize, ImageView imageView, ProgressBar progressBar) {
		ImageViewAsyncCallback callback = new ImageViewAsyncCallback(imageView, progressBar);
		
		AppGluAsyncCallbackTask<Bitmap> asyncTask = new AppGluAsyncCallbackTask<Bitmap>(callback, new Callable<Bitmap>() {
			public Bitmap call() throws Exception {
				return downloadAsBitmap(url, inSampleSize);
			}
		});
		asyncTask.execute();
	}
	
}