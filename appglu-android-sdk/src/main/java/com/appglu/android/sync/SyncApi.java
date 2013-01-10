package com.appglu.android.sync;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

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
	
	public SyncApi(Context context, SyncOperations syncOperations, SyncRepository syncRepository) {
		this.context = context;
		this.syncService = new SyncService(syncOperations, syncRepository);
	}
	
	protected void doSyncDatabase() {
		this.syncService.syncDatabase();
	}

	protected void doSyncTables(List<String> tables) {
		this.syncService.syncTables(tables);
	}
	
	public boolean isSyncIntentServiceRunning() {
		ActivityManager manager = (ActivityManager) this.context.getSystemService(Activity.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (AppGluSyncIntentService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public void startSyncDatabaseIntentService() {
		this.startSyncIntentService(null, null);
	}

	public void startSyncTablesIntentService(String... tables) {
		this.startSyncIntentService(null, Arrays.asList(tables));
	}
	
	public void startSyncTablesIntentService(List<String> tables) {
		this.startSyncIntentService(null, tables);
	}
	
	public void startSyncDatabaseIntentService(Notification notification) {
		this.startSyncIntentService(notification, null);
	}

	public void startSyncTablesIntentService(Notification notification, String... tables) {
		this.startSyncIntentService(notification, Arrays.asList(tables));
	}
	
	public void startSyncTablesIntentService(Notification notification, List<String> tables) {
		this.startSyncIntentService(notification, tables);
	}
	
	private void startSyncIntentService(Notification notification, List<String> tables) {
		if (this.isSyncIntentServiceRunning()) {
			this.logger.info("AppGluSyncIntentService was not started because it is already running");
			return;
		}
		
		Intent intent = new Intent(this.context, AppGluSyncIntentService.class);
		this.validateSyncIntent(intent);
		
		if (tables != null) {
			intent.putStringArrayListExtra(AppGluSyncIntentService.TABLES_STRING_ARRAY_EXTRA, new ArrayList<String>(tables));
		}
		
		if (notification != null) {
			intent.putExtra(AppGluSyncIntentService.NOTIFICATION_PARCELABLE_EXTRA, notification);
		}
		
		this.context.startService(intent);
		this.logger.info("AppGluSyncIntentService has being started");
	}
	
	private void validateSyncIntent(Intent intent) {
		PackageManager packageManager = this.context.getPackageManager();
		List<ResolveInfo> resolveInfo = packageManager.queryIntentServices(intent, PackageManager.MATCH_DEFAULT_ONLY);
		
		if (resolveInfo.size() == 0) {
			throw new AppGluNotProperlyConfiguredException("To be able to execute sync you must declare " +
					"a service named com.appglu.android.sync.AppGluSyncIntentService in the AndroidManifest.xml");
		}
	}

}