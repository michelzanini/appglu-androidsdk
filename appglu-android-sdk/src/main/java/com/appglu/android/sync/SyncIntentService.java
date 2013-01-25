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
import com.appglu.android.sync.SyncIntentServiceRequest.SyncRequestOperation;

public class SyncIntentService extends IntentService {
	
	public static final String SYNC_OPERATION_SERIALIZABLE_EXTRA = "SyncIntentService.SYNC_OPERATION_SERIALIZABLE_EXTRA";
	
	public static final String SYNC_FILES_BOOLEAN_EXTRA = "SyncIntentService.SYNC_FILES_BOOLEAN_EXTRA";
	
	public static final String TABLES_STRING_ARRAY_EXTRA = "SyncIntentService.TABLES_STRING_ARRAY_EXTRA";
	
	public static final String EXECUTING_SYNC_NOTIFICATION_PARCELABLE_EXTRA = "SyncIntentService.EXECUTING_SYNC_NOTIFICATION_PARCELABLE_EXTRA";
	
	public static final String CHANGES_APPLIED_NOTIFICATION_PARCELABLE_EXTRA = "SyncIntentService.CHANGES_APPLIED_NOTIFICATION_PARCELABLE_EXTRA";
	
	public static final String PRE_EXECUTE_ACTION = "SyncIntentService.ON_PRE_EXECUTE_ACTION";
	
	public static final String NO_INTERNET_CONNECTION_ACTION = "SyncIntentService.ON_NO_INTERNET_CONNECTION_ACTION";
	
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
		
		if (this.hasExecutingSyncNotificationExtra(intent)) {
			Notification notification = (Notification) intent.getParcelableExtra(EXECUTING_SYNC_NOTIFICATION_PARCELABLE_EXTRA);
			this.sendNotification(notification, false);
		}
	}

	private boolean hasExecutingSyncNotificationExtra(Intent intent) {
		return intent.hasExtra(EXECUTING_SYNC_NOTIFICATION_PARCELABLE_EXTRA);
	}
	
	private boolean hasChangesAppliedNotificationExtra(Intent intent) {
		return intent.hasExtra(CHANGES_APPLIED_NOTIFICATION_PARCELABLE_EXTRA);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		boolean successful = false;
		
		try {
			if (!AppGlu.hasInternetConnection()) {
				this.broadcastAction(NO_INTERNET_CONNECTION_ACTION);
				return;
			}
			
			this.broadcastAction(PRE_EXECUTE_ACTION);
			
			SyncRequestOperation syncOperation = (SyncRequestOperation) intent.getSerializableExtra(SYNC_OPERATION_SERIALIZABLE_EXTRA);
			ArrayList<String> tables = intent.getStringArrayListExtra(TABLES_STRING_ARRAY_EXTRA);
			boolean syncFiles = intent.getBooleanExtra(SYNC_FILES_BOOLEAN_EXTRA, false);
			
			if (SyncRequestOperation.APPLY_CHANGES.equals(syncOperation)) {
				successful = AppGlu.syncApi().applyChanges();
			
			} else if (SyncRequestOperation.DISCARD_CHANGES.equals(syncOperation)) {
				successful = AppGlu.syncApi().discardChanges();
			
			} else if (SyncRequestOperation.DOWNLOAD_CHANGES.equals(syncOperation)) {
				successful = this.downloadChangesAndFilesForTables(tables, syncFiles);
			
			} else if (SyncRequestOperation.DOWNLOAD_AND_APPLY_CHANGES.equals(syncOperation)) {
				boolean hasChanges = this.downloadChangesAndFilesForTables(tables, syncFiles);
				if (hasChanges) {
					successful = AppGlu.syncApi().applyChanges();
				} else {
					successful = false;
				}
			}
			
			this.broadcastResult(successful);
	
		} catch (Exception exception) {
			this.broadcastException(exception);
		} finally {
			if (this.hasExecutingSyncNotificationExtra(intent)) {
				if (successful && this.hasChangesAppliedNotificationExtra(intent)) {
					Notification notification = (Notification) intent.getParcelableExtra(CHANGES_APPLIED_NOTIFICATION_PARCELABLE_EXTRA);
					this.sendNotification(notification, true);
				} else {
					this.cancelNotification();
				}
			}
		}
		
		this.broadcastAction(FINISH_ACTION);
	}
	
	protected boolean downloadChangesAndFilesForTables(ArrayList<String> tables, boolean syncFiles) {
		if (tables == null) {
			if (syncFiles) {
				return AppGlu.syncApi().downloadChangesAndFiles();
			} else {
				return AppGlu.syncApi().downloadChanges();
			}
		} else {
			if (syncFiles) {
				return AppGlu.syncApi().downloadChangesAndFilesForTables(tables);
			} else {
				return AppGlu.syncApi().downloadChangesForTables(tables);
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