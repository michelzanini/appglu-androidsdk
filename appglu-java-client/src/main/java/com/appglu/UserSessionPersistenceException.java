package com.appglu;

@SuppressWarnings("serial")
public class UserSessionPersistenceException extends AppGluRestClientException {

	public UserSessionPersistenceException(String msg, Throwable ex) {
		super(msg, ex);
	}

}