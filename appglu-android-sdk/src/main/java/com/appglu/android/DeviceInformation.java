package com.appglu.android;

import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.appglu.android.util.AppGluUtils;

public class DeviceInformation {
	
	static final String UUID_KEY = "com.appglu.android.DeviceInformation.UUID_KEY";
	
	private Context context;
	
	private String deviceUUID;
	
	private String deviceOS;
	
	private String deviceOSVersion;
	
	private String appName;
	
	private String appVersion;
	
	private String appIdentifier;
	
	private String deviceModel;
	
	private String deviceManufacturer;
	
	public DeviceInformation(Context context) {
		AppGluUtils.assertNotNull(context, "Context cannot be null");
		this.context = context.getApplicationContext();
		
		this.setDeviceUUID(context);
		this.setDeviceInfo(context);
		this.setAppInfo(context);
	}

	public String getDeviceUUID() {
		return deviceUUID;
	}

	public String getDeviceOS() {
		return deviceOS;
	}

	public String getDeviceOSVersion() {
		return deviceOSVersion;
	}

	public String getAppName() {
		return appName;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public String getAppIdentifier() {
		return appIdentifier;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public String getDeviceManufacturer() {
		return deviceManufacturer;
	}
	
	public boolean hasInternetConnection() {
		ConnectivityManager connectivityManager = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
	    return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}

	protected void setDeviceUUID(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(AppGlu.APPGLU_PREFERENCES_KEY, Context.MODE_PRIVATE);
		String uuid = sharedPreferences.getString(UUID_KEY, null);
		if (uuid == null) {
			uuid = UUID.randomUUID().toString();
			
			Editor editor = sharedPreferences.edit();
	    	editor.putString(UUID_KEY, uuid);
	    	editor.commit();
		}
		this.deviceUUID = uuid;
	}

	protected void setDeviceInfo(Context context) {
		this.deviceOS = "Android";
		this.deviceOSVersion = Build.VERSION.RELEASE;
		this.deviceModel = Build.MODEL;
		this.deviceManufacturer = Build.MANUFACTURER;
	}

	protected void setAppInfo(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
			CharSequence applicationLabel = packageManager.getApplicationLabel(applicationInfo);

			this.appName = applicationLabel == null ? "" : applicationLabel.toString();
			this.appVersion = packageInfo.versionName;
			this.appIdentifier = packageInfo.packageName;
		} catch (Exception e) {
			this.appName = "";
			this.appVersion = "";
			this.appIdentifier = "";
		}
	}
	
	@Override
	public String toString() {
		return "DeviceInformation [deviceUUID=" + deviceUUID + ", deviceOS="
				+ deviceOS + ", deviceOSVersion=" + deviceOSVersion
				+ ", appName=" + appName + ", appVersion=" + appVersion
				+ ", appIdentifier=" + appIdentifier + ", deviceModel="
				+ deviceModel + ", deviceManufacturer=" + deviceManufacturer
				+ "]";
	}

}