package com.appglu.android.sync;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.appglu.StorageOperations;
import com.appglu.SyncOperations;
import com.appglu.android.AppGlu;
import com.appglu.android.AppGluNotProperlyConfiguredException;
import com.appglu.android.log.Logger;
import com.appglu.android.log.LoggerFactory;

public final class SyncApi {
	
	public static final int NOTIFICATION_ID = Integer.MAX_VALUE;
	
	private Logger logger = LoggerFactory.getLogger(AppGlu.LOG_TAG);
	
	private Context context;
	
	private SyncService syncService;
	
	public SyncApi(Context context, SyncOperations syncOperations, StorageOperations storageOperations, SyncRepository syncRepository) {
		this.context = context;
		this.syncService = new SyncService(syncOperations, storageOperations, syncRepository);
	}
	
	protected boolean doSyncDatabase() {
		return this.syncService.syncDatabase();
	}

	protected boolean doSyncDatabaseAndFiles() {
		return this.syncService.syncDatabaseAndFiles();
	}
	
	protected boolean doSyncTables(List<String> tables) {
		return this.syncService.syncTables(tables);
	}

	protected boolean doSyncTablesAndFiles(List<String> tables) {
		return this.syncService.syncTablesAndFiles(tables);
	}
	
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
		if (this.isSyncIntentServiceRunning()) {
			this.logger.info("SyncIntentService was not started because it is already running");
			return;
		}
		
		Intent intent = new Intent(this.context, SyncIntentService.class);
		this.validateSyncIntent(intent);
		
		intent.putExtra(SyncIntentService.SYNC_FILES_BOOLEAN_EXTRA, request.getSyncFiles());
		
		if (request.getTablesToSync() != null) {
			intent.putStringArrayListExtra(SyncIntentService.TABLES_STRING_ARRAY_EXTRA, new ArrayList<String>(request.getTablesToSync()));
		}
		
		if (request.getExecutingSyncNotification() != null) {
			intent.putExtra(SyncIntentService.EXECUTING_SYNC_NOTIFICATION_PARCELABLE_EXTRA, request.getExecutingSyncNotification());
			
			if (request.getChangesAppliedNotification() != null) {
				intent.putExtra(SyncIntentService.CHANGES_APPLIED_NOTIFICATION_PARCELABLE_EXTRA, request.getChangesAppliedNotification());
			}
		}
		
		this.context.startService(intent);
		this.logger.info("SyncIntentService has being started");
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