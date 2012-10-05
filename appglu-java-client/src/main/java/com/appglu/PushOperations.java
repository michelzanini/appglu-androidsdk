package com.appglu;

public interface PushOperations {
	
	void registerDevice(Device device) throws AppGluRestClientException;
	
	Device readDevice(String token) throws AppGluRestClientException;
	
	boolean removeDevice(String token) throws AppGluRestClientException;

}