package com.appglu.android;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import android.util.DisplayMetrics;

import com.appglu.DevicePlatform;
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
	
	private String deviceResolution;
	
	private String deviceLanguage;
	
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
	
	public String getDeviceResolution() {
		return deviceResolution;
	}
	
	public String getDeviceLanguage() {
		return deviceLanguage;
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
		this.deviceOS = DevicePlatform.ANDROID.toString();
		this.deviceOSVersion = Build.VERSION.RELEASE;
		this.deviceModel = Build.MODEL;
		this.deviceManufacturer = Build.MANUFACTURER;
		
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		if (displayMetrics != null) {
			this.deviceResolution = displayMetrics.densityDpi + "dpi";
		}
		
		this.deviceLanguage = Locale.getDefault().getLanguage();
		if (AppGluUtils.hasText(Locale.getDefault().getCountry())) {
			this.deviceLanguage += "_" + Locale.getDefault().getCountry();
		}
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
	
	protected Map<String, List<String>> createDefaultHeaders() {
		Map<String, List<String>> httpHeaders = new HashMap<String, List<String>>();
		
		httpHeaders.put("User-Agent", Arrays.asList(this.createUserAgentHeader()));
		httpHeaders.put("X-AppGlu-Client-Id", Arrays.asList(this.getDeviceUUID()));
		httpHeaders.put("X-AppGlu-Client-App", Arrays.asList(this.createClientAppHeader()));
		httpHeaders.put("X-AppGlu-Client-Device", Arrays.asList(this.createClientDeviceHeader()));
		
		return httpHeaders;
	}
	
	private String createUserAgentHeader() {
		return String.format("%s/%s (%s) %s/%s (%s)", "AndroidSDK", AppGlu.VERSION, 
			this.createClientDeviceHeader(), this.getAppName(), this.getAppVersion(), this.getAppIdentifier());
	}
	
	private String createClientAppHeader() {
		return String.format("%s;%s;%s", this.getAppName(), this.getAppVersion(), this.getAppIdentifier());
	}
	
	private String createClientDeviceHeader() {
		return String.format("%s;%s;%s;%s;%s;%s", this.getDeviceOS(), this.getDeviceOSVersion(), 
			this.getDeviceModel(), this.getDeviceManufacturer(), this.getDeviceResolution(), this.getDeviceLanguage());
	}

	@Override
	public String toString() {
		return "DeviceInformation [context=" + context + ", deviceUUID="
				+ deviceUUID + ", deviceOS=" + deviceOS + ", deviceOSVersion="
				+ deviceOSVersion + ", appName=" + appName + ", appVersion="
				+ appVersion + ", appIdentifier=" + appIdentifier
				+ ", deviceModel=" + deviceModel + ", deviceManufacturer="
				+ deviceManufacturer + ", deviceResolution=" + deviceResolution
				+ ", deviceLanguage=" + deviceLanguage + "]";
	}

}