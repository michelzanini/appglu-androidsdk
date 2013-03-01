package com.appglu.android.sync;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.appglu.AsyncCallback;
import com.appglu.StorageFile;
import com.appglu.SyncOperations;
import com.appglu.android.AppGlu;
import com.appglu.android.AppGluAsyncCallbackTask;
import com.appglu.android.AppGluNotProperlyConfiguredException;
import com.appglu.android.ImageViewAsyncCallback;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;

/**
 * TODO
 * @since 1.0.0
 */
public final class SyncApi {
	
	public static final int NOTIFICATION_ID = Integer.MAX_VALUE;
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.LOG_TAG);
	
	private Context context;
	
	private SyncService syncService;
	
	private static SyncApi lastUsedInstance;
	
	public SyncApi(Context context, SyncOperations syncOperations, SyncRepository syncRepository, SyncFileStorageService syncStorageService) {
		this.context = context;
		this.syncService = new SyncService(syncOperations, syncRepository, syncStorageService);
	}
	
	/* Methods to be used by SyncIntentService */
	
	/* package */ static SyncApi getLastUsedInstance() {
		return lastUsedInstance;
	}
	
	/* package */ boolean downloadChanges() {
		return syncService.downloadChanges();
	}

	/* package */ boolean downloadChangesAndFiles() {
		return syncService.downloadChangesAndFiles();
	}

	/* package */ boolean downloadChangesForTables(List<String> tables) {
		return syncService.downloadChangesForTables(tables);
	}

	/* package */ boolean downloadChangesAndFilesForTables(List<String> tables) {
		return syncService.downloadChangesAndFilesForTables(tables);
	}
	
	/* package */ boolean hasDownloadedChanges() {
		return syncService.hasDownloadedChanges();
	}
	
	/* package */ boolean discardChanges() {
		return syncService.discardChanges();
	}

	/* package */ boolean applyChanges() {
		return syncService.applyChanges();
	}
	
	/* Methods to check if database is synchronized */
	
	public boolean checkIfDatabaseIsSynchronized() {
		return syncService.checkIfDatabaseIsSynchronized();
	}
	
	public boolean checkIfTablesAreSynchronized(String... tables) {
		return syncService.checkIfTablesAreSynchronized(Arrays.asList(tables));
	}
	
	public boolean checkIfTablesAreSynchronized(List<String> tables) {
		return syncService.checkIfTablesAreSynchronized(tables);
	}
	
	public void checkIfDatabaseIsSynchronizedInBackground(AsyncCallback<Boolean> callback) {
		AppGluAsyncCallbackTask<Boolean> asyncTask = new AppGluAsyncCallbackTask<Boolean>(callback, new Callable<Boolean>() {
			public Boolean call() throws Exception {
				return checkIfDatabaseIsSynchronized();
			}
		});
		asyncTask.execute();
	}
	
	public void checkIfTablesAreSynchronizedInBackground(AsyncCallback<Boolean> callback, String... tables) {
		this.checkIfTablesAreSynchronizedInBackground(Arrays.asList(tables), callback);
	}
	
	public void checkIfTablesAreSynchronizedInBackground(final List<String> tables, AsyncCallback<Boolean> callback) {
		AppGluAsyncCallbackTask<Boolean> asyncTask = new AppGluAsyncCallbackTask<Boolean>(callback, new Callable<Boolean>() {
			public Boolean call() throws Exception {
				return checkIfTablesAreSynchronized(tables);
			}
		});
		asyncTask.execute();
	}
	
	/* Methods to return files from sync storage */
	
	public File readFileFromFileStorage(StorageFile storageFile) {
		return this.syncService.getFileFromFileStorage(storageFile);
	}
	
	public InputStream readInputStreamFromFileStorage(StorageFile storageFile) {
		return this.syncService.readInputStreamFromFileStorage(storageFile);
	}
	
	public byte[] readByteArrayFromFileStorage(StorageFile storageFile) {
		return this.syncService.readByteArrayFromFileStorage(storageFile);
	}
	
	public Bitmap readBitmapFromFileStorage(StorageFile storageFile) {
		return this.syncService.readBitmapFromFileStorage(storageFile);
	}
	
	public Bitmap readBitmapFromFileStorage(StorageFile storageFile, int inSampleSize) {
		return this.syncService.readBitmapFromFileStorage(storageFile, inSampleSize);
	}
	
	public Bitmap readBitmapFromFileStorage(StorageFile storageFile, int requestedWidth, int requestedHeight) {
		return this.syncService.readBitmapFromFileStorage(storageFile, requestedWidth, requestedHeight);
	}
	
	/* Methods to return files from sync storage in a background thread */
	
	public void readFileFromFileStorageInBackground(final StorageFile storageFile, AsyncCallback<File> callback) {
		AppGluAsyncCallbackTask<File> asyncTask = new AppGluAsyncCallbackTask<File>(callback, new Callable<File>() {
			public File call() throws Exception {
				return readFileFromFileStorage(storageFile);
			}
		});
		asyncTask.execute();
	}

	public void readInputStreamFromFileStorageInBackground(final StorageFile storageFile, AsyncCallback<InputStream> callback) {
		AppGluAsyncCallbackTask<InputStream> asyncTask = new AppGluAsyncCallbackTask<InputStream>(callback, new Callable<InputStream>() {
			public InputStream call() throws Exception {
				return readInputStreamFromFileStorage(storageFile);
			}
		});
		asyncTask.execute();
	}
	
	
	public void readByteArrayFromFileStorageInBackground(final StorageFile storageFile, AsyncCallback<byte[]> callback) {
		AppGluAsyncCallbackTask<byte[]> asyncTask = new AppGluAsyncCallbackTask<byte[]>(callback, new Callable<byte[]>() {
			public byte[] call() throws Exception {
				return readByteArrayFromFileStorage(storageFile);
			}
		});
		asyncTask.execute();
	}
	
	public void readBitmapFromFileStorageInBackground(final StorageFile storageFile, AsyncCallback<Bitmap> callback) {
		AppGluAsyncCallbackTask<Bitmap> asyncTask = new AppGluAsyncCallbackTask<Bitmap>(callback, new Callable<Bitmap>() {
			public Bitmap call() throws Exception {
				return readBitmapFromFileStorage(storageFile);
			}
		});
		asyncTask.execute();
	}
	
	public void readBitmapFromFileStorageInBackground(final StorageFile storageFile, final int inSampleSize, AsyncCallback<Bitmap> callback) {
		AppGluAsyncCallbackTask<Bitmap> asyncTask = new AppGluAsyncCallbackTask<Bitmap>(callback, new Callable<Bitmap>() {
			public Bitmap call() throws Exception {
				return readBitmapFromFileStorage(storageFile, inSampleSize);
			}
		});
		asyncTask.execute();
	}

	
	public void readBitmapFromFileStorageInBackground(final StorageFile storageFile, final int requestedWidth, final int requestedHeight, AsyncCallback<Bitmap> callback) {
		AppGluAsyncCallbackTask<Bitmap> asyncTask = new AppGluAsyncCallbackTask<Bitmap>(callback, new Callable<Bitmap>() {
			public Bitmap call() throws Exception {
				return readBitmapFromFileStorage(storageFile, requestedWidth, requestedHeight);
			}
		});
		asyncTask.execute();
	}
	
	/* Methods to read an image from sync storage in background using an ImageView and ProgressBar to update the UI  */
	
	public void readBitmapToImageViewInBackground(final StorageFile storageFile, ImageView imageView, ProgressBar progressBar) {
		ImageViewAsyncCallback callback = new ImageViewAsyncCallback(imageView, progressBar);
		
		AppGluAsyncCallbackTask<Bitmap> asyncTask = new AppGluAsyncCallbackTask<Bitmap>(callback, new Callable<Bitmap>() {
			public Bitmap call() throws Exception {
				return readBitmapFromFileStorage(storageFile);
			}
		});
		asyncTask.execute();
	}
	
	public void readBitmapToImageViewInBackground(final StorageFile storageFile, final int inSampleSize, ImageView imageView, ProgressBar progressBar) {
		ImageViewAsyncCallback callback = new ImageViewAsyncCallback(imageView, progressBar);
		
		AppGluAsyncCallbackTask<Bitmap> asyncTask = new AppGluAsyncCallbackTask<Bitmap>(callback, new Callable<Bitmap>() {
			public Bitmap call() throws Exception {
				return readBitmapFromFileStorage(storageFile, inSampleSize);
			}
		});
		asyncTask.execute();
	}

	
	public void readBitmapToImageViewInBackground(final StorageFile storageFile, final int requestedWidth, final int requestedHeight, ImageView imageView, ProgressBar progressBar) {
		ImageViewAsyncCallback callback = new ImageViewAsyncCallback(imageView, progressBar);
		
		AppGluAsyncCallbackTask<Bitmap> asyncTask = new AppGluAsyncCallbackTask<Bitmap>(callback, new Callable<Bitmap>() {
			public Bitmap call() throws Exception {
				return readBitmapFromFileStorage(storageFile, requestedWidth, requestedHeight);
			}
		});
		asyncTask.execute();
	}
	
	/* Methods related to SyncIntentService */
	
	public boolean isSyncIntentServiceRunning() {
		ActivityManager manager = (ActivityManager) this.context.getSystemService(Activity.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (SyncIntentService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public void startSyncIntentService(SyncIntentServiceRequest request) {
		synchronized (SyncApi.class) {
			if (this.isSyncIntentServiceRunning()) {
				this.logger.info("SyncIntentService was not started because it is already running");
				return;
			}
			
			Intent intent = new Intent(this.context, SyncIntentService.class);
			this.validateSyncIntent(intent);
			
			intent.putExtra(SyncIntentService.SYNC_OPERATION_SERIALIZABLE_EXTRA, request.getSyncRequestOperation());
			intent.putExtra(SyncIntentService.SYNC_FILES_BOOLEAN_EXTRA, request.getSyncFiles());
			
			if (request.getTablesToSync() != null) {
				intent.putStringArrayListExtra(SyncIntentService.TABLES_STRING_ARRAY_EXTRA, new ArrayList<String>(request.getTablesToSync()));
			}
			
			if (request.getSyncServiceRunningNotification() != null) {
				intent.putExtra(SyncIntentService.SYNC_SERVICE_RUNNING_NOTIFICATION_PARCELABLE_EXTRA, request.getSyncServiceRunningNotification());
				
				if (request.getSyncServiceCompletedNotification() != null) {
					intent.putExtra(SyncIntentService.SYNC_SERVICE_COMPLETED_NOTIFICATION_PARCELABLE_EXTRA, request.getSyncServiceCompletedNotification());
				}
			}
			
			lastUsedInstance = this;
			
			this.context.startService(intent);
			this.logger.info("SyncIntentService has being started");
		}
	}
	
	private void validateSyncIntent(Intent intent) {
		PackageManager packageManager = this.context.getPackageManager();
		List<ResolveInfo> resolveInfo = packageManager.queryIntentServices(intent, PackageManager.MATCH_DEFAULT_ONLY);
		
		if (resolveInfo.size() == 0) {
			throw new AppGluNotProperlyConfiguredException("To be able to execute sync you must declare " +
					"a service named com.appglu.android.sync.SyncIntentService in the AndroidManifest.xml");
		}
	}

}