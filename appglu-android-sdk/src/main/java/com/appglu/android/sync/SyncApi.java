package com.appglu.android.sync;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.appglu.SyncOperations;
import com.appglu.android.AppGluNotProperlyConfiguredException;

public final class SyncApi {
	
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
	
	public boolean isIntentServiceRunning() {
		return AppGluSyncIntentService.isRunning();
	}
	
	public void startSyncDatabaseIntentService() {
		this.startSyncIntentService(null);
	}

	public void startSyncTablesIntentService(String... tables) {
		this.startSyncIntentService(Arrays.asList(tables));
	}
	
	public void startSyncTablesIntentService(List<String> tables) {
		this.startSyncIntentService(tables);
	}

	private void startSyncIntentService(List<String> tables) {
		Intent intent = new Intent(this.context, AppGluSyncIntentService.class);
		
		if (!this.isServiceAvailableForIntent(intent)) {
			throw new AppGluNotProperlyConfiguredException("To be able to execute sync you must declare " +
				"a service named com.appglu.android.sync.AppGluSyncIntentService in the AndroidManifest.xml");
		}
		
		if (tables != null) {
			intent.putStringArrayListExtra(AppGluSyncIntentService.TABLES_STRING_ARRAY_EXTRA, new ArrayList<String>(tables));
		}
		this.context.startService(intent);
	}
	
	private boolean isServiceAvailableForIntent(Intent intent) {
		PackageManager packageManager = this.context.getPackageManager();
		List<ResolveInfo> resolveInfo = packageManager.queryIntentServices(intent, PackageManager.MATCH_DEFAULT_ONLY);
		if (resolveInfo.size() > 0) {
			return true;
		}
		return false;
	}

}