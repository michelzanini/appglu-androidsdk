package com.appglu;

public interface AsyncPushOperations {
	
	void registerDeviceInBackground(Device device, AsyncCallback<Void> registerCallback);
	
	void readDeviceInBackground(String token, AsyncCallback<Device> readCallback);
	
	void removeDeviceInBackground(String token, AsyncCallback<Boolean> removeCallback);

}