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
import android.os.Build;
import android.util.DisplayMetrics;

import com.appglu.DevicePlatform;
import com.appglu.android.util.AppGluUtils;
import com.appglu.impl.util.StringUtils;

/**
 * Represents a installation of your app on a device. <br>
 * Contains information like the UUID of the installation as well as device specific info.<br>
 * For instance, the operating system name and version, the app name and version, the device model and manufacturer, etc...<br>
 * It is used mainly for analytics purposes.
 * 
 * @see AppGlu#deviceInstallation()
 * @since 1.0.0
 */
public class DeviceInstallation {
	
	static final String UUID_KEY = "com.appglu.android.DeviceInstallation.UUID_KEY";
	
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
	
	public DeviceInstallation(Context context) {
		AppGluUtils.assertNotNull(context, "Context cannot be null");
		this.context = context.getApplicationContext();
		
		this.setDeviceUUID(context);
		this.setDeviceInfo(context);
		this.setAppInfo(context);
		this.replaceSemicolonForHeaders();
	}

	/**
	 * @return unique identified number representing this installation
	 */
	public String getDeviceUUID() {
		return deviceUUID;
	}

	/**
	 * @return always return "android"
	 */
	public String getDeviceOS() {
		return deviceOS;
	}

	/**
	 * @return the Android OS version
	 */
	public String getDeviceOSVersion() {
		return deviceOSVersion;
	}

	/**
	 * @return your application name defined on AndroidManifest.xml
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * @return your application version defined on AndroidManifest.xml
	 */
	public String getAppVersion() {
		return appVersion;
	}

	/**
	 * @return your application package defined on AndroidManifest.xml
	 */
	public String getAppIdentifier() {
		return appIdentifier;
	}

	/**
	 * @return the model of the device that this application is running on
	 */
	public String getDeviceModel() {
		return deviceModel;
	}

	/**
	 * @return the manufacturer of the device that this application is running on
	 */
	public String getDeviceManufacturer() {
		return deviceManufacturer;
	}
	
	/**
	 * @return the resolution of the device that this application is running on
	 */
	public String getDeviceResolution() {
		return deviceResolution;
	}
	
	/**
	 * @return the language of the device that this application is running on
	 */
	public String getDeviceLanguage() {
		return deviceLanguage;
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
		if (StringUtils.isNotEmpty(Locale.getDefault().getCountry())) {
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
	
	protected void replaceSemicolonForHeaders() {
		this.deviceOS = StringUtils.replaceSemicolon(this.deviceOS);
		this.deviceOSVersion = StringUtils.replaceSemicolon(this.deviceOSVersion);
		this.deviceModel = StringUtils.replaceSemicolon(this.deviceModel);
		this.deviceManufacturer = StringUtils.replaceSemicolon(this.deviceManufacturer);
		this.deviceLanguage = StringUtils.replaceSemicolon(this.deviceLanguage);
		this.appName = StringUtils.replaceSemicolon(this.appName);
		this.appVersion = StringUtils.replaceSemicolon(this.appVersion);
		this.appIdentifier = StringUtils.replaceSemicolon(this.appIdentifier);
	}
	
	protected Map<String, List<String>> createDefaultHeaders(AppGluSettings settings) {
		Map<String, List<String>> httpHeaders = new HashMap<String, List<String>>();
		
		httpHeaders.put("User-Agent", Arrays.asList(this.createUserAgentHeader()));
		httpHeaders.put("X-AppGlu-Client-Id", Arrays.asList(this.getDeviceUUID()));
		httpHeaders.put("X-AppGlu-Client-App", Arrays.asList(this.createClientAppHeader()));
		httpHeaders.put("X-AppGlu-Client-Device", Arrays.asList(this.createClientDeviceHeader()));
		
		/* X-AppGlu-Environment header is added by the Java Client, @see DefaultHeadersHttpRequestInterceptor */
		
		if (StringUtils.isNotEmpty(settings.getApplicationVersion())) {
			httpHeaders.put("X-AppGlu-Client-Version", Arrays.asList(settings.getApplicationVersion()));
		} else {
			httpHeaders.put("X-AppGlu-Client-Version", Arrays.asList(this.getAppVersion()));
		}
		
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
		return "DeviceInstallation [context=" + context + ", deviceUUID="
				+ deviceUUID + ", deviceOS=" + deviceOS + ", deviceOSVersion="
				+ deviceOSVersion + ", appName=" + appName + ", appVersion="
				+ appVersion + ", appIdentifier=" + appIdentifier
				+ ", deviceModel=" + deviceModel + ", deviceManufacturer="
				+ deviceManufacturer + ", deviceResolution=" + deviceResolution
				+ ", deviceLanguage=" + deviceLanguage + "]";
	}

}