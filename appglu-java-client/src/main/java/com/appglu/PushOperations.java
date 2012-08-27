package com.appglu;

public interface PushOperations {
	
	void registerDevice(Device device);
	
	Device readDevice(String token);
	
	boolean removeDevice(String token);

}