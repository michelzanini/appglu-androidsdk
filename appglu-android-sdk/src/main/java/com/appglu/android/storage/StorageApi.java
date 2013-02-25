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

public final class StorageApi {
	
	private StorageService storageService;
	
	public StorageApi(StorageService storageService) {
		this.storageService = storageService;
	}
	
	/* Methods to return files from storage API using a URL */

	public InputStream downloadAsInputStream(String url) {
		return this.storageService.downloadAsInputStream(new StorageFile(url));
	}

	public byte[] downloadAsByteArray(String url) {
		return this.storageService.downloadAsByteArray(new StorageFile(url));
	}
	
	public Bitmap downloadAsBitmap(String url) {
		return this.storageService.downloadAsBitmap(new StorageFile(url));
	}
	
	public Bitmap downloadAsBitmap(String url, int inSampleSize) {
		return this.storageService.downloadAsBitmap(new StorageFile(url), inSampleSize);
	}
	
	/* Methods to return files from storage API using a URL in a background thread */
	
	public void downloadAsInputStreamInBackground(final String url, AsyncCallback<InputStream> callback) {
		AppGluAsyncCallbackTask<InputStream> asyncTask = new AppGluAsyncCallbackTask<InputStream>(callback, new Callable<InputStream>() {
			public InputStream call() throws Exception {
				return downloadAsInputStream(url);
			}
		});
		asyncTask.execute();
	}
	
	public void downloadAsByteArrayInBackground(final String url, AsyncCallback<byte[]> callback) {
		AppGluAsyncCallbackTask<byte[]> asyncTask = new AppGluAsyncCallbackTask<byte[]>(callback, new Callable<byte[]>() {
			public byte[] call() throws Exception {
				return downloadAsByteArray(url);
			}
		});
		asyncTask.execute();
	}
	
	public void downloadAsBitmapInBackground(final String url, AsyncCallback<Bitmap> callback) {
		AppGluAsyncCallbackTask<Bitmap> asyncTask = new AppGluAsyncCallbackTask<Bitmap>(callback, new Callable<Bitmap>() {
			public Bitmap call() throws Exception {
				return downloadAsBitmap(url);
			}
		});
		asyncTask.execute();
	}
	
	public void downloadAsBitmapInBackground(final String url, final int inSampleSize, AsyncCallback<Bitmap> callback) {
		AppGluAsyncCallbackTask<Bitmap> asyncTask = new AppGluAsyncCallbackTask<Bitmap>(callback, new Callable<Bitmap>() {
			public Bitmap call() throws Exception {
				return downloadAsBitmap(url, inSampleSize);
			}
		});
		asyncTask.execute();
	}
	
	/* Methods to download an image in background using an ImageView and ProgressBar to update the UI */
	
	public void downloadToImageViewInBackground(final String url, ImageView imageView, ProgressBar progressBar) {
		ImageViewAsyncCallback callback = new ImageViewAsyncCallback(imageView, progressBar);
		
		AppGluAsyncCallbackTask<Bitmap> asyncTask = new AppGluAsyncCallbackTask<Bitmap>(callback, new Callable<Bitmap>() {
			public Bitmap call() throws Exception {
				return downloadAsBitmap(url);
			}
		});
		asyncTask.execute();
	}
	
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