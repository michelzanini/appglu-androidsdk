package com.appglu.android.sync;

import java.util.ArrayList;

import org.springframework.web.client.RestClientException;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.appglu.AppGluHttpClientException;
import com.appglu.AppGluHttpServerException;
import com.appglu.AppGluRestClientException;
import com.appglu.android.AppGlu;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;
import com.appglu.android.sync.SyncIntentServiceRequest.SyncRequestOperation;

public class SyncIntentService extends IntentService {
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.LOG_TAG);
	
	public static final String SYNC_OPERATION_SERIALIZABLE_EXTRA = "SyncIntentService.SYNC_OPERATION_SERIALIZABLE_EXTRA";
	
	public static final String SYNC_FILES_BOOLEAN_EXTRA = "SyncIntentService.SYNC_FILES_BOOLEAN_EXTRA";
	
	public static final String TABLES_STRING_ARRAY_EXTRA = "SyncIntentService.TABLES_STRING_ARRAY_EXTRA";
	
	public static final String SYNC_SERVICE_RUNNING_NOTIFICATION_PARCELABLE_EXTRA = "SyncIntentService.SYNC_SERVICE_RUNNING_NOTIFICATION_PARCELABLE_EXTRA";
	
	public static final String SYNC_SERVICE_COMPLETED_NOTIFICATION_PARCELABLE_EXTRA = "SyncIntentService.SYNC_SERVICE_COMPLETED_NOTIFICATION_PARCELABLE_EXTRA";
	
	public static final String PRE_EXECUTE_ACTION = "SyncIntentService.ON_PRE_EXECUTE_ACTION";
	
	public static final String NO_INTERNET_CONNECTION_ACTION = "SyncIntentService.ON_NO_INTERNET_CONNECTION_ACTION";
	
	public static final String ON_TRANSACTION_START_ACTION = "SyncIntentService.ON_TRANSACTION_START_ACTION";
	
	public static final String ON_TRANSACTION_FINISH_ACTION = "SyncIntentService.ON_TRANSACTION_FINISH_ACTION";
	
	public static final String RESULT_ACTION = "SyncIntentService.RESULT_ACTION";
	
	public static final String EXCEPTION_ACTION = "SyncIntentService.EXCEPTION_ACTION";
	
	public static final String FINISH_ACTION = "SyncIntentService.FINISH_ACTION";
	
	public static final String EXCEPTION_WRAPPER_SERIALIZABLE_EXTRA = "SyncIntentService.EXCEPTION_WRAPPER_SERIALIZABLE_EXTRA";
	
	public static final String CHANGES_WERE_APPLIED_BOOLEAN_EXTRA = "SyncIntentService.CHANGES_WERE_APPLIED_BOOLEAN_EXTRA";
	
	public SyncIntentService() {
		super("SyncIntentService");
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		if (this.hasSyncServiceRunningNotificationExtra(intent)) {
			Notification notification = (Notification) intent.getParcelableExtra(SYNC_SERVICE_RUNNING_NOTIFICATION_PARCELABLE_EXTRA);
			this.sendNotification(notification, false);
		}
	}

	private boolean hasSyncServiceRunningNotificationExtra(Intent intent) {
		return intent.hasExtra(SYNC_SERVICE_RUNNING_NOTIFICATION_PARCELABLE_EXTRA);
	}
	
	private boolean hasSyncServiceCompletedNotificationExtra(Intent intent) {
		return intent.hasExtra(SYNC_SERVICE_COMPLETED_NOTIFICATION_PARCELABLE_EXTRA);
	}
	
	private SyncApi getSyncApiInstance() {
		if (SyncApi.getLastUsedInstance() == null) {
			return AppGlu.syncApi();
		}
		return SyncApi.getLastUsedInstance();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		boolean successful = false;
		
		try {
			SyncRequestOperation syncOperation = (SyncRequestOperation) intent.getSerializableExtra(SYNC_OPERATION_SERIALIZABLE_EXTRA);
			
			boolean isApplyChanges = SyncRequestOperation.APPLY_CHANGES.equals(syncOperation);
			boolean isDiscardChanges = SyncRequestOperation.DISCARD_CHANGES.equals(syncOperation);

			boolean isDownloadChanges = SyncRequestOperation.DOWNLOAD_CHANGES.equals(syncOperation);
			boolean isDownloadAndApplyChanges = SyncRequestOperation.DOWNLOAD_AND_APPLY_CHANGES.equals(syncOperation);

			boolean requiresInternet = isDownloadChanges || isDownloadAndApplyChanges;
			
			if (requiresInternet && !AppGlu.hasInternetConnection()) {
				this.broadcastAction(NO_INTERNET_CONNECTION_ACTION);
				return;
			}
			
			this.broadcastAction(PRE_EXECUTE_ACTION);
			
			ArrayList<String> tables = intent.getStringArrayListExtra(TABLES_STRING_ARRAY_EXTRA);
			boolean syncFiles = intent.getBooleanExtra(SYNC_FILES_BOOLEAN_EXTRA, false);
			
			if (isApplyChanges) {
				successful = this.applyChanges();
			} 
			
			if (isDiscardChanges) {
				successful = this.discardChanges();
			}
			
			if (isDownloadChanges) {
				successful = this.downloadChangesAndFilesForTables(tables, syncFiles);
			}
			
			if (isDownloadAndApplyChanges) {
				boolean hasChanges = this.downloadChangesAndFilesForTables(tables, syncFiles);
				if (hasChanges) {
					successful = this.applyChanges();
				} else {
					successful = false;
				}
			}
			
			this.broadcastResult(successful);
	
		} catch (Exception exception) {
			this.broadcastException(exception);
		} finally {
			if (this.hasSyncServiceRunningNotificationExtra(intent)) {
				if (successful && this.hasSyncServiceCompletedNotificationExtra(intent)) {
					Notification notification = (Notification) intent.getParcelableExtra(SYNC_SERVICE_COMPLETED_NOTIFICATION_PARCELABLE_EXTRA);
					this.sendNotification(notification, true);
				} else {
					this.cancelNotification();
				}
			}
		}
		
		this.broadcastAction(FINISH_ACTION);
	}

	protected boolean applyChanges() {
		if (!this.getSyncApiInstance().hasDownloadedChanges()) {
			this.logger.info("No changes to apply");
			return false;
		}
		
		try {
			this.broadcastAction(ON_TRANSACTION_START_ACTION);
			return this.getSyncApiInstance().applyChanges();
		} finally {
			this.broadcastAction(ON_TRANSACTION_FINISH_ACTION);
		}
	}
	
	protected boolean discardChanges() {
		return this.getSyncApiInstance().discardChanges();
	}
	
	protected boolean downloadChangesAndFilesForTables(ArrayList<String> tables, boolean syncFiles) {
		if (tables == null) {
			if (syncFiles) {
				return this.getSyncApiInstance().downloadChangesAndFiles();
			} else {
				return this.getSyncApiInstance().downloadChanges();
			}
		} else {
			if (syncFiles) {
				return this.getSyncApiInstance().downloadChangesAndFilesForTables(tables);
			} else {
				return this.getSyncApiInstance().downloadChangesForTables(tables);
			}
		}
	}
	
	protected void sendNotification(Notification notification, boolean autoCancelNotification) {
		if (autoCancelNotification) {
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.flags &= ~Notification.FLAG_ONGOING_EVENT;
			notification.flags &= ~Notification.FLAG_NO_CLEAR;
		} else {
			notification.flags &= ~Notification.FLAG_AUTO_CANCEL;
			notification.flags &= ~Notification.FLAG_ONGOING_EVENT;
			notification.flags |= Notification.FLAG_NO_CLEAR;
		}
		
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(SyncApi.NOTIFICATION_ID, notification);
	}

	protected void cancelNotification() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(SyncApi.NOTIFICATION_ID);
	}
	
	protected void broadcastAction(String action) {
		Intent intent = new Intent();
		intent.setAction(action);
		this.sendBroadcast(intent);
	}
	
	protected void broadcastResult(boolean changesWereApplied) {
		Intent intent = new Intent();
		intent.putExtra(CHANGES_WERE_APPLIED_BOOLEAN_EXTRA, changesWereApplied);
		intent.setAction(RESULT_ACTION);
		this.sendBroadcast(intent);
	}
	
	protected void broadcastException(Exception exception) {
		SyncExceptionWrapper wrapper = new SyncExceptionWrapper(exception);
		
		Intent intent = new Intent();
		intent.putExtra(EXCEPTION_WRAPPER_SERIALIZABLE_EXTRA, wrapper);
		intent.setAction(EXCEPTION_ACTION);
		
		try {	
			this.sendBroadcast(intent);
		} catch (RuntimeException e) {
			this.broadcastNotSerializableException(wrapper);
		}
	}
	
	/**
	 * If the original exception is not serializable then broadcast a exception that it is
	 */
	private void broadcastNotSerializableException(SyncExceptionWrapper wrapper) {
		if (wrapper.isHttpClientException()) {
			AppGluHttpClientException httpClientException = wrapper.getHttpClientException();
			this.broadcastException(new AppGluHttpClientException(httpClientException.getStatusCode(), httpClientException.getError()));
			return;
		}
		
		if (wrapper.isHttpServerException()) {
			AppGluHttpServerException httpServerException = wrapper.getHttpServerException();
			this.broadcastException(new AppGluHttpServerException(httpServerException.getStatusCode(), httpServerException.getError()));
			return;
		}
		
		if (wrapper.isRestClientException()) {
			AppGluRestClientException restException = wrapper.getRestClientException();
			this.broadcastException(new RestClientException(restException.getMessage()));
			return;
		}
		
		Exception exception = wrapper.getException();
		this.broadcastException(new Exception(exception.getMessage()));
	}

}